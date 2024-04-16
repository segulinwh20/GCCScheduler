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
}




