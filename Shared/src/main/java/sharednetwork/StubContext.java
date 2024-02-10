package sharednetwork;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class StubContext {
    public static final Map<String, Remote> stubs = new HashMap<>();;
    public static Registry registry;
    private static final int  ServerPort = 1099;
    private static StubContext instance = null;

    private static String serverIP;
    //    private static boolean firstRun;
    public static boolean isRunning = true;
    private  StubContext() {
        try {

            System.setProperty("java.rmi.server.hostname", serverIP);

            registry  =  LocateRegistry.createRegistry(ServerPort);
//            firstRun = true;
            isRunning = true;
            System.out.println("StubContext Manager Created and server is Running");
            System.out.println("private constructor: " +serverIP);


        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static StubContext getInstance() {
        if (instance == null) {
            instance = new StubContext();
        }
        return instance;
    }
    public static void setServerIP(String ip){
        serverIP = ip;
    }

    public static void addStub(String name, Remote stub) {

        try {
            Naming.rebind(name, stub);

        } catch (RemoteException e) {
            System.out.println("addStub exception ya naas");
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        stubs.put(name, stub);
    }

    public static Remote getStub(String name) {
        try {
            Registry registry = LocateRegistry.getRegistry(serverIP, ServerPort);
            //String registryURL = "rmi://" + serverIP  + "/" + name;
            Remote stub = registry.lookup(name);
            //Remote stub = Naming.lookup(name);

            System.out.println("getStub: " +serverIP);

            if (stub != null) {
                stubs.put(name, stub);
            }
            return stub;
        } catch (Exception e) {
            System.out.println("getStub exception ya naas");
            e.printStackTrace();
            return null;
        }
    }

    public static Remote getStub(String name, String serverIP) {
        try {
            Registry registry = LocateRegistry.getRegistry(serverIP, ServerPort);
            String registryURL = "rmi://" + serverIP  + "/" + name;
            Remote stub = registry.lookup(name);
            //Remote stub = Naming.lookup(name);

            System.out.println("getStub: " +serverIP);

            if (stub != null) {
                stubs.put(name, stub);
            }
            return stub;
        } catch (Exception e) {
            System.out.println("getStub exception ya naas");
            e.printStackTrace();
            return null;
        }
    }
}
