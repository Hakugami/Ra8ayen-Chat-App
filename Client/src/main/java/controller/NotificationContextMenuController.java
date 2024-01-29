package controller;

import dto.Model.UserModel;
import dto.requests.FriendRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import notification.NotificationManager;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class NotificationContextMenuController implements Initializable {
    public ListView<UserModel> notificationList;
    public ObservableList<UserModel> notificationListItems;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notificationListItems = FXCollections.observableArrayList();
        populateNotificationListItems();
        notificationList.setItems(notificationListItems);
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
    }

public void populateNotificationListItems() {
    notificationListItems = FXCollections.observableArrayList(
        NotificationManager.getInstance().getNotifactionsList().stream()
            .filter(notificationModel -> notificationModel instanceof FriendRequest)
            .map(notificationModel -> ((FriendRequest) notificationModel).getUserModel())
            .distinct() // This will remove duplicates
            .collect(Collectors.toList())
    );
}
}