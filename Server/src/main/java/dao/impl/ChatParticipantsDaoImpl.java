package dao.impl;

import dao.ChatParticipantsDao;
import model.entities.ChatParticipant;
import persistence.connection.DataSourceSingleton;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatParticipantsDaoImpl implements ChatParticipantsDao {

    public ChatParticipant get(int groupId, int participantUserId) {
        String query = "SELECT * FROM GroupParticipants WHERE GroupID = ? AND ParticipantUserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupId);
            statement.setInt(2, participantUserId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createGroupParticipantFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //useless ?
    @Override
    public ChatParticipant get(int id){
        String query = "SELECT * FROM GroupParticipants WHERE GroupID = ?";
        try(Connection connection = DataSourceSingleton.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
                statement.setInt(1, id);
                try(ResultSet resultSet = statement.executeQuery()){
                    if(resultSet.next()){
                        return createGroupParticipantFromResultSet(resultSet);
                    }
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
            return null;

    }

    public List<ChatParticipant> getGroups(int participantUserId) {
        List<ChatParticipant> chatParticipants = new ArrayList<>();
        String query = "SELECT * FROM GroupParticipants WHERE ParticipantUserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, participantUserId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    chatParticipants.add(createGroupParticipantFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatParticipants;
    }

    @Override
    public List<ChatParticipant> getAll() {
        List<ChatParticipant> chatParticipants = new ArrayList<>();
        String query = "SELECT * FROM GroupParticipants";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                chatParticipants.add(createGroupParticipantFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatParticipants;
    }

    @Override
    public void save(ChatParticipant chatParticipant) {
        String query = "INSERT INTO GroupParticipants (GroupID, ParticipantUserID, ParticipantStartDate) VALUES (?, ?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chatParticipant.getGroupId());
            statement.setInt(2, chatParticipant.getParticipantUserId());
            statement.setTimestamp(3, Timestamp.valueOf(chatParticipant.getParticipantStartDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ChatParticipant chatParticipant) {
        String query = "UPDATE GroupParticipants SET ParticipantStartDate = ? WHERE GroupID = ? AND ParticipantUserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(chatParticipant.getParticipantStartDate()));
            statement.setInt(2, chatParticipant.getGroupId());
            statement.setInt(3, chatParticipant.getParticipantUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ChatParticipant chatParticipant) {
        String query = "DELETE FROM GroupParticipants WHERE GroupID = ? AND ParticipantUserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chatParticipant.getGroupId());
            statement.setInt(2, chatParticipant.getParticipantUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ChatParticipant createGroupParticipantFromResultSet(ResultSet resultSet) throws SQLException {
        int groupId = resultSet.getInt("GroupID");
        int participantUserId = resultSet.getInt("ParticipantUserID");
        LocalDateTime participantStartDate = resultSet.getTimestamp("ParticipantStartDate").toLocalDateTime();
        return new ChatParticipant(groupId, participantUserId, participantStartDate.toString());
    }
}