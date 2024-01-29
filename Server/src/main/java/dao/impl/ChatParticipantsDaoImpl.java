package dao.impl;

import dao.ChatParticipantsDao;
import model.entities.ChatParticipant;
import model.entities.ChatParticipantTable;
import persistence.connection.DataSourceSingleton;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatParticipantsDaoImpl implements ChatParticipantsDao {

    public ChatParticipant get(int chatId, int participantUserId) {
        String query = "SELECT * FROM ChatParticipants WHERE ChatID = ? AND ParticipantUserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chatId);
            statement.setInt(2, participantUserId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createChatParticipantFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public ChatParticipant get(int id){
        String query = "SELECT * FROM ChatParticipants WHERE ChatID = ?";
        try(Connection connection = DataSourceSingleton.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
                statement.setInt(1, id);
                try(ResultSet resultSet = statement.executeQuery()){
                    if(resultSet.next()){
                        return createChatParticipantFromResultSet(resultSet);
                    }
                }
            } catch(SQLException e){
                System.out.println(e.getMessage());
            }
            return null;
    }

    public List<ChatParticipant> getChats(int participantUserId) {
        List<ChatParticipant> chatParticipants = new ArrayList<>();
        String query = "SELECT * FROM ChatParticipants WHERE ParticipantUserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, participantUserId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    chatParticipants.add(createChatParticipantFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return chatParticipants;
    }

    @Override
    public List<ChatParticipant> getAll() {
        List<ChatParticipant> chatParticipants = new ArrayList<>();
        String query = "SELECT * FROM ChatParticipants";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                chatParticipants.add(createChatParticipantFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return chatParticipants;
    }

    @Override
    public boolean save(ChatParticipant chatParticipant) {
        String query = "INSERT INTO ChatParticipants (ChatID, ParticipantUserID) VALUES (?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chatParticipant.getChatId());
            statement.setInt(2, chatParticipant.getParticipantUserId());
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
    public boolean update(ChatParticipant chatParticipant) {
        String query = "UPDATE ChatParticipants SET ParticipantStartDate = ? WHERE ChatID = ? AND ParticipantUserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(chatParticipant.getParticipantStartDate()));
            statement.setInt(2, chatParticipant.getChatId());
            statement.setInt(3, chatParticipant.getParticipantUserId());
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
    public boolean delete(ChatParticipant chatParticipant) {
        String query = "DELETE FROM ChatParticipants WHERE ChatID = ? AND ParticipantUserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chatParticipant.getChatId());
            statement.setInt(2, chatParticipant.getParticipantUserId());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private ChatParticipant createChatParticipantFromResultSet(ResultSet resultSet) throws SQLException {
        int chatId = resultSet.getInt(ChatParticipantTable.ChatID.name());
        int participantUserId = resultSet.getInt(ChatParticipantTable.ParticipantUserID.name());
        LocalDateTime participantStartDate = resultSet.getTimestamp(ChatParticipantTable.ParticipantStartDate.name()).toLocalDateTime();
        return new ChatParticipant(chatId, participantUserId, participantStartDate.toString());
    }
}