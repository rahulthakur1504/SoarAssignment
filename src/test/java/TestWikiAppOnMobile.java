import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.options.BaseOptions;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URL;

public class TestWikiAppOnMobile extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestWikiAppOnMobile.class);
    private AndroidDriver driver;
    private WikiAppPage wikiAppPage;


  @BeforeTest
    public void setUp() throws Exception {


        Thread.sleep(5000);
        String appiumServerUrl = "http://127.0.0.1:4723";
        var options = new BaseOptions()
                .amend("appium:automationName", "uiautomator2")
                .amend("appium:platformName", "Android")
                .amend("appium:platformVersion", "15")
                .amend("appium:deviceName", "emulator-5554")
                .amend("appium:app", System.getProperty("user.dir") + "/App/WikipediaSample.apk")
                .amend("appium:newCommandTimeout", 3600)
                .amend("appium:connectHardwareKeyboard", true)
                .amend("appium:autoGrantPermissions", true);

        Thread.sleep(6000);
        driver = new AndroidDriver(new URL(appiumServerUrl), options);
        wikiAppPage = new WikiAppPage(driver);
        Thread.sleep(3000);

    }
    // Use Case -scroll
    //down to the end then click on three icons My lists, History and nearby
    // and wait for three seconds at every page. The last step the go back to home by click on browse icon then scroll up to First topic of app.

    @Test(priority = 0)
    public void testWikiApp() throws Exception {

        logger.info("Starting the WikiApp test...");

        // Step 1: Close app popup message
        logger.info("Closing app popup.");
        wikiAppPage.closeAppPopUp();

        // Step 2: Scroll to the last element
        logger.info("Scrolling to the last element.");
        wikiAppPage.scrollToLastElement();

        // Step 3: Click on "My Lists" icon
        logger.info("Clicking on 'My Lists' icon.");
        wikiAppPage.clickMyLists();

        // Step 4: Click on "History" icon
        logger.info("Clicking on 'History' icon.");
        wikiAppPage.clickHistory();

        // Step 5: Click on "Nearby" icon
        logger.info("Clicking on 'Nearby' icon.");
        wikiAppPage.clickNearby();

        // Step 6: Go back to the home screen by clicking the "Browse" icon
        logger.info("Going back to home screen by clicking 'Browse' icon.");
        wikiAppPage.clickBrowse();

        // Step 7: Scroll to the top
        logger.info("Scrolling to the top of the page.");
        wikiAppPage.scrollToTop();

        // Step 8: Search for "New York"
        logger.info("Performing search for 'New York'.");
        wikiAppPage.searchHeaderBox();

        // Step 9: Disable app settings switches
        logger.info("Disabling all switches in the app settings.");
        wikiAppPage.appSettingsDisable();

        logger.info("WikiApp test completed.");

    }

    // Close the app and quit the driver after all tests are finished
    @AfterClass
    public void tearDown() {
        logger.info("Closing the app and quitting the driver.");
        if (driver != null) {
            driver.quit();  // Properly quit the Appium session
        }
        logger.info("Driver session closed and resources released.");
    }



}
