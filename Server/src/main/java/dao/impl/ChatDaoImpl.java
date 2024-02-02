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
    public List<Chat> getGroupChats(int userId) {
        List<Chat> chatGroups = new ArrayList<>();
        String query = "SELECT c.* FROM Chat c " +
                "INNER JOIN ChatParticipants cp ON c.ChatID = cp.ChatID " +
                "WHERE cp.ParticipantUserID = ? AND c.AdminID IS NOT NULL";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    chatGroups.add(createChatFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return chatGroups;
    }

    public Chat getPrivateChat(int userId, int friendId) {
        Chat privateChat = null;
        String query =
                "SELECT c.* " +
                        "FROM ChatParticipants cp " +
                        "INNER JOIN Chat c ON cp.ChatID = c.ChatID " +
                        "WHERE cp.ParticipantUserID IN (?, ?) " +
                        "AND c.AdminID IS NULL " +
                        "GROUP BY cp.ChatID " +
                        "HAVING COUNT(cp.ParticipantUserID) = 2 ";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, friendId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    privateChat = createChatFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return privateChat;
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
            if(rowsAffected >= 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    @Override
    public int saveGroupChat(Chat chatGroup) {
        String query = "INSERT INTO Chat (ChatName, AdminID, ChatImage) VALUES (?, ?, ?)";
        ResultSet generatedKeys = null;
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, chatGroup.getName());
            statement.setInt(2, chatGroup.getAdminId());
            statement.setBytes(3, chatGroup.getChatImage());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected >= 1) {
                generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return -1;
    }

    @Override
    public int savePrivateChat(Chat chat) {
        String query = "INSERT INTO Chat (ChatName) VALUES (?)";
        ResultSet generatedKeys = null;
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, chat.getName());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected >= 1) {
                generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return -1;
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
            if(rowsAffected >= 1) {
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
            if(rowsAffected >= 1) {
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
      // byte[] image = imageBlob.getBytes(1, (int) imageBlob.length());
        byte[] profilePicture =  null;
        if (imageBlob != null) {
            profilePicture = imageBlob.getBytes(1, (int) imageBlob.length());
        }
        String creationDate = resultSet.getTimestamp(ChatTable.CREATIONDATE.name()).toString();
        String lastModified = resultSet.getTimestamp(ChatTable.LASTMODIFIED.name()).toString();
        return new Chat(id, name, adminId, profilePicture, creationDate, lastModified);
    }
}