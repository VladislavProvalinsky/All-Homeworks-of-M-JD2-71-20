package by.academy.it;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static Logger log = Logger.getLogger(Main.class.getName());
    private static final String dbURL = "jdbc:mysql://localhost:3306/ListExpenses";
    private static List<Double> values = new ArrayList<>();

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            properties.put("user", "root");
            properties.put("password", "root");
            properties.put("useSSL", "false");
            properties.put("serverTimezone", "UTC");
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, e.getMessage());
        }

        try (Connection connection = DriverManager.getConnection(dbURL, properties)) {

            log.info("Adding values in list...");
            try (Statement statement = connection.createStatement();
                  ResultSet resultSet = statement.executeQuery("select value from listexpenses.expenses")) {
                while (resultSet.next()) {
                    values.add(resultSet.getDouble("value"));
                }
            } catch (SQLException e) {
                log.log(Level.SEVERE, e.getMessage());
            }

            log.info("Starting inserting values in table...");
            try (PreparedStatement preparedStatement1 = connection.prepareStatement("insert into listexpenses.quantity (value) values (?)")) {
                for (Double value : values) {
                    preparedStatement1.setDouble(1, value);
                    preparedStatement1.executeUpdate();
                }
            } catch (SQLException e) {
                log.log(Level.SEVERE, e.getMessage());
            }

            log.info("Selecting values from table...");
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("select * from listexpenses.quantity")) {
                File file = new File("src/main/resources/result");
                FileWriter fr = new FileWriter(file, false);
                while (resultSet.next()) {
                    fr.write("Id = " + resultSet.getInt(1) + " value = " + resultSet.getDouble(2));
                    fr.append('\n');
                    fr.flush();
                }
            } catch (SQLException | IOException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
            log.info("Finished successfully");
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }
}
