package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import dao.UserDao;
import model.entities.User;
import model.entities.User.Gender;
import model.entities.User.UserStatus;
import model.entities.UserTable;
import persistence.connection.DataSourceSingleton;

public class UserDaoImp implements UserDao {

    @Override
    public User getUserById(int userId) {

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
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> Users = new ArrayList<>();
        String query = "SELECT * FROM UserAccounts";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Users.add(convertResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Users;
    }

    @Override
    public void addUser(User user) {
        String query = "INSERT INTO User (PhoneNumber, DisplayName, EmailAddress, " +
                "ProfilePicture, PasswordHash, Gender, Country, DateOfBirth, Bio, UserStatus, LastLogin) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            add(statement, user);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        String query = "UPDATE User SET " +
                "DisplayName = ?, EmailAddress = ?, " +
                "ProfilePicture = ?, PasswordHash = ?, Gender = ?, Country = ?, " +
                "DateOfBirth = ?, Bio = ?, UserStatus = ?" +
                "WHERE UserID = ?";

        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
                update(statement, user);
                statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(int userId) {
        String query = "DELETE FROM User WHERE UserID = ?";

        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }

    // create user using resultSet or convert resultSet from db to user object
    // may throwing checked exceptions(SQLException)
    private User convertResultSetToUser(ResultSet resultSet) throws SQLException {
        int userID = resultSet.getInt(UserTable.USER_ID.name());
        String phoneNumber = resultSet.getString(UserTable.PHONE_NUMBER.name());
        String displayName = resultSet.getString(UserTable.DISPLAY_NAME.name());
        String emailAddress = resultSet.getString(UserTable.EMAIL_ADDRESS.name());
        byte[] profilePicture = resultSet.getBytes(UserTable.PROFILE_PICTURE.name());
        String passwordHash = resultSet.getString(UserTable.PASSWORD_HASH.name());
        Gender gender = Gender.valueOf(resultSet.getString(UserTable.GENDER.name()));
        String country = resultSet.getString(UserTable.COUNTRY.name());
        String dateOfBirth = resultSet.getDate(UserTable.DATE_OF_BIRTH.name()).toString();
        String bio = resultSet.getString(UserTable.BIO.name());
        UserStatus userStatus = UserStatus.valueOf(resultSet.getString(UserTable.USER_STATUS.name()));
        String lastLogin = resultSet.getDate(UserTable.LAST_LOGIN.name()).toString();

        return new User(userID, phoneNumber, displayName, emailAddress, profilePicture,
                passwordHash, gender, country, dateOfBirth, bio, userStatus, lastLogin);
    }

    private void add(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getPhoneNumber());
        statement.setString(2, user.getUserName());
        statement.setString(3, user.getEmailAddress());
        statement.setBytes(4, user.getProfilePicture());
        statement.setString(5, user.getPasswordHash());
        /* Gender */
        statement.setString(6, user.getGender().name());
        statement.setString(7, user.getCountry());
        statement.setDate(8, Date.valueOf(user.getDateOfBirth()));
        statement.setString(9, user.getBio());
        statement.setString(10, user.getUserStatus().name());
        statement.setTimestamp(11, Timestamp.valueOf(user.getLastLogin()));
    }

    private void update(PreparedStatement statement, User user) throws SQLException {

        statement.setString(1, user.getUserName());
        statement.setString(2, user.getEmailAddress());
        statement.setBytes(3, user.getProfilePicture());
        statement.setString(4, user.getPasswordHash());
        statement.setString(5, user.getGender().name());
        statement.setString(6, user.getCountry());
        statement.setDate(7, Date.valueOf(user.getDateOfBirth()));
        statement.setString(8, user.getBio());
        statement.setString(9, user.getUserStatus().name());

    }

}
