package controller;

import dto.Model.UserModel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class NotificationContextMenuController implements Initializable {
    public ListView<UserModel> notificationList;
//    private NotificationService notificationService; // Assume this service provides the friend requests data

@Override
public void initialize(URL url, ResourceBundle resourceBundle) {
    notificationList.setCellFactory(param -> new ListCell<>() {
        @Override
        protected void updateItem(UserModel item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NavigationBar/NotificationElement.fxml"));
                    Pane pane = loader.load();
                    NotificationElementController controller = loader.getController();
                    controller.setData(item); // Assume this method sets the data on the controller
                    setGraphic(pane);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });

    // Create some dummy data
    UserModel user1 = new UserModel();
    user1.setUserID(1);
    user1.setUserName("Alice");

    UserModel user2 = new UserModel();
    user2.setUserID(2);
    user2.setUserName("Bob");

    UserModel user3 = new UserModel();
    user3.setUserID(3);
    user3.setUserName("Charlie");

    // Add the dummy data to the ListView
    notificationList.getItems().addAll(user1, user2, user3);
}

//    public void setNotificationService(NotificationService notificationService) {
//        this.notificationService = notificationService;
//    }
}