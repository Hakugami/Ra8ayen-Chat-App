package controller;

import dto.Controller.CallBackController;
import dto.Model.MessageModel;
import javafx.application.Platform;
import org.controlsfx.control.Notifications;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CallBackControllerImpl extends UnicastRemoteObject implements CallBackController, Serializable {
    private static CallBackControllerImpl callBackController;
    private  static boolean isClientConnected = false;
    private CallBackControllerImpl() throws RemoteException {
        super();
    }

    public static CallBackControllerImpl getInstance() throws RemoteException {
        if (callBackController == null) {
            callBackController = new CallBackControllerImpl();
        }
        return callBackController;
    }

    @Override
    public void respond() throws RemoteException {
        System.out.println("You are still connected");
        isClientConnected = true;
    }

    @Override
    public void receiveNewMessage(MessageModel message) throws RemoteException {

    }

    @Override
    public void receiveAnnouncement(String announcement, String announcementTitle) {
        Platform.runLater(()->Notifications.create().title(announcementTitle).text(announcement).showInformation());
    }
}
