package dao.impl;

import dao.ChatDao;
import model.entities.Chat;
import persistence.connection.DataSourceSingleton;

import java.io.InputStream;
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
            statement.setString(3, chat.getChatImage());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Chat chat) {
        String query = "UPDATE Chat SET ChatName = ?, AdminID = ?, ChatImage = ? WHERE ChatID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chat.getName());
            statement.setInt(2, chat.getAdminId());
            statement.setString(3, chat.getChatImage());
            statement.setInt(4, chat.getChatId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Chat chat) {
        String query = "DELETE FROM Chat WHERE ChatID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chat.getChatId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Chat createChatFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ChatID");
        String name = resultSet.getString("ChatName");
        int adminId = resultSet.getInt("AdminID");
        Blob imageBlob = resultSet.getBlob("ChatImage");
        String image = imageBlob == null ? null : new String(imageBlob.getBytes(1, (int) imageBlob.length()));
        String creationDate = resultSet.getTimestamp("CreationDate").toString();
        String lastModified = resultSet.getTimestamp("LastModified").toString();
        return new Chat(id, name, adminId, image, creationDate, lastModified);
    }
}