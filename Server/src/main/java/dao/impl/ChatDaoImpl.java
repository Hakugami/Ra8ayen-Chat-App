package dao.impl;

import dao.ChatDao;
import model.entities.Chat;
import persistence.connection.DataSourceSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ChatDaoImpl implements ChatDao {

    @Override
    public Chat get(int id) {
        String query = "SELECT * FROM Chat WHERE ChatID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createChatFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Chat getChatByName(String name) {
        String query = "SELECT * FROM Chat WHERE ChatName = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createChatFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Chat> getAll() {
        List<Chat> chatGroups = new ArrayList<>();
        String query = "SELECT * FROM Chat";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                chatGroups.add(createChatFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatGroups;
    }

    @Override
    public void save(Chat chat) {
        String query = "INSERT INTO Chat (ChatName, AdminID, ChatImage) VALUES (?, ?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chat.getName());
            statement.setInt(2, chat.getAdminId());
            statement.setBinaryStream(3, chat.g);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Chat chatGroup) {
        String query = "UPDATE ChatGroups SET GroupName = ?, CreatedByUserID = ? WHERE GroupID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chatGroup.getName());
            statement.setInt(2, chatGroup.getAdminId());
            statement.setInt(3, chatGroup.getGroupId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Chat chatGroup) {
        String query = "DELETE FROM Chat WHERE ChatID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chatGroup.getGroupId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Chat createChatFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("GroupID");
        String name = resultSet.getString("GroupName");
        int adminId = resultSet.getInt("CreatedByUserID");
        String creationDate = resultSet.getTimestamp("CreationTimestamp").toString();
        return new Chat(id, name, adminId, creationDate);
    }
}