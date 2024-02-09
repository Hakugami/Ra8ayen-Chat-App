package controller;

import controller.soundUtils.AudioChat;
import dto.Controller.CallBackController;
import dto.Model.MessageModel;
import dto.Model.NotificationModel;
import dto.Model.UserModel;
import dto.requests.*;
import dto.responses.AcceptVoiceCallResponse;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import java.util.List;
import java.util.Optional;

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
    public void updateUserModel(UserModel userModel) throws RemoteException {
        CurrentUser.getInstance().setPhoneNumber(userModel.getPhoneNumber());
        CurrentUser.getInstance().setUserName(userModel.getUserName());
        CurrentUser.getInstance().setEmailAddress(userModel.getEmailAddress());
        //CurrentUser.getInstance().setProfilePicture(userModel.getProfilePicture());
        CurrentUser.getInstance().setGender(userModel.getGender());
        CurrentUser.getInstance().setBio(userModel.getBio());
        CurrentUser.getInstance().setCountry(userModel.getCountry());
        CurrentUser.getInstance().setDateOfBirth(userModel.getDateOfBirth());

        Platform.runLater(() -> {
            try {
                Model.getInstance().getControllerFactory().getContactsController().setTreeViewData();
                Model.getInstance().getControllerFactory().getContactsController().setImageProfileData();
            } catch (SQLException | NotBoundException | ClassNotFoundException  | RemoteException e) {
                Notifications.create().title("Error").text("Error updating the image").showError();
            }
        });
    }
    @Override
    public void respond() throws RemoteException {
        System.out.println("You are still connected");
        isClientConnected = true;
    }

    @Override
    public void userIsOnline(String friendName) throws RemoteException {
        Platform.runLater(() ->Notifications.create().title("Friend Online").text(friendName+" is now online").showInformation());
    }

    @Override
    public void userIsOffline(String friendName) throws RemoteException {
        Platform.runLater(() ->Notifications.create().title("Friend Offline").text(friendName+" is now offline").showInformation());
    }

    @Override
    public void receiveNotification(NotificationModel notification) throws RemoteException {
        NotificationManager.getInstance().addNotification(notification);
            Platform.runLater(()-> {
                Model.getInstance().getControllerFactory().getNotificationContextMenuController().populateNotificationListItems();
                Notifications.create().title("New Friend Request").text("You have a new friend request").showInformation();
            });
    }

    @Override
    public void receiveNewMessage(MessageModel message) throws RemoteException {
        if(Model.getInstance().getViewFactory().getSelectedContact().get() instanceof ContactData){
                CurrentUser.getInstance().addMessageToCache(message.getChatId(), message);
                if(((ContactData)Model.getInstance().getViewFactory().getSelectedContact().get()).getChatId()==message.getChatId()){
                    Model.getInstance().getControllerFactory().getChatController().setNewMessage(message);
                }
                if(Model.getInstance().getControllerFactory().getSideNavBarControllerBot()) {
                    Model.getInstance().getControllerFactory().getChatController().botSendMessage(message);
                }
        }
        else {
            CurrentUser.getInstance().addMessageToCache(message.getChatId(), message);
            Platform.runLater(() -> Notifications.create().title("New Message").text(message.getSender().getUserName() + " sent you a new message.").showInformation());
        }
        // Find the contact in the contact list that matches the sender of the new message
        for (ContactData contact : CurrentUser.getInstance().getContactDataList()) {
            if (contact.getChatId()==message.getChatId()) {
                // Update the lastMessage property of the contact
                System.out.println("Setting last message to: " + message.getMessageContent());
                contact.setLastMessage(message.getMessageContent());
                Platform.runLater(() -> {
                    try {
                        Model.getInstance().getControllerFactory().getContactsController().setTreeViewData();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            }
        }
    }
    @Override
    public void receiveGroupChatMessage(MessageModel message) throws RemoteException {
        if(Model.getInstance().getViewFactory().getSelectedContact().get() instanceof Group){
            CurrentUser.getInstance().addMessageToCache(message.getChatId(), message);
            if(((Group)Model.getInstance().getViewFactory().getSelectedContact().get()).getGroupId()==message.getChatId()){
                Model.getInstance().getControllerFactory().getChatController().setNewMessage(message);
            }
            if(Model.getInstance().getControllerFactory().getSideNavBarControllerBot()) {
                Model.getInstance().getControllerFactory().getChatController().botSendMessage(message);
            }
        }
        else {
            CurrentUser.getInstance().addMessageToCache(message.getChatId(), message);
            Optional<Group> groupName = CurrentUser.getCurrentUser().getGroupList().stream().
                    filter(group -> group.getGroupId() == message.getChatId()).findFirst();
            groupName.ifPresent(group -> Platform.runLater(() -> Notifications.create().title("New Group Message").text(message.getSender().getUserName() + " sent a new message to " + group.getGroupName()).showInformation()));
        }
        for (Group group : CurrentUser.getInstance().getGroupList()) {
            if (group.getGroupId()==message.getChatId()) {
                // Update the lastMessage property of the contact
                System.out.println("Setting last message to: " + message.getMessageContent());
                group.setLastMessage(message.getMessageContent());
                Platform.runLater(() -> {
                    try {
                        Model.getInstance().getControllerFactory().getContactsController().setTreeViewData();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            }
        }
    }


    @Override
    public void receiveAddContactRequest(FriendRequest friendRequest) throws RemoteException {

    }

    @Override
    public void updateNotificationList(AcceptFriendRequest notification) throws RemoteException {
        List<NotificationModel> notifications = NotificationManager.getInstance().getNotifactionsList();
        notifications.removeIf(n -> n.getId() == notification.getId());
        ObservableList<UserModel> users = Model.getInstance().getControllerFactory().getNotificationContextMenuController().notificationListItems;
        users.removeIf(u -> u.getPhoneNumber().equals(notification.getFriendPhoneNumber()));
        Platform.runLater(() -> {
            Model.getInstance().getControllerFactory().getNotificationContextMenuController().populateNotificationListItems();
        });
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

    @Override
    public void logout() throws RemoteException {
        Platform.runLater(()->{
            try {
                try {
                    Model.getInstance().getViewFactory().showLoginWindow();
                    Notifications.create().title("Logged Out").text("You have been logged out").showInformation();
                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void receiveVoiceCallRequest(VoiceCallRequest voiceCallRequest) throws RemoteException {
        Platform.runLater(()->{
            try {
                Model.getInstance().getControllerFactory().getChatController().receiveVoiceChatRequest(voiceCallRequest.getSenderPhoneNumber());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void establishVoiceCall(AcceptVoiceCallResponse acceptVoiceCallResponse) throws RemoteException {
        Platform.runLater(()->{
            try {
                Model.getInstance().getControllerFactory().getVoiceChatPopUpController().establishVoiceCall(acceptVoiceCallResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void receiveVoiceMessage(SendVoicePacketRequest voicePacketRequest) throws RemoteException {
//        System.out.println("This should be the receiver "+CurrentUser.getInstance().getPhoneNumber()+" and this should be the sender "+voicePacketRequest.getSenderPhoneNumber());
//        Platform.runLater(()->{
//            AudioChat.getInstance().setReceivedData(voicePacketRequest);
//        });
    }
}
