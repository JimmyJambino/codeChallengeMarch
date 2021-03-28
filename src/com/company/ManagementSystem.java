package com.company;

import com.company.Tools.Input;

import javax.swing.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

public class ManagementSystem {

    JDBCWriter writer = new JDBCWriter(this);
    JDBCReader reader = new JDBCReader();
    private Menu menu = new Menu(this);
    private ArrayList<TableInformation> appointmentList = new ArrayList<>();
    private ArrayList<TableInformation> clientList = new ArrayList<>();
    private boolean hasConnection = false;

    public ManagementSystem() {
    }

    public void setupJDBC(String username, String password) {

        writer.setConnection(username, password);
        reader.setConnection(writer.getConnection());
        clientList = reader.readAllClients();
    }
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

    public ArrayList<TableInformation> getAppointmentList() {
        return appointmentList;
    }

    public ArrayList<TableInformation> getClientList() {
        return clientList;
    }


    public void addClientToList(Client client) {
        clientList.add(client);
    }

    public void setHasConnection(boolean hasConnection) {
        this.hasConnection = hasConnection;
    }
    public boolean getHasConnection() {
        return hasConnection;
    }

}
