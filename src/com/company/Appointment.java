package com.company;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Appointment implements TableInformation{
    private Client client;
    private Calendar myCalendar;
    private int columnCount = 2;
    private String[] columnNames = {"Client","Date"};

    public Appointment(Client client, Calendar calendar) {
        this.client = client;
        this.myCalendar = calendar;
    }

    public int[] getCalendarDateAsArray() {
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        int month = myCalendar.get(Calendar.MONTH);
        int year = myCalendar.get(Calendar.YEAR);
        int time = myCalendar.get(Calendar.HOUR);

        return new int[]{day,month,year,time};
    }

    public Client getClient() {
        return client;
    }

    public Calendar getCalendarDate() {
        return myCalendar;
    }

    public int getColumnCount() {
        return columnCount;
    }
    public String[] getColumnNames() {
        return columnNames;
    }
    public String[] getColumnInfo() {
        String[] info = {client.getName(),myCalendar.getTime().toString()};
        return info;
    }
}
