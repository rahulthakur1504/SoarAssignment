import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class AddProductAndCheckOut extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(AddProductAndCheckOut.class);
    private static WebDriver driver;

    private static String priceForAllProduct = null;
    private static String deliveryFeesForDeliverySelected = null;


    private static final By availableProducts = By.xpath("//mat-grid-list/div/mat-grid-tile");
    private static final By productAddToCartBtn = By.xpath(".//span[contains(text(),'Add to Basket')]/parent::span/parent::button");// Add to Basket button for each product
    private static final By successPopup = By.xpath("//simple-snack-bar//span");
    private static final By cartItemCount = By.xpath("//span[contains(text(),' Your Basket')]/parent::span//span[2]");
    private static final By productBasketBtn = By.xpath("//button[@aria-label='Show the shopping cart']");
    private static final By increaseQuantityBtn = By.xpath("//mat-table/mat-row/mat-cell[3]/button[2]");
    private static final By quantityLimitPopup = By.xpath("//simple-snack-bar//span[contains(text(),'You can order only up to 1 items of this product.')]");
    // private static final By deleteProductBtn = By.xpath("//parent::mat-cell//parent::mat-row/mat-cell[5]/button");
    private static final By deleteProductBtn = By.xpath("//app-purchase-basket/mat-table/mat-row[1]/mat-cell[5]/button");
    private static final By totalCartValue = By.xpath("//*[@id='price']");
    private static final By checkOutBtn = By.xpath("//button[@id='checkoutButton']");

    //Loctaors for Adding Delivery Address

    private static final By deliverAddressBtn = By.xpath("//button[@aria-label='Add a new address']");
    private static final By countryField = By.xpath("//input[@placeholder='Please provide a country.']");
    private static final By UserName = By.xpath("//input[@placeholder='Please provide a name.']");
    private static final By mobileNumberField = By.xpath("//input[@placeholder='Please provide a mobile number.']");
    private static final By zipCodeField = By.xpath("//input[@placeholder='Please provide a ZIP code.']");
    private static final By addressField = By.xpath("//*[@id='address']");
    private static final By cityField = By.xpath("//input[@placeholder='Please provide a city.']");
    private static final By stateField = By.xpath("//input[@placeholder='Please provide a state.']");
    private static final By submitBtn = By.xpath("//button[@id='submitButton']");
    private static final By selectAddedAdress = By.xpath("//mat-radio-button");
    private static final By continueToPayment = By.xpath("//button[@aria-label='Proceed to payment selection']");
    private static final By deliveryOptions = By.xpath("//mat-table//mat-row");
    private static final By paymentContinueBtn = By.xpath("//button[@aria-label='Proceed to delivery method selection']");

    //Payment Methods

    private static final By walletBalance = By.xpath("//span[contains(text(),'Wallet Balance')]/parent::b//span[2]");
    private static final By addCardButton = By.xpath("//mat-panel-title[contains(text(),' Add new card ')]");
    private static final By cardHolderName = By.xpath("//mat-label[contains(text(),'Name')]/parent::label/parent::span/parent::div/input");
    private static final By cardNumberField = By.xpath("//mat-label[contains(text(),'Card Number')]/parent::label/parent::span/parent::div/input");
    private static final By expiryMonthField = By.xpath("//mat-label[contains(text(),'Expiry Month')]/parent::label/parent::span/parent::div/select");
    private static final By expiryYearField = By.xpath("//mat-label[contains(text(),'Expiry Year')]/parent::label/parent::span/parent::div/select");
    private static final By submitPaymentBtn = By.xpath("//button[@id='submitButton']");

    //Order Preview and Order Summary
    private static final By proceedToReviewOrderBtn = By.xpath("//button[@aria-label='Proceed to review']");
    private static final By orderSummaryPage = By.xpath("//div[contains(text(),'Order Summary')]");
    private static final By orderSummaryPrice = By.xpath("//table/tr[4]/td[2]");
    private static final By completePurchaseBtn = By.xpath("//button[@aria-label='Complete your purchase']");
    private static final By purchaseSuccessMessage = By.xpath("//h1");


    private static Properties addressProperties = new Properties();

    public AddProductAndCheckOut(WebDriver driver) {
        AddProductAndCheckOut.driver = driver;
        loadAddressProperties();
    }

    // Utility method to check if an element is present
    public static boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true; // Element is present
        } catch (Exception e) {
            return false; // Element is not found
        }
    }

    // Load the address properties from the properties file
    private static void loadAddressProperties() {
        try (InputStream inputStream = AddProductAndCheckOut.class.getClassLoader().getResourceAsStream("deliveryAddress.properties")) {
            if (inputStream == null) {
                logger.error("Unable to find deliveryAddress.properties file");
                return;
            }
            addressProperties.load(inputStream);
            logger.info("Address properties loaded successfully.");
        } catch (IOException e) {
            logger.error("Error loading address properties file: {}", e.getMessage());
        }
    }

    public static void addRandomProductsToCart() throws InterruptedException {
        Thread.sleep(3000);
        // Get the list of all product elements on the product page
        List<WebElement> productList = driver.findElements(availableProducts);

        if (productList.size() < 5) {
            throw new AssertionError("Not enough products available on the page.");
        }
        Random rand = new Random();

        // Select 5 random products from the list
        for (int i = 0; i < 5; i++) {
            int randomIndex = rand.nextInt(productList.size());
            WebElement selectedProduct = productList.get(randomIndex);

            // Get the product name
            String productName = selectedProduct.findElement(By.xpath(".//mat-card/div[@aria-label='Click for more information about the product']/div[2]/div")).getText();
            logger.info("Adding product to cart: {}", productName);

            try {
                // Find the 'Add to Basket' button within the product container
                Thread.sleep(2000);
                WebElement addToCartButton = selectedProduct.findElement(productAddToCartBtn);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartButton);
                logger.info("Product '{}' added to the cart.", productName);
                Thread.sleep(1000);

                // Wait for the success popup to appear and verify the product name in the popup
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                wait.until(ExpectedConditions.visibilityOfElementLocated(successPopup));
                WebElement successPopupMessage = driver.findElement(successPopup);
                String popupMessage = successPopupMessage.getText();
                System.out.println(popupMessage);
                Thread.sleep(2000);

                // Check if the popup message contains "We are out of stock"
                if (popupMessage.toLowerCase().contains("we are out of stock")) {
                    logger.info("Product '{}' is out of stock. Moving to the next product.", productName);
                    continue;  // Skip to the next product
                }

                if (!popupMessage.contains(productName)) {
                    throw new AssertionError("Success popup message does not contain the expected product name.");
                }
                logger.info("Success popup for '{}' verified.", productName);

                // Assert that the cart number is updated correctly after each product is added
                assertCartItemCount(i + 1);

                // Remove the selected product from the list so it can't be selected again
                productList.remove(randomIndex);  // Remove the selected product to avoid duplicates
            } catch (Exception e) {
                logger.error("Error adding product '{}' to the cart: {}", productName, e.getMessage());
            }
        }

        // After adding 5 products, assert that the cart number has been updated to 5
        assertCartItemCount(5);
    }

    // Method to assert the cart item count
    public static void assertCartItemCount(int expectedCount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for the cart item count to be visible
        WebElement cartItemCountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cartItemCount));

        // Get the actual count from the UI and compare
        String actualCartCount = cartItemCountElement.getText();
        logger.info("Verifying cart item count: Expected: {}, Actual: {}", expectedCount, actualCartCount);

        if (!actualCartCount.equals(String.valueOf(expectedCount))) {
            throw new AssertionError("Cart count mismatch. Expected " + expectedCount + " but got " + actualCartCount);
        } else {
            logger.info("Cart count is as expected: {}", expectedCount);
        }
    }

    public static void modifyProductBasket() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.elementToBeClickable(productBasketBtn)).click();

        // Verify that random products are added in the basket
        WebDriverWait waitForBasket = new WebDriverWait(driver, Duration.ofSeconds(30));
        List<WebElement> basketItems = waitForBasket.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//mat-table/mat-row")));

        if (basketItems.size() < 5) {
            throw new AssertionError("Not all 5 products are present in the basket.");
        }

        boolean quantityIncreased = false;
        String priceAfterIncrease = null;
        int attemptCount = 0;  // Counter to track the number of attempts made
        Random rand = new Random();

        while (!quantityIncreased && attemptCount < basketItems.size()) {
            // Select a random product from the basket to modify its quantity
            WebElement selectedBasketItem = basketItems.get(rand.nextInt(basketItems.size()));

            try {
                // Try to click the increase quantity button
                WebElement increaseQuantityButton = selectedBasketItem.findElement(increaseQuantityBtn);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", increaseQuantityButton);
                Thread.sleep(2000);

                // Check if the "Only 1 product can be added" popup appears
                if (isElementPresent(quantityLimitPopup)) {
                    logger.info("Popup displayed: 'Only 1 product can be added'. Skipping to the next product.");

                    // Skip to the next product by incrementing attemptCount and continue
                    attemptCount++;
                    continue;  // Skip to the next product
                }

                // Capture the total price after increasing the quantity
                WebElement totalPriceElement = driver.findElement(totalCartValue);
                priceAfterIncrease = totalPriceElement.getText();
                logger.info("Total Price After Increasing Quantity: {}", priceAfterIncrease);

                // Set flag to true to break the loop after successful quantity increase
                quantityIncreased = true;

            } catch (Exception e) {
                logger.error("Error increasing quantity for product: {}", e.getMessage());
                // Skip to the next product if any error occurs
                attemptCount++;
                continue;  // Continue to the next product in case of error
            }

            // If quantity is successfully increased, proceed with deleting the same product
            if (quantityIncreased) {
                // Wait for a brief moment before trying to delete
                Thread.sleep(2000);
                driver.navigate().refresh();
                Thread.sleep(3000);

                // Locate and click the delete button
                WebElement deleteButton = driver.findElement(deleteProductBtn);
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteButton);

                } catch (StaleElementReferenceException e) {
                    // Handle stale element reference by re-locating the delete button
                    deleteButton = selectedBasketItem.findElement(deleteProductBtn);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteButton);  // Try clicking again
                }
                Thread.sleep(2000);

                // Get the final price after cart modification
                getAllProductPrice(priceAfterIncrease);
            }
        }

        // If after several attempts no quantity was increased, log an error
        if (!quantityIncreased) {
            logger.error("Unable to increase the quantity of any product after {} attempts.", attemptCount);
            throw new AssertionError("Failed to increase quantity for any product in the basket.");
        }
    }

    public static void chekoutProduct() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement checkoutBtn = driver.findElement(checkOutBtn);
        checkoutBtn.click();
        wait.until(ExpectedConditions.elementToBeClickable(deliverAddressBtn)).click();

        //Method to Fill Delivery Address
        fillAddressForm();

        //Click on Submit to Save address
        wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();

        //Select Added Address
        wait.until(ExpectedConditions.elementToBeClickable(selectAddedAdress)).click();
        Thread.sleep(2000);

        //Proceed to Payment Page
        wait.until(ExpectedConditions.elementToBeClickable(continueToPayment)).click();

        //Method to Select Deliver Type as Standard, One Day or Fast
        selectRandomDeliveryOption();


    }

    private static void fillAddressForm() throws InterruptedException {
        // Fill in the address details from the properties file
        WebElement countryFieldElement = driver.findElement(countryField);
        countryFieldElement.sendKeys(addressProperties.getProperty("country"));

        WebElement nameFieldElement = driver.findElement(UserName);
        nameFieldElement.sendKeys(addressProperties.getProperty("name"));

        WebElement mobileNumberFieldElement = driver.findElement(mobileNumberField);
        mobileNumberFieldElement.sendKeys(addressProperties.getProperty("mobile"));

        WebElement zipCodeFieldElement = driver.findElement(zipCodeField);
        zipCodeFieldElement.sendKeys(addressProperties.getProperty("zipcode"));

        WebElement addressFieldElement = driver.findElement(addressField);
        addressFieldElement.sendKeys(addressProperties.getProperty("address"));

        WebElement cityFieldElement = driver.findElement(cityField);
        cityFieldElement.sendKeys(addressProperties.getProperty("city"));

        WebElement stateFieldElement = driver.findElement(stateField);
        stateFieldElement.sendKeys(addressProperties.getProperty("state"));
        Thread.sleep(2000);
        logger.info("Address form filled out successfully with data from properties file.");
    }

    // Method to select a random delivery option (One Day, Fast, Standard)

    private static void selectRandomDeliveryOption() throws InterruptedException {
        Thread.sleep(2000);
        List<WebElement> deliveryOptionsList = driver.findElements(deliveryOptions);

        if (deliveryOptionsList.isEmpty()) {
            throw new AssertionError("No delivery options available.");
        }

        // Randomly select an option from the list
        WebElement selectedOption;
        Random rand = new Random();
        int randomIndex = rand.nextInt(deliveryOptionsList.size());

        selectedOption = deliveryOptionsList.get(randomIndex);

        // Capture the name of the selected delivery option
        String selectedOptionText = selectedOption.findElement(By.xpath(".//mat-cell[2]")).getText();
        logger.info("Selected Delivery Option: {}", selectedOptionText);

        //Method for Get Delivery Fees
        getDeliveryFees(selectedOption);

        //Capture the estimated delivery days for the selected delivery option
        WebElement deliveryDayElement = selectedOption.findElement(By.xpath(".//mat-cell[4]"));
        String deliveryDayText = deliveryDayElement.getText();
        logger.info("Estimated Delivery Day for '{}': {}", selectedOptionText, deliveryDayText);


        // Click on the radio button to select the delivery option
        WebElement radioButton = selectedOption.findElement(By.xpath(".//mat-radio-button"));
        radioButton.click();

        // Log the selection
        logger.info("Selected Delivery Option '{}' with Price: {} and Estimated Delivery:", selectedOptionText, deliveryDayText);

    }

    public static void paymentMethod() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Click on the "Continue" button to proceed to the payment page
        WebElement continueButton = driver.findElement(paymentContinueBtn);
        continueButton.click();
        Thread.sleep(3000);
        logger.info("Clicked on Continue button to move to the Payment Page.");

        // Check if wallet balance is zero
        WebElement walletBalanceElement = driver.findElement(walletBalance);
        String walletBalanceText = walletBalanceElement.getText();

        if (walletBalanceText.contains("0")) {
            logger.info("Wallet balance is 0. Proceeding to add a credit card.");

            // Add credit card
            addCreditCardInformation();

            //Proceed to review Order
            wait.until(ExpectedConditions.elementToBeClickable(proceedToReviewOrderBtn));
            WebElement reviewOrderBtn = driver.findElement(proceedToReviewOrderBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", reviewOrderBtn);
            Thread.sleep(1000);

            logger.info("Proceeding with payment after adding the credit card.");
        } else {
            logger.info("Wallet balance is sufficient. Proceeding with payment.");
        }

    }

    private static void addCreditCardInformation() throws InterruptedException {
        // Click on 'Add Credit Card' button
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement addCardBtnElement = wait.until(ExpectedConditions.elementToBeClickable(addCardButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addCardBtnElement);
        Thread.sleep(3000);

        // Wait for the credit card form to be visible and fill the details
        WebElement nameOnCard = driver.findElement(cardHolderName);
        nameOnCard.click();
        Thread.sleep(2000);
        nameOnCard.sendKeys("John Automation Test Card");

        WebElement cardNumberFieldElement = driver.findElement(cardNumberField);
        cardNumberFieldElement.click();
        cardNumberFieldElement.sendKeys(generateRandomCardNumber());

        WebElement expiryMonthFieldElement = driver.findElement(expiryMonthField);
        selectRandomExpiryMonth(expiryMonthFieldElement);

        WebElement expiryYearFieldElement = driver.findElement(expiryYearField);
        selectRandomExpiryYear(expiryYearFieldElement);
        logger.info("Credit card details have been successfully entered.");

        //Save Card
        Thread.sleep(3000);
        WebElement paymentContinueButton = driver.findElement(submitPaymentBtn);
        paymentContinueButton.click();
        Thread.sleep(2000);
        logger.info("Credit card details have been successfully Saved.");

        //Selecting Save Credit Card
        WebElement selectAddedCreditCard = driver.findElement(By.xpath("//mat-row/mat-cell/mat-radio-button"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectAddedCreditCard);
        Thread.sleep(1000);
        logger.info("Selected Added Credit Card");
    }

    // Method to generate a random credit card number
    private static String generateRandomCardNumber() {
        Random random = new Random();
        return "4" + String.format("%015d", random.nextLong(1000000000000000L));
    }

    // Method to generate a random expiry date (MM/YY format)
    private static void selectRandomExpiryMonth(WebElement expiryMonthFieldElement) {
        Select monthDropdown = new Select(expiryMonthFieldElement);
        List<WebElement> monthOptions = monthDropdown.getOptions();

        // Get a random index from the list of available month options (excluding the first option which may be a placeholder)
        Random random = new Random();
        int randomIndex = random.nextInt(monthOptions.size() - 1) + 1;  // Exclude the first option (typically 'Select Month')

        // Select the random month from the options list
        String selectedMonth = monthOptions.get(randomIndex).getText();
        monthDropdown.selectByVisibleText(selectedMonth);
        logger.info("Selected random expiry month: {}", selectedMonth);
    }

    // Method to select a random expiry year from available options
    private static void selectRandomExpiryYear(WebElement expiryYearFieldElement) {
        Select yearDropdown = new Select(expiryYearFieldElement);
        List<WebElement> yearOptions = yearDropdown.getOptions();

        // Get a random index from the list of available year options (excluding the first option which may be a placeholder)
        Random random = new Random();
        int randomIndex = random.nextInt(yearOptions.size() - 1) + 1;  // Exclude the first option (typically 'Select Year')

        // Select the random year from the options list
        String selectedYear = yearOptions.get(randomIndex).getText();
        yearDropdown.selectByVisibleText(selectedYear);
        logger.info("Selected random expiry year: {}", selectedYear);
    }

    // Helper Class for Capturing Price after we Modify the Cart
    public static void getAllProductPrice(String priceIncreaseValue) throws InterruptedException {
        // Capture the total price after deleting the product
        WebDriverWait waitForUpdatedPrice = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement updatedTotalPriceElement = waitForUpdatedPrice.until(ExpectedConditions.visibilityOfElementLocated(totalCartValue));
        String priceAfterDelete = updatedTotalPriceElement.getText();

        logger.info("Total Price After Deleting Product: {}", priceAfterDelete);

        // Assert that the price has changed
        if (priceIncreaseValue.equals(priceAfterDelete)) {
            throw new AssertionError("Total price did not change after deleting the product.");
        }
        priceForAllProduct = priceAfterDelete;
    }

    public static void getDeliveryFees(WebElement ele) {
        // Capture the price for the selected delivery option
        WebElement deliveryPriceElement = ele.findElement(By.xpath(".//mat-cell[3]"));
        String deliveryPrice = deliveryPriceElement.getText();
        logger.info("Delivery Price for '{}': {}", ele, deliveryPrice);
        deliveryFeesForDeliverySelected = deliveryPrice;
    }

    public static void reviewOrderSummaryPage() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(orderSummaryPage));

        // Calculate the expected total price
        double calculatedTotalPrice = Double.parseDouble(priceForAllProduct.replaceAll("[^0-9.]", ""))
                + Double.parseDouble(deliveryFeesForDeliverySelected.replaceAll("[^0-9.]", ""));

        // Round the calculated total price to 2 decimal places
        BigDecimal roundedCalculatedPrice = new BigDecimal(calculatedTotalPrice).setScale(2, RoundingMode.HALF_UP);

        // Capture the total price from the Order Summary page
        WebElement orderSummaryPriceElement = driver.findElement(orderSummaryPrice);
        String totalPriceText = orderSummaryPriceElement.getText();
        double displayedTotalPrice = Double.parseDouble(totalPriceText.replaceAll("[^0-9.]", ""));

        // Round the displayed total price to 2 decimal places
        BigDecimal roundedDisplayedPrice = new BigDecimal(displayedTotalPrice).setScale(2, RoundingMode.HALF_UP);

        // Log the calculated vs displayed total price
        logger.info("Calculated Total Price (Price After Delete + Delivery Fee): {}", roundedCalculatedPrice);
        logger.info("Displayed Total Price on Order Summary Page: {}", roundedDisplayedPrice);

        // Compare the calculated total price with the displayed total price
        if (roundedCalculatedPrice.compareTo(roundedDisplayedPrice) == 0) {
            logger.info("Total Price is correct. Calculated Total Price: {} is equal to the displayed Total Price: {}",
                    roundedCalculatedPrice, roundedDisplayedPrice);
        } else {
            logger.error("Total Price mismatch! Calculated Total Price: {} is not equal to the displayed Total Price: {}",
                    roundedCalculatedPrice, roundedDisplayedPrice);
            throw new AssertionError("Total Price mismatch. Expected: " + roundedCalculatedPrice + " but got: " + roundedDisplayedPrice);
        }

        // Click on the "Purchase" button
        WebElement purchaseButton = driver.findElement(completePurchaseBtn);
        purchaseButton.click();
        Thread.sleep(3000);

        // Wait for the purchase success message to appear
        WebDriverWait successWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement successMessageElement = successWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Thank you for your purchase!')]")));

        // Capture and log the success message
        String successMessage = successMessageElement.getText();
        logger.info("Purchase Success Message: {}", successMessage);

        // Assert the purchase success message
        if (successMessage.contains("Thank you for your purchase!")) {
            logger.info("Purchase was successful.");
        } else {
            logger.error("Purchase was not successful. Message: {}", successMessage);
            throw new AssertionError("Purchase failed. Expected success message not found.");
        }
    }
}

