package controllers;

public enum Scenes {
    SERVER("/Fxml/Server.fxml"),
    DASHBOARD("/Fxml/Dashboard.fxml"),
    ANNOUNCEMENT("/Fxml/Announcement.fxml"),
    SERVICE_START("/Fxml/ServiceStart.fxml"),
    USER_LIST_VIEW("/Fxml/UserListView.fxml");

    private final String fxmlPath;

    Scenes(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }
    public String getFxmlPath() {
        return fxmlPath;
    }
}
