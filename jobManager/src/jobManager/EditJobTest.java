package jobManager;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class EditJobTest {
    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    JavascriptExecutor js;

    String baseUrl = "http://localhost/Product%20Filter%20And%20Search/";

    // Predefined sample job data for updates
    private static final String[] NEW_JOB_TITLES = {
        "Senior DevOps Engineer", "Technical Content Writer", "Business Data Analyst",
        "Creative Graphic Designer", "Customer Support Specialist", "Senior UI/UX Designer",
        "Full Stack Developer", "Senior Product Manager", "Software Engineering Lead"
    };

    private static final String[] NEW_COMPANIES = {
        "Cloud Innovations", "Tech Publications", "Analytics Pro",
        "Design Studio", "Support Solutions", "UX Excellence",
        "Web Development Hub", "Product Leaders", "Code Crafters"
    };

    private static final String[] NEW_LOCATIONS = {
        "Seattle, WA", "Boston, MA", "Denver, CO",
        "Manchester, UK", "Munich, Germany", "Osaka, Japan",
        "Melbourne, Australia", "Vancouver, Canada", "Lyon, France"
    };

    public static void main(String[] args) {
        EditJobTest test = new EditJobTest();
        test.setup();
        test.testJobEditing();
        test.teardown();
    }

    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\All Files\\College Files\\03. Sem 4 - Germany\\Software Testing\\Selenium WebDriver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    public void testJobEditing() {
        try {
            System.out.println("Starting job editing test...");

            // before Navigating to homepage
            driver.get(baseUrl + "index.html");
            Thread.sleep(3000); // after waiting for page to load

            // before Fetching existing job cards (excluding the add job card)
            List<WebElement> jobCards = driver.findElements(By.cssSelector("div.card:not(.add-job-card)"));

            if (jobCards.isEmpty()) {
                System.out.println("No jobs found in database. Skipping edit test.");
                return;
            }

            // before Selecting a random job card to edit
            Random rand = new Random();
            int randomIndex = rand.nextInt(jobCards.size());
            WebElement selectedCard = jobCards.get(randomIndex);

            String originalTitle = selectedCard.findElement(By.tagName("h5")).getText();
            String originalCompany = selectedCard.findElements(By.tagName("p")).get(0).getText();
            String originalLocation = selectedCard.findElements(By.tagName("p")).get(1).getText();

            System.out.println("Selected job to edit: " + originalTitle + " at " + originalCompany + ", " + originalLocation);

            // Scroll and hover
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", selectedCard);
            Thread.sleep(2000);
            actions.moveToElement(selectedCard).pause(1000).perform();
            Thread.sleep(2000);

            // Click edit
            WebElement editButton = selectedCard.findElement(By.cssSelector(".edit-btn"));
            System.out.println("Clicking edit button...");
            editButton.click();
            Thread.sleep(3000);

            // before Verifying navigation to edit page
            if (!driver.getCurrentUrl().contains("edit.php")) {
                System.out.println("Not redirected to edit page! Current URL: " + driver.getCurrentUrl());
                assert false : "Edit page not loaded";
            }

            // Generate new data
            int dataIndex = rand.nextInt(NEW_JOB_TITLES.length);
            String newTitle = NEW_JOB_TITLES[dataIndex];
            String newCompany = NEW_COMPANIES[dataIndex];
            String newLocation = NEW_LOCATIONS[dataIndex];

            System.out.println("Updating to: " + newTitle + " at " + newCompany + ", " + newLocation);

            // Fill form fields
            WebElement titleField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title")));
            titleField.clear();
            titleField.sendKeys(newTitle);
            Thread.sleep(1000);

            WebElement companyField = driver.findElement(By.name("company"));
            companyField.clear();
            companyField.sendKeys(newCompany);
            Thread.sleep(1000);

            WebElement locationField = driver.findElement(By.name("location"));
            locationField.clear();
            locationField.sendKeys(newLocation);
            Thread.sleep(1000);

            // Select image
            WebElement imageDropdown = driver.findElement(By.name("jobImg"));
            Select select = new Select(imageDropdown);
            int optionIndex = rand.nextInt(select.getOptions().size() - 1) + 1;
            select.selectByIndex(optionIndex);
            Thread.sleep(1000);

            // Submit form
            System.out.println("Saving changes...");
            WebElement saveButton = driver.findElement(By.cssSelector("input[type='submit'], button[type='submit']"));
            saveButton.click();
            Thread.sleep(3000);

            // Verify redirection
            if (!driver.getCurrentUrl().contains("index.html")) {
                System.out.println("Not redirected to homepage! Current URL: " + driver.getCurrentUrl());
                assert false : "Redirection failed";
            }

            // Verify updated job appears
            System.out.println("Verifying updates...");
            Thread.sleep(2000);

            try {
                WebElement updatedCard = driver.findElement(By.xpath(
                    "//h5[contains(text(), '" + newTitle + "')]/ancestor::div[@class='card']"));

                js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", updatedCard);
                Thread.sleep(2000);

                String actualCompany = updatedCard.findElements(By.tagName("p")).get(0).getText();
                String actualLocation = updatedCard.findElements(By.tagName("p")).get(1).getText();

                System.out.println("Updated job details:");
                System.out.println(" - Title: " + newTitle);
                System.out.println(" - Company: " + actualCompany);
                System.out.println(" - Location: " + actualLocation);

                if (!actualCompany.equals(newCompany)) {
                    System.out.println("Company mismatch! Expected: " + newCompany + ", Actual: " + actualCompany);
                    assert false : "Company not updated";
                }

                if (!actualLocation.equals(newLocation)) {
                    System.out.println("Location mismatch! Expected: " + newLocation + ", Actual: " + actualLocation);
                    assert false : "Location not updated";
                }

                System.out.println("Edit test completed successfully.");
            } catch (NoSuchElementException e) {
                System.out.println("Updated job card not found! Title: " + newTitle);
                assert false : "Job card not found after edit";
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}