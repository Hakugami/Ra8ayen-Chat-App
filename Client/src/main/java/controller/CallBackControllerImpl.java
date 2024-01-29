package controller;

import dto.Controller.CallBackController;
import dto.Model.MessageModel;
import dto.Model.NotificationModel;
import dto.requests.FriendRequest;
import dto.requests.GetContactsRequest;
import javafx.application.Platform;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import notification.NotificationManager;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

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
    public void receiveNotification(NotificationModel notification) throws RemoteException {
        NotificationManager.getInstance().addNotification(notification);

            Platform.runLater(()-> {
                try {
                    Model.getInstance().getControllerFactory().getNotificationContextMenuController().populateNotificationListItems();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    public void receiveNewMessage(MessageModel message) throws RemoteException {

    }

    @Override
    public void receiveAddContactRequest(FriendRequest friendRequest) throws RemoteException {

    }

    @Override
    public void createNewChat(String senderPhoneNumber) throws RemoteException {

    }

    @Override
    public void receiveAnnouncement(String announcement, String announcementTitle) {
        Platform.runLater(()->Notifications.create().title(announcementTitle).text(announcement).showInformation());
    }

    @Override
    public void updateOnlineList() throws RemoteException, SQLException, NotBoundException, ClassNotFoundException {
        CurrentUser.getInstance().loadContactsList(NetworkFactory.getInstance().getContacts(new GetContactsRequest(CurrentUser.getInstance().getUserID())));
        Platform.runLater(()-> {
            try {
                Model.getInstance().getControllerFactory().getContactsController().setTreeViewData();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
