package model;

import controller.CallBackControllerImpl;
import dto.Model.UserModel;

import java.io.Serializable;
import java.rmi.RemoteException;

public class CurrentUser extends UserModel implements Serializable {
    private static CurrentUser currentUser;
    private CallBackControllerImpl callBackController = CallBackControllerImpl.getInstance();

    private CurrentUser() throws RemoteException {
    }

    public static CurrentUser getInstance() throws RemoteException {
        if (currentUser == null) {
            currentUser = new CurrentUser();
        }
        return currentUser;
    }

    public CallBackControllerImpl getCallBackController() {
        return callBackController;
    }


}
