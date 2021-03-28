package com.company;

import java.sql.*;
import java.util.ArrayList;

public class JDBCReader {

    JDBCWriter writerReference = new JDBCWriter();
    Connection connection; //TODO: Write this better, don't need to instantiate new writer.

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<TableInformation> readAllClients() {
        ArrayList<TableInformation> clientList = new ArrayList<>();
        String readClientsSQL = "SELECT * FROM ManagementSystem.tblClients;";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(readClientsSQL);
            while(resultSet.next()) {
                String name = resultSet.getString("client_name");
                int age = resultSet.getInt("client_age");
                String note = resultSet.getString("client_note");
                clientList.add(new Client(name, age, note));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
        return clientList;
    }

    public void readAllAppointments() {

    }

}
