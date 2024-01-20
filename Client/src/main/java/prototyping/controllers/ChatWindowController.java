package prototyping.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class ChatWindowController {
    @FXML
    public VBox chatBox;
    @FXML
    public TextField inputField;
    @FXML
    public ListView<String> chatListView;
    @FXML
    public Button sendButton;
    public Button emoji_btn;
    public Button voiceCall_btn;
    public Text contactName_field;
    public Button videoCall_btn;

    private ObservableList<String> chatMessages;

    public void initialize() {
        chatMessages = FXCollections.observableArrayList();
        chatListView.setItems(chatMessages);

        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });

        sendButton.setOnAction(event -> sendMessage());

        // Allow multiple items to be selected
        chatListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        chatListView.setCellFactory(param -> new ListCell<>() {
            private final Label label = new Label();
            private final ContextMenu contextMenu = new ContextMenu();
            private final MenuItem copyMenuItem = new MenuItem("Copy");

            {
                label.getStyleClass().add("chat_message_bubble");
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                copyMenuItem.setOnAction(event -> {
                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    final ClipboardContent content = new ClipboardContent();
                    // Copy all selected items to the clipboard
                    content.putString(String.join("\n", chatListView.getSelectionModel().getSelectedItems()));
                    clipboard.setContent(content);
                });

                contextMenu.getItems().add(copyMenuItem);
                contextMenu.getStyleClass().add("context_menu"); // Add the new CSS class to the context menu
                setContextMenu(contextMenu);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    setGraphic(label);
                }
            }
        });
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            Platform.runLater(() -> {
                chatMessages.add(message);
                inputField.clear();
            });
        }
    }
}