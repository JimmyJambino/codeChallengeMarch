package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class JDBCReader {

    ManagementSystem managementSystemReference;
    Connection connection; //TODO: Write this better, don't need to instantiate new writer.
    public JDBCReader (ManagementSystem managementSystemReference) {
        this.managementSystemReference = managementSystemReference;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<TableInformation> readAllClients() {
        ArrayList<TableInformation> clientList = new ArrayList<>();
        String readClientsSQL = "SELECT * FROM ManagementSystem.tblclients;";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(readClientsSQL);
            while(resultSet.next()) {
                int id = resultSet.getInt("client_id");
                String name = resultSet.getString("client_name");
                int age = resultSet.getInt("client_age");
                String note = resultSet.getString("client_note");
                Client client = new Client(name, age, note);
                client.setId(id);
                clientList.add(client);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
        return clientList;
    }

    public ArrayList<TableInformation> readAllAppointments() {
        ArrayList<TableInformation> appointmentList = new ArrayList<>();
        String readAppointmentsSQL = "SELECT * FROM ManagementSystem.tblAppointments;";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(readAppointmentsSQL);
            while(resultSet.next()) {
                Calendar calendar = new GregorianCalendar();
                Timestamp date = resultSet.getTimestamp("appointment_date");
                String dateString = resultSet.getString("appointment_date");
                System.out.println(dateString);
                calendar.setTime(date);
                int client_id = resultSet.getInt("appointment_client");

                Client client = managementSystemReference.getClientById(client_id);
                appointmentList.add(new Appointment(client,calendar));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
        return appointmentList;
    }

}
