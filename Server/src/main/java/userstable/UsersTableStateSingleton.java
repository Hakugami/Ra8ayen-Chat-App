package userstable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.entities.User;

public class UsersTableStateSingleton {
    private ObservableList<User> users;
    private static UsersTableStateSingleton instance;

    private UsersTableStateSingleton() {
        this.users = FXCollections.observableArrayList();
    }

    public static UsersTableStateSingleton getInstance() {
        if (instance == null) {
            instance = new UsersTableStateSingleton();
        }
        return instance;
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public void setUsers(ObservableList<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void updateUser(User user) {
        int index = -1;
        for (int i = 0; i < this.users.size(); i++) {
            if (this.users.get(i).getUserID() == user.getUserID()) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            this.users.set(index, user);
        }
    }
}