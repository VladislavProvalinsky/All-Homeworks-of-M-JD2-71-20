package by.academy.it.mysql;

import by.academy.it.Dao;
import by.academy.it.Expense;
import by.academy.it.Receiver;
import com.mysql.jdbc.exceptions.*;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyDao implements Dao {

    private Connection connection;
    private Logger log = Logger.getLogger(MyDao.class.getName());
    private final String GENERAL_BASE = "listexpenses"; // главная БД
    private final String TEST_BASE = "listexpenses_test"; // тестовая БД
    private boolean isTestDatabase;

    public MyDao() throws SQLException {
        isTestDatabase = false;
        connection = MySQLDataBase.getConnection();
    }

    public MyDao(boolean isTestDatabase) throws SQLException {
        this.isTestDatabase = isTestDatabase;
        connection = MySQLDataBase.getTestConnection();
    }

    public boolean isTestDatabase() {
        return isTestDatabase;
    }

    @Override
    public Receiver getReceiver(int num) throws SQLException {
        Receiver receiver = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from " +
                (isTestDatabase ? TEST_BASE : GENERAL_BASE) + ".receivers where num = ?")) {
            preparedStatement.setInt(1, num);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                receiver = new Receiver.ReceiverBuilder()
                        .setNum(resultSet.getInt(1))
                        .setName(resultSet.getString(2))
                        .build();
            }
            boolean result = preparedStatement.execute();
            if (!result) log.info("Error!");
        }
        return receiver;
    }

    @Override
    public ArrayList<Receiver> getReceivers() throws SQLException {
        ArrayList<Receiver> receivers = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from " +
                    (isTestDatabase ? TEST_BASE : GENERAL_BASE) + ".receivers");
            while (resultSet.next()){
                receivers.add(new Receiver.ReceiverBuilder()
                        .setNum(resultSet.getInt(1))
                        .setName(resultSet.getString(2))
                        .build());
            }
            boolean result = statement.execute("select * from " +
                    (isTestDatabase ? TEST_BASE : GENERAL_BASE) + ".receivers");
            if (!result) log.info("Error!");
        }
        return receivers;
    }

    @Override
    public Expense getExpense(int num) throws SQLException {
        Expense expense = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from " +
                (isTestDatabase ? TEST_BASE : GENERAL_BASE) + ".expenses where num = ?")) {
            preparedStatement.setInt(1, num);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                expense = new Expense.ExpenseBuilder()
                        .setNum(resultSet.getInt(1))
                        .setPaydate(resultSet.getString(2))
                        .setReceiver(resultSet.getInt(3))
                        .setValue(resultSet.getDouble(4))
                        .build();
            }
            boolean result = preparedStatement.execute();
            if (!result) log.info("Error!");
        }
        return expense;
    }

    @Override
    public ArrayList<Expense> getExpenses() throws SQLException {
        ArrayList<Expense> expenses = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from " +
                    (isTestDatabase ? TEST_BASE : GENERAL_BASE) + ".expenses");
            while (resultSet.next()){
                expenses.add(new Expense.ExpenseBuilder()
                        .setNum(resultSet.getInt(1))
                        .setPaydate(resultSet.getString(2))
                        .setReceiver(resultSet.getInt(3))
                        .setValue(resultSet.getDouble(4))
                        .build());
            }
            boolean result = statement.execute("select * from " +
                    (isTestDatabase ? TEST_BASE : GENERAL_BASE) + ".expenses");
            if (!result) log.info("Error!");
        }
        return expenses;
    }

    @Override
    public int addReceiver(Receiver receiver) throws SQLException {
        if (receiver.getNum()<=0) {
            log.info("Num null, less or equals zero!");
            return -1;
        } else if (getReceiver(receiver.getNum())!=null && getReceiver(receiver.getNum()).getNum() == (receiver.getNum())){
            log.info("Receiver is situated in the table!");
            throw new InvalidParameterException();
        }
        else {
            try (PreparedStatement preparedStatement = connection.prepareStatement("insert into "
                    + (isTestDatabase ? TEST_BASE : GENERAL_BASE) + ".receivers values (?,?)")) {
                preparedStatement.setInt(1, receiver.getNum());
                preparedStatement.setString(2, receiver.getName());
                boolean result = preparedStatement.execute();
                if (!result) log.info("Error!");
            }
        }
        return receiver.getNum();
    }

    @Override
    public int addExpense(Expense expense) throws SQLException {
        if (expense.getNum()<=0) {
            log.info("Num null, less or equals zero!");
            return -1;
        } else if (expense.getValue() < 0) {
            log.info("Value null or less zero!");
            return -1;
        } else if (getExpense(expense.getNum())!=null && getExpense(expense.getNum()).getNum() == (expense.getNum())){
            log.info("Expense is situated in the table!");
            throw new InvalidParameterException();
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into "
                +(isTestDatabase ? TEST_BASE : GENERAL_BASE)+ ".expenses values (?,?,?,?)")){
            preparedStatement.setInt(1, expense.getNum());
            preparedStatement.setString(2,expense.getPaydate());
            preparedStatement.setInt(3,expense.getReceiver());
            preparedStatement.setDouble(4,expense.getValue());
            boolean result = preparedStatement.execute();
            if (!result) log.info("Error!");
        }
        return expense.getNum();
    }

    public void truncateTable (String table) throws SQLException {
        try (Statement statement = connection.createStatement()){
            int result = statement.executeUpdate("truncate table " + (isTestDatabase ? TEST_BASE : GENERAL_BASE) + "." + table);
            if (result != 0) log.info("Error!");
        }
    }
}
