package com.classes;

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

    public Course(){

    }

    public Course(int id, String title, int credits, String department,
                  String description, String courseCode, String semester, int year, Professor professor,
                  List<TimeSlot> times, char sectionLetter, int seats) { // with all info included in csv
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
    }

    public int getId(){
        return id;
    }

    public String toCSVFormat() {
        StringBuilder s = new StringBuilder();
        s.append(year);
        return "";
    }
}
