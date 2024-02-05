package dao.impl;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.*;
import dao.UserDao;
import model.entities.User;
import model.entities.User.Gender;
import model.entities.User.UserStatus;
import model.entities.UserTable;
import persistence.connection.DataSourceSingleton;

public class UserDaoImpl implements UserDao {

    @Override
    public User get(int userId) {

        User user = null;
        String query = "SELECT * FROM UserAccounts WHERE UserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = convertResultSetToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }


    @Override
    public List<User> getAll() {
        List<User> Users = new ArrayList<>();
        String query = "SELECT * FROM UserAccounts";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Users.add(convertResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Users;
    }

    @Override
    public boolean save(User user) throws SQLException {
        String query = "INSERT INTO UserAccounts (PhoneNumber, DisplayName, EmailAddress, " +
                "ProfilePicture,PasswordHash,Gender,Country,DateOfBirth,Bio,LastLogin) " +
                "VALUES (?, ?, ?, ?,?,?,?,?,?,?)";

        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            add(statement, user);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected >= 1;
        }
    }

    @Override
    public boolean update(User user) {
        User originalUser = get(user.getUserID());
        if(originalUser == null) {
            return false;
        }
        StringBuilder query = new StringBuilder("UPDATE UserAccounts SET ");
        List<Object> parameters = new ArrayList<>();

        if (!user.getUserName().equals(originalUser.getUserName())) {
            query.append("DisplayName = ?, ");
            parameters.add(user.getUserName());
        }

        if (!user.getEmailAddress().equals(originalUser.getEmailAddress())) {
            query.append("EmailAddress = ?, ");
            parameters.add(user.getEmailAddress());
        }

        if (!Arrays.equals(user.getProfilePicture(), originalUser.getProfilePicture())) {
            query.append("ProfilePicture = ?, ");
            parameters.add(user.getProfilePicture());
        }

        if (!user.getBio().equals(originalUser.getBio())) {
            query.append("Bio = ?, ");
            parameters.add(user.getBio());
        }

        if (user.getUserStatus() != originalUser.getUserStatus()) {
            query.append("UserStatus = ?, ");
            parameters.add(user.getUserStatus().name());
        }

        if (user.getUsermode() != originalUser.getUsermode()) {
            query.append("UserMode = ?, ");
            parameters.add(user.getUsermode().name());
        }

        if (!parameters.isEmpty()) {
            query.setLength(query.length() - 2);
            query.append(" WHERE UserID = ?");
            parameters.add(user.getUserID());

            try (Connection connection = DataSourceSingleton.getInstance().getConnection();
                 PreparedStatement statement = connection.prepareStatement(query.toString())) {

                createUpdateStatement(statement, parameters);

                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.out.println("Failed to update user");
            }
        }

        return false;
    }

    @Override
    public boolean delete(User user) {
        String query = "DELETE FROM UserAccounts WHERE UserID = ?";

        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserID());
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
    public boolean delete(int userId) {
        String query = "DELETE FROM UserAccounts WHERE UserID = ?";

        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
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
    public User getUserByPhoneNumber(String phoneNumber) {
        User user = null;
        String query = "SELECT * FROM UserAccounts WHERE PhoneNumber = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, phoneNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = convertResultSetToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    @Override
    public List<User> getContactsByUserID(int userId) {

        List<User> userList = new ArrayList<>();
        String query = "SELECT ua.* FROM UserContacts uc " +
                "INNER JOIN  UserAccounts ua ON uc.FriendID = ua.UserID " +
                "WHERE uc.UserID = ?;";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    userList.add(convertResultSetToUser(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userList;
    }

    @Override
    public List<String> getContactsPhoneNumbers(int userID) {
        List<String> phoneNumbers = new ArrayList<>();
        String query = "SELECT ua.PhoneNumber FROM UserAccounts ua " +
                "INNER JOIN UserContacts uc ON ua.UserID  = uc.FriendID " +
                "WHERE uc.UserID = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    phoneNumbers.add(resultSet.getString(UserTable.PhoneNumber.name()));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return phoneNumbers;
    }

    private User convertResultSetToUser(ResultSet resultSet) throws SQLException {
        int userID = resultSet.getInt(UserTable.UserID.name());
        String phoneNumber = resultSet.getString(UserTable.PhoneNumber.name());
        String displayName = resultSet.getString(UserTable.DisplayName.name());
        String emailAddress = resultSet.getString(UserTable.EmailAddress.name());
        Blob imageBlob = resultSet.getBlob(UserTable.ProfilePicture.name());
        byte[] profilePicture =  null;
        if (imageBlob != null) {
            profilePicture = imageBlob.getBytes(1, (int) imageBlob.length());
        }
        String passwordHash = resultSet.getString(UserTable.PasswordHash.name());
        Gender gender = Gender.valueOf(resultSet.getString(UserTable.Gender.name()));
        String country = resultSet.getString(UserTable.Country.name());
        Date dateOfBirth = resultSet.getDate(UserTable.DateOfBirth.name());
        String bio = resultSet.getString(UserTable.Bio.name());
        UserStatus userStatus = UserStatus.valueOf(resultSet.getString(UserTable.UserStatus.name()));
        User.UserMode userMode = User.UserMode.valueOf(resultSet.getString(UserTable.UserMode.name()));
        String lastLogin = resultSet.getTimestamp(UserTable.LastLogin.name()).toLocalDateTime().toString();

        return new User(userID, phoneNumber, displayName, emailAddress, profilePicture,
                passwordHash, gender, country, dateOfBirth, bio, userStatus,userMode, lastLogin);
    }

    private void add(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getPhoneNumber());
        statement.setString(2, user.getUserName());
        statement.setString(3, user.getEmailAddress());
        ByteArrayInputStream input = new ByteArrayInputStream(user.getProfilePicture());
        statement.setBinaryStream(4, input);
        statement.setString(5, user.getPasswordHash());
        statement.setString(6, user.getGender().name());
        statement.setString(7, user.getCountry());
        statement.setDate(8, new java.sql.Date(user.getDateOfBirth().getTime()));
        statement.setString(9, user.getBio());
        //get the latest Time stamp
        statement.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
    }

    private void createUpdateStatement(PreparedStatement statement, List<Object> parameters) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i) instanceof String) {
                statement.setString(i + 1, (String) parameters.get(i));
            } else if (parameters.get(i) instanceof Integer) {
                statement.setInt(i + 1, (Integer) parameters.get(i));
            } else if (parameters.get(i) instanceof byte[]) {
                ByteArrayInputStream input = new ByteArrayInputStream((byte[]) parameters.get(i));
                statement.setBinaryStream(i + 1, input);
            }
        }
    }

}
