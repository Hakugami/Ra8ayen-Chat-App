package dao.impl;

import dao.GroupParticipantDao;
import model.entities.GroupParticipant;
import persistence.connection.DataSourceSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupParticipantDaoImpl implements GroupParticipantDao {

    @Override
    public GroupParticipant get(int id) {
        String query = "SELECT * FROM GroupParticipants WHERE GroupMemberID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
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
        String query = "INSERT INTO GroupParticipants (GroupID, ParticipantUserID) VALUES (?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupParticipant.getGroupId());
            statement.setInt(2, groupParticipant.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GroupParticipant groupParticipant) {
        String query = "UPDATE GroupParticipants SET GroupID = ?, ParticipantUserID = ? WHERE GroupMemberID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupParticipant.getGroupId());
            statement.setInt(2, groupParticipant.getUserId());
            statement.setInt(3, groupParticipant.getGroupMemberId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(GroupParticipant groupParticipant) {
        String query = "DELETE FROM GroupParticipants WHERE GroupMemberID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupParticipant.getGroupMemberId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private GroupParticipant createGroupParticipantFromResultSet(ResultSet resultSet) throws SQLException {
        int groupMemberId = resultSet.getInt("GroupMemberID");
        int groupId = resultSet.getInt("GroupID");
        int userId = resultSet.getInt("ParticipantUserID");
        return new GroupParticipant(groupMemberId, groupId, userId);
    }
}