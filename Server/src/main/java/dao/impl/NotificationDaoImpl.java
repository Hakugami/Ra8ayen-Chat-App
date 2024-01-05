package dao.impl;

import dao.NotificationDao;
import persistence.connection.DataSourceSingleton;
import java.util.stream.Stream;
import java.sql.*;
import java.util.*;
import model.entities.*;

public class NotificationDaoImpl implements NotificationDao {
    @Override
    public Optional<Notification> getNotificationBySenderId(int id) {
        String query = "SELECT * FROM usernotifications WHERE SenderID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(createNotification(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Stream<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM usernotifications";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                notifications.add(createNotification(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications.stream();
    }

    @Override
    public void addNotification(Notification notification) {
        String query = "INSERT INTO usernotifications (UserID, SenderID, MessageContent, NotificationTimestamp) VALUES (?, ?, ?, ?)";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, notification.getReceiverId());
            statement.setInt(2, notification.getSenderId());
            statement.setString(3, notification.getMessageContent());
            statement.setString(4, notification.getNotificationSendDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteNotification(Notification notification) {
        String query = "DELETE FROM usernotifications WHERE NotificationID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, notification.getNotificationId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateNotification(Notification notification) {
        String query = "UPDATE usernotifications SET MessageContent = ? WHERE NotificationID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, notification.getMessageContent());
            statement.setInt(2, notification.getNotificationId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Notification createNotification(ResultSet resultSet) throws SQLException {
        int notificationId = resultSet.getInt(NotificationTable.NOTIFICATIONID.name());
        int receiverId = resultSet.getInt(NotificationTable.USERID.name());
        int senderId = resultSet.getInt(NotificationTable.SENDERID.name());
        String notificationSendDate = resultSet.getTimestamp(NotificationTable.NOTIFICATIONTIMESTAMP.name()).toString();
        String notificationMessageContent = resultSet.getString(NotificationTable.MESSAGECONTENT.name());

        return new Notification(notificationId, receiverId, senderId, notificationSendDate, notificationMessageContent);
    }
}
