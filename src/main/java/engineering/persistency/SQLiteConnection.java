package engineering.persistency;

import exception.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLiteConnection {

    private final String url;
    private static SQLiteConnection instance = null; //single connection to db
    private Connection connection;


    private SQLiteConnection(){

        try(InputStream is = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(is);

            this.url = prop.getProperty("db.url");

            this.connection = DriverManager.getConnection(url);

        } catch (IOException | SQLException e) {
            throw new DAOException("Can't connect to database", e);
        }

    }

    public static SQLiteConnection getInstance(){
        try {
            if (instance == null || instance.connection.isClosed()) {
                instance = new SQLiteConnection();
            }
        } catch (SQLException e) {
            throw new DAOException("Instance connection error", e);
        }
        return instance;
    }

    public Connection getConnection(){
        return connection;
    }

}
