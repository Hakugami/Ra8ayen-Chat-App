package model;


import view.ControllerFactory;
import view.ViewFactory;

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

}
