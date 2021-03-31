package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class JDBCReader {

    private ManagementSystem managementSystemReference;
    private Connection connection;

    /**
     * Sets a reference for the ManagementSystem inside the JDBCReader class
     * @param managementSystemReference
     */
    public JDBCReader (ManagementSystem managementSystemReference) {
        this.managementSystemReference = managementSystemReference;
    }

    /**
     * Uses a Connection argument given by the JDBCWriter in ManagementSystem.
     * @param connection
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Reads all data from the tblClients and creates a new ArrayList<TableInformation> that contains those Client objects
     * @return
     */
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
                String phone = resultSet.getString("client_phone");
                String email = resultSet.getString("client_email");
                String industry = resultSet.getString("client_industry");
                String note = resultSet.getString("client_note");
                Client client = new Client(name, age, phone, email, industry, note);
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

    /**
     * Reads all data from the tblAppointments and creates a new ArrayList<TableInformation> that contains those Appointment objects
     * @return
     */
    public ArrayList<TableInformation> readAllAppointments() {
        ArrayList<TableInformation> appointmentList = new ArrayList<>();
        String readAppointmentsSQL = "SELECT * FROM ManagementSystem.tblAppointments;";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(readAppointmentsSQL);
            while(resultSet.next()) {
                Calendar calendar = new GregorianCalendar();
                Timestamp date = resultSet.getTimestamp("appointment_date");
                //String dateString = resultSet.getString("appointment_date");
                //System.out.println(dateString);
                calendar.setTime(date);
                int appointment_id = resultSet.getInt("appointment_id");
                int client_id = resultSet.getInt("appointment_client");
                int hour = resultSet.getInt("appointment_hour");
                Client client = managementSystemReference.getClientById(client_id);
                Appointment appointment = new Appointment(client, calendar, hour);
                int[] timeArray = appointment.getCalendarDateAsArray();
                Calendar actualCalendar = new GregorianCalendar(timeArray[2],timeArray[1]+1,timeArray[0], hour, 0);
                appointment.setMyCalendar(actualCalendar);
                appointment.setId(appointment_id);
                appointmentList.add(appointment);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
        return appointmentList;
    }

}
