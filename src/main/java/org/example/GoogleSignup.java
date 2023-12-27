package org.example;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.io.InputStreamReader;
import java.util.Collections;

import static spark.Spark.*;

public class GoogleSignup {

    public static void main(String[] args) {
        port(4567);

        // Define the Google Sign-Up endpoint
        get("/google-signup", (request, response) -> {
            try {
                // Redirect the user to the Google Sign-In page
                response.redirect(getGoogleSignInUrl());
                return null;
            } catch (Exception e) {
                // Handle errors
                e.printStackTrace();
                response.status(500); // Internal Server Error
                return "Error during Google Sign-Up";
            }
        });

        // Define the callback endpoint for Google Sign-In
        get("/google-callback", (request, response) -> {
            try {
                // Retrieve the authorization code from the query parameters
                String code = request.queryParams("code");

                // Exchange the authorization code for tokens
                GoogleTokenResponse tokenResponse = exchangeCodeForTokens(code);

                // Retrieve user information from the ID token
                String idToken = tokenResponse.getIdToken();
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new InputStreamReader(getClass().getResourceAsStream("/your-google-api-client-id.json")))
                        .setAudience(Collections.singletonList(CLIENT_ID))
                        .build();

                GoogleIdToken googleIdToken = verifier.verify(idToken);
                if (googleIdToken != null) {
                    Payload payload = googleIdToken.getPayload();

                    // Extract user information from the payload
                    String userId = payload.getSubject();
                    String email = payload.getEmail();
                    String name = (String) payload.get("name");

                    // Store user information in the database (replace this with your logic)
                    storeGoogleUserDataInDatabase(userId, name, email);

                    // Return a success response (modify as needed)
                    JSONObject successResponse = new JSONObject();
                    successResponse.put("status", "success");
                    successResponse.put("message", "Google Sign-Up successful");
                    return successResponse.toString();
                } else {
                    response.status(401); // Unauthorized
                    return "Invalid Google ID token";
                }
            } catch (Exception e) {
                // Handle errors
                e.printStackTrace();
                response.status(500); // Internal Server Error
                return "Error during Google Sign-Up callback";
            }
        });
    }

    // Helper method to construct the Google Sign-In URL
    private static String getGoogleSignInUrl() {
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleCredential.HTTP_TRANSPORT,
                GoogleCredential.JSON_FACTORY,
                new GoogleClientSecrets().setWeb(new GoogleClientSecrets.Details()
                        .setClientId(CLIENT_ID)
                        .setClientSecret(CLIENT_SECRET)))
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();

        return flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    }

    // Helper method to exchange the authorization code for tokens
    private static GoogleTokenResponse exchangeCodeForTokens(String code) throws Exception {
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleCredential.HTTP_TRANSPORT,
                GoogleCredential.JSON_FACTORY,
                new GoogleClientSecrets().setWeb(new GoogleClientSecrets.Details()
                        .setClientId(CLIENT_ID)
                        .setClientSecret(CLIENT_SECRET)))
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();

        return flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
    }

    // Replace this method with your actual logic to store Google user data in the database
    private static void storeGoogleUserDataInDatabase(String userId, String name, String email) {
        System.out.println("Storing Google user data in the database:");
        System.out.println("User ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        DBUtil.storeUserData(userId,name,email);
    }

}
