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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import network.manager.NetworkManager;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class BlockedContactsController {

    @FXML
    ListView<GetBlockedContactResponse> blockedListScreen;

    ObservableList<GetBlockedContactResponse>blockedListData;

    @FXML
    Button exitButton;

    static Popup popup;
    @FXML
    void initialize(){
        Model.getInstance().getControllerFactory().setBlockedContactsController(this);
        blockedListData = FXCollections.observableArrayList();
        blockedListScreen.setCellFactory(param -> new CustomListCell());
        getData();
        blockedListScreen.setItems(blockedListData);
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED,this::hidePopup);
    }
    public class CustomListCell extends ListCell<GetBlockedContactResponse> {
        private BlockedContacElementController controller;

        public CustomListCell() {
            super();
            try {
                controller = Model.getInstance().getControllerFactory().getBlockedContacElementController();
                Model.getInstance().getViewFactory().getBlockedElementScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        @Override
        protected void updateItem(GetBlockedContactResponse item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                controller.setNameLabel(item.getName());
                controller.setPhoneNumberLabel(item.getFriendPhoneNumber());
                controller.getDeleteButton().setOnAction(event -> {
                    System.out.println("item need to delete");
                    if (deleteBlockedContact(item)) {
                        getListView().getItems().remove(item);
                        //updateStatusForBlocked();
                    }
                });
                setGraphic(controller.getView());
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
    void setPopUp(Popup popup){
        this.popup= popup;
    }
    void hidePopup(MouseEvent mouseEvent){
        popup.hide();
    }
}
