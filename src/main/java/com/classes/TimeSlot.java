package com.classes;

import java.util.List;

public class TimeSlot {
    private int startHour, endHour, startMinute, endMinute;
    private char dayOfWeek;

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

    public boolean timeFallsInRange(int hour, int minute) {
        if (hour < startHour || hour > endHour) {
            return false;
        }
        if (hour > startHour && hour < endHour) {
            return true;
        }
        if (hour == startHour && minute >= startMinute && hour < endHour) {
            return true;
        }
        if (hour == endHour && minute <= endMinute && hour > startHour) {
            return true;
        }
        if (hour == startHour && hour == endHour && minute >= startMinute && minute <= endMinute) {
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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dayOfWeek);
        s.append(' ');
        if (startHour < 12) {
            if (startHour == 0) {
                s.append(12);
            }
            else {
                s.append(startHour);
            }
            s.append(':');
            s.append(String.format("%02d", startMinute));
            s.append("AM");
        }
        else {
            if (startHour == 12) {
                s.append(12);
            }
            else {
                s.append(startHour-12);
            }
            s.append(':');
            s.append(String.format("%02d", startMinute));
            s.append("PM");
        }
        s.append('-');
        if (endHour < 12) {
            if (endHour == 0) {
                s.append(12);
            }
            else {
                s.append(endHour);
            }
            s.append(':');
            s.append(String.format("%02d", endMinute));
            s.append("AM");
        }
        else {
            if (endHour == 12) {
                s.append(12);
            }
            else {
                s.append(endHour-12);
            }
            s.append(':');
            s.append(String.format("%02d", endMinute));
            s.append("PM");
        }
        return s.toString();
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

    public boolean sameStartEndTime(TimeSlot other) {
        return this.startHour == other.startHour && this.startMinute == other.startMinute &&
                this.endHour == other.endHour && this.endMinute == other.endMinute;
    }

    public boolean sameDayOfWeek(TimeSlot other) {
        return this.dayOfWeek == other.dayOfWeek;
    }

    public String csvFormattedStartTime() {
        StringBuilder s = new StringBuilder();
        boolean isAM = startHour >= 12;
        if (startHour < 12) {
            if (startHour == 0) {
                s.append(12);
            }
            else {
                s.append(startHour);
            }
            s.append(':');
            s.append(String.format("%02d", startMinute));
            s.append(":00 AM");
        }
        else {
            if (startHour == 12) {
                s.append(12);
            }
            else {
                s.append(startHour-12);
            }
            s.append(':');
            s.append(String.format("%02d", startMinute));
            s.append(":00 PM");
        }
        return s.toString();
    }

    public String csvFormattedEndTime() {
        StringBuilder s = new StringBuilder();
        if (endHour < 12) {
            if (endHour == 0) {
                s.append(12);
            }
            else {
                s.append(endHour);
            }
            s.append(':');
            s.append(String.format("%02d", endMinute));
            s.append(":00 AM");
        }
        else {
            if (endHour == 12) {
                s.append(12);
            }
            else {
                s.append(endHour-12);
            }
            s.append(':');
            s.append(String.format("%02d", endMinute));
            s.append(":00 PM");
        }
        return s.toString();
    }
}
