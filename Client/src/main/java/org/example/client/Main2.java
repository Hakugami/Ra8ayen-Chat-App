package org.example.client;

import dto.Controller.CallBackController;
import dto.Controller.OnlineController;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main2 {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry(1099);

        // Lookup the OnlineControllerSingleton from the RMI registry
        OnlineController onlineController = (OnlineController) registry.lookup("ONLINECONTROLLER");

        // Implement the CallBackController interface
        CallBackController callBackController = new CallBackControllerImpl();

        // Connect to the server
        onlineController.connect("clientName2", callBackController);
    }
}