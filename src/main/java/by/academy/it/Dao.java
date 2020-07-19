package by.academy.it;

import java.sql.SQLException;
import java.util.ArrayList;

public interface Dao {

    Receiver getReceiver (int num) throws SQLException;
    ArrayList<Receiver> getReceivers () throws SQLException;
    Expense getExpense (int num) throws SQLException;
    ArrayList<Expense> getExpenses () throws SQLException;
    int addReceiver (Receiver receiver) throws SQLException;
    int addExpense (Expense expense) throws SQLException;
}
