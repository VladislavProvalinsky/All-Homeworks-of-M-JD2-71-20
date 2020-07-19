package by.academy.it;

import by.academy.it.mysql.MyDao;

import java.security.InvalidParameterException;
import java.sql.SQLException;

public class MyDaoFactory {

    private static MyDao myDao;

    public static MyDao getMyDao (String database) throws SQLException {
        if ("mysql".equals(database)) {
            if (myDao == null) {
                myDao = new MyDao();
            }
            return myDao;
        } else if ("mysql_test".equals(database)) {
            if (myDao == null) {
                myDao = new MyDao(true);
            }
            return myDao;
        }
        throw new InvalidParameterException("No such database implemented " + database);
    }
}
