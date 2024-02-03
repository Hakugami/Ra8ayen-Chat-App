package application;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;


import java.io.IOException;
import java.rmi.NotBoundException;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException, NotBoundException {
        Model.getInstance().getViewFactory().showLoginWindow();
    }
}