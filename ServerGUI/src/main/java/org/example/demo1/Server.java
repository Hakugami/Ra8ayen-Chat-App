package org.example.demo1;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {

    public static void main(String[] args) {
        try {
            Hello remoteObject = new Hello();

            LocateRegistry.createRegistry(1099);
            String ipAddress = "localhost";
            String bindingName = "Hello";
            Naming.rebind("rmi://" + ipAddress + "/" + bindingName, remoteObject);
        } catch (RemoteException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}