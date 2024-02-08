package controller;

import dto.Controller.CallBackController;
import dto.Controller.SendHeartBeatToServerFromClient;
import dto.requests.LoginRequest;
import model.CurrentUser;
import network.NetworkFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Logout  {



    public void startHeartbeat() {
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                //send heartbeat from client to the server using phoneNumber
//                NetworkFactory.getInstance().sendHeartbeat(CurrentUser.getCurrentUser().getPhoneNumber(), CurrentUser.getInstance().getCallBackController());
//            } catch (RemoteException | NotBoundException e) {
//                e.printStackTrace();
//            }
//        }, 0, 10, TimeUnit.MINUTES);
    }
}
