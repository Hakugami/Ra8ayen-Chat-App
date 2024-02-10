package controller;

import dto.Model.UserModel;
import dto.requests.CreateGroupChatRequest;
import dto.responses.CreateGroupChatResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddGroupGroupController implements Initializable {
    public TextField phoneField;
    public Label serverReply;
    public ImageView groupImage;
    public TextField groupName;
    public Button addButton;
    public static Popup popup;
    public ListView contactsToAddList;
    public Button addAllButton;
    public ObservableList<Node> contactsToAdd;


    public static void setPopup(Popup pop) {
        popup = pop;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        groupImage.setOnMouseClicked(mouseEvent -> handleProfilePicSelection());
        contactsToAdd = FXCollections.observableArrayList();
        contactsToAddList.setItems(contactsToAdd);
        addButton.setOnAction(this::addContact);
        addAllButton.setOnAction(this::addAll);

    }


    private void addContact(ActionEvent actionEvent) {
        try {
            UserModel userModel = NetworkFactory.getInstance().getUserModelByPhoneNumber(phoneField.getText());
            if(userModel==null){
                serverReply.setText("User not found");
                serverReply.setStyle("-fx-text-fill: red");
            }else{
                Parent root = Model.getInstance().getViewFactory().getAddContactElement();
                AddContactElementController addContactElementController = (AddContactElementController) root.getProperties().get("controller");
                addContactElementController.setDataGroup(userModel.getUserName(), userModel.getProfilePicture(), userModel.getPhoneNumber(), root, this);
                root.setUserData(addContactElementController); // Set the controller as user data
                contactsToAdd.add(root);
            }
        } catch (NotBoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeFromListGroups(String phoneNumber) {
        contactsToAdd.removeIf(node -> {
            AddContactElementController controller = (AddContactElementController) node.getUserData();
            return controller.phoneNumber.getText().equals(phoneNumber);
        });
    }

    private void addAll(ActionEvent actionEvent) {
        CreateGroupChatRequest createGroupChatRequest = new CreateGroupChatRequest();
        try {
            createGroupChatRequest.setAdminID(CurrentUser.getInstance().getUserID());
            createGroupChatRequest.setAdminPhoneNumber(CurrentUser.getInstance().getPhoneNumber());
            createGroupChatRequest.setGroupName(groupName.getText());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if (groupImage.getImage() != null) {
            BufferedImage bImage = SwingFXUtils.fromFXImage(groupImage.getImage(), null);
            ByteArrayOutputStream s = new ByteArrayOutputStream();
            try {
                ImageIO.write(bImage, "png", s);
                byte[] res = s.toByteArray();
                createGroupChatRequest.setGroupImage(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<String> friendsPhoneNumbers = contactsToAdd.stream()
                .map(node -> ((AddContactElementController) node.getUserData()).phoneNumber.getText())
                .collect(Collectors.toList());
        createGroupChatRequest.setFriendsPhoneNumbers(friendsPhoneNumbers);
        try {
            CreateGroupChatResponse response = NetworkFactory.getInstance().createGroupChat(createGroupChatRequest);
            if (response.isCreated()) {
                serverReply.setText("Group added successfully");
                serverReply.setStyle("-fx-text-fill: green");
                contactsToAdd.clear();
            } else {
                serverReply.setText("Group not added");
                serverReply.setStyle("-fx-text-fill: red");
            }
        } catch (RemoteException | NotBoundException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

//    private void addGroup() {
//        CreateGroupChatRequest createGroupChatRequest = new CreateGroupChatRequest();
//        try {
//            createGroupChatRequest.setAdminID(CurrentUser.getInstance().getUserID());
//            createGroupChatRequest.setAdminPhoneNumber(CurrentUser.getInstance().getPhoneNumber());
//            createGroupChatRequest.setGroupName(groupName.getText());
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
//        if (groupImage.getImage() != null) {
//            BufferedImage bImage = SwingFXUtils.fromFXImage(groupImage.getImage(), null);
//            ByteArrayOutputStream s = new ByteArrayOutputStream();
//            try {
//                ImageIO.write(bImage, "png", s);
//                byte[] res = s.toByteArray();
//                createGroupChatRequest.setGroupImage(res);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        String text = phoneField.getText();
//        List<String> friendsPhoneNumbers = List.of(Arrays.stream(text.split(",")).map(String::trim).toArray(String[]::new));
//        createGroupChatRequest.setFriendsPhoneNumbers(friendsPhoneNumbers);
//        try {
//           CreateGroupChatResponse response= NetworkFactory.getInstance().createGroupChat(createGroupChatRequest);
//            if(response.isCreated()){
//                serverReply.setText("Group added successfully");
//                serverReply.setStyle("-fx-text-fill: green");
//            }else{
//                serverReply.setText("Group not added");
//                serverReply.setStyle("-fx-text-fill: red");
//            }
//        } catch (RemoteException | NotBoundException | SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    private void handleProfilePicSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        // Disable autoHide before showing the FileChooser
        popup.setAutoHide(false);
        // Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            groupImage.setImage(new javafx.scene.image.Image(file.toURI().toString()));
        }
        // Enable autoHide after the FileChooser is closed
        popup.setAutoHide(true);
    }
}

