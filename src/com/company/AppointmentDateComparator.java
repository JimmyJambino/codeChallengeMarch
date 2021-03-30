package com.company;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class AppointmentDateComparator implements Comparator<Appointment> {

    public int compare(Appointment a1, Appointment a2) {
        int compare;
        Calendar a1Calendar = a1.getCalendarDate();
        Calendar a2Calendar = a2.getCalendarDate();

        if(a1Calendar.after(a2Calendar)) {
            compare = 1;
        } else if(a1Calendar.before(a2Calendar)) {
            compare = -1;
        } else {
            compare = 0;
        }
        return compare;
    }


}
