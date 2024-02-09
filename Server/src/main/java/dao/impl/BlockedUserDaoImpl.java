package dao.impl;

import dao.BlockedUserDao;
import persistence.connection.DataSourceSingleton;
import java.sql.*;
import java.util.*;
import model.entities.*;

public class BlockedUserDaoImpl implements BlockedUserDao {
    @Override
    public BlockedUsers get(int id) {
        String query = "SELECT * FROM BlockedUsers WHERE BlockID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createBlockedUser(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<BlockedUsers> getAll() {
        List<BlockedUsers> blockedUsersList = new ArrayList<>();
        String query = "SELECT * FROM BlockedUsers";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                blockedUsersList.add(createBlockedUser(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return blockedUsersList;
    }

    @Override
    public boolean save(BlockedUsers blockedUser) {
        String query = "INSERT INTO BlockedUsers (BlockingUserID, BlockedUserID) VALUES (?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, blockedUser.getBlockingUserId());
            statement.setInt(2, blockedUser.getBlockedUserId());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected >= 1) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    @Override
    public int saveBlockedUser(BlockedUsers blockedUser) {
        String query = "INSERT INTO BlockedUsers (BlockingUserID, BlockedUserID) VALUES (?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, blockedUser.getBlockingUserId());
            statement.setInt(2, blockedUser.getBlockedUserId());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            return -1;
        }
        return -1;
    }

    @Override
    public boolean delete(BlockedUsers blockedUser) {
        String query = "DELETE FROM BlockedUsers WHERE BlockID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, blockedUser.getBlockId());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected >= 1) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }


    @Override
    public int deleteByBlockingAndBlockedUser(BlockedUsers blockedUser) {
        String query = "DELETE FROM BlockedUsers WHERE BlockingUserID = ? AND BlockedUserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, blockedUser.getBlockingUserId());
            statement.setInt(2, blockedUser.getBlockedUserId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean update(BlockedUsers blockedUser) {
        String query = "UPDATE BlockedUsers SET BlockingUserID = ?, BlockedUserID = ? WHERE BlockID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, blockedUser.getBlockingUserId());
            statement.setInt(2, blockedUser.getBlockedUserId());
            statement.setInt(3, blockedUser.getBlockId());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected >= 1) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean FriendIsBlocked(BlockedUsers blockedUsers) {
        String query = "SELECT * FROM BlockedUsers WHERE BlockingUserID = ? AND BlockedUserID = ?";
        try(Connection connection= DataSourceSingleton.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1,blockedUsers.getBlockingUserId());
            statement.setInt(2,blockedUsers.getBlockedUserId());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                System.out.println("Message From Doa Block Exist "+resultSet.toString());
                return true;
            }else{
                System.out.println("Message From Doa No Block Found ");
                return false;
            }
        } catch (SQLException e) {
           // throw new RuntimeException(e);
            System.out.println(" Message From Doa Exception happened " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<BlockedUsers> getBlockedContact(BlockedUsers blockedUsers){
        List<BlockedUsers> blockedUsersList = new ArrayList<>();
        String query = "SELECT * FROM BlockedUsers WHERE BlockingUserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, blockedUsers.getBlockingUserId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    blockedUsersList.add(createBlockedUser(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return blockedUsersList;
        }
        return blockedUsersList;
    }
    private BlockedUsers createBlockedUser(ResultSet resultSet) throws SQLException {
        int blockId = resultSet.getInt(BlockedUsersTable.BLOCKID.name());
        int blockingUserId = resultSet.getInt(BlockedUsersTable.BLOCKINGUSERID.name());
        int blockedUserId = resultSet.getInt(BlockedUsersTable.BLOCKEDUSERID.name());
        String blockDate = resultSet.getString(BlockedUsersTable.BLOCKDATE.name());

        return new BlockedUsers(blockId, blockingUserId, blockedUserId, blockDate);
    }

}