package dto.requests;

import dto.Model.UserModel;

import java.io.Serializable;

public class UpdateUserRequest implements Serializable {
    private UserModel userModel;

    public UpdateUserRequest(UserModel userModel) {
        this.userModel = userModel;
    }

    public UpdateUserRequest() {
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "userModel=" + userModel +
                '}';
    }
}
