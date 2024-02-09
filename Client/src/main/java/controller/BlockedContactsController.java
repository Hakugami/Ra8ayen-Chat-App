package controller;

import dto.Model.UserModel;
import dto.requests.DeleteBlockContactRequest;
import dto.requests.GetBlockedContactRequest;
import dto.requests.UpdateUserRequest;
import dto.responses.DeleteBlockContactResponse;
import dto.responses.GetBlockedContactResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import network.manager.NetworkManager;
import org.controlsfx.control.Notifications;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class BlockedContactsController {

    @FXML
    ListView<GetBlockedContactResponse> blockedListScreen;

    ObservableList<GetBlockedContactResponse>blockedListData;
    @FXML
    void initialize(){
       // System.out.println("Blocked Initialize ");
        blockedListData = FXCollections.observableArrayList();
     //   blockedListScreen = new ListView<>();
        blockedListScreen.setCellFactory(param -> new CustomListCell());
        getData();
        blockedListScreen.setItems(blockedListData);
    }
    private class CustomListCell extends ListCell<GetBlockedContactResponse> {
        private final Label nameLabel = new Label();
        private final Label phoneNumberLabel = new Label();
        private final Button deleteButton = new Button("Delete");

        private final HBox hbox = new HBox(10, nameLabel, phoneNumberLabel, deleteButton); // Adjust spacing here

        public CustomListCell() {
            // Adjust font size here
            nameLabel.setFont(new Font(14)); // Example font size
            phoneNumberLabel.setFont(new Font(14)); // Example font size

            deleteButton.setOnAction(event -> {
                GetBlockedContactResponse item = getItem();
                System.out.println("item need to delete");
                if (deleteBlockedContact(item)) {
                    getListView().getItems().remove(item);
                    updateStatusForBlocked();
                }
                //  getListView().getItems().remove(item);
            });
        }

        @Override
        protected void updateItem(GetBlockedContactResponse item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                nameLabel.setText(item.getName());
                phoneNumberLabel.setText(item.getFriendPhoneNumber());
                setGraphic(hbox);
            }
        }
    }

    void getData(){
        GetBlockedContactRequest getBlockedContactRequest = new GetBlockedContactRequest();
        getBlockedContactRequest.setUserID(CurrentUser.getCurrentUser().getUserID());
        getBlockedContactRequest.setPhoneNumber(CurrentUser.getCurrentUser().getPhoneNumber());
        try {
            if(blockedListData==null){
                return;
            }
            blockedListData.addAll(NetworkFactory.getInstance().getBlockedContactResponseList(getBlockedContactRequest));
       System.out.println("Data Size "+blockedListData.size());
        } catch (RemoteException e) {
            System.out.println("Remote Exception throw");
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            System.out.println("Runtime Exception throw");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    boolean deleteBlockedContact(GetBlockedContactResponse getBlockedContactResponse){
        DeleteBlockContactRequest deleteBlockContactRequest = new DeleteBlockContactRequest();

        deleteBlockContactRequest.setUserID(CurrentUser.getCurrentUser().getUserID());
        deleteBlockContactRequest.setFriendUserID(getBlockedContactResponse.getFriendID());
        deleteBlockContactRequest.setPhoneNumberUser(CurrentUser.getCurrentUser().getPhoneNumber());
        deleteBlockContactRequest.setPhoneNumberFriend(getBlockedContactResponse.getFriendPhoneNumber());

        try {
           DeleteBlockContactResponse deleteBlockContactResponse=NetworkFactory.getInstance().deleteBlockedContact(deleteBlockContactRequest);
            if(deleteBlockContactResponse.isDeleted()){
                Notifications.create().title("Success").text(deleteBlockContactResponse.getDeleteMessage()).showInformation();
            }else{
                Notifications.create().title("Failed").text(deleteBlockContactResponse.getDeleteMessage()).showInformation();
            }

            return deleteBlockContactResponse.isDeleted();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateStatusForBlocked(){
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        UserModel userModel = new UserModel();
        try {
            userModel.setUserName(CurrentUser.getCurrentUser().getUserName());
            userModel.setUserStatus(CurrentUser.getCurrentUser().getUserStatus());
            userModel.setEmailAddress(CurrentUser.getCurrentUser().getEmailAddress());
            userModel.setBio(CurrentUser.getCurrentUser().getBio());
            userModel.setCountry(CurrentUser.getCurrentUser().getCountry());
            userModel.setDateOfBirth(CurrentUser.getCurrentUser().getDateOfBirth());
            userModel.setUserID(CurrentUser.getCurrentUser().getUserID());
            userModel.setProfilePicture(CurrentUser.getCurrentUser().getProfilePicture());
            userModel.setGender(CurrentUser.getCurrentUser().getGender());
            userModel.setUserStatus(CurrentUser.getCurrentUser().getUserStatus());
            userModel.setUserMode(CurrentUser.getCurrentUser().getUserMode());
            userModel.setLastLogin(CurrentUser.getInstance().getLastLogin());

            updateUserRequest.setUserModel(userModel);
            NetworkFactory.getInstance().updateUser(updateUserRequest);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
