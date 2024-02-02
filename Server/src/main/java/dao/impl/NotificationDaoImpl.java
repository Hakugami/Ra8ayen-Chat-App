package dao.impl;

import dao.NotificationDao;
import persistence.connection.DataSourceSingleton;
import java.sql.*;
import java.util.*;
import model.entities.*;

public class NotificationDaoImpl implements NotificationDao {
    @Override
    public Notification get(int id) {
        String query = "SELECT * FROM usernotifications WHERE SenderID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createNotification(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Notification> getAll() {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM usernotifications";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                notifications.add(createNotification(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return notifications;
    }

    @Override
    public boolean save(Notification notification) {
        String query = "INSERT INTO usernotifications (ReceiverID, SenderID, NotificationMessage) VALUES (?, ?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, notification.getReceiverId());
            statement.setInt(2, notification.getSenderId());
            statement.setString(3, notification.getNotificationMessage());
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
    public boolean delete(Notification notification) {
        String query = "DELETE FROM usernotifications WHERE NotificationID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, notification.getNotificationId());
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
    public boolean update(Notification notification) {
        String query = "UPDATE usernotifications SET NotificationMessage = ? WHERE NotificationID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, notification.getNotificationMessage());
            statement.setInt(2, notification.getNotificationId());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected >= 1) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

  private Notification createNotification(ResultSet resultSet) throws SQLException {
    int notificationId = resultSet.getInt(NotificationTable.NOTIFICATIONID.name());
    int receiverId = resultSet.getInt(NotificationTable.RECEIVERID.name());
    int senderId = resultSet.getInt(NotificationTable.SENDERID.name());
    String notificationMessageContent = resultSet.getString(NotificationTable.NOTIFICATIONMESSAGE.name());

    return new Notification(notificationId, receiverId, senderId, notificationMessageContent);
}
    public boolean checkInvite(Notification notification){
        String query = "SELECT * FROM usernotifications WHERE SenderID = ? and ReceiverID = ?";
        ResultSet resultSet = null;
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, notification.getReceiverId());
            statement.setInt(2,notification.getSenderId());
            resultSet = statement.executeQuery();
           if(resultSet.next()){
               return true;
           }else{
               return false;
           }
        } catch (SQLException e) {
            return false;
        }
    }
}
