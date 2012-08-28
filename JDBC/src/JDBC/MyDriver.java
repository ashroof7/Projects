package JDBC;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

import DBSystem.MyDatabase;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyDriver implements Driver {

    public boolean acceptsURL(String url) throws SQLException {
        return url.equals("jdbc:odbc:Mydriver");
    }

    public Connection connect(String url, Properties info) throws SQLException {
        String username = info.getProperty("username");
        String password = info.getProperty("password");
        boolean accepted = false;
        try {
            accepted = MyDatabase.checkPassword(username, password);
        } catch (Exception ex) {
           accepted=true;
        }
        if (acceptsURL(url) && accepted) {
            MyConnection conn = new MyConnection(url, info);
            return conn;
        }
        throw new SQLException("Connection Failed");
    }

    public int getMajorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getMinorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
            throws SQLException {
        DriverPropertyInfo[] probInfo = new DriverPropertyInfo[1];
        probInfo[1] = new DriverPropertyInfo("JDBC-DB-CSE", "ver 1.1");
        return probInfo;
    }

    public boolean jdbcCompliant() {
        // TODO Auto-generated method stub
        return false;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
