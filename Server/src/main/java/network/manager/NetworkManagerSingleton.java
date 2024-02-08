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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkManagerSingleton {
    private static NetworkManagerSingleton instance;
    private Registry registry;
    private static final int PORT = 2000;
    private boolean isServerRunning;
    private boolean isFirsTimeStart;
    private List<Remote> stubs;
    private Map<String, Remote> lookUpMap;

    private NetworkManagerSingleton() {
        try {
            registry = LocateRegistry.createRegistry(PORT);
            isServerRunning = false;
            isFirsTimeStart = true;
            stubs = Arrays.asList(
                    OnlineControllerImpl.getInstance(),
                    AuthenticationControllerSingleton.getInstance(),
                    GroupChatControllerSingleton.getInstance(),
                    InvitationControllerSingleton.getInstance(),
                    MessageControllerSingleton.getInstance(),
                    UserProfileControllerSingleton.getInstance(),
                    ContactsControllerSingleton.getInstance(),
                    TrackOnlineUsersService.getInstance(),
                    SendHeartBeatService.getInstance(),
                    VoiceChatControllerSingleton.getInstance()
            );
            lookUpMap = new HashMap<>();
            lookUpMap.put(LookUpNames.ONLINECONTROLLER.name(), OnlineControllerImpl.getInstance());
            lookUpMap.put(LookUpNames.AUTHENTICATIONCONTROLLER.name(), AuthenticationControllerSingleton.getInstance());
            lookUpMap.put(LookUpNames.GROUPCHATCONTROLLER.name(), GroupChatControllerSingleton.getInstance());
            lookUpMap.put(LookUpNames.INVITATIONCONTROLLER.name(), InvitationControllerSingleton.getInstance());
            lookUpMap.put(LookUpNames.MESSAGECONTROLLER.name(), MessageControllerSingleton.getInstance());
            lookUpMap.put(LookUpNames.USERPROFILECONTROLLER.name(), UserProfileControllerSingleton.getInstance());
            lookUpMap.put(LookUpNames.CONTACTCONTROLLER.name(), ContactsControllerSingleton.getInstance());
            lookUpMap.put(LookUpNames.TRACKONLINEUSERS.name(), TrackOnlineUsersService.getInstance());
            lookUpMap.put(LookUpNames.VOICECHATCONTROLLER.name(), VoiceChatControllerSingleton.getInstance());
            lookUpMap.put(LookUpNames.SENDHEARTBEATTOSERVERFROMCLIENT.name(), SendHeartBeatService.getInstance());
            lookUpMap.put(LookUpNames.BLOCKUSERCONTROLLER.name(),BlockedUserControllerSinglton.getInstance());
        } catch (RemoteException | MalformedURLException e) {
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
                for (Remote stub : stubs) {
                    UnicastRemoteObject.exportObject(stub, PORT);
                }
            }
            else {
                isFirsTimeStart = false;
            }
            for (Map.Entry<String, Remote> entry : lookUpMap.entrySet()) {
                registry.rebind(entry.getKey(), entry.getValue());
            }
            setServerRunning(true);
        } catch (RemoteException e) {
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

    public void unexportStubsOnExit() {
        for (Remote stub : stubs) {
            try {
                UnicastRemoteObject.unexportObject(stub, true);
            } catch (NoSuchObjectException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void unexportRegistry() {
        try {
            UnicastRemoteObject.unexportObject(registry, true);
        } catch (NoSuchObjectException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isFirsTimeStart() {
        return isFirsTimeStart;
    }
}

