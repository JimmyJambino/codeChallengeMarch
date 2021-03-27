package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCWriter {

    Connection connection;

    public void setConnection(String username, String password) {

        //TODO: Change kindergarten to proper database, also have method to initialize database
        final String url = String.format("jdbc:mysql://localhost?user=%s?serverTimezone=UTC",username);
        try {
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String createDatabase = "CREATE DATABASE IF NOT EXISTS ManagementSystem;" +
                    "\nCREATE TABLE ManagementSystem.Clients(" +
                    "client_id INT NOT NULL UNIQUE PRIMARY KEY AUTO_INCREMENT, " +
                    "client_name VARCHAR(50) NOT NULL, " +
                    "client_age INT NOT NULL, " +
                    "client_note VARCHAR(300)" + //TODO: Make sure note string can't be longer than 300 chars
                    ");" +
                    "\nCREATE TABLE ManagementSystem.Appointments(" +
                    "appointment_date DATE NOT NULL, " +
                    "appointment_client INT NOT NULL, " +
                    "client_fk FOREIGN KEY (appointment_client) REFERENCES Clients(client_id)" +
                    ");";

        } catch (SQLException ioerr) {
            System.out.println("Not connected =" + ioerr.getMessage());
        }
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
