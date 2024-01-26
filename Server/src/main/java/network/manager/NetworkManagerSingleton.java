package network.manager;

import controllers.AuthenticationControllerSingleton;
import controllers.GroupChatControllerSingleton;
import controllers.InvitationControllerSingleton;
import controllers.OnlineControllerImpl;
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
            System.out.println(e.getMessage());
        }
    }

    public static NetworkManagerSingleton getInstance() {
        if (instance == null) {
            instance = new NetworkManagerSingleton();
        }
        return instance;
    }
    public boolean isServerRunning() {
        return isServerRunning;
    }
    public void setServerRunning(boolean isServerRunning) {
        this.isServerRunning = isServerRunning;
    }
    public void start() {
        try {
            registry.rebind(LookUpNames.ONLINECONTROLLER.name(), OnlineControllerImpl.getInstance());
            registry.rebind(LookUpNames.AUTHENTICATIONCONTROLLER.name(), AuthenticationControllerSingleton.getInstance());
            registry.rebind(LookUpNames.GROUPCHATCONTROLLER.name(), GroupChatControllerSingleton.getInstance());
            registry.rebind(LookUpNames.INVITATIONCONTROLLER.name(), InvitationControllerSingleton.getInstance());
            setServerRunning(true);
        } catch (RemoteException | MalformedURLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void stop() {
        try {
            setServerRunning(false);
            for(String bind : registry.list()) {
                Remote stub = registry.lookup(bind);
                if(stub != null) {
                    UnicastRemoteObject.unexportObject(stub, true);
                }
                Naming.unbind(bind);
            }
            UnicastRemoteObject.unexportObject(registry, true);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Registry getRegistry() {
        return registry;
    }
}

