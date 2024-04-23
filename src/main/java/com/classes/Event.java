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

    //TODO: create a toCSVFormat() method for event
//    public String toCSVFormat() {
//        StringBuilder s = new StringBuilder();
//
//        List<List<TimeSlot>> groups = getTimeSlotGroups();
//
//        for (List<TimeSlot> group : groups) {
//            s.append(year);
//            s.append(',');
//            s.append(semester.equals("Fall") ? 10 : 30);
//            s.append(',');
//            s.append(department);
//            s.append(',');
//            s.append(id);
//            s.append(',');
//            s.append(sectionLetter);
//            s.append(',');
//            s.append(title);
//            s.append(',');
//            s.append(credits);
//            s.append(',');
//            s.append(seats); // this might be wrong
//            s.append(',');
//            s.append(seats);
//            s.append(',');
//
//            // Time logic
//            String daysOfWeek = "";
//            for (TimeSlot t : group) {
//                daysOfWeek += t.getDayOfWeek();
//            }
//            if (daysOfWeek.contains("M")) {
//                s.append('M');
//            }
//            s.append(',');
//            if (daysOfWeek.contains("T")) {
//                s.append('T');
//            }
//            s.append(',');
//            if (daysOfWeek.contains("W")) {
//                s.append('W');
//            }
//            s.append(',');
//            if (daysOfWeek.contains("R")) {
//                s.append('R');
//            }
//            s.append(',');
//            if (daysOfWeek.contains("F")) {
//                s.append('F');
//            }
//            s.append(',');
//            s.append(group.get(0).csvFormattedStartTime());
//            s.append(',');
//            s.append(group.get(0).csvFormattedEndTime());
//            s.append(',');
//
//            s.append(professor.getLast());
//            s.append(',');
//            s.append(professor.getFirst());
//            s.append(',');
//            s.append(professor.getPreferred());
//            s.append(',');
//            s.append(description);
//            s.append('\n');
//        }
//
//        return s.toString();
//    }
}




