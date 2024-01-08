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
        String query= "INSERT INTO UserMessages(SenderID, ReceiverID, MessageContent,MessageTimestamp,FontStyle,FontColor,TextBackground,FontSize,Bold,Italic,Underline,Emoji) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
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
        String query = "SELECT * FROM UserMessages where SenderID = ? and ReceiverID = ? ";
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
        String query = "SELECT * FROM UserMessages";
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
        String query = "SELECT * FROM UserMessages where SenderID = ? ";
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
        String query = "UPDATE UserMessages SET MessageContent = ? WHERE MessageID = ?";
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
        String query = "DELETE FROM usermessages  WHERE MessageID = ?";
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
        statement.setString(5,message.getFontStyle());
        statement.setString(6,message.getFontColor());
        statement.setString(7,message.getTextBackground());
        statement.setInt(8,message.getFontSize());
        statement.setBoolean(9,message.getBold());
        statement.setBoolean(10,message.getItalic());
        statement.setBoolean(11,message.getUnderline());
        statement.setString(12,message.getEmoji());
    }
    private Message getMessageFromResultSet(ResultSet resultSet) throws SQLException {
        Message message = new Message();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        message.setMessageId(resultSet.getInt(MessageTable.MessageID.name));
        message.setSenderId(resultSet.getInt(MessageTable.SenderID.name));
        message.setReceiverId(resultSet.getInt(MessageTable.ReceiverID.name));
        message.setMessageContent(resultSet.getString(MessageTable.MessageContent.name));
        message.setTime(LocalDateTime.parse(resultSet.getString(MessageTable.MessageTimestamp.name),formatter));
        message.setFontStyle(resultSet.getString(MessageTable.MessageFontStyle.name));
        message.setFontColor(resultSet.getString(MessageTable.MessageFontColor.name));
        message.setTextBackground(resultSet.getString(MessageTable.TextBackground.name));
        message.setFontSize(resultSet.getInt(MessageTable.FontSize.name));
        message.setBold(resultSet.getBoolean(MessageTable.Bold.name));
        message.setItalic(resultSet.getBoolean(MessageTable.Italic.name));
        message.setUnderline(resultSet.getBoolean(MessageTable.Underline.name));
        message.setEmoji(resultSet.getString(MessageTable.Emoji.name));
        return  message;
    }
}
