package org.example.client;

import dto.Controller.CallBackController;
import javafx.application.Platform;
import org.controlsfx.control.Notifications;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CallBackControllerImpl extends UnicastRemoteObject implements CallBackController, Serializable {
    protected CallBackControllerImpl() throws RemoteException {
        super();
    }
    @Override
    public void receiveAnnouncement(String announcement, String announcementTitle) {
        Platform.runLater(()->Notifications.create().title(announcementTitle).text(announcement).showInformation());
    }
}
