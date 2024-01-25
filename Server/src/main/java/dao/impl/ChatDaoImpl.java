package dao.impl;

import dao.ChatDao;
import model.entities.Chat;
import model.entities.ChatTable;
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }
        return chatGroups;
    }

    @Override
    public boolean save(Chat chat) {
        String query = "INSERT INTO Chat (ChatName, AdminID, ChatImage) VALUES (?, ?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chat.getName());
            statement.setInt(2, chat.getAdminId());
            statement.setBytes(3, chat.getChatImage());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Chat chat) {
        String query = "UPDATE Chat SET ChatName = ?, AdminID = ?, ChatImage = ? WHERE ChatID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chat.getName());
            statement.setInt(2, chat.getAdminId());
            statement.setBytes(3, chat.getChatImage());
            statement.setInt(4, chat.getChatId());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Chat chat) {
        String query = "DELETE FROM Chat WHERE ChatID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chat.getChatId());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private Chat createChatFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(ChatTable.CHATID.name());
        String name = resultSet.getString(ChatTable.CHATNAME.name());
        int adminId = resultSet.getInt(ChatTable.ADMINID.name());
        Blob imageBlob = resultSet.getBlob(ChatTable.CHATIMAGE.name());
        byte[] image = imageBlob.getBytes(1, (int) imageBlob.length());
        String creationDate = resultSet.getTimestamp(ChatTable.CREATIONDATE.name()).toString();
        String lastModified = resultSet.getTimestamp(ChatTable.LASTMODIFIED.name()).toString();
        return new Chat(id, name, adminId, image, creationDate, lastModified);
    }
}