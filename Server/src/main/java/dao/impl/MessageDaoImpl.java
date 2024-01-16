package dao.impl;

import dao.MessageDao;
import model.entities.Message;
import persistence.connection.DataSourceSingleton;

import model.entities.MessageTable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MessageDaoImpl implements MessageDao {
    @Override
    public void save(Message message) {
        String query= "INSERT INTO Messages(SenderID, ReceiverID, MessageContent,MessageTimestamp,IsAttachment) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            createStatementForInsert(statement,message);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> get(int sender, int receiver) {
        String query = "SELECT * FROM Messages where SenderID = ? and ReceiverID = ? ";
        List<Message> result= new ArrayList<>();
        try(Connection connection = DataSourceSingleton.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ){
            statement.setInt(1,sender);
            statement.setInt(2,receiver);
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    Message message ;
                    message=getMessageFromResultSet(resultSet);
                    result.add(message);
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public void update(Message message) {
        String query = "UPDATE Messages SET MessageContent = ? WHERE MessageID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            createStatementForUpdate(statement,message);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Message message) {
        String query = "DELETE FROM Messages  WHERE MessageID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            createStatementForDelete(statement,message);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        statement.setString(4,message.getTime().toString());
    }
    private Message getMessageFromResultSet(ResultSet resultSet) throws SQLException {
        Message message = new Message();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        message.setMessageId(resultSet.getInt(MessageTable.MessageID.name));
        message.setSenderId(resultSet.getInt(MessageTable.SenderID.name));
        message.setReceiverId(resultSet.getInt(MessageTable.ReceiverID.name));
        message.setMessageContent(resultSet.getString(MessageTable.MessageContent.name));
        message.setTime(LocalDateTime.parse(resultSet.getString(MessageTable.MessageTimestamp.name),formatter));
        message.setAttachment(resultSet.getBoolean(resultSet.getInt(MessageTable.IsAttachment.name)));
        return  message;
    }
}
