package com.classes;

import java.util.List;

public class TimeSlot {
    private int startHour, endHour, startMinute, endMinute;
    private char dayOfWeek; // MTWRF (S for Sat, U for Sun, X for no day)

    public TimeSlot(char dayOfWeek, int startHour, int startMinute, int endHour, int endMinute) {
        dayOfWeek = Character.toUpperCase(dayOfWeek);
        if ("MTWRFSU".indexOf(dayOfWeek) >= 0) {
            this.dayOfWeek = dayOfWeek;
        }
        else {
            this.dayOfWeek = 'X';
        }
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    private boolean timeFallsInRange(int hour, int minute) {
        if (hour < startHour || hour > endHour) {
            System.out.println("A");
            return false;
        }
        if (hour > startHour && hour < endHour) {
            System.out.println("B");
            return true;
        }
        if (hour == startHour && minute > startMinute) {
            System.out.println("C");
            return true;
        }
        if (hour == endHour && minute < endMinute) {
            System.out.println("D");
            return true;
        }
        return false;
    }

    public static boolean conflicts(TimeSlot t1, TimeSlot t2) {
        if (t1.dayOfWeek != t2.dayOfWeek) {
            return false;
        }
        return t1.timeFallsInRange(t2.startHour, t2.startMinute) || t1.timeFallsInRange(t2.endHour, t2.endMinute);
    }

    public static boolean conflicts(List<TimeSlot> l1, List<TimeSlot> l2) {
        for (int i = 0; i < l1.size(); i++) {
            for (int j = 0; j < l2.size(); j++) {
                if (TimeSlot.conflicts(l1.get(i), l2.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public char getDayOfWeek() {
        return dayOfWeek;
    }
}
