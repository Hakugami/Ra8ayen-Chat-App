package org.example.demo1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Hello extends UnicastRemoteObject implements IHello {
    public Hello() throws RemoteException {
    }

    @Override
    public int printMsg() {
        return  2;
    }
}