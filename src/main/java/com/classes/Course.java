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
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCredits() {
        return credits;
    }

    public String getDepartment() {
        return department;
    }

    public String getDescription() {
        return description;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getSemester() {
        return semester;
    }

    public Professor getProfessor() {
        return professor;
    }

    public List<TimeSlot> getTimes() {
        return times;
    }

    public char getSectionLetter() {
        return sectionLetter;
    }

    public int getSeats() {
        return seats;
    }



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
