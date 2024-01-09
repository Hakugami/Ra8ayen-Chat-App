//package dao.impl;
//
//import dao.GroupChatMessageDao;
//import model.entities.GroupChatMessage;
//import persistence.connection.DataSourceSingleton;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class GroupChatMessageDaoImpl implements GroupChatMessageDao {
//    @Override
//    public GroupChatMessage get(int id) {
//        String query = "SELECT * FROM GroupChatMessages WHERE GroupMessageID = ?";
//        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, id);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    return createGroupChatMessageFromResultSet(resultSet);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public List<GroupChatMessage> getAll() {
//        List<GroupChatMessage> groupChatMessages = new ArrayList<>();
//        String query = "SELECT * FROM GroupChatMessages";
//        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(query)) {
//            while (resultSet.next()) {
//                groupChatMessages.add(createGroupChatMessageFromResultSet(resultSet));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return groupChatMessages;
//    }
//
//    @Override
//    public void save(GroupChatMessage groupChatMessage) {
//        String query = "INSERT INTO GroupChatMessages (GroupID, SenderUserID, MessageContent) VALUES (?, ?, ?)";
//        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, groupChatMessage.getGroupId());
//            statement.setInt(2, groupChatMessage.getSenderId());
//            statement.setString(3, groupChatMessage.getMessageContent());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void update(GroupChatMessage groupChatMessage) {
//        String query = "UPDATE GroupChatMessages SET GroupID = ?, SenderUserID = ?, MessageContent = ? WHERE GroupMessageID = ?";
//        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, groupChatMessage.getGroupId());
//            statement.setInt(2, groupChatMessage.getSenderId());
//            statement.setString(3, groupChatMessage.getMessageContent());
//            statement.setInt(4, groupChatMessage.getGroupMessageId());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void delete(GroupChatMessage groupChatMessage) {
//        String query = "DELETE FROM GroupChatMessages WHERE GroupMessageID = ?";
//        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, groupChatMessage.getGroupMessageId());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    // helper method
//    private GroupChatMessage createGroupChatMessageFromResultSet(ResultSet resultSet) throws SQLException {
//        int groupMessageId = resultSet.getInt("GroupMessageID");
//        int groupId = resultSet.getInt("GroupID");
//        int senderId = resultSet.getInt("SenderUserID");
//        String messageContent = resultSet.getString("MessageContent");
//        String messageDate = resultSet.getTimestamp("MessageTimestamp").toString();
//        return new GroupChatMessage(groupMessageId, groupId, senderId, messageContent, messageDate);
//    }
//}