package org.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static org.example.demo1.HelloApplication.isServerRunning;

public class ServiceStartController {
    private Process serverProcess;
    private @FXML VBox vbRoot;
    private @FXML Button startButton;
    private @FXML Button shutdownButton;
    private @FXML ProgressIndicator progressIndicator;
    private Registry rmiRegistry;
    VBox getVBoxRoot()
    {
        return vbRoot;
    }

    public void init() {
        startButton = new Button();
        shutdownButton = new Button();
        if(isServerRunning)
        {
            progressIndicator.setStyle("-fx-progress-color: green;");
        }
        else
        {
            progressIndicator.setStyle("-fx-progress-color: red;");
        }
    }
    @FXML
    private void handleStartButtonClick() throws RemoteException, MalformedURLException {
        try {
            isServerRunning = true;
            rmiRegistry = LocateRegistry.createRegistry(1099);
            String ipAddress = "localhost";
            Hello hello = new Hello();
            String bindingName = "Hello";
            Naming.rebind("rmi://" + ipAddress + "/" + bindingName, hello);
            progressIndicator.setStyle("-fx-progress-color: green;");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    @FXML
    private void handleShutdownButtonClick() throws RemoteException, MalformedURLException, NotBoundException {
        isServerRunning = false;
        progressIndicator.setStyle("-fx-progress-color: red;");
        Hello hello = new Hello();
        try {
            for(String binds : rmiRegistry.list())
            {
                Remote remote = rmiRegistry.lookup(binds);
                System.out.println(binds);
                if(remote instanceof UnicastRemoteObject) {
                    UnicastRemoteObject.unexportObject(remote, true);
                }
            }
            UnicastRemoteObject.unexportObject(rmiRegistry, true);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
