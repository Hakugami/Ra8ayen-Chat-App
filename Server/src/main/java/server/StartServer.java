package server;

import static network.ConnectionIP.serverIP;

public class StartServer {
    public static void main(String[] args) {
        System.setProperty("java.rmi.server.hostname", serverIP);
        ServerApplication.main(args);
    }
}
