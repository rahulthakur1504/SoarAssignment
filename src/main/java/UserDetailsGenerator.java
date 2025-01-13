import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.security.SecureRandom;

public class UserDetailsGenerator {
    // Helper function to generate random email
    public static String generateRandomEmail() {
        int randomNumber = (int) (Math.random() * 10000); // Random number between 0-9999
        return "user" + randomNumber + "@gmail.com";
    }

    // Helper function to generate secure random password
//    public static String generatePassword() {
//        SecureRandom random = new SecureRandom();
//        StringBuilder password = new StringBuilder();
//
//        String lowercase = "abcdefghijklmnopqrstuvwxyz";
//        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        String digits = "0123456789";
//        String specialChars = "!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";
//
//        // Ensure at least one lowercase, one uppercase, one digit, and one special character
//        password.append(lowercase.charAt(random.nextInt(lowercase.length())));
//        password.append(uppercase.charAt(random.nextInt(uppercase.length())));
//        password.append(digits.charAt(random.nextInt(digits.length())));
//        password.append(specialChars.charAt(random.nextInt(specialChars.length())));
//
//        // Fill the rest with random characters from all categories
//        String allChars = lowercase + uppercase + digits + specialChars;
//        while (password.length() < 8) {
//            password.append(allChars.charAt(random.nextInt(allChars.length())));
//        }
//
//        // Shuffle the password to make it more random
//        String passwordStr = password.toString();
//        StringBuilder shuffledPassword = new StringBuilder(passwordStr);
//        for (int i = 0; i < passwordStr.length(); i++) {
//            int randomIndex = random.nextInt(passwordStr.length());
//            char temp = shuffledPassword.charAt(i);
//            shuffledPassword.setCharAt(i, shuffledPassword.charAt(randomIndex));
//            shuffledPassword.setCharAt(randomIndex, temp);
//        }
//
//        return shuffledPassword.toString();
//    }

    // Write email and password to properties file
    public static void saveCredentialsToProperties(String email) throws IOException {
        String password="Test@1234";
        Properties properties = new Properties();
        properties.setProperty("email", email);
        properties.setProperty("password",password);

        try (FileOutputStream fos = new FileOutputStream("userDetails.properties")) {
            properties.store(fos, "Generated User Details");
        }
    }
}

