package by.academy.it.mysql;

import by.academy.it.Expense;
import by.academy.it.MyDaoFactory;
import by.academy.it.Receiver;
import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.MySQLDataException;
import com.mysql.jdbc.exceptions.jdbc4.*;
import junit.framework.TestCase;
import org.apache.poi.sl.draw.geom.Path;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class MyDaoTest extends TestCase {

    private Logger log = Logger.getLogger(MyDaoTest.class.getName());
    private MyDao myDao;
    private IDatabaseConnection connection;

    @Before
    public void setUp() throws Exception {
        try {
            myDao = MyDaoFactory.getMyDao("mysql_test");
            connection = new MySqlConnection(MySQLDataBase.getTestConnection(), "listexpenses_test");
        } catch (SQLException e){
            log.log(Level.SEVERE, e.getMessage());
        }
        assertNotNull(myDao); // проверяем является ли наш аксесс к тестовой БД не null
        assertTrue(myDao.isTestDatabase()); // проверяем является ли наше соединение именно с тестовой БД
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
        myDao.truncateTable("receivers");
        myDao.truncateTable("expenses");
        myDao = null;
    }

    /*
    1. Что будет если в параметр передать 0?
    2. Что будет если в параметр передать число < 0?
    3. Что будет если попробовать получить несуществующего получателя (num > 0)?
    4. Что будет если получать получателя в пустой таблице?
     */
    @Test
    public void testGetReceiverWhenNumEqualsZero() throws DatabaseUnitException, SQLException {
        // Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(MyDaoTest.class.getResourceAsStream("ReceiversTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection,dataSet);
        //When
        Receiver receiver = myDao.getReceiver(0);
        //Then
        assertNull(receiver);
        DatabaseOperation.DELETE.execute(connection,dataSet);
    }

    @Test
    public void testGetReceiverWhenNumLessThanZero() throws DatabaseUnitException, SQLException {
        // Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(MyDaoTest.class.getResourceAsStream("ReceiversTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection,dataSet);
        //When
        Receiver receiver = myDao.getReceiver(-3);
        //Then
        assertNull(receiver);
        DatabaseOperation.DELETE.execute(connection,dataSet);
    }

    public void testGetReceiverWhenNoThatReceiver() throws DatabaseUnitException, SQLException {
        // Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(MyDaoTest.class.getResourceAsStream("ReceiversTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection,dataSet);
        //When
        Receiver receiver = myDao.getReceiver(1);
        //Then
        assertNull(receiver);
        DatabaseOperation.DELETE.execute(connection,dataSet);
    }

    public void testGetReceiverInEmptyTable() throws SQLException {
        // Given (empty table)
        //When
        Receiver receiver = myDao.getReceiver(1);
        //Then
        assertNull(receiver);
    }

    /*
    1. Что будет если таблица пуста?
     */
    @Test
    public void testGetReceiversFromEmptyTable() throws SQLException {
        // Given (empty table)
        //When
        ArrayList<Receiver> receivers = myDao.getReceivers();
        //Then
        assertEquals(0, receivers.size());
    }

    /*
    1. Что будет если в параметр передать 0?
    2. Что будет если в параметр передать число < 0?
    3. Что будет если попробовать получить несуществующий расход (num > 0)?
    4. Что будет если получать расход в пустой таблице?
     */
    @Test
    public void testGetExpenseWhenNumEqualsZero() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(MyDaoTest.class.getResourceAsStream("ExpensesTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection,dataSet);
        //When
        Expense expense = myDao.getExpense(0);
        //Then
        assertNull(expense);
        DatabaseOperation.DELETE.execute(connection,dataSet);
    }

    @Test
    public void testGetExpenseWhenNumLessThanZero() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(MyDaoTest.class.getResourceAsStream("ExpensesTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection,dataSet);
        //When
        Expense expense = myDao.getExpense(-2);
        //Then
        assertNull(expense);
        DatabaseOperation.DELETE.execute(connection,dataSet);
    }

    @Test
    public void testGetExpenseWhenExpenseDoesNotExist() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(MyDaoTest.class.getResourceAsStream("ExpensesTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection,dataSet);
        //When
        Expense expense = myDao.getExpense(1);
        //Then
        assertNull(expense);
        DatabaseOperation.DELETE.execute(connection,dataSet);
    }

    @Test
    public void testGetExpenseInEmptyTable() throws DatabaseUnitException, SQLException {
        //Given (empty table)
        //When
        Expense expense = myDao.getExpense(1);
        //Then
        assertNull(expense);
    }

    /*
    1. Что будет если таблица пуста?
     */
    @Test
    public void testGetExpenses() throws SQLException {
        // Given (empty table)
        //When
        ArrayList<Expense> expenses = myDao.getExpenses();
        //Then
        assertEquals(0, expenses.size());
    }

    /*
    1. Что будет если получатель переадваемый в метод null?
    2. Что будет если num получателя передаваемое в метод null?
    3. Что будет если num получателя <= 0?
    4. Что будет если name получателя > 30 символов?
    5. Что будет если передать в метод получателя с повторным num?
     */
    @Test
    public void testAddReceiverWhenReceiverIsNull() throws SQLException {
        //Given
        Receiver receiver = null;
        boolean exception = false;
        //When
        try {
            myDao.addReceiver(receiver);
        } catch (NullPointerException e){
            exception = true;
        }
        //Then
        assertTrue(exception);
    }

    @Test
    public void testAddReceiverWhenReceiverNumIsNull() throws SQLException {
        //Given
        Receiver receiver = new Receiver.ReceiverBuilder()
                .setName("Hello")
                .build();
        //When
        int result = myDao.addReceiver(receiver);
        //Then
        assertEquals(-1, result);
    }

    @Test
    public void testAddReceiverWhenReceiverNumLessOrEqualsZero() throws SQLException {
        //Given
        Receiver receiver = new Receiver.ReceiverBuilder()
                .setNum(-4)
                .setName("Hello")
                .build();
        //When
        int result = myDao.addReceiver(receiver);
        //Then
        assertEquals(-1, result);
    }

    @Test
    public void testAddReceiverWhenReceiverNameUpTo30() throws SQLException {
        //Given
        Receiver receiver = new Receiver.ReceiverBuilder()
                .setNum(1)
                .setName("at com.intellij.rt.jat com.intellij.rt.jat com.intellij.rt.jat com.intellij.rt.j")
                .build();
        boolean result = false;
        //When
        try {
            myDao.addReceiver(receiver);
        } catch (MysqlDataTruncation e) {
            result = true;
        }
        //Then
        assertTrue(result);
    }

    @Test
    public void testAddReceiverWhenReceiverIsTheSame() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(MyDaoTest.class.getResourceAsStream("ReceiversTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection,dataSet);
        Receiver receiver = new Receiver.ReceiverBuilder()
                .setNum(2)
                .setName("Shop")
                .build();
        boolean result = false;
        //When
        try {
            myDao.addReceiver(receiver);
        } catch (InvalidParameterException e) {
            result = true;
        }
        //Then
        assertTrue(result);
    }

    /*
    1. Что будет если расход переадваемый в метод null?
    2. Что будет если num расхода передаваемое в метод null?
    3. Что будет если num расхода <= 0?
    4. Что будет если date расхода неправильного формата?
    5. Что будет если передать в метод расход с повторным num?
    6. Что будет если value расхода < 0?
    */

    @Test
    public void testAddExpenseWhenExpenseIsNull() throws SQLException {
        //Given
        Expense expense = null;
        boolean exception = false;
        //When
        try {
            myDao.addExpense(expense);
        } catch (NullPointerException e){
            exception = true;
        }
        //Then
        assertTrue(exception);
    }

    @Test
    public void testAddExpenseWhenExpenseNumIsNull() throws SQLException {
        //Given
        Expense expense = new Expense.ExpenseBuilder()
                .setPaydate("2020-01-01")
                .setReceiver(1)
                .setValue(20000)
                .build();
        //When
        int result = myDao.addExpense(expense);
        //Then
        assertEquals(-1, result);
    }

    @Test
    public void testAddExpenseWhenExpenseLessOrEqualsZero() throws SQLException {
        //Given
        Expense expense = new Expense.ExpenseBuilder()
                .setNum(-1)
                .setPaydate("2020-01-01")
                .setReceiver(1)
                .setValue(20000)
                .build();
        //When
        int result = myDao.addExpense(expense);
        //Then
        assertEquals(-1, result);
    }

    @Test
    public void testAddExpenseWhenDateNonFormat() throws SQLException {
        //Given
        Expense expense = new Expense.ExpenseBuilder()
                .setNum(1)
                .setPaydate("01-01-2020")
                .setReceiver(1)
                .setValue(20000)
                .build();
        boolean result = false;
        //When
        try {
            myDao.addExpense(expense);
        } catch (MysqlDataTruncation e){
            result = true;
        }
        //Then
        assertTrue(result);
    }

    @Test
    public void testAddExpenseWhenExpenseIsTheSame() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(MyDaoTest.class.getResourceAsStream("ExpensesTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection,dataSet);
        Expense expense = new Expense.ExpenseBuilder()
                .setNum(2)
                .setPaydate("2020-01-01")
                .setReceiver(1)
                .setValue(20000)
                .build();
        boolean result = false;
        //When
        try {
            myDao.addExpense(expense);
        } catch (InvalidParameterException e) {
            result = true;
        }
        //Then
        assertTrue(result);
    }

    @Test
    public void testAddExpenseWhenValueLessZero() throws SQLException {
        //Given
        Expense expense = new Expense.ExpenseBuilder()
                .setNum(1)
                .setPaydate("2020-01-01")
                .setReceiver(1)
                .setValue(-2000)
                .build();
        //When
        int result = myDao.addExpense(expense);
        //Then
        assertEquals(-1, result);
    }

}