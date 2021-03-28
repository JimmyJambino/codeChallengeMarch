package com.company;

import java.sql.*;
import java.util.Date;

public class JDBCWriter {

    ManagementSystem managementSystemReference;
    Connection connection;

    public JDBCWriter (ManagementSystem managementSystemReference) {
        this.managementSystemReference = managementSystemReference;
    }
    public void setConnection(String username, String password) {
        System.out.println(username + " " + password);

        //TODO: Change kindergarten to proper database, also have method to initialize database
        final String url = String.format("jdbc:mysql://localhost?user=%s?serverTimezone=UTC",username);
        try {
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String createDatabase = "CREATE DATABASE IF NOT EXISTS ManagementSystem;" +
                    "CREATE TABLE IF NOT EXISTS ManagementSystem.tblClients(" +
                    "client_id INT NOT NULL UNIQUE PRIMARY KEY AUTO_INCREMENT, " +
                    "client_name VARCHAR(50) NOT NULL, " +
                    "client_age INT NOT NULL, " +
                    "client_note VARCHAR(300)" + //TODO: Make sure note string can't be longer than 300 chars
                    ");" +
                    "\nCREATE TABLE IF NOT EXISTS ManagementSystem.tblAppointments(" +
                    "appointment_date DATE NOT NULL, " +
                    "appointment_client INT NOT NULL, " +
                    "client_fk FOREIGN KEY (appointment_client) REFERENCES Clients(client_id)" +
                    ");";

            String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS ManagementSystem;";
            String tableClientSQL = "CREATE TABLE IF NOT EXISTS ManagementSystem.tblClients(" +
                    "client_id INT NOT NULL UNIQUE PRIMARY KEY AUTO_INCREMENT, " +
                    "client_name VARCHAR(50) NOT NULL, " +
                    "client_age INT NOT NULL, " +
                    "client_note VARCHAR(300)" + //TODO: Make sure note string can't be longer than 300 chars
                    ");";
            String tableAppointmentSQL = "CREATE TABLE IF NOT EXISTS ManagementSystem.tblAppointments(" +
                    "appointment_date DATE NOT NULL, " +
                    "appointment_client INT NOT NULL, " +
                    "client_fk FOREIGN KEY (appointment_client) REFERENCES Clients(client_id)" +
                    ");";
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

    public void saveAppointmentToDabase(Appointment appointment) {
        Date date = appointment.getCalendarDate();
        String dateString = date.toString();
        Client client = appointment.getClient();
        int clientId = client.getId();

        // SQL Statement clientSQL adds a new appointment's details into the database when executed.
        String appointmentSQL = "INSERT INTO ManagementSystem.tblAppointments VALUES (" +
                "'" + dateString + "'," + client + ");";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(appointmentSQL);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void deleteClientFromDatabase(Client client) {
        int clientId = client.getId();
        String deleteClientSQL = "DELETE FROM ManagementSystem.tblClients WHERE client_id = " + clientId + ";";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(deleteClientSQL);
        }  catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void deleteAppointmentFromDatabase(Appointment appointment) {
        Date date = appointment.getCalendarDate();
        String dateString = date.toString();
        Client client = appointment.getClient();
        int clientId = client.getId();

        String deleteAppointmentSQL = "DELETE FROM ManagementSystem.tblAppointments WHERE appointment_date = '" +
                dateString + "' AND appointment_client = " + clientId + ";";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(deleteAppointmentSQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }
}
