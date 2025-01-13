import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.io.IOException;

public class TestLaunchJuiceApp extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestLaunchJuiceApp.class);
    private JuiceAppHomePage homePage;
    private UserRegistration userRegistrationPage;
    private UserDetailsGenerator userDetails;
    private AddProductAndCheckOut addProductAndCheckOutPage;

    @BeforeClass
    public void setUp() throws InterruptedException, IOException {
        logger.info("Initializing test setup...");

        // Setup WebDriver and navigate to the app
        setup();
        navigateToApp();
        logger.info("Juice Shop application is launched.");

        homePage = new JuiceAppHomePage(driver);
        userRegistrationPage = new UserRegistration(driver);// Initialize registration page object
        AddProductAndCheckOut addProductAndCheckOutPage = new AddProductAndCheckOut(driver);

        // Generate and save credentials
        String email = UserDetailsGenerator.generateRandomEmail();
        UserDetailsGenerator.saveCredentialsToProperties(email);
        logger.info("Generated and saved user credentials.");
    }

    @Test(priority = 1)
    public void testHomePageLanding() {
        logger.info("Starting Home Page Landing Test...");
        homePage.validateHomePageTitle();
        homePage.dismissWelcomeBanner();
        logger.info("Home Page Landing Test Passed.");
    }

    @Test(priority = 2)
    public void testItemsPerPage() {
        logger.info("Starting Items per Page Test...");
        homePage.scrollToItemsPerPage();
        homePage.selectItemsPerPageMax();
        homePage.assertItemsAreDisplayed();
        logger.info("Items per Page Test Passed.");
    }

    @Test(priority = 3)
    public void testProductPopup() {
        logger.info("Starting Product Popup Test...");
        homePage.clickOnFirstProduct();
        homePage.assertProductPopupAndImage();
        logger.info("Product Popup Test Passed.");
    }

    @Test(priority = 4)
    public void testReviews() {
        logger.info("Starting Reviews Test...");
        homePage.expandAndVerifyReviews();
        homePage.closeReviewPopup();
        logger.info("Reviews Test Passed.");
    }

    @Test(priority = 5)
    public void TestUserRegistrationValidation() throws InterruptedException {
        logger.info("Starting User Registration Test...");
        userRegistrationPage.navigateToRegistrationPage();
        userRegistrationPage.assertInputValidationForUserRegistration();
        logger.info("User Registration Test Passed.");
    }

    @Test(priority = 6)
    public void TestNewUserRegistration() throws InterruptedException, IOException {
        logger.info("Starting New User Registration Test...");

        // Read credentials from properties
        String email = userRegistrationPage.readCredentialsFromProperties("email");
        String password = userRegistrationPage.readCredentialsFromProperties("password");
        userRegistrationPage.userRegister(email, password, password, "This Is Security Answer");
        Thread.sleep(2000);

        logger.info("New User Registration completed.");

        logger.info("Validating Registration Success Message...");
        userRegistrationPage.assertRegistrationSuccessMessage();
        logger.info("Successfully validated Registration Success Message.");
    }

    @Test(priority = 7)
    public void TestLoginUser() throws InterruptedException, IOException {
        logger.info("Starting Login User Test...");
        userRegistrationPage.loginWithValidCredentials();

    }

    @Test(priority = 8)
    public void TestAddingProductToCart() throws InterruptedException, IOException {
        AddProductAndCheckOut.addRandomProductsToCart();
        AddProductAndCheckOut.modifyProductBasket();
        AddProductAndCheckOut.chekoutProduct();

        logger.info("Starting Payment And Checkout Test...");
        AddProductAndCheckOut.paymentMethod();
        AddProductAndCheckOut.reviewOrderSummaryPage();
    }


    @AfterClass
    public void tearDown() {
        logger.info("Cleaning up after tests...");
        cleanup();  // Close WebDriver and clean up
        logger.info("Test execution completed.");
    }
}