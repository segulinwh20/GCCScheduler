package com.classes;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String major;
    private List<Schedule> schedules;

    public Student(String[] studentParams){
        setFirst(studentParams[0]);
        setLast(studentParams[1]);
        StringBuilder major = new StringBuilder();
        major.append(studentParams[2]);
        for (int i = 3; i < studentParams.length; i++) {
            major.append(" ").append(studentParams[i]);
        }
        this.major = String.valueOf(major);
        this.schedules = new ArrayList<Schedule>();
    }

    public File getStatusSheet() {
        return null;
    }

    public void compare(Schedule s1, Schedule s2) {

    }

    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void updateSchedule(int index, Schedule schedule){
        schedules.set(index, schedule);
    }
}
