package dto.responses;

import dto.Model.UserModel;

import java.io.Serializable;
import java.util.Arrays;

public class UpdateUserResponse implements Serializable {
    private UserModel userModel;
    private boolean updated;

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

    @Override
    public String toString() {
        return "UpdateUserResponse{" +
                "userModel=" + userModel +
                ", updated=" + updated +
                '}';
    }
}
