//package org.example.client;
//
//import dto.Interfaces.LoginService;
//import dto.Interfaces.RegisterService;
//
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//
//public class Main {
//    public static void main(String[] args) throws Exception {
//        Registry registry = LocateRegistry.getRegistry(2400);
//        RegisterService registerService = (RegisterService) registry.lookup("register");
//        assert registerService != null;
////        boolean result = registerService.register("1234567890", "Test User", "test@example.com", "passwordHash", "Male", "Country", "2000-01-01", "Bio");
////        System.out.println("Registration successful: " + result);
//        //login to the this user
//        LoginService loginService = (LoginService) registry.lookup("login");
//        assert loginService != null;
//        boolean loginResult = loginService.authenticate("1234567890", "passwordHash");
//        System.out.println("Login successful: " + loginResult);
//    }
//}