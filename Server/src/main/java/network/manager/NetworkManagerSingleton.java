package network.manager;

import controllers.AuthenticationControllerSingleton;
import lookupnames.LookUpNames;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NetworkManagerSingleton {
    private static NetworkManagerSingleton instance;
    private Registry registry;
    private static final int PORT = 1099;
    private boolean isServerRunning;

    private NetworkManagerSingleton() {
        try {
            registry = LocateRegistry.createRegistry(PORT);
            isServerRunning = false;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static NetworkManagerSingleton getInstance() {
        if (instance == null) {
            instance = new NetworkManagerSingleton();
        }
        return instance;
    }
    public synchronized boolean isServerRunning() {
        return isServerRunning;
    }
    public synchronized void setServerRunning(boolean isServerRunning) {
        this.isServerRunning = isServerRunning;
    }
    public void start() {
        try {

            AuthenticationControllerSingleton.getInstance();
//            for(LookUpNames bind : LookUpNames.values()) {
//                //Naming.rebind(bind.name());
//                System.out.println(bind.name());
//            }
            setServerRunning(true);
        } catch (RemoteException e) {
            e.getCause();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
//
//public void start() {
//    try {
//        if (!isServerRunning()) {
//            registry = LocateRegistry.createRegistry(PORT);
//            AuthenticationControllerSingleton.getInstance();
//            setServerRunning(true);
//            while (isServerRunning()) {
//                // Wait for client connections
//                Thread.sleep(1000);
//            }
//        }
//    } catch (RemoteException | MalformedURLException | InterruptedException e) {
//        throw new RuntimeException(e);
//    }
//}
    public void stop() {
        try {
            for(String bind : registry.list()) {
                Remote stub = registry.lookup(bind);
                UnicastRemoteObject.unexportObject(stub, true);
                Naming.unbind(bind);
            }
            UnicastRemoteObject.unexportObject(registry, true);
            setServerRunning(false);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Registry getRegistry() {
        return registry;
    }
}