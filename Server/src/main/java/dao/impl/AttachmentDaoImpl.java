package dao.impl;

import dao.AttachmentDao;
import model.entities.Attachment;
import persistence.connection.DataSourceSingleton;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttachmentDaoImpl implements AttachmentDao {

    @Override
    public boolean save(Attachment attachment) {
        String query = "INSERT INTO Attachment (MessageID, Attachment) VALUES (?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, attachment.getMessageId());
            ByteArrayInputStream input = new ByteArrayInputStream(attachment.getAttachment());
            statement.setBinaryStream(2, input);
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Attachment get(int id) {
        String query = "SELECT * FROM Attachment WHERE AttachmentID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createAttachmentFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Attachment> getAll() {
        List<Attachment> attachments = new ArrayList<>();
        String query = "SELECT * FROM Attachment";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                attachments.add(createAttachmentFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return attachments;
    }

    @Override
    public boolean update(Attachment attachment) {
        String query = "UPDATE Attachment SET Attachment = ? WHERE AttachmentID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ByteArrayInputStream input = new ByteArrayInputStream(attachment.getAttachment());
            statement.setBinaryStream(1, input);
            statement.setInt(2, attachment.getAttachmentId());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Attachment attachment) {
        String query = "DELETE FROM Attachment WHERE AttachmentID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, attachment.getAttachmentId());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private Attachment createAttachmentFromResultSet(ResultSet resultSet) throws SQLException {
        Attachment attachment = new Attachment();
        attachment.setAttachmentId(resultSet.getInt("AttachmentID"));
        attachment.setMessageId(resultSet.getInt("MessageID"));
        Blob blob = resultSet.getBlob("Attachment");
        attachment.setAttachment(blob.getBytes(1, (int) blob.length()));
        return attachment;
    }
}