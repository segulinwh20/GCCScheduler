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
    private Professor professor;
    private List<TimeSlot> times;
    private char sectionLetter;
    private String location;
    private int seats;

    public Course(){

    }

    public Course(int id, String title, int credits, String department,
                  String description, String courseCode, String semester, Professor professor,
                  List<TimeSlot> times, char sectionLetter, int seats){ // with all info included in csv
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
