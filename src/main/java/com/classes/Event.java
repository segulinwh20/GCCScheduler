package com.classes;

import java.util.LinkedList;
import java.util.List;

public class Event {
    private String title;
    private List<TimeSlot> times;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TimeSlot> getTimes() {
        return times;
    }

    public void setTimes(List<TimeSlot> times) {
        this.times = times;
    }

    public Event (String title, List<TimeSlot> times){
        this.title = title;
        this.times = times;
    }
}




