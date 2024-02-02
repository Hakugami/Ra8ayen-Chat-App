package dao.impl;

import dao.MessageDao;
import model.entities.Message;
import persistence.connection.DataSourceSingleton;
import model.entities.MessageTable;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl implements MessageDao {
    @Override
    public boolean save(Message message) {
        String query= "INSERT INTO Messages(SenderID, ReceiverID, MessageContent,MessageTimestamp,IsAttachment) VALUES(?,?,?,?,?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            createStatementForInsert(statement,message);
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
    public List<Message> getChatMessages(int chatID) {
        String query = "SELECT * FROM Messages where  ReceiverID = ? ";
        List<Message> result= new ArrayList<>();
        try(Connection connection = DataSourceSingleton.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ){
            statement.setInt(1,chatID);
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    Message message ;
                    message=getMessageFromResultSet(resultSet);
                    result.add(message);
                }

            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return result;

    }

    @Override
    public List<Message> getAll() {
        String query = "SELECT * FROM Messages";
        List<Message> result= new ArrayList<>();
        try(Connection connection = DataSourceSingleton.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ){
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    Message message ;
                    message=getMessageFromResultSet(resultSet);
                    result.add(message);
                }

            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public Message get(int senderId) {
        String query = "SELECT * FROM Messages where SenderID = ? ";
        Message message = new Message();
        try(Connection connection = DataSourceSingleton.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ){
            statement.setInt(1,senderId);
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    message=getMessageFromResultSet(resultSet);
                }

            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }

    @Override
    public boolean update(Message message) {
        String query = "UPDATE Messages SET MessageContent = ? WHERE MessageID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            createStatementForUpdate(statement,message);
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
    public boolean delete(Message message) {
        String query = "DELETE FROM Messages  WHERE MessageID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            createStatementForDelete(statement,message);
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected >= 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    private void createStatementForUpdate(PreparedStatement statement , Message message) throws SQLException{
        statement.setString(1, message.getMessageContent());
        statement.setInt(2, message.getMessageId());
    }
    private void createStatementForDelete(PreparedStatement statement, Message message) throws SQLException{
        statement.setInt(1,message.getMessageId());
    }
private void createStatementForInsert( PreparedStatement statement, Message message) throws SQLException {
    statement.setInt(1,message.getSenderId());
    statement.setInt(2,message.getReceiverId());
    statement.setString(3,message.getMessageContent());
    statement.setTimestamp(4, Timestamp.valueOf(message.getTime()));
    statement.setBoolean(5, message.isAttachment());
}

private Message getMessageFromResultSet(ResultSet resultSet) throws SQLException {
    Message message = new Message();
    message.setMessageId(resultSet.getInt(MessageTable.MessageID.name));
    message.setSenderId(resultSet.getInt(MessageTable.SenderID.name));
    message.setReceiverId(resultSet.getInt(MessageTable.ReceiverID.name));
    message.setMessageContent(resultSet.getString(MessageTable.MessageContent.name));
    message.setTime(resultSet.getTimestamp(MessageTable.MessageTimestamp.name).toLocalDateTime());
    message.setAttachment(resultSet.getBoolean(MessageTable.IsAttachment.name));
    return  message;
}
}
