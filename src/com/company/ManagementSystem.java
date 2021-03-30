package com.company;

import com.company.Tools.Input;

import javax.swing.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class ManagementSystem {

    JDBCWriter writer = new JDBCWriter(this);
    JDBCReader reader = new JDBCReader(this);
    private Menu menu = new Menu(this);
    private ArrayList<TableInformation> appointmentList = new ArrayList<>();
    private ArrayList<TableInformation> clientList = new ArrayList<>();
    private boolean hasConnection = false;
    private ArrayList<String> quoteList = new ArrayList<>();


    public ManagementSystem() {
        setQuoteList();
    }

    public void setQuoteList() {
        quoteList.add("Discipline is the bridge between goals and accomplishments.");
        quoteList.add("If you can't do great things, do small things in a great way.");
        quoteList.add("Build your own dreams, or someone else will hire you to build theirs.");
        quoteList.add("Done is better than perfect.");
        quoteList.add("Failure is simply an opportunity to begin again, this time more intelligently.");
        quoteList.add("What would you do if you weren't afraid?");
        quoteList.add("All things are a difficult before they are easy.");
        quoteList.add("If we're growing, we're always going to be out of comfort zone.");
        quoteList.add("Every morning we are born again. What we do today is what matters most.");
        quoteList.add("There is no path to happiness: happiness is the path.");
        quoteList.add("Nothing can harm you as much as your own thoughts unguarded.");
        quoteList.add("Anyone who has never made a mistake has never tried anything new.");
        quoteList.add("We must all suffer one of two things: the pain of discipline or the pain of regret.");
        quoteList.add("When you have exhausted all possibilities, remember this - you haven't.");
    }

    public String randomQuote() {
        Random random = new Random();
        int randomNumber = random.nextInt(quoteList.size()); // both ArrayList and Random are 0-indexed so this is fine.
        System.out.println(randomNumber);
        return quoteList.get(randomNumber);
    }

    public void setupJDBC(String username, String password) {

        writer.setConnection(username, password);
        reader.setConnection(writer.getConnection());
        clientList = reader.readAllClients();
        appointmentList = reader.readAllAppointments();
    }
    //TODO: Remove this
    public static void printSelectedRowIndex(JTable table) {

        boolean ex = table.getSelectionModel().isSelectionEmpty();
        if(!ex){
            int index = table.getSelectedRows()[0]; // 0 because it is 0 indexed and we always return the top selected row.
            index = table.getSelectedRow(); // singular row, delete above?
            System.out.println(index);
        } else {
            System.out.println("nothing");
        }
    }

    public int getTableIndex(JTable table) {
        return table.getSelectedRow();
    }
    public Client getClientFromTable(JTable table) {
        int index = table.getSelectedRow();
        return (Client)clientList.get(index);
    }

    public ArrayList<TableInformation> getAppointmentList() {
        return appointmentList;
    }

    public ArrayList<TableInformation> getClientList() {
        return clientList;
    }


    public void addClientToList(Client client) {
        clientList.add(client);
    }

    public void addAppointmentToList(Appointment appointment) {
        appointmentList.add(appointment);
    }

    public void setHasConnection(boolean hasConnection) {
        this.hasConnection = hasConnection;
    }
    public boolean getHasConnection() {
        return hasConnection;
    }

    public boolean appointmentDateAvailable(String date, String time) {
        for(int i = 0; i < appointmentList.size(); i++) {
            String dateString = appointmentList.get(i).getColumnInfo()[1]; // "YYYY-MM-DD HH"
            Scanner scanner = new Scanner(dateString);
            String day = scanner.next(); // "YYYY-MM-DD"
            String clock = scanner.next(); // "HH"
            if(date.equals(day) && time.equals(clock)) {
                return false;
            }
        }
        return true;
    }

    public void deleteClientFromList(int index) {
        clientList.remove(index);
    }
    public Client getClientById(int id) {
        for(int i = 0; i < clientList.size(); i++) {
            Client client = (Client)clientList.get(i);
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }

}
