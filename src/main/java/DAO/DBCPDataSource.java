package DAO;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

class DBCPDataSource {

    private static final String URL = "jdbc:postgresql://localhost:5432/login";
    private static final String USER = "postgres";
    private static final String PASS = "12345";
    private static BasicDataSource ds = new BasicDataSource();

    static {
        ds.setUrl(URL);
        ds.setUsername(USER);
        ds.setPassword(PASS);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    DBCPDataSource(){ }
}
