package server;

import controllers.AuthenticationControllerSingleton;
import dto.requests.LoginRequest;
import dto.requests.RegisterRequest;
import network.manager.NetworkManagerSingleton;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class StartServer {
    public static void main(String[] args) throws MalformedURLException, RemoteException {
//        NetworkManagerSingleton networkManagerSingleton = NetworkManagerSingleton.getInstance();
//        networkManagerSingleton.start();
//        System.out.println("Server started."
//                + "\nServer is running: " + NetworkManagerSingleton.getInstance().isServerRunning());
//
        NetworkManagerSingleton networkManagerSingleton = NetworkManagerSingleton.getInstance();
        networkManagerSingleton.start();

//
//        AuthenticationControllerSingleton authenticationControllerSingleton = AuthenticationControllerSingleton.getInstance();
//
//        // Create a RegisterRequest object
//        RegisterRequest registerRequest = new RegisterRequest();
//
//        // Set the necessary fields
//        registerRequest.setPhoneNumber("1234567890"); // replace with actual phone number
//        registerRequest.setUserName("TestUser"); // replace with actual user name
//        registerRequest.setEmailAddress("testuser@example.com"); // replace with actual email address
//        registerRequest.setPasswordHash("password"); // replace with actual password hash
//        registerRequest.setGender(RegisterRequest.Gender.Male); // replace with actual gender
//        registerRequest.setCountry("Country"); // replace with actual country
//        //Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]
//        registerRequest.setDateOfBirth(new Date()); // replace with actual date of birth
//
//        // Call the register method
//        System.out.println(  authenticationControllerSingleton.register(registerRequest));
    }
}
