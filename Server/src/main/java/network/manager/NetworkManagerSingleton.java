package network.manager;

import controllers.*;
import lookupnames.LookUpNames;
import service.SendHeartBeatService;
import service.TrackOnlineUsersService;
//import service.TrackOnlineUsersService;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NetworkManagerSingleton {
    private static NetworkManagerSingleton instance;
    private Registry registry;
    private static final int PORT = 2000;
    private boolean isServerRunning;
    private boolean isFirsTimeStart;


    private NetworkManagerSingleton() {
        try {
            registry = LocateRegistry.createRegistry(PORT);
            isServerRunning = false;
            isFirsTimeStart = true;
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
            if(!isFirsTimeStart) {
                exportRemoteObjects();
            }
            else {
                isFirsTimeStart = false;
            }
            registryBinding();
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
                registry.unbind(bind);
            }
        } catch (RemoteException | NotBoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void registryBinding() throws RemoteException, MalformedURLException {
        registry.rebind(LookUpNames.ONLINECONTROLLER.name(), OnlineControllerImpl.getInstance());
        registry.rebind(LookUpNames.AUTHENTICATIONCONTROLLER.name(), AuthenticationControllerSingleton.getInstance());
        registry.rebind(LookUpNames.GROUPCHATCONTROLLER.name(), GroupChatControllerSingleton.getInstance());
        registry.rebind(LookUpNames.INVITATIONCONTROLLER.name(), InvitationControllerSingleton.getInstance());
        registry.rebind(LookUpNames.MESSAGECONTROLLER.name(), MessageControllerSingleton.getInstance());
        registry.rebind(LookUpNames.USERPROFILECONTROLLER.name(), UserProfileControllerSingleton.getInstance());
        registry.rebind(LookUpNames.CONTACTCONTROLLER.name(), ContactsControllerSingleton.getInstance());
        registry.rebind(LookUpNames.TRACKONLINEUSERS.name(), TrackOnlineUsersService.getInstance());
        registry.rebind(LookUpNames.VOICECHATCONTROLLER.name(), VoiceChatControllerSingleton.getInstance());
        registry.rebind(LookUpNames.SENDHEARTBEATTOSERVERFROMCLIENT.name(), SendHeartBeatService.getInstance());
    }

    private void exportRemoteObjects() throws RemoteException, MalformedURLException {
        UnicastRemoteObject.exportObject(OnlineControllerImpl.getInstance(), PORT);
        UnicastRemoteObject.exportObject(AuthenticationControllerSingleton.getInstance(), PORT);
        UnicastRemoteObject.exportObject(GroupChatControllerSingleton.getInstance(), PORT);
        UnicastRemoteObject.exportObject(InvitationControllerSingleton.getInstance(), PORT);
        UnicastRemoteObject.exportObject(MessageControllerSingleton.getInstance(), PORT);
        UnicastRemoteObject.exportObject(UserProfileControllerSingleton.getInstance(), PORT);
        UnicastRemoteObject.exportObject(ContactsControllerSingleton.getInstance(), PORT);
        UnicastRemoteObject.exportObject(TrackOnlineUsersService.getInstance(), PORT);
        UnicastRemoteObject.exportObject(SendHeartBeatService.getInstance(), PORT);
    }

    public Registry getRegistry() {
        return registry;
    }
}

