package dao.impl;

import dao.UserContactsDao;
import model.entities.User;
import model.entities.UserContacts;
import persistence.connection.DataSourceSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserContactsDaoImpl implements UserContactsDao {

    @Override
    public void save(UserContacts userContacts) {
        String query = "INSERT INTO UserContacts (FriendID, UserID, CreationDate) VALUES (?, ?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userContacts.getFriendID());
            statement.setInt(2, userContacts.getUserID());
            statement.setString(3, userContacts.getCreationDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserContacts get(int userId) {
        String query = "SELECT * FROM UserContacts WHERE UserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createUserContactsFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UserContacts> getAll() {
        List<UserContacts> userContactsList = new ArrayList<>();
        String query = "SELECT * FROM UserContacts";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                userContactsList.add(createUserContactsFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userContactsList;
    }

    @Override
    public void update(UserContacts userContacts) {
        String query = "UPDATE UserContacts SET FriendID = ? WHERE UserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userContacts.getFriendID());
            statement.setInt(2, userContacts.getUserID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UserContacts userContacts) {
        String query = "DELETE FROM UserContacts WHERE UserID = ? AND FriendID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userContacts.getUserID());
            statement.setInt(2, userContacts.getFriendID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<UserContacts> getContactById(User user) {
        List<UserContacts> userContactsList = new ArrayList<>();
        String query = "SELECT * FROM UserContacts WHERE UserID = ? AND FriendID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                userContactsList.add(createUserContactsFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userContactsList;
    }

    private UserContacts createUserContactsFromResultSet(ResultSet resultSet) throws SQLException {
        int friendId = resultSet.getInt("FriendID");
        int userId = resultSet.getInt("UserID");
        String creationDate = resultSet.getString("CreationDate");
        return new UserContacts(friendId, userId, creationDate);
    }
}