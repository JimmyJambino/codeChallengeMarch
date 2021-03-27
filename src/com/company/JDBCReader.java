package com.company;

import java.sql.Connection;

public class JDBCReader {

    Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
