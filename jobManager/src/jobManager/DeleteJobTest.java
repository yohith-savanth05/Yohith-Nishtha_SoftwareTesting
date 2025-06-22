package jobManager;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class DeleteJobTest {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    JavascriptExecutor js;
    String baseUrl = "http://localhost/Product%20Filter%20And%20Search/";

    // Selected job info for verification
    private String selectedJobTitle;
    private String selectedJobCompany;
    private String selectedJobLocation;
    private WebElement selectedCard;

    // BeforeAll
    public static void main(String[] args) {
        DeleteJobTest test = new DeleteJobTest();
        test.setup();
        test.testJobDeletion();
        test.teardown();
    }

    // BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\All Files\\College Files\\03. Sem 4 - Germany\\Software Testing\\Selenium WebDriver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    // Test
    public void testJobDeletion() {
        try {
            System.out.println("---------- STARTING JOB DELETION TEST ----------");

            driver.get(baseUrl + "index.html");
            Thread.sleep(3000);

            List<WebElement> jobCards = driver.findElements(By.cssSelector("div.card:not(.add-job-card)"));
            if (jobCards.isEmpty()) {
                System.out.println("No job listings found. Skipping delete test.");
                return;
            }

            int randomIndex = new Random().nextInt(jobCards.size());
            selectedCard = jobCards.get(randomIndex);

            selectedJobTitle = selectedCard.findElement(By.tagName("h5")).getText();
            List<WebElement> paragraphs = selectedCard.findElements(By.tagName("p"));
            selectedJobCompany = paragraphs.get(0).getText();
            selectedJobLocation = paragraphs.get(1).getText();

            System.out.println("Selected job for deletion:");
            System.out.println("Title   : " + selectedJobTitle);
            System.out.println("Company : " + selectedJobCompany);
            System.out.println("Location: " + selectedJobLocation);

            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", selectedCard);
            Thread.sleep(3000);

            actions.moveToElement(selectedCard).pause(1000).perform();
            Thread.sleep(3000);

            WebElement deleteButton = selectedCard.findElement(By.cssSelector(".delete-btn"));
            deleteButton.click();
            Thread.sleep(3000);

            testModalOption("close", "X Button");
            testModalOption("cancel", "Cancel Button");
            testModalOption("delete", "Confirm Delete Button");

            System.out.println("---------- ALL DELETION TEST CASES PASSED ----------");

        } catch (Exception e) {
            System.out.println("Error occurred during test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper Test Method
    private void testModalOption(String option, String optionLabel) throws InterruptedException {
        System.out.println("\n--- Testing modal option: " + optionLabel + " ---");

        WebElement modal = null;
        try {
            modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-modal")));
            WebElement modalMessage = modal.findElement(By.id("modal-message"));
            String modalText = modalMessage.getText();

            if (!modalText.contains(selectedJobTitle)) {
                System.out.println("Warning: Modal message does not contain expected job title.");
            } else {
                System.out.println("Modal shows correct job title.");
            }

        } catch (TimeoutException e) {
            System.out.println("Modal not visible. Reopening...");
            reopenModalForJob();
            modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-modal")));
        }

        WebElement actionButton = null;
        switch (option) {
            case "close":
                actionButton = modal.findElement(By.id("modal-close"));
                break;
            case "cancel":
                actionButton = modal.findElement(By.id("modal-cancel"));
                break;
            case "delete":
                actionButton = modal.findElement(By.id("modal-confirm"));
                break;
        }

        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", actionButton);
        Thread.sleep(1000);
        actionButton.click();
        Thread.sleep(3000);

        if (option.equals("delete")) {
            verifyJobDeleted();
        } else {
            verifyJobStillExists(optionLabel);
        }
    }

    // Reopens modal in case it is closed
    private void reopenModalForJob() throws InterruptedException {
        List<WebElement> jobCards = driver.findElements(By.cssSelector("div.card:not(.add-job-card)"));

        for (WebElement card : jobCards) {
            String title = card.findElement(By.tagName("h5")).getText();
            List<WebElement> paragraphs = card.findElements(By.tagName("p"));

            if (paragraphs.size() < 2) continue;

            String company = paragraphs.get(0).getText();
            String location = paragraphs.get(1).getText();

            if (title.equals(selectedJobTitle) && company.equals(selectedJobCompany) && location.equals(selectedJobLocation)) {
                js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", card);
                Thread.sleep(2000);
                actions.moveToElement(card).pause(1000).perform();
                Thread.sleep(2000);

                WebElement deleteButton = card.findElement(By.cssSelector(".delete-btn"));
                deleteButton.click();
                Thread.sleep(2000);
                return;
            }
        }

        throw new NoSuchElementException("Could not find matching job card to reopen modal.");
    }

    // Verifies job is still visible (after cancel or close)
    private void verifyJobStillExists(String action) {
        System.out.println("Verifying job exists after action: " + action);

        List<WebElement> jobCards = driver.findElements(By.cssSelector("div.card:not(.add-job-card)"));
        boolean jobFound = false;

        for (WebElement card : jobCards) {
            String title = card.findElement(By.tagName("h5")).getText();
            List<WebElement> paragraphs = card.findElements(By.tagName("p"));

            if (paragraphs.size() < 2) continue;

            String company = paragraphs.get(0).getText();
            String location = paragraphs.get(1).getText();

            if (title.equals(selectedJobTitle) && company.equals(selectedJobCompany) && location.equals(selectedJobLocation)) {
                jobFound = true;
                js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", card);
                break;
            }
        }

        if (!jobFound) {
            throw new AssertionError("Job was deleted unexpectedly after: " + action);
        }
    }

    // Verifies job is deleted (after confirmation)
    private void verifyJobDeleted() {
        System.out.println("Verifying job has been deleted...");

        List<WebElement> jobCards = driver.findElements(By.cssSelector("div.card:not(.add-job-card)"));
        boolean jobFound = false;

        for (WebElement card : jobCards) {
            try {
                String title = card.findElement(By.tagName("h5")).getText();
                List<WebElement> paragraphs = card.findElements(By.tagName("p"));

                if (paragraphs.size() < 2) continue;

                String company = paragraphs.get(0).getText();
                String location = paragraphs.get(1).getText();

                if (title.equals(selectedJobTitle) && company.equals(selectedJobCompany) && location.equals(selectedJobLocation)) {
                    jobFound = true;
                    break;
                }
            } catch (NoSuchElementException ignored) {}
        }

        if (jobFound) {
            throw new AssertionError("Job still exists after deletion.");
        } else {
            System.out.println("Job successfully deleted.");
        }

        js.executeScript("window.scrollTo(0, 0)");
    }

    // AfterEach
    public void teardown() {
        if (driver != null) {
            System.out.println("Closing browser...");
            driver.quit();
        }
    }
}
