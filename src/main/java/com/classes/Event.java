package com.classes;

import java.util.LinkedList;
import java.util.List;

public class Event {
    private StringBuilder title;
    private List<TimeSlot> times;

    public StringBuilder getTitle() {
        return title;
    }

    public void setTitle(StringBuilder title) {
        this.title = title;
    }

    public List<TimeSlot> getTimes() {
        return times;
    }

    public void setTimes(List<TimeSlot> times) {
        this.times = times;
    }

    public Event (StringBuilder title, List<TimeSlot> times){
        this.title = title;
        this.times = times;
    }

    //TODO: actually check to see if this works
    public String toCSVFormat() {
        StringBuilder s = new StringBuilder();

        List<List<TimeSlot>> groups = getTimeSlotGroups();

        for (List<TimeSlot> group : groups) {
            s.append(title);
            s.append(',');

            // Time logic
            String daysOfWeek = "";
            for (TimeSlot t : group) {
                daysOfWeek += t.getDayOfWeek();
            }
            if (daysOfWeek.contains("M")) {
                s.append('M');
            }
            s.append(',');
            if (daysOfWeek.contains("T")) {
                s.append('T');
            }
            s.append(',');
            if (daysOfWeek.contains("W")) {
                s.append('W');
            }
            s.append(',');
            if (daysOfWeek.contains("R")) {
                s.append('R');
            }
            s.append(',');
            if (daysOfWeek.contains("F")) {
                s.append('F');
            }
            if(daysOfWeek.contains("S")){
                s.append('S');
            }
            s.append(',');
            if(daysOfWeek.contains("U")){
                s.append('U');
            }
            s.append(',');
            s.append(group.get(0).csvFormattedStartTime());
            s.append(',');
            s.append(group.get(0).csvFormattedEndTime());
            s.append(',');
            s.append('\n');
        }
        return s.toString();
    }

    private List<List<TimeSlot>> getTimeSlotGroups() {
        List<List<TimeSlot>> groups = new LinkedList<>();
        for (TimeSlot t : times) {
            boolean updated = false;
            for (int i = 0; i < groups.size(); i++) {
                if(groups.get(i).size() > 0 && t.sameStartEndTime(groups.get(i).get(0))) {
                    groups.get(i).add(t);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                List<TimeSlot> group = new LinkedList<>();
                group.add(t);
                groups.add(group);
            }
        }
        return groups;
    }
    public String toLogFormat(){
        StringBuilder s = new StringBuilder();

        List<List<TimeSlot>> groups = getTimeSlotGroups();

        for (List<TimeSlot> group : groups) {
            s.append(title);
            s.append(' ');

            // Time logic
            String daysOfWeek = "";
            for (TimeSlot t : group) {
                daysOfWeek += t.getDayOfWeek();
            }
            if (daysOfWeek.contains("M")) {
                s.append('M');
            }
            s.append(',');
            if (daysOfWeek.contains("T")) {
                s.append('T');
            }
            s.append(',');
            if (daysOfWeek.contains("W")) {
                s.append('W');
            }
            s.append(',');
            if (daysOfWeek.contains("R")) {
                s.append('R');
            }
            s.append(',');
            if (daysOfWeek.contains("F")) {
                s.append('F');
            }
            if (daysOfWeek.contains("S")) {
                s.append('S');
            }
            s.append(',');
            if (daysOfWeek.contains("U")) {
                s.append('U');
            }
            s.append(' ');
            s.append(group.get(0).logFormattedStartTime());
            s.append(' ');
            s.append(group.get(0).logFormattedEndTime());
        }
        return s.toString();
    }
}