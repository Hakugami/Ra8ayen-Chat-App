package persistence.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
public class DataSourceSingleton {
    private static DataSourceSingleton instance;
    private MysqlConnectionPoolDataSource dataSource;
    private DataSourceSingleton() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setURL(properties.getProperty("url"));
            dataSource.setUser(properties.getProperty("user"));
            dataSource.setPassword(properties.getProperty("password"));
        } catch (IOException e) {
            System.out.println("Error loading properties file");
        }

    }
    public static DataSourceSingleton getInstance() {
        if (instance == null) {
            instance = new DataSourceSingleton();
        }
        return instance;
    }
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
