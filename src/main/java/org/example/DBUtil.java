package org.example;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public  class DBUtil {
    // Define your database URL, username, and password
    private static final String DB_URL = "jdbc:mysql://localhost:3306/digitalIdentity";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";

    // Define your table name
    private static final String USER_TABLE = "user";

    // Store user data in the database
    public static void storeUserData(String username, String bio, String websiteUrl, String twitterUrl, String linkedinUrl, String telegramProfileUrl) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Create the SQL insert query
            String insertQuery = "INSERT INTO " + USER_TABLE + " (username, bio, website_url, twitter_url, linkedin_url, telegram_profile_url) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                // Set parameters for the insert query
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, bio);
                preparedStatement.setString(3, websiteUrl);
                preparedStatement.setString(4, twitterUrl);
                preparedStatement.setString(5, linkedinUrl);
                preparedStatement.setString(6, telegramProfileUrl);

                // Execute the insert query
                preparedStatement.executeUpdate();

                System.out.println("User data stored in the database successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle database-related exceptions
        }
    }

    public static void storeUserDataForThirdPartySignup(String userId, String name, String email) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Create the SQL insert query
            String insertQuery = "INSERT INTO " + USER_TABLE + " (userId, name) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                // Set parameters for the insert query
                preparedStatement.setString(1, userId);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, email);


                // Execute the insert query
                preparedStatement.executeUpdate();

                System.out.println("User data stored in the database successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle database-related exceptions
        }
    }

}
