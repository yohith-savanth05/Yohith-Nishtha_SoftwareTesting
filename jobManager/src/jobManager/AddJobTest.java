package jobManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.Random;

public class AddJobTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "http://localhost/Product Filter And Search/index.html";

    private static final String[] JOB_TITLES = {
        "DevOps Engineer", "Content Writer", "Data Analyst",
        "Graphic Designer", "Support Specialist", "UI/UX Designer",
        "Web Developer", "Product Manager", "Software Engineer"
    };

    private static final String[] COMPANIES = {
        "Tech Innovations", "Global Media", "Data Insights Co.",
        "Creative Designs", "Customer First", "Digital Experience",
        "Web Solutions", "Product Visionaries", "Code Masters"
    };

    private static final String[] LOCATIONS = {
        "New York, NY", "San Francisco, CA", "Austin, TX",
        "London, UK", "Berlin, Germany", "Tokyo, Japan",
        "Sydney, Australia", "Toronto, Canada", "Paris, France"
    };

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\All Files\\College Files\\03. Sem 4 - Germany\\Software Testing\\Selenium WebDriver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    public void testAddJobViaNavButton() throws InterruptedException {
        System.out.println("Running positive test case: Add Job via Navigation Button");
        runAddJobTest(true);
    }

    @Test
    public void testSubmitWithEmptyFields() throws InterruptedException {
        System.out.println("Running negative test case: Submit With Empty Fields");
        driver.get(baseUrl);
        Thread.sleep(1000);

        // Navigate to Add Job page
        WebElement navButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("a[href='PHP/add.php'] button.applynow")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", navButton);
        Thread.sleep(1000);
        navButton.click();
        Thread.sleep(1000);

        // Wait for form
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("title")));

        // Leave all fields empty and attempt submission
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("input[type='submit'], button[type='submit']")));
        Thread.sleep(1000);
        submitButton.click();
        Thread.sleep(1000);

        // Check for validation behavior (either URL doesn't change or error is shown)
        String currentUrl = driver.getCurrentUrl();
        if (!currentUrl.contains("add.php")) {
            throw new AssertionError("Form submitted with empty fields. Expected to stay on add page.");
        }

        System.out.println("Form validation works correctly — submission blocked when fields are empty.");
    }

    private void runAddJobTest(boolean useNavButton) throws InterruptedException {
        driver.get(baseUrl);
        Thread.sleep(1000);

        // Randomized data
        Random rand = new Random();
        int index = rand.nextInt(JOB_TITLES.length);
        String jobTitle = JOB_TITLES[index];
        String company = COMPANIES[index];
        String location = LOCATIONS[index];

        if (useNavButton) {
            WebElement navButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a[href='PHP/add.php'] button.applynow")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", navButton);
            Thread.sleep(1000);
            navButton.click();
        } else {
            WebElement addCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div.add-job-card")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addCard);
            Thread.sleep(1000);
            addCard.click();
        }

        // Wait for form
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("title")));
        Thread.sleep(1000);

        // Fill the form
        WebElement titleField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title")));
        titleField.sendKeys(jobTitle);
        Thread.sleep(500);

        WebElement companyField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("company")));
        companyField.sendKeys(company);
        Thread.sleep(500);

        WebElement locationField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("location")));
        locationField.sendKeys(location);
        Thread.sleep(500);

        // Dropdown
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("jobImg"))));
        int optionIndex = rand.nextInt(select.getOptions().size() - 1) + 1;
        select.selectByIndex(optionIndex);
        Thread.sleep(500);

        // Submit
        WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("input[type='submit'], button[type='submit']")));
        Thread.sleep(1000);
        submit.click();

        // Validate redirect
        wait.until(ExpectedConditions.urlContains("index.html"));
        Thread.sleep(1000);

        // Verify card is visible
        By jobTitleLocator = By.xpath("//h5[contains(text(), '" + jobTitle + "')]");
        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(jobTitleLocator));
        WebElement jobCard = titleElement.findElement(By.xpath("./ancestor::div[@class='card']"));
        Thread.sleep(1000);

        String actualCompany = jobCard.findElement(By.xpath(".//p[1]")).getText();
        String actualLocation = jobCard.findElement(By.xpath(".//p[2]")).getText();

        if (!actualCompany.equals(company) || !actualLocation.equals(location)) {
            throw new AssertionError("Job details mismatch. Expected: " + company + ", " + location);
        }

        System.out.println("Job successfully added and verified.");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
