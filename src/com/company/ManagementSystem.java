package com.company;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

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

    /**
     * Adds all the String objects containing daily quotes to the system
     */
    public void setQuoteList() {
        quoteList.add("Discipline is the bridge between goals and accomplishments.");
        quoteList.add("If you can't do great things, do small things in a great way.");
        quoteList.add("Build your own dreams, or someone else will hire you to build theirs.");
        quoteList.add("Done is better than perfect.");
        quoteList.add("Failure is simply an opportunity to begin again, this time more intelligently.");
        quoteList.add("What would you do if you weren't afraid?");
        quoteList.add("All things are difficult before they are easy.");
        quoteList.add("If we're growing, we're always going to be out of comfort zone.");
        quoteList.add("Every morning we are born again. What we do today is what matters most.");
        quoteList.add("There is no path to happiness: happiness is the path.");
        quoteList.add("Nothing can harm you as much as your own thoughts unguarded.");
        quoteList.add("Anyone who has never made a mistake has never tried anything new.");
        quoteList.add("We must all suffer one of two things: the pain of discipline or the pain of regret.");
        quoteList.add("When you have exhausted all possibilities, remember this - you haven't.");
    }

    /**
     * Chooses a random quote from the quoteList
     * @return
     */
    public String randomQuote() {
        Random random = new Random();
        int randomNumber = random.nextInt(quoteList.size()); // both ArrayList and Random are 0-indexed so this is fine.
        return quoteList.get(randomNumber);
    }

    /**
     * Sets a connection to the MySQL server given a String username and String password.
     * Reads data from the tables tblClients and tblAppointments after connecting.
     * @param username
     * @param password
     */
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
            int index = table.getSelectedRow(); //
            System.out.println(index);
        } else {
            System.out.println("nothing");
        }
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

    /**
     * Loops through the clientList to find a Client with the given int id. Considering this is used in conjunction with
     * the JDBCReader that has the id from the table tblClients in the database, this will never return null unless that
     * id has been changed from within the database.
     * @param id
     * @return
     */
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
