import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class JuiceAppHomePage extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(JuiceAppHomePage.class);

    // Locators for Home Page
    private final By homePageLanding = By.xpath("//app-welcome-banner/div/h1/span");
    private final By btnDismiss = By.xpath("//span[text()='Dismiss']/parent::span/parent::button");
    private final By itemsCombobox = By.xpath("//mat-select[@role='combobox']");
    private final By itemsPerPageDropdown = By.xpath("//div[@role='listbox']/mat-option//span[contains(text(),'48')]");
    private final By itemCards = By.xpath("//mat-grid-tile");

    // Locators for Product Popup
    private final By firstProduct = By.xpath("//mat-grid-tile//div[contains(text(),'Apple Juice')]");
    private final By productPopup = By.xpath("//mat-dialog-container");
    private final By productImage = By.xpath("//mat-dialog-container//img[contains(@alt,'Apple Juice')]");
    private final By reviewSection = By.xpath("//span[contains(text(),'Reviews')]/parent::mat-panel-title");
    private final By reviewList = By.xpath("//mat-expansion-panel/div/div/div");
    private final By noReviewsMessage = By.xpath("//mat-dialog-container//mat-expansion-panel/div/div/div/span");
    private final By closePopupButton = By.xpath("//button[@aria-label='Close Dialog']");

    // Constructor
    public JuiceAppHomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Validate Home Page Title
    public void validateHomePageTitle() {
        logger.info("Validating Home Page title...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement homePageTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(homePageLanding));

        String actualTitle = homePageTitleElement.getText();
        if (!"Welcome to OWASP Juice Shop!".equals(actualTitle)) {
            logger.error("Home page title is incorrect. Found: {}", actualTitle);
            throw new AssertionError("Home page title is incorrect. Expected 'Welcome to OWASP Juice Shop' but found: " + actualTitle);
        }
        logger.info("Home page title is correct.");
    }

    // Dismiss Welcome Banner
    public void dismissWelcomeBanner() {
        logger.info("Clicking the dismiss button...");
        driver.findElement(btnDismiss).click();
        logger.info("Dismiss button clicked successfully.");
    }

    // Scroll to "Items per page" Element
    public void scrollToItemsPerPage() {
        logger.info("Scrolling to the 'Items per page' element...");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//div[contains(text(), 'Items per page:')]")));
        logger.info("Scrolled to 'Items per page' element.");
    }

    // Change Items Per Page to Maximum (48)
    public void selectItemsPerPageMax() {
        logger.info("Selecting '48' items per page...");
        driver.findElement(itemsCombobox).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement maxItemsOption = wait.until(ExpectedConditions.elementToBeClickable(itemsPerPageDropdown));
        maxItemsOption.click();
        logger.info("Selected '48' items per page.");
    }

    // Assert All Items are Displayed
    public void assertItemsAreDisplayed() {
        logger.info("Verifying if all items are displayed...");
        List<WebElement> items = driver.findElements(itemCards);
        if (items.isEmpty()) {
            logger.error("No items found on the page.");
            throw new AssertionError("No items found on the page.");
        }
        logger.info("Total items displayed: {}", items.size());
    }

    // Click on First Product: 'Apple Juice'
    public void clickOnFirstProduct() {
        logger.info("Clicking on the 'Apple Juice' product...");
        driver.findElement(firstProduct).click();
        logger.info("Clicked on 'Apple Juice' product.");
    }

    // Assert Product Popup and Image
    public void assertProductPopupAndImage() {
        logger.info("Verifying if product popup is displayed...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(productPopup));

        if (popup == null) {
            logger.error("Product popup did not appear.");
            throw new AssertionError("Product popup did not appear.");
        }

        logger.info("Verifying if product image is displayed...");
        WebElement productImg = driver.findElement(productImage);
        if (!productImg.isDisplayed()) {
            logger.error("Product image is missing.");
            throw new AssertionError("Product image is missing.");
        }

        logger.info("Product popup and image verified successfully.");
    }

    // Expand and Verify Reviews
    public void expandAndVerifyReviews() {
        logger.info("Checking if review section is available...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            WebElement reviewSectionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(reviewSection));

            if (reviewSectionElement.isDisplayed()) {
                logger.info("Review section found. Expanding...");
                Thread.sleep(3000);
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", reviewSectionElement);
                Thread.sleep(2000);

                // Wait for reviews to load
                WebElement reviewListElement = wait.until(ExpectedConditions.visibilityOfElementLocated(reviewList));

                try {
                    List<WebElement> reviews = reviewListElement.findElements(By.xpath("//div[@class='comment ng-star-inserted']/div/div"));
                    if (reviews.isEmpty()) {
                        logger.info("No reviews available for this product.");
                    } else {
                        logger.info("Number of reviews found: {}", reviews.size());
                    }
                } catch (Exception e) {
                    logger.error("Error while fetching reviews: {}", e.getMessage());
                }
            } else {
                logger.info("Review section not available.");
            }
        } catch (Exception e) {
            logger.error("Error while checking the review section: {}", e.getMessage());
        }
    }

    public void closeReviewPopup() {
        logger.info("Closing the review popup using JavaScript...");

        // Execute JavaScript to find the 'Close' button and click it
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement closeButton = driver.findElement(closePopupButton);

        // Use JavaScript to click the button
        js.executeScript("arguments[0].click();", closeButton);

        logger.info("Review popup closed successfully using JavaScript.");
    }
}
