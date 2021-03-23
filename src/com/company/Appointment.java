package com.company;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Appointment {
    private Client client = new Client("testName",0,"testNote");
    private Date calendarDate = new Date();

    public Appointment(Client client, String calendarDate) {
        this.client = client;
        setCalendarDate(calendarDate);
    }

    public void setCalendarDate(String calendarDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = format.parse(calendarDate);
        } catch (ParseException parseException) {
            System.out.println("Wrong date format: " + parseException);
        }
        this.calendarDate = date;
    }
}
