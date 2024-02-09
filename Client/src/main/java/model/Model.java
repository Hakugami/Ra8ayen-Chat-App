package model;


import javafx.scene.image.Image;
import view.ControllerFactory;
import view.ViewFactory;

import java.util.Objects;

public class Model {
    private static Model instance = null;
    private final ViewFactory viewFactory;
    private final ControllerFactory controllerFactory;

    private Model() {
        this.viewFactory = new ViewFactory();
        this.controllerFactory = new ControllerFactory();
    }

    public static synchronized Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }
    public ControllerFactory getControllerFactory() {
        return controllerFactory;
    }

    public Image getIcon() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/speak.png")));
    }
}
