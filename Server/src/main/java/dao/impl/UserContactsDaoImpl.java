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
    public boolean save(UserContacts userContacts) {
        String query = "INSERT INTO UserContacts (FriendID, UserID, CreationDate) VALUES (?, ?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userContacts.getFriendID());
            statement.setInt(2, userContacts.getUserID());
            statement.setString(3, userContacts.getCreationDate());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected >= 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }
        return userContactsList;
    }

    @Override
    public boolean update(UserContacts userContacts) {
        String query = "UPDATE UserContacts SET FriendID = ? WHERE UserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userContacts.getFriendID());
            statement.setInt(2, userContacts.getUserID());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected >= 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(UserContacts userContacts) {
        String query = "DELETE FROM UserContacts WHERE UserID = ? AND FriendID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userContacts.getUserID());
            statement.setInt(2, userContacts.getFriendID());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected >= 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private UserContacts createUserContactsFromResultSet(ResultSet resultSet) throws SQLException {
        int friendId = resultSet.getInt("FriendID");
        int userId = resultSet.getInt("UserID");
        String creationDate = resultSet.getString("CreationDate");
        return new UserContacts(friendId, userId, creationDate);
    }


}