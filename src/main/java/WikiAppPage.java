import dev.failsafe.internal.util.*;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class WikiAppPage {
    private static final Logger logger = LoggerFactory.getLogger(WikiAppPage.class);

    private static AndroidDriver driver;

    // Constructor to initialize the driver
    public WikiAppPage(AndroidDriver driver) {
        WikiAppPage.driver = driver;
    }

    // Locator for the button using the AppiumBy.id
    private static final By closePopUp = AppiumBy.id("android:id/button1");
    private static final By myListIcon = AppiumBy.xpath("//android.widget.FrameLayout[@content-desc='My lists']");
    private static final By historyIcon = AppiumBy.xpath("//android.widget.FrameLayout[@content-desc='History']/android.view.ViewGroup");
    private static final By nearByIcon = AppiumBy.xpath("//android.widget.FrameLayout[@content-desc='Nearby']");
    private static final By browseIcon = AppiumBy.xpath("(//android.widget.ImageView[@resource-id='org.wikipedia.alpha:id/icon'])[1]");
    private static final By firstTopicLocator = AppiumBy.xpath("(//android.widget.TextView[@resource-id='org.wikipedia.alpha:id/view_featured_article_card_article_title'])");

    private static final By scrollAction = AppiumBy.androidUIAutomator(
            "new UiScrollable(new UiSelector().scrollable(true).instance(0)).flingToEnd(5)"
    );

    private static final By scrollUpTop = AppiumBy.androidUIAutomator(
            "new UiScrollable(new UiSelector().scrollable(true).instance(0)).flingToBeginning(5)"
    );

    private static final By searchHeader = AppiumBy.id("org.wikipedia.alpha:id/fragment_feed_header");
    private static final By searchTextBox= AppiumBy.id("org.wikipedia.alpha:id/search_src_text");
    private static final By clearTextBox= AppiumBy.id("org.wikipedia.alpha:id/search_close_btn");

    private static final By appSettingsIcon= AppiumBy.id("org.wikipedia.alpha:id/menu_overflow_button");
    private static final  By settingNavigationBtn= AppiumBy.id("org.wikipedia.alpha:id/explore_overflow_settings");

    private static final By navigateBackToHome= AppiumBy.xpath("//android.widget.ImageButton[@content-desc='Navigate up']");


    // Method to click on PopUp message after Login
    public static void closeAppPopUp() throws InterruptedException {
        WebElement button1 = driver.findElement(closePopUp);
        button1.click();
        Thread.sleep(2000);
    }

    // Method to click on "My Lists" icon
    public void clickMyLists() throws InterruptedException {
        logger.info("Clicking on 'My Lists' icon.");
        WebElement myListsIconBtn = waitForElementToBeClickable(myListIcon);
        myListsIconBtn.click();
        Thread.sleep(3000);
        logger.info("Navigated to 'My Lists' page.");
    }

    // Method to click on "History" icon
    public void clickHistory() throws InterruptedException {
        logger.info("Clicking on 'History' icon.");
        WebElement historyIconBtn = waitForElementToBeClickable(historyIcon);
        historyIconBtn.click();
        Thread.sleep(3000);
        logger.info("Navigated to 'History' page.");
    }

    // Method to click on "Nearby" icon
    public void clickNearby() throws InterruptedException {
        logger.info("Clicking on 'Nearby' icon.");
        WebElement nearbyIconBtn = waitForElementToBeClickable(nearByIcon);
        nearbyIconBtn.click();
        Thread.sleep(3000);
        logger.info("Navigated to 'Nearby' page.");
    }

    // Method to click on "Browse" icon
    public void clickBrowse() {
        logger.info("Clicking on 'Browse' icon.");
        WebElement browseIconBtn = waitForElementToBeClickable(browseIcon);
        browseIconBtn.click();
        logger.info("Navigated to the Home page.");
    }

    // Method to scroll to the last element, without waiting for specific elements to load
    public void scrollToLastElement() throws InterruptedException {
        boolean isAtEnd = false;
        int scrollCount = 0;
        int maxScrolls = 10;  // You can adjust this as needed
        String previousState = "";

        logger.info("Starting scroll to the last element. Max scrolls allowed: {}", maxScrolls);

        // Perform the scroll action until we hit the end or after max scrolls
        while (!isAtEnd && scrollCount < maxScrolls) {
            try {
                // Define the UiScrollable expression to perform a scroll action
                By scrollAction = AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true).instance(0)).flingToEnd(5)"
                );

                logger.debug("Performing scroll action... Attempt #{}", scrollCount + 1);

                // Try to find the scroll action element (if we can't find it, we're at the end)
                WebElement scrollElement = driver.findElement(scrollAction);

                // Check if the page state has changed to ensure the content is being loaded
                String currentState = driver.getPageSource();

                // If the page content hasn't changed after the scroll, we are at the end
                if (currentState.equals(previousState)) {
                    isAtEnd = true;
                    logger.info("Reached the end of the scrollable view.");
                } else {
                    // Otherwise, update the previousState and continue scrolling
                    previousState = currentState;
                }

                // Increase scroll count
                scrollCount++;
                logger.debug("Scroll attempt #{} completed.", scrollCount);

                // Optional: Sleep for a short time between scrolls to give time for content to load
                Thread.sleep(2000);  // Adjust based on your app's content loading speed

            } catch (NoSuchElementException e) {
                // If an exception occurs (e.g., NoSuchElementException), we are at the end of the scrollable view
                isAtEnd = true;
                logger.error("No such element found during scrolling. Reached the end of the scrollable view.", e);
            }
        }

        if (scrollCount >= maxScrolls) {
            logger.warn("Maximum scroll attempts reached. Stopping scroll.");
        }
    }


    public void scrollToTop() throws InterruptedException {
        boolean isAtTop = false;
        int scrollCount = 0;
        int maxScrolls = 10;  // You can adjust this as needed
        String previousState = "";

        // Perform the scroll action until we hit the top or after max scrolls
        while (!isAtTop && scrollCount < maxScrolls) {
            try {
                // Define the UiScrollable expression to perform a scroll action upwards
                By scrollAction = AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollBackward()"
                );

                // Try to find the scroll action element (if we can't find it, we're at the top)
                WebElement scrollElement = driver.findElement(scrollUpTop);

                // Check if the page state has changed to ensure the content is being loaded
                String currentState = driver.getPageSource();

                // If the page content hasn't changed after the scroll, we are at the top
                if (currentState.equals(previousState)) {
                    isAtTop = true;
                    System.out.println("Reached the top of the scrollable view.");
                } else {
                    // Otherwise, update the previousState and continue scrolling
                    previousState = currentState;
                }

                // Increase scroll count
                scrollCount++;

                // Optional: Sleep for a short time between scrolls to give time for content to load
                Thread.sleep(2000);  // Adjust based on your app's content loading speed
            } catch (NoSuchElementException e) {
                // If an exception occurs (e.g., NoSuchElementException), we are at the top of the scrollable view
                isAtTop = true;
                System.out.println("Reached the top of the scrollable view.");
            }
        }
    }

    // Method to search a Text in App Home Page
    public void searchHeaderBox() throws InterruptedException {
        logger.info("Clicking on the search header.");
        Thread.sleep(1000);
        WebElement searchHeaderBtn = waitForElementToBeClickable(AppiumBy.id("org.wikipedia.alpha:id/fragment_feed_header"));
        searchHeaderBtn.click();

        WebElement searchBox = waitForElementToBeVisible(searchTextBox);
        searchBox.clear();
        searchBox.sendKeys("New York");
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);

        logger.info("Searching for 'New York'.");

        // Wait for search results
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        List<WebElement> searchResults = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//android.widget.FrameLayout[@resource-id='org.wikipedia.alpha:id/view_list_card_header']")));

        Assert.assertTrue(searchResults.size() > 0, "No search results returned.");
        logger.info("Search results displayed.");

        WebElement closeSearchBox = waitForElementToBeClickable(clearTextBox);
        closeSearchBox.click();
        Thread.sleep(1000);
        logger.info("Closed the search Text box.");
        closeSearchBox.click();
        logger.info("Closed the search Bar.");
    }

    // Method to disable all switches in settings
    public void appSettingsDisable() throws InterruptedException {
        logger.info("Navigating to app settings.");
        Thread.sleep(2000);
        WebElement settingsIcon = waitForElementToBeClickable(appSettingsIcon);
        settingsIcon.click();

        Thread.sleep(1000);
        WebElement navigationSettingsBtn = waitForElementToBeClickable(settingNavigationBtn);
        navigationSettingsBtn.click();

        List<WebElement> switches = driver.findElements(By.xpath("//android.widget.Switch[@resource-id='org.wikipedia.alpha:id/switchWidget']"));

        for (int i = 0; i < switches.size(); i++) {
            WebElement switchElement = switches.get(i);

            if (switchElement.isDisplayed()) {
                logger.info("Disabling switch " + (i + 1) + ".");

                    switchElement.click();
                    logger.info("Switch " + (i + 1) + " disabled.");

            }
        }

        // Navigate back to the home page
        WebElement navigateBackBtn = waitForElementToBeClickable(navigateBackToHome);
        navigateBackBtn.click();
        logger.info("Navigated back to the home page.");
    }

    //*****************************************************************************************************************************************************************//
    //Helper Methods
    // Method to wait for an element to be clickable
    private WebElement waitForElementToBeClickable(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Method to wait for an element to be visible
    private WebElement waitForElementToBeVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

}
