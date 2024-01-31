package dto.requests;

import dto.Model.UserModel;

public class UpdateUserRequest {
    private UserModel userModel;

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
