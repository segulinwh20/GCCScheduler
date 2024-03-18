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
   // private String location;
    private int seats;

    public Course(String semester, String department, int id, char sectionLetter, String courseCode, String title, int credits, int seats, List<TimeSlot> times, Professor professor, String description){
        this.semester = semester;
        this.department = department;
        this.courseCode = courseCode;
        this.id = id;
        this.sectionLetter = sectionLetter;
        this.title = title;
        this.credits = credits;
        this.seats = seats;
        this.times = times;
        this.professor = professor;
        this.description = description;
    }
}
