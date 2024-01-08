package dao.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return Users;
    }

    @Override
    public void save(User user) {
        String query = "INSERT INTO UserAccounts (PhoneNumber, DisplayName, EmailAddress, " +
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
    public void update(User user) {
        String query = "UPDATE UserAccounts SET " +
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
    public void delete(User user) {
        String query = "DELETE FROM UserAccounts WHERE UserID = ?";

        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserID());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int userId) {
        String query = "DELETE FROM UserAccounts WHERE UserID = ?";

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
        int userID = resultSet.getInt(UserTable.UserID.name());
        String phoneNumber = resultSet.getString(UserTable.PhoneNumber.name());
        String displayName = resultSet.getString(UserTable.DisplayName.name());
        String emailAddress = resultSet.getString(UserTable.EmailAddress.name());
        byte[] profilePicture = resultSet.getBytes(UserTable.ProfilePicture.name());
        String passwordHash = resultSet.getString(UserTable.PasswordHash.name());
        Gender gender = Gender.valueOf(resultSet.getString(UserTable.Gender.name()));
        String country = resultSet.getString(UserTable.Country.name());
        String dateOfBirth = resultSet.getDate(UserTable.DateOfBirth.name()).toLocalDate().toString();
        String bio = resultSet.getString(UserTable.Bio.name());
        UserStatus userStatus = UserStatus.valueOf(resultSet.getString(UserTable.UserStatus.name()));
        String lastLogin = resultSet.getTimestamp(UserTable.LastLogin.name()).toLocalDateTime().toString();

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
        statement.setDate(8, Date.valueOf(LocalDate.parse(user.getDateOfBirth())));
        statement.setString(9, user.getBio());
        statement.setString(10, user.getUserStatus().name());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        statement.setTimestamp(11, Timestamp.valueOf(LocalDateTime.parse(user.getLastLogin(), formatter)));
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