package com.company;

import com.company.Tools.Input;

import java.sql.Connection;
import java.util.ArrayList;

public class ManagementSystem {

    JDBCWriter writer = new JDBCWriter();
    JDBCReader reader = new JDBCReader();

    private ArrayList<TableInformation> appointmentList = new ArrayList<>();
    private ArrayList<TableInformation> clientList = new ArrayList<>();
    private Menu menu = new Menu(this);

    public void setupJDBC(String username, String password) {

        writer.setConnection(username, password);
        reader.setConnection(writer.getConnection());
    }

}
