import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class UserRegistration extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistration.class);
    private WebDriver driver;

    private final By accountBtn = By.xpath("//span[contains(text(),'Account ')]/parent::span/parent::button");
    private final By loginBtn= By.xpath("//button[@aria-label='Go to login page']");
    private final By registrationLink = By.xpath("//div[@id='newCustomerLink']/a");

    private final By emailField = By.xpath("//input[@aria-label='Email address field']");
    private final By passwordField = By.xpath("//input[@aria-label='Field for the password']");
    private final By confirmPasswordField = By.xpath("//input[@aria-label='Field to confirm the password']");
    private final By showPasswordBtn= By.xpath("//span[contains(text(),'Show password advice')]/parent::label/span/input");
    private final By securityQuestionComoBox= By.xpath("//mat-select[@aria-label='Selection list for the security question']");
    private final By selectSecurityQuestion = By.xpath("//div[@role='listbox']/mat-option/span");
    private final By securityAnswer= By.xpath("//input[@aria-label='Field for the answer to the security question']");
    private final By registerBtn= By.xpath("//button[@id='registerButton']");
    private final By registrationSuccessMessage = By.xpath("//span[contains(text(),'Registration completed successfully. You can now log in.')]");

    //Error Message
    private final By emailFieldErrorMessage = By.xpath("//mat-error[contains(text(),'Please provide an email address.')]");
    private final By passwordFieldErrorMessage = By.xpath("//mat-error[contains(text(),'Please provide a password.')]");
    private final By confirmPasswordFieldErrorMessage = By.xpath("//mat-error[contains(text(),' Please repeat your password. ')]");
    private final By securityQuestionErrorMessage = By.xpath("//mat-error[contains(text(),' Please select a security question. ')]");
    private final By securityAnswerErrorMessage= By.xpath("//mat-error[contains(text(),' Please provide an answer to your security question. ')]");

    // Locators for login page
    private final By loginFormEmailField = By.xpath("//input[@aria-label='Text field for the login email']");
    private final By loginFormPasswordField = By.xpath("//input[@aria-label='Text field for the login password']");
    private final By loginButton = By.xpath("//button[@id='loginButton']");




    public UserRegistration(WebDriver driver) {
        this.driver = driver;
    }

    // Method to navigate to the Registration Page
    public void navigateToRegistrationPage() {
        logger.info("Navigating to Registration Page...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountBtn));
        driver.findElement(accountBtn).click();
        logger.info("Account button clicked.");

        wait.until(ExpectedConditions.visibilityOfElementLocated(loginBtn));
        driver.findElement(loginBtn).click();
        logger.info("Login button clicked.");

        wait.until(ExpectedConditions.visibilityOfElementLocated(registrationLink));
        driver.findElement(registrationLink).click();
        logger.info("Registration link clicked, now on the registration page.");
    }

    // Method to assert input validation for required fields
    public void assertInputValidationForUserRegistration() throws InterruptedException {
        logger.info("Asserting input validation for required fields...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Trigger validation by interacting with each field
        logger.info("Clicking on email field to trigger validation...");
        driver.findElement(emailField).click();

        logger.info("Clicking on password field to trigger validation...");
        driver.findElement(passwordField).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordField));
        logger.info("Clicking on confirm password field to trigger validation...");
        driver.findElement(confirmPasswordField).click();

        driver.findElement(confirmPasswordField).sendKeys(Keys.TAB);
        driver.findElement(showPasswordBtn).sendKeys(Keys.TAB);

        wait.until(ExpectedConditions.visibilityOfElementLocated(securityQuestionComoBox));
        driver.findElement(securityQuestionComoBox).sendKeys(Keys.TAB);

        wait.until(ExpectedConditions.visibilityOfElementLocated(securityAnswer));
        driver.findElement(securityAnswer).click();
        driver.findElement(securityAnswer).sendKeys(Keys.TAB);

        // Validate email field
        WebElement emailValidationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(emailFieldErrorMessage));
        if (!emailValidationMessage.isDisplayed()) {
            logger.error("Validation message for email is not displayed. Expected message: 'Please provide an email address.'");
            throw new AssertionError("Validation message for email was not displayed.");
        }
        logger.info("Validation message for email is displayed: {}", emailValidationMessage.getText());

        // Validate password field
        WebElement passwordValidationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordFieldErrorMessage));
        if (!passwordValidationMessage.isDisplayed()) {
            logger.error("Validation message for password is not displayed. Expected message: 'Please provide a password.'");
            throw new AssertionError("Validation message for password was not displayed.");
        }
        logger.info("Validation message for password is displayed: {}", passwordValidationMessage.getText());

        // Validate confirm password field
        WebElement confirmPasswordValidationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordFieldErrorMessage));
        if (!confirmPasswordValidationMessage.isDisplayed()) {
            logger.error("Validation message for confirm password is not displayed. Expected message: 'Please confirm your password.'");
            throw new AssertionError("Validation message for confirm password was not displayed.");
        }
        logger.info("Validation message for confirm password is displayed: {}", confirmPasswordValidationMessage.getText());

        // Validate security question field
        WebElement securityQuestionValidationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(securityQuestionErrorMessage));
        if (!securityQuestionValidationMessage.isDisplayed()) {
            logger.error("Validation message for security question is not displayed. Expected message: 'Please select a security question.'");
            throw new AssertionError("Validation message for security question was not displayed.");
        }
        logger.info("Validation message for security question is displayed: {}", securityQuestionValidationMessage.getText());

        // Validate security answer field
        WebElement securityAnswerValidationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(securityAnswerErrorMessage));
        if (!securityAnswerValidationMessage.isDisplayed()) {
            logger.error("Validation message for security answer is not displayed. Expected message: 'Please provide an answer to your security question.'");
            throw new AssertionError("Validation message for security answer was not displayed.");
        }
        logger.info("Validation message for security answer is displayed: {}", securityAnswerValidationMessage.getText());

        logger.info("All validation messages displayed as expected.");
    }

    // Method to select a random security question
    private void selectRandomSecurityQuestion() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        logger.info("Clicking on the security question combo box to display options...");
        driver.findElement(securityQuestionComoBox).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(selectSecurityQuestion));
        List<WebElement> options = driver.findElements(selectSecurityQuestion);

        if (!options.isEmpty()) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(options.size());
            WebElement randomOption = options.get(randomIndex);

            logger.info("Randomly selecting security question: {}", randomOption.getText());
            randomOption.click();
        } else {
            logger.error("No security questions found!");
        }
    }

    // Method to register the user using generated credentials
    public void userRegister(String email, String password, String confirmPassword, String securityAnswerText) throws InterruptedException {
        logger.info("Registering user with email: {}", email);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(confirmPasswordField).sendKeys(confirmPassword);

        selectRandomSecurityQuestion();
        driver.findElement(securityAnswer).sendKeys(securityAnswerText);
        Thread.sleep(2000);
        driver.findElement(registerBtn).click();

        logger.info("Registration form submitted for email: {}", email);
    }

    // Method to assert the registration success message
    public void assertRegistrationSuccessMessage() {
        logger.info("Validating registration success message...");

        String expectedMessage = "Registration completed successfully. You can now log in.";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement successMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(registrationSuccessMessage));
        String actualMessage = successMessageElement.getText();

        if (actualMessage.equals(expectedMessage)) {
            logger.info("Registration success message is displayed as expected: {}", actualMessage);
        } else {
            logger.error("Registration success message mismatch. Expected: {}, but got: {}", expectedMessage, actualMessage);
            throw new AssertionError("Registration success message assertion failed.");
        }
    }

    // Method to read credentials from the properties file
    public String readCredentialsFromProperties(String key) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("userDetails.properties")) {
            properties.load(fis);
        }
        return properties.getProperty(key);
    }

    // Method to login via valid credentials
    public void loginWithValidCredentials() throws IOException, InterruptedException {
        logger.info("Logging in with valid credentials...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Get credentials from properties file
        String email = readCredentialsFromProperties("email");
        String password = readCredentialsFromProperties("password");

        // Fill out the login form
        driver.findElement(loginFormEmailField).sendKeys(email);
        Thread.sleep(2000);
        driver.findElement(loginFormPasswordField).sendKeys(password);
        Thread.sleep(2000);
        driver.findElement(loginButton).click();
        Thread.sleep(2000);

        logger.info("Login attempt completed for email: {}", email);

        // Wait for successful login page or dashboard
        wait.until(ExpectedConditions.urlContains("search"));
        logger.info("Successfully logged in and redirected to the dashboard.");
    }


}




