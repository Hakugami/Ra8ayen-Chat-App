package dao.impl;

import dao.GroupParticipantDao;
import model.entities.GroupParticipant;
import persistence.connection.DataSourceSingleton;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GroupParticipantDaoImpl implements GroupParticipantDao {

    public GroupParticipant get(int groupId, int participantUserId) {
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
    public GroupParticipant get(int id){
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

    public List<GroupParticipant> getGroups(int participantUserId) {
        List<GroupParticipant> groupParticipants = new ArrayList<>();
        String query = "SELECT * FROM GroupParticipants WHERE ParticipantUserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, participantUserId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    groupParticipants.add(createGroupParticipantFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupParticipants;
    }

    @Override
    public List<GroupParticipant> getAll() {
        List<GroupParticipant> groupParticipants = new ArrayList<>();
        String query = "SELECT * FROM GroupParticipants";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                groupParticipants.add(createGroupParticipantFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupParticipants;
    }

    @Override
    public void save(GroupParticipant groupParticipant) {
        String query = "INSERT INTO GroupParticipants (GroupID, ParticipantUserID, ParticipantStartDate) VALUES (?, ?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupParticipant.getGroupId());
            statement.setInt(2, groupParticipant.getParticipantUserId());
            statement.setTimestamp(3, Timestamp.valueOf(groupParticipant.getParticipantStartDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GroupParticipant groupParticipant) {
        String query = "UPDATE GroupParticipants SET ParticipantStartDate = ? WHERE GroupID = ? AND ParticipantUserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(groupParticipant.getParticipantStartDate()));
            statement.setInt(2, groupParticipant.getGroupId());
            statement.setInt(3, groupParticipant.getParticipantUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(GroupParticipant groupParticipant) {
        String query = "DELETE FROM GroupParticipants WHERE GroupID = ? AND ParticipantUserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupParticipant.getGroupId());
            statement.setInt(2, groupParticipant.getParticipantUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private GroupParticipant createGroupParticipantFromResultSet(ResultSet resultSet) throws SQLException {
        int groupId = resultSet.getInt("GroupID");
        int participantUserId = resultSet.getInt("ParticipantUserID");
        LocalDateTime participantStartDate = resultSet.getTimestamp("ParticipantStartDate").toLocalDateTime();
        return new GroupParticipant(groupId, participantUserId, participantStartDate.toString());
    }
}