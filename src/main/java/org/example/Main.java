package org.example;

import com.github.pwrlabs.pwrj.Utils.Response;
import com.github.pwrlabs.pwrj.protocol.PWRJ;
import com.github.pwrlabs.pwrj.wallet.PWRWallet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.json.JSONObject;
public class Main {

    public static final long appId = 20;

    public static void main(String[] args) throws IOException, InterruptedException {
        PWRJ.setRpcNodeUrl("https://pwrrpc.pwrlabs.io/");

        PWRWallet wallet = new PWRWallet();
        System.out.println("Address: " + wallet.getAddress());
        try {
            // Get the latest block number
            // PWRJ.getLatestBlockNumber();

            System.out.println("Latest Block Number: " + PWRJ.getLatestBlockNumber());
        } catch (Exception e) {
            handleException(e);
        }

        Listener.listen();

        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("> ");
            String message = scanner.nextLine();

            try {
                JSONObject jsonData = new JSONObject();
                jsonData.put("key", message);
                Response r = wallet.sendVmDataTxn(appId, jsonData.toString().getBytes(StandardCharsets.UTF_8));

                if(r.isSuccess()) {
                    System.out.println("Txn Hash: " + r.getMessage());
                } else {
                    System.out.println("Error: " + r.getError());
                }
            } catch (Exception e) { e.printStackTrace(); }

            System.out.print("> ");
        }
    }
    private static void handleException(Exception e) {
        e.printStackTrace();

        // Check if the error message contains "status"
        if (e.getMessage() != null && e.getMessage().contains("status")) {
            try {
                // Extract the "status" from the error message using JSON parsing
                JSONObject errorJson = new JSONObject(e.getMessage());
                if (errorJson.has("status")) {
                    String status = errorJson.getString("status");
                    System.out.println("Error Status: " + status);
                } else {
                    System.out.println("Error: Response does not contain status information");
                }
            } catch (Exception jsonException) {
                // If parsing as JSON fails, print the raw response
                System.out.println("Raw Response: " + e.getMessage());
            }
        }
    }
}