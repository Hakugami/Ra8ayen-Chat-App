package org.example.demo1;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        IHello remoteObject = (IHello) registry.lookup("Hello");
        int i = remoteObject.printMsg();
        System.out.println(i);
    }

}
