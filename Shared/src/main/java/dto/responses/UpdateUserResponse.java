package dto.responses;

import dto.Model.UserModel;

import java.io.Serializable;
import java.util.Arrays;

public class UpdateUserResponse implements Serializable {
    private UserModel userModel;
    private boolean updated;
    private String errorMessage;

    public UpdateUserResponse(UserModel userModel, boolean updated) {
        this.userModel = userModel;
        this.updated = updated;
    }
    public UpdateUserResponse() {
    }
    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "UpdateUserResponse{" +
                "userModel=" + userModel +
                ", updated=" + updated +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
