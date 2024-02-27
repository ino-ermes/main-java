package com.mio.dao;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBConnectionPool {

    private DBConnectionPool() {

    }

    private static BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/todols");
        dataSource.setUsername("ino");
        dataSource.setPassword("ino");

        // Set additional connection pool properties
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
    }

    public static BasicDataSource getDataSource() {
        return dataSource;
    }
}
