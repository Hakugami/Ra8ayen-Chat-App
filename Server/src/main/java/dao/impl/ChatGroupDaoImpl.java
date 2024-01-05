package dao.impl;

import dao.ChatGroupDao;
import model.entities.ChatGroup;
import persistence.connection.DataSourceSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatGroupDaoImpl implements ChatGroupDao {

    @Override
    public ChatGroup getChatGroupById(int id) {
        String query = "SELECT * FROM ChatGroups WHERE GroupID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createChatGroupFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ChatGroup getChatGroupByName(String name) {
        String query = "SELECT * FROM ChatGroups WHERE GroupName = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createChatGroupFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ChatGroup> findAllChatGroups() {
        List<ChatGroup> chatGroups = new ArrayList<>();
        String query = "SELECT * FROM ChatGroups";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                chatGroups.add(createChatGroupFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatGroups;
    }

    @Override
    public void saveChatGroup(ChatGroup chatGroup) {
        String query = "INSERT INTO ChatGroups (GroupName, CreatedByUserID) VALUES (?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chatGroup.getName());
            statement.setInt(2, chatGroup.getAdminId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateChatGroup(ChatGroup chatGroup) {
        String query = "UPDATE ChatGroups SET GroupName = ?, CreatedByUserID = ? WHERE GroupID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chatGroup.getName());
            statement.setInt(2, chatGroup.getAdminId());
            statement.setInt(3, chatGroup.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteChatGroup(ChatGroup chatGroup) {
        String query = "DELETE FROM ChatGroups WHERE GroupID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chatGroup.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ChatGroup createChatGroupFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("GroupID");
        String name = resultSet.getString("GroupName");
        int adminId = resultSet.getInt("CreatedByUserID");
        String creationDate = resultSet.getTimestamp("CreationTimestamp").toString();
        return new ChatGroup(id, name, adminId, creationDate);
    }
}