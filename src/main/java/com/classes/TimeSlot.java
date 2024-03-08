package com.classes;

import java.util.List;

public class TimeSlot {
    private int startHour, endHour, startMinute, endMinute;
    private char dayOfWeek; // MTWRF (S for Sat, U for Sun)

    public static boolean conflicts(TimeSlot t1, TimeSlot t2) {
        return false;
    }

    public static boolean conflicts(List<TimeSlot> l1, List<TimeSlot> l2) {
        return false;
    }
}
