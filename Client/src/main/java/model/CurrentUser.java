package model;

import controller.CallBackControllerImpl;
import dto.Model.MessageModel;
import dto.Model.UserModel;
import dto.responses.GetContactsResponse;
import dto.responses.GetGroupResponse;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import utils.ImageUtls;

import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CurrentUser extends UserModel {

    private static final int MAX_MESSAGES = 100;

    private static CurrentUser currentUser;
    private Image profilePictureImage;
    private CallBackControllerImpl callBackController = CallBackControllerImpl.getInstance();
    private Map<Integer, Image> imageCache ;


    private List<ContactData> contactDataList;
    private List<Group> groupList;
    private Map<ContactData, ChatData> chatList;

    private Map<Integer, List<MessageModel>> chatMessageMap;
    private CurrentUser() throws RemoteException {
        contactDataList = new CopyOnWriteArrayList<>();
        chatList = new ConcurrentHashMap<>();
        chatMessageMap = new ConcurrentHashMap<>();
        imageCache = new ConcurrentHashMap<>();
        groupList = new CopyOnWriteArrayList<>();
    }

    public static CurrentUser getInstance() throws RemoteException {
        if (currentUser == null) {
            currentUser = new CurrentUser();
        }
        return currentUser;
    }

    public void setChatMessageMap(Map<Integer, List<MessageModel>> chatMessageMap) {
        this.chatMessageMap = chatMessageMap;
    }

    public void addImageToCache(int userId, Image image) {
        imageCache.put(userId, image);
    }

    public Image getImageFromCache(int userId) {
        return imageCache.get(userId);
    }
    public void addMessageToCache(int chatId, MessageModel message) {
        if (chatMessageMap.containsKey(chatId)) {
            List<MessageModel> messageModelList = chatMessageMap.get(chatId);
            messageModelList.add(message);

            // If the number of messages exceeds the maximum, remove the oldest one
            if (messageModelList.size() > MAX_MESSAGES) {
                messageModelList.removeFirst();
            }
        } else {
            List<MessageModel> messageModelList = new CopyOnWriteArrayList<>();
            messageModelList.add(message);
            chatMessageMap.put(chatId, messageModelList);
        }
    }


    public boolean isMessageCached(MessageModel messageModel) {
        if (chatMessageMap.containsKey(messageModel.getChatId())) {
            List<MessageModel> messageModelList = chatMessageMap.get(messageModel.getChatId());
            for (MessageModel message : messageModelList) {
                // Only consider messages with a message ID greater than the last message in the cache as new
                System.out.println("Message id====="+message.getMessageId());
                System.out.println("MessageModel id====="+messageModel.getMessageId());
                if (message.getMessageId() < messageModel.getMessageId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<Integer, List<MessageModel>> getChatMessageMap() {
        return chatMessageMap;
    }

    public void setChatList(Map<ContactData, ChatData> chatList) {
        this.chatList = chatList;
    }
    public void addInChatList(ContactData contactData, ChatData chatData){
        chatList.put(contactData,chatData);
    }
    public CallBackControllerImpl getCallBackController() {
        return callBackController;
    }


    public Image getProfilePictureImage() {
        return profilePictureImage;
    }

    public List<ContactData> getContactDataList() {
        return contactDataList;
    }

    public void setContactDataList(List<ContactData> contactDataList) {
        this.contactDataList = contactDataList;
    }

    public static CurrentUser getCurrentUser() {
        return currentUser;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public Map<ContactData, ChatData> getChatList() {
        return chatList;
    }

    public void loadContactsList(List<GetContactsResponse> contactDataList) {
        this.contactDataList.clear();
        for (GetContactsResponse userModel : contactDataList) {
            ContactData contactData = new ContactData();
            contactData.setName(userModel.getName());
            contactData.setPhoneNumber(userModel.getPhoneNumber());
            System.out.println(contactData.getPhoneNumber());
            contactData.setId(userModel.getIdOfFriend());
            Color color = null;
            if(userModel.getUserMode().equals(GetContactsResponse.UserMode.Available)){
                color = Color.GREEN;
            }
            else if(userModel.getUserStatus().equals(GetContactsResponse.UserStatus.Offline)){
                color = Color.GRAY;
            }
            else if(userModel.getUserMode().equals(GetContactsResponse.UserMode.Busy)){
                color = Color.RED;
            }
            else if(userModel.getUserMode().equals(GetContactsResponse.UserMode.Away)){
                color = Color.YELLOW;
            }
            contactData.setColor(color);
            contactData.setChatId(userModel.getChatId());
            BufferedImage bufferedImage = ImageUtls.convertByteToImage(userModel.getProfilePicture());
            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
            contactData.setImage(new ImageView(fxImage));
            this.contactDataList.add(contactData);
        }
    }
    public void loadUser(UserModel user) {
        this.setUserID(user.getUserID());
        this.setUserName(user.getUserName());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setProfilePicture(user.getProfilePicture());
        this.setUserStatus(user.getUserStatus());
        this.setCountry(user.getCountry());
        this.setEmailAddress(user.getEmailAddress());
        this.setBio(user.getBio());
        this.setDateOfBirth(user.getDateOfBirth());
        this.setGender(user.getGender());
        //set last login to the current time
        this.setLastLogin(String.valueOf(new Date()));
        BufferedImage bufferedImage = ImageUtls.convertByteToImage(user.getProfilePicture());
        Image fxImage= SwingFXUtils.toFXImage(bufferedImage, null);
        this.profilePictureImage = fxImage;
    }

    public void loadGroups(List<GetGroupResponse> getGroupResponses){
        this.groupList.clear();
        for (GetGroupResponse groupResponse : getGroupResponses) {
            Group group = new Group();
            group.setGroupId(groupResponse.getGroupId());
            group.setGroupName(groupResponse.getGroupName());
            BufferedImage bufferedImage = ImageUtls.convertByteToImage(groupResponse.getGroupPicture());
            Image fxImage= SwingFXUtils.toFXImage(bufferedImage, null);
            group.setGroupImage(new ImageView(fxImage));
            this.groupList.add(group);
        }
    }


}
