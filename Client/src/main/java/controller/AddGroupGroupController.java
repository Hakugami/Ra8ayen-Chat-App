package controller;

import dto.requests.CreateGroupChatRequest;
import dto.responses.CreateGroupChatResponse;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import model.CurrentUser;
import network.NetworkFactory;
import utils.ImageUtls;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AddGroupGroupController implements Initializable {
    public TextField phoneField;
    public Label serverReply;
    public ImageView groupImage;
    public TextField groupName;
    public Button addButton;
    public static Popup popup;

    public static void setPopup(Popup pop) {
        popup = pop;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        groupImage.setOnMouseClicked(mouseEvent -> handleProfilePicSelection());
        addButton.setOnAction(actionEvent -> addGroup());

    }

    private void addGroup() {
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
        String text = phoneField.getText();
        List<String> friendsPhoneNumbers = List.of(Arrays.stream(text.split(",")).map(String::trim).toArray(String[]::new));
        createGroupChatRequest.setFriendsPhoneNumbers(friendsPhoneNumbers);
        try {
           CreateGroupChatResponse response= NetworkFactory.getInstance().createGroupChat(createGroupChatRequest);
            if(response.isCreated()){
                serverReply.setText("Group added successfully");
                serverReply.setStyle("-fx-text-fill: green");
            }else{
                serverReply.setText("Group not added");
                serverReply.setStyle("-fx-text-fill: red");
            }
        } catch (RemoteException | NotBoundException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

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

