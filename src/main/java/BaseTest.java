
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {

    protected WebDriver driver;
    protected String baseUrl = "https://juice-shop.herokuapp.com/#/";

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);


    // Initialize WebDriver
    public void setup() {
        logger.info("Initializing WebDriver...");

        String driverPath = "/Users/thakur.r/SoarAssignment/untitled1/src/main/resources/ChromeDriver/chromedriver";
        System.setProperty("webdriver.chrome.driver", driverPath);

        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");  // Optional: Run in headless mode
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        logger.info("WebDriver initialized successfully.");
    }

    // Navigate to the base URL
    public void navigateToApp() throws InterruptedException {
        logger.info("Navigating to the URL: {}", baseUrl);
        driver.get(baseUrl);
        logger.info("Successfully navigated to: {}", baseUrl);
        Thread.sleep(2000);
    }

    // Cleanup: close the browser
    public void cleanup() {
        logger.info("Cleaning up the WebDriver.");
        if (driver != null) {
            driver.quit();
            logger.info("WebDriver closed successfully.");
        }
    }
}
