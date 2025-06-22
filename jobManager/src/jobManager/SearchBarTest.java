package jobManager;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;

public class SearchBarTest {
    WebDriver driver;
    WebDriverWait wait;
    String baseUrl = "http://localhost/Product%20Filter%20And%20Search/";
    List<String> allJobTitles = new ArrayList<>();
    boolean hasJobs = false;

    public static void main(String[] args) {
        SearchBarTest test = new SearchBarTest();

        // Setup before tests
        test.setup();

        // Run test cases
        test.testSearchFunctionality();

        // Cleanup after tests
        test.teardown();
    }

    // Could be annotated as @BeforeEach in a testing framework
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\All Files\\College Files\\03. Sem 4 - Germany\\Software Testing\\Selenium WebDriver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(baseUrl + "index.html");

        collectJobTitles();
    }

    private void collectJobTitles() {
        List<WebElement> jobCards = driver.findElements(By.cssSelector("div.card:not(.add-job-card)"));
        hasJobs = !jobCards.isEmpty();

        if (hasJobs) {
            for (WebElement card : jobCards) {
                try {
                    String title = card.findElement(By.tagName("h5")).getText();
                    allJobTitles.add(title);
                } catch (NoSuchElementException e) {
                    System.out.println("Skipping card missing a title");
                }
            }
            System.out.println("Collected " + allJobTitles.size() + " job titles for testing.");
        } else {
            System.out.println("No job cards found — only 'Add Job' card is visible.");
        }
    }

    public void testSearchFunctionality() {
        try {
            System.out.println("\n========== STARTING SEARCH BAR TESTS ==========");

            WebElement searchInput = driver.findElement(By.id("search-input"));

            if (!hasJobs) {
                System.out.println("\n--- Testing with no jobs available ---");
                testSearchTerm(searchInput, "", "Empty search with no jobs", 0);
                testSearchTerm(searchInput, "developer", "Search with no jobs", 0);

                List<WebElement> visibleCards = driver.findElements(By.cssSelector("div.card:not(.hide)"));
                validateOnlyAddCardVisible(visibleCards);
                return;
            }

            testSearchTerm(searchInput, "", "Show all jobs on empty search", allJobTitles.size());

            String randomTitle = getRandomTitle();
            testSearchTerm(searchInput, randomTitle, "Exact match", 1);

            String partial = getPartialTitle(allJobTitles.get(0));
            testSearchTerm(searchInput, partial, "Partial match: " + partial, countTitleMatches(partial));

            testSearchTerm(searchInput, randomizeCase(randomTitle), "Case-insensitive match", 1);
            testSearchTerm(searchInput, "xyz123nonexistent", "No match test", 0);
            testSearchTerm(searchInput, "@#$%", "Special characters test", 0);
            testSearchTerm(searchInput, "  " + randomTitle + "  ", "Whitespace trimming test", 1);

            testCommonSubstringSearch();

            System.out.println("========== SEARCH BAR TESTS COMPLETED ==========\n");

        } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void testSearchTerm(WebElement input, String searchTerm, String description, int expectedCount) throws InterruptedException {
        System.out.println("\n--- " + description + " ---");
        System.out.println("Searching for: '" + searchTerm + "' | Expected matches: " + expectedCount);

        clearSearchInput(input);
        Thread.sleep(500);

        for (char c : searchTerm.toCharArray()) {
            input.sendKeys(String.valueOf(c));
            Thread.sleep(100);
        }

        Thread.sleep(1500);

        List<WebElement> visibleJobs = driver.findElements(By.cssSelector("div.card:not(.add-job-card):not(.hide)"));
        System.out.println("Found " + visibleJobs.size() + " visible job cards");

        if (visibleJobs.size() != expectedCount) {
            throw new AssertionError("Expected " + expectedCount + " matches but found " + visibleJobs.size());
        }

        WebElement addCard = driver.findElement(By.cssSelector("div.add-job-card"));
        if (!addCard.isDisplayed()) {
            throw new AssertionError("Add Job card should always be visible");
        }

        if (expectedCount > 0) {
            for (WebElement card : visibleJobs) {
                String title = card.findElement(By.tagName("h5")).getText();
                if (!title.toLowerCase().contains(searchTerm.toLowerCase().trim())) {
                    throw new AssertionError("Mismatch in visible job title: " + title);
                }
            }
        }

        System.out.println("Test passed");
    }

    private void clearSearchInput(WebElement input) throws InterruptedException {
        int length = input.getAttribute("value").length();

        for (int i = 0; i < length; i++) {
            input.sendKeys(Keys.BACK_SPACE);
            Thread.sleep(50);
        }

        if (!input.getAttribute("value").isEmpty()) {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = ''; arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", input
            );
        }

        Thread.sleep(500);
    }

    private int countTitleMatches(String term) {
        String cleanTerm = term.toLowerCase().trim();
        int count = 0;

        for (String title : allJobTitles) {
            if (title.toLowerCase().contains(cleanTerm)) {
                count++;
            }
        }

        return count;
    }

    private String getRandomTitle() {
        return allJobTitles.get(new Random().nextInt(allJobTitles.size()));
    }

    private String getPartialTitle(String title) {
        return title.substring(0, Math.min(3, title.length()));
    }

    private String randomizeCase(String text) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();

        for (char c : text.toCharArray()) {
            sb.append(rand.nextBoolean() ? Character.toUpperCase(c) : Character.toLowerCase(c));
        }

        return sb.toString();
    }

    private void testCommonSubstringSearch() throws InterruptedException {
        System.out.println("\n--- Testing common substring ---");

        WebElement searchInput = driver.findElement(By.id("search-input"));
        String common = findCommonSubstring();

        if (common != null) {
            int matches = countTitleMatches(common);
            testSearchTerm(searchInput, common, "Common substring: " + common, matches);
        } else {
            System.out.println("No common substring found for this test.");
        }
    }

    private String findCommonSubstring() {
        if (allJobTitles.size() < 2) return null;

        String base = allJobTitles.get(0);
        String[] words = base.split("\\s+");

        for (String word : words) {
            if (word.length() > 3 && allTitlesContain(word)) {
                return word;
            }
        }

        for (int len = 4; len > 1; len--) {
            for (int i = 0; i <= base.length() - len; i++) {
                String sub = base.substring(i, i + len);
                if (allTitlesContain(sub)) {
                    return sub;
                }
            }
        }

        return null;
    }

    private boolean allTitlesContain(String fragment) {
        for (String title : allJobTitles) {
            if (!title.toLowerCase().contains(fragment.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    private void validateOnlyAddCardVisible(List<WebElement> visibleCards) {
        if (visibleCards.size() != 1 || !visibleCards.get(0).getAttribute("class").contains("add-job-card")) {
            throw new AssertionError("Only 'Add Job' card should be visible");
        }
        System.out.println("Only 'Add Job' card is visible as expected.");
    }

    // Could be annotated as @AfterEach in a testing framework
    public void teardown() {
        if (driver != null) {
            System.out.println("Closing browser...");
            driver.quit();
        }
    }
}
