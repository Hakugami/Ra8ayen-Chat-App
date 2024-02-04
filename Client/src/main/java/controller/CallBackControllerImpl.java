package controller;

import dto.Controller.CallBackController;
import dto.Model.MessageModel;
import dto.Model.NotificationModel;
import dto.requests.FriendRequest;
import dto.requests.GetContactsRequest;
import dto.requests.GetGroupRequest;
import javafx.application.Platform;
import model.ContactData;
import model.CurrentUser;
import model.Group;
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
       // Model.getInstance().getControllerFactory().getChatController();
        System.out.println("Message Received----------------------------------------------------------------------------------------");
        if(Model.getInstance().getViewFactory().getSelectedContact().get() instanceof ContactData){
            Platform.runLater(() -> Notifications.create().text(message.getSender().getUserName() +
                    " sent you a message").title("New Message").showInformation());
        }
        else if(Model.getInstance().getViewFactory().getSelectedContact().get() instanceof Group){
            Platform.runLater(() -> Notifications.create().text(message.getSender().getUserName() +
                    " sent a message to group").title("New Message").showInformation());
        }
        Model.getInstance().getControllerFactory().getChatController().setNewMessage(message);
        if(message.getSender()!=null){

            System.out.println(message.getSender().getUserID());
            System.out.println(message.getSender().getProfilePicture());
        }else{
            System.out.println("Sender Object is null");
        }
        //get ChatID of message and display on

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
        System.out.println("updateOnlineList");
        CurrentUser.getInstance().loadContactsList(NetworkFactory.getInstance().getContacts(new GetContactsRequest(CurrentUser.getInstance().getUserID())));
        CurrentUser.getInstance().getContactDataList().forEach(contactData ->{
            System.out.println(contactData.getColor());
        });
        CurrentUser.getInstance().loadGroups(NetworkFactory.getInstance().getGroups(new GetGroupRequest(CurrentUser.getInstance().getUserID())));
        CurrentUser.getInstance().getGroupList().forEach(groupData ->{
            System.out.println(groupData.getGroupName());
        });
        Platform.runLater(()-> {
            try {
                Model.getInstance().getControllerFactory().getContactsController().setTreeViewData();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
