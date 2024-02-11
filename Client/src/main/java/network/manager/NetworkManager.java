package network.manager;

import sharednetwork.ConnectionIP;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NetworkManager {
    private static NetworkManager instance;
    private static Registry registry;
    private static final int PORT = 2000;
    private NetworkManager() {
    }

    public static NetworkManager getInstance() throws RemoteException {
        if (instance == null) {
            instance = new NetworkManager();
            registry= LocateRegistry.getRegistry(ConnectionIP.serverIP, PORT);
//            registry= LocateRegistry.getRegistry( PORT);
        }
        return instance;
    }

    public Registry getRegistry() {
        return registry;
    }



}
