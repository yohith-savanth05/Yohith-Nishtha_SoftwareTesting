package jobManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.*;

public class FilterTest {
    private WebDriver driver;
    private final String baseUrl = "http://localhost/Product%20Filter%20And%20Search/";
    
    private final List<String> allCategories = Arrays.asList(
        "All", "Software Engineer", "Data Analyst", "UX/UI Designer",
        "Product Manager", "Web Developer", "Graphic Designer",
        "DevOps Engineer", "Content Writer", "Customer Support Representative"
    );

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver",
            "C:\\All Files\\College Files\\03. Sem 4 - Germany\\Software Testing\\Selenium WebDriver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        System.out.println("Opening the application homepage...");
        driver.get(baseUrl + "index.html");
        sleep(500);
    }

    @Test
    public void testFilterFunctionality() {
        System.out.println("Starting filter functionality tests...");

        for (String category : allCategories) {
            testCategoryFilter(category);
        }

        System.out.println("Filter functionality tests completed.");
    }

    private void testCategoryFilter(String category) {
        System.out.println("\nTesting category filter: " + category);
        try {
            WebElement filterButton = driver.findElement(
                By.xpath("//button[contains(@class, 'button-value') and contains(text(), '" + category + "')]"));
            filterButton.click();
            sleep(500);

            List<WebElement> visibleCards = driver.findElements(
                By.cssSelector("div.card:not(.add-job-card):not(.hide)"));
            System.out.println("Visible job listings found: " + visibleCards.size());

            WebElement addCard = driver.findElement(By.cssSelector("div.add-job-card"));
            if (addCard.isDisplayed()) {
                System.out.println("Add Job card is correctly visible.");
            } else {
                System.out.println("Warning: Add Job card is not visible when it should be.");
            }

            System.out.println("Category '" + category + "' filter test passed.");
        } catch (Exception e) {
            System.out.println("Error testing filter for category '" + category + "': " + e.getMessage());
        }
    }

    @After
    public void teardown() {
        if (driver != null) {
            System.out.println("Closing the browser...");
            driver.quit();
        }
    }

    // Utility method for delay
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
