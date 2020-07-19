package by.academy.it.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLDataBase {

    static Properties properties = new Properties();
    private static Logger log = Logger.getLogger(MySQLDataBase.class.getName());
    static final String url = "jdbc:mysql://localhost:3306/listexpenses";
    static final String test_url = "jdbc:mysql://localhost:3306/listexpenses_test";

    static {
        properties.put("user", "root");
        properties.put("password", "root");
        properties.put("useSSL", "false");
        properties.put("serverTimezone", "UTC");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    public static Connection getConnection () throws SQLException {
        return DriverManager.getConnection(url,properties);
    }

    public static Connection getTestConnection () throws SQLException {
        return DriverManager.getConnection(test_url,properties);
    }

}
