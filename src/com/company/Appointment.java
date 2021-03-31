package com.company;


import java.util.Calendar;

public class Appointment implements TableInformation{
    private Client client;
    private int id;
    private Calendar myCalendar;
    private int hour;
    private int columnCount = 2;
    private String[] columnNames = {"Client","Date"};

    public Appointment(Client client, Calendar calendar, int hour) {
        this.client = client;
        this.myCalendar = calendar;
        setHour(hour);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
    public void setMyCalendar(Calendar calendar) {
        myCalendar = calendar;
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

    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getHour() {
        return hour;
    }
}
