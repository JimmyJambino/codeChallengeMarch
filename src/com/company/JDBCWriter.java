package com.company;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;

public class JDBCWriter {

    ManagementSystem managementSystemReference;
    Connection connection;

    /**
     * Sets a reference for the ManagementSystem inside the JDBCWriter class
     * @param managementSystemReference
     */
    public JDBCWriter (ManagementSystem managementSystemReference) {
        this.managementSystemReference = managementSystemReference;
    }

    /**
     * Attempts to connect to a MySQL address given a String username and String password and
     * if successful, creates a database named ManagementSystem if no database currently exists
     * with that name at that MySQL address.
     * Two tables will also be created, tblClients and tblAppointments that will store
     * information for Client and Appointment objects.
     * @param username
     * @param password
     */
    public void setConnection(String username, String password) {
        System.out.println(username + " " + password);

        //TODO: Change kindergarten to proper database, also have method to initialize database
        final String JDBC = String.format("jdbc:mysql://localhost?user=%s?serverTimezone=UTC",username);
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS ManagementSystem;";
        String tableClientSQL = "CREATE TABLE IF NOT EXISTS ManagementSystem.tblClients(" +
                "client_id INT NOT NULL UNIQUE PRIMARY KEY AUTO_INCREMENT, " +
                "client_name VARCHAR(50) NOT NULL, " +
                "client_age INT NOT NULL, " +
                "client_note VARCHAR(300)" +
                ");";
        String tableAppointmentSQL = "CREATE TABLE IF NOT EXISTS ManagementSystem.tblAppointments(" +
                "appointment_id INT NOT NULL UNIQUE PRIMARY KEY AUTO_INCREMENT," +
                "appointment_date DATETIME NOT NULL," +
                "appointment_isAM BOOLEAN NOT NULL," +
                "appointment_client INT NOT NULL, " +
                "CONSTRAINT client_fk FOREIGN KEY (appointment_client) REFERENCES tblClients(client_id)" +
                ");";
        try {
            connection = DriverManager.getConnection(JDBC, username, password);
            Statement statement = connection.createStatement();

            statement.executeUpdate(createDatabaseSQL);
            statement.executeUpdate(tableClientSQL);
            statement.executeUpdate(tableAppointmentSQL);
            managementSystemReference.setHasConnection(true);

        } catch (SQLException ioerr) {
            System.out.println("Not connected : " + ioerr.getMessage());
            managementSystemReference.setHasConnection(false);
        }
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Saves the given Client to the database
     * @param client
     */
    public void saveClientToDatabase(Client client) {
        String name = client.getName();
        int age = client.getAge();
        String note = client.getNote();

        // SQL Statement clientSQL adds a new client's details into the database when executed.
        String clientSQL = "INSERT INTO ManagementSystem.tblClients VALUES (" +
                "Default, '" + name + "'," + age + ",'" +
                note + "');";

        // SQL Statement lastAddedSQL returns the last added client's id from the database when executed.
        String lastAddedSQL = "SELECT client_id FROM ManagementSystem.tblClients ORDER BY client_id DESC LIMIT 1;";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(clientSQL);
            ResultSet rs = statement.executeQuery(lastAddedSQL);
            rs.next();  //puts cursor in the right spot for data retrieval

            //sets the id from the most recent client added to the database to the field in client.
            //this is useful for later updating or deleting in the database later
            int clientId = rs.getInt("client_id");      //gets the id of the most recent Client entry
            client.setId(clientId);        //sets the id of given Client to the id from database
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Saves the given Appointment to the database
     * @param appointment
     */
    public void saveAppointmentToDatabase(Appointment appointment) {
        //Calendar date = appointment.getCalendarDate();
        int[] dateTime = appointment.getCalendarDateAsArray(); // {day,month,year,hour)
        boolean isAM = appointment.getIsAM();
        int isPM = isAM ? 1 : 0;

        String dateString = String.format("%d-%d-%d %d",dateTime[2],dateTime[1]+1,dateTime[0], dateTime[3]); // year, month, day, hour

        Client client = appointment.getClient();
        int clientId = client.getId();

        // SQL Statement clientSQL adds a new appointment's details into the database when executed.
        String appointmentSQL = "INSERT INTO ManagementSystem.tblAppointments VALUES (DEFAULT, " +
                "'" + dateString + "'," + 1 + "," + clientId + ");";

        // SQL Statement lastAddedSQL returns the last added appointments's id from the database when executed.
        String lastAddedSQL = "SELECT appointment_id FROM ManagementSystem.tblAppointments ORDER BY appointment_id DESC LIMIT 1;";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(appointmentSQL);
            ResultSet rs = statement.executeQuery(lastAddedSQL);
            rs.next();
            int appointmentId = rs.getInt("appointment_id");
            appointment.setId(appointmentId);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Deletes the given Client from the database and subsequently deletes all appointments for that client.
     * @param client
     */
    public void deleteClientFromDatabase(Client client) {
        int clientId = client.getId();
        String deleteClientAppointments = "DELETE FROM ManagementSystem.tblAppointments WHERE appointment_client = " +
                "" + clientId + ";";
        String deleteClientSQL = "DELETE FROM ManagementSystem.tblClients WHERE client_id = " + clientId + ";";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(deleteClientAppointments);
            statement.executeUpdate(deleteClientSQL);
        }  catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Deletes the given Appointment from the database
     * @param appointment
     */
    public void deleteAppointmentFromDatabase(Appointment appointment) {
        int[] dateTime = appointment.getCalendarDateAsArray(); // {day,month,year,hour)
        String dateString;
        if(appointment.getIsAM()) {
            dateString = String.format("%d-%d-%d %d",dateTime[2],dateTime[1],dateTime[0], dateTime[3]);
        } else {
            dateString = String.format("%d-%d-%d %d",dateTime[2],dateTime[1],dateTime[0], dateTime[3]+12);
        }

        Client client = appointment.getClient();
        int clientId = client.getId();

        String deleteAppointmentSQL = "DELETE FROM ManagementSystem.tblAppointments WHERE appointment_date = '" +
                dateString + "' AND appointment_client = " + clientId + ";";
        System.out.println("ID: " + appointment.getId());
        System.out.println("CLIENT: " + clientId);
        String delete = "DELETE FROM ManagementSystem.tblAppointments WHERE appointment_id = " + appointment.getId() +
                " AND appointment_client = " + clientId + ";";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(delete);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Updates the note for the given Client in the database
     * @param client
     */
    public void updateClient(Client client) {
        int id = client.getId();
        String note = client.getNote();

        String updateClientSQL = "UPDATE ManagementSystem.tblClients " +
                "SET client_note = '" + note + "' " +
                "WHERE client_id = " + id + ";";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(updateClientSQL);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
