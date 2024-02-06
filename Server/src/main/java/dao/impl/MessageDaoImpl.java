package dao.impl;

import dao.MessageDao;
import model.entities.Message;
import model.entities.MessageTable;
import persistence.connection.DataSourceSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl implements MessageDao {
    @Override
    public boolean save(Message message) {
        String query = "INSERT INTO Messages(SenderID, ReceiverID, MessageContent,MessageTimestamp,IsAttachment, FontStyle, FontColor, TextBackground, FontSize, IsBold, IsItalic, IsUnderline) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            createStatementForInsert(statement, message);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected >= 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


    public int saveAndReturnId(Message message) {
        String query = "INSERT INTO Messages(SenderID, ReceiverID, MessageContent,MessageTimestamp,IsAttachment, FontStyle, FontColor, TextBackground, FontSize, IsBold, IsItalic, IsUnderline) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            createStatementForInsert(statement, message);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating message failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating message failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    @Override
    public List<Message> getChatMessages(int chatID) {
        String query = "SELECT * FROM Messages where  ReceiverID = ? ";
        List<Message> result = new ArrayList<>();
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, chatID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Message message;
                    message = getMessageFromResultSet(resultSet);
                    result.add(message);
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;

    }

    @Override
    public List<Message> getAll() {
        String query = "SELECT * FROM Messages";
        List<Message> result = new ArrayList<>();
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Message message;
                    message = getMessageFromResultSet(resultSet);
                    result.add(message);
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public Message get(int senderId) {
        String query = "SELECT * FROM Messages where SenderID = ? ";
        Message message = new Message();
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, senderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    message = getMessageFromResultSet(resultSet);
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    @Override
    public boolean update(Message message) {
        String query = "UPDATE Messages SET MessageContent = ? WHERE MessageID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            createStatementForUpdate(statement, message);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected >= 1) {
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
            createStatementForDelete(statement, message);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected >= 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void createStatementForUpdate(PreparedStatement statement, Message message) throws SQLException {
        statement.setString(1, message.getMessageContent());
        statement.setInt(2, message.getMessageId());
    }

    private void createStatementForDelete(PreparedStatement statement, Message message) throws SQLException {
        statement.setInt(1, message.getMessageId());
    }

    private void createStatementForInsert(PreparedStatement statement, Message message) throws SQLException {
        statement.setInt(1, message.getSenderId());
        statement.setInt(2, message.getReceiverId());
        statement.setString(3, message.getMessageContent());
        statement.setTimestamp(4, Timestamp.valueOf(message.getTime()));
        statement.setBoolean(5, message.getIsAttachment());
        statement.setString(6, message.getFontStyle());
        statement.setString(7, message.getFontColor());
        statement.setString(8, message.getTextBackground());
        statement.setInt(9, message.getFontSize());
        statement.setBoolean(10, message.isBold());
        statement.setBoolean(11, message.isItalic());
        statement.setBoolean(12, message.isUnderline());
    }

 private Message getMessageFromResultSet(ResultSet resultSet) throws SQLException {
    Message message = new Message();
    message.setMessageId(resultSet.getInt(MessageTable.MessageID.name));
    message.setSenderId(resultSet.getInt(MessageTable.SenderID.name));
    message.setReceiverId(resultSet.getInt(MessageTable.ReceiverID.name));
    message.setMessageContent(resultSet.getString(MessageTable.MessageContent.name));
    message.setTime(resultSet.getTimestamp(MessageTable.MessageTimestamp.name).toLocalDateTime());
    message.setIsAttachment(resultSet.getBoolean(MessageTable.IsAttachment.name));
    message.setFontStyle(resultSet.getString(MessageTable.FontStyle.name));
    message.setFontColor(resultSet.getString(MessageTable.FontColor.name));
    message.setTextBackground(resultSet.getString(MessageTable.TextBackground.name));
    message.setFontSize(resultSet.getInt(MessageTable.FontSize.name));
    message.setBold(resultSet.getBoolean(MessageTable.IsBold.name));
    message.setItalic(resultSet.getBoolean(MessageTable.IsItalic.name));
    message.setUnderline(resultSet.getBoolean(MessageTable.IsUnderline.name));
    message.setFontStyle(resultSet.getString(MessageTable.FontStyle.name));
    return message;
}

    public int sendMessageWithAttachment(Message message) {
        ResultSet generatedKeys = null;
        String query = "INSERT INTO Messages(SenderID, ReceiverID, MessageContent,MessageTimestamp,IsAttachment, FontStyle, FontColor, TextBackground, FontSize, IsBold, IsItalic, IsUnderline) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            createStatementForInsert(statement, message);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected >= 1) {
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
}
