package com.classes;

import java.util.LinkedList;
import java.util.List;

public class Course {
    private int id;
    private String title;
    private int credits;
    private String department;
    private String description;
    private String courseCode;
    private String semester;

    private int year;
    private Professor professor;
    private List<TimeSlot> times;
    private char sectionLetter;
    private String location;
    private int seats;

    private String comments;

    public Course(){

    }

    public Course(int id, String title, int credits, String department,
                  String description, String courseCode, String semester, int year, Professor professor,
                  List<TimeSlot> times, char sectionLetter, int seats, String comments) { // with all info included in csv
        this.id = id;
        this.title = title;
        this.credits = credits;
        this.department = department;
        this.description = description;
        this.courseCode = courseCode;
        this.semester = semester;
        this.year = year;
        this.professor = professor;
        this.times = times;
        this.sectionLetter = sectionLetter;
        this.seats = seats;
        this.comments = comments;
    }

    public int getId(){
        return id;
    }

    public String toCSVFormat() {
        StringBuilder s = new StringBuilder();

        List<List<TimeSlot>> groups = getTimeSlotGroups();

        for (List<TimeSlot> group : groups) {
            s.append(year);
            s.append(',');
            s.append(semester.equals("Fall") ? 10 : 30);
            s.append(',');
            s.append(department);
            s.append(',');
            s.append(courseCode);
            s.append(',');
            s.append(sectionLetter);
            s.append(',');
            s.append(title);
            s.append(',');
            s.append(credits);
            s.append(',');
            s.append(seats); // this might be wrong
            s.append(',');
            s.append(seats);
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
            s.append(',');
            s.append(group.get(0).csvFormattedStartTime());
            s.append(',');
            s.append(group.get(0).csvFormattedEndTime());
            s.append(',');

            s.append(professor.getLast());
            s.append(',');
            s.append(professor.getFirst());
            s.append(',');
            s.append(professor.getPreferred());
            s.append(',');
            s.append(comments);
            s.append('\n');
        }

        return s.toString();
    }

    // this is for the toCSVFormat method
    public List<List<TimeSlot>> getTimeSlotGroups() {
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Course) {
            Course o = (Course) obj;
            return this.courseCode.equals(o.courseCode) && this.sectionLetter == o.sectionLetter
                    && this.year == o.year && this.semester == o.semester;
        }
        return false;
    }

    public List<TimeSlot> getTimes() {
        return times;
    }

    public void setTimes(List<TimeSlot> times) {
        this.times = times;
    }
}
