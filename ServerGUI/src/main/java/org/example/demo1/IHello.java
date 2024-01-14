package org.example.demo1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IHello extends Remote {
    int printMsg() throws RemoteException;
} 