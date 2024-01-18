package org.example.client;

import dto.Controller.AuthenticationController;
import dto.requests.LoginRequest;
import dto.responses.LoginResponse;

import java.rmi.Naming;
import java.rmi.registry.Registry;

public class LoginTest {
    public static void main(String[] args) {
        try {
            Registry registry = java.rmi.registry.LocateRegistry.getRegistry(1099);
            // Create a LoginRequest object
            LoginRequest loginRequest = new LoginRequest();
            // Set the phone number and password
            loginRequest.setPhoneNumber("1234567890"); // replace with actual phone number
            loginRequest.setPassword("password"); // replace with actual password
            // Get the AuthenticationControllerSingleton instance using RMI lookup
            AuthenticationController authenticationController = (AuthenticationController) registry.lookup("AuthenticationController");
            // Call the login method
            LoginResponse loginResponse = authenticationController.login(loginRequest);
            // Print the result of the login operation
            if (loginResponse.getSuccess()) {
                System.out.println("Login successful. Token: " + loginResponse.getToken());
            } else {
                System.out.println("Login failed. Error: " + loginResponse.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}