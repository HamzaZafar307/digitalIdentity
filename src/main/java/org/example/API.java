package org.example;
import com.github.pwrlabs.pwrj.Utils.Response;
import com.github.pwrlabs.pwrj.protocol.PWRJ;
import com.github.pwrlabs.pwrj.wallet.PWRWallet;
import org.json.JSONObject;
import spark.Request;

import java.nio.charset.StandardCharsets;

import static spark.Spark.*;
public class API {

    public static final long APP_ID = 20;
    public static void main(String[] args) {
        post("/signUp", (request, response) -> {
            try {
                System.out.println("Handling request for /latestTransactions");
                response.header("Content-Type", "application/json");

                // Retrieve data from the request body (assuming it's JSON)
                JSONObject requestBody = new JSONObject(request.body());

                // Extract parameters from the request body
                String username = requestBody.getString("Username");
                String bio = requestBody.getString("Bio");
                String websiteUrl = requestBody.getString("WebsiteUrl");
                String twitterUrl = requestBody.getString("TwitterUrl");
                String linkedinUrl = requestBody.getString("LinkedinUrl");
                String telegramProfileUrl = requestBody.getString("TelegramProfileUrl");

                // Dump data through the wallet and send it to the blockchain
                dumpDataThroughWallet(username, bio, websiteUrl, twitterUrl, linkedinUrl, telegramProfileUrl);

                // Return a response (modify as needed)
                JSONObject responseData = new JSONObject();
                responseData.put("status", "success");
                responseData.put("message", "Data dumped through the wallet and sent to the blockchain successfully");

                return responseData.toString();
            } catch (Exception e) {
                System.out.println("An error occurred while processing /latestTransactions");

                // Return an error response (modify as needed)
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("status", "error");
                errorResponse.put("message", e.getLocalizedMessage());

                response.status(500); // Internal Server Error
                return errorResponse.toString();
            }
        });

        post("/storeUserData", (request, response) -> {
            try {
                System.out.println("Handling request for /storeUserData");
                response.header("Content-Type", "application/json");

                // Retrieve data from the request body (assuming it's JSON)
                JSONObject requestBody = new JSONObject(request.body());

                // Extract parameters from the request body
                String username = requestBody.getString("Username");
                String bio = requestBody.getString("Bio");
                String websiteUrl = requestBody.getString("WebsiteUrl");
                String twitterUrl = requestBody.getString("TwitterUrl");
                String linkedinUrl = requestBody.getString("LinkedinUrl");
                String telegramProfileUrl = requestBody.getString("TelegramProfileUrl");

                // Store data in the database
                DBUtil.storeUserData(username, bio, websiteUrl, twitterUrl, linkedinUrl, telegramProfileUrl);

                // Return a response (modify as needed)
                JSONObject responseData = new JSONObject();
                responseData.put("status", "success");
                responseData.put("message", "User data stored in the database successfully");

                return responseData.toString();
            } catch (Exception e) {
                System.out.println("An error occurred while processing /storeUserData");

                // Return an error response (modify as needed)
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("status", "error");
                errorResponse.put("message", e.getLocalizedMessage());

                response.status(500); // Internal Server Error
                return errorResponse.toString();
            }
        });


    }






    private static void dumpDataThroughWallet(String username, String bio, String websiteUrl, String twitterUrl, String linkedinUrl, String telegramProfileUrl) {
        try {
            PWRJ.setRpcNodeUrl("https://pwrrpc.pwrlabs.io/");
            PWRWallet wallet = new PWRWallet();
            System.out.println("Address: " + wallet.getAddress());

            // Prepare data for the blockchain
            JSONObject jsonData = new JSONObject();
            jsonData.put("Username", username);
            jsonData.put("Bio", bio);
            jsonData.put("WebsiteUrl", websiteUrl);
            jsonData.put("TwitterUrl", twitterUrl);
            jsonData.put("LinkedinUrl", linkedinUrl);
            jsonData.put("TelegramProfileUrl", telegramProfileUrl);

            // Send data to the blockchain using PWRWallet
            Response r = wallet.sendVmDataTxn(APP_ID, jsonData.toString().getBytes(StandardCharsets.UTF_8));

            if (r.isSuccess()) {
                System.out.println("response: " + r.getMessage());
            } else {
                System.out.println("Error: " + r.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}



