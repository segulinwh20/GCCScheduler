package com.classes;

import java.util.List;

public class Course {
    private int id;
    private String title;
    private int credits;
    private String department;
    private String description;
    private int courseCode;
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
                  String description, int courseCode, String semester, int year, Professor professor,
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
        s.append(year);
        s.append(',');
        s.append(semester.equals("fall") ? 10 : 30);
        s.append(',');
        s.append(department);
        s.append(',');
        s.append(courseCode);
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

        s.append(',');
        s.append(professor.getLast());
        s.append(',');
        s.append(professor.getFirst());
        s.append(',');
        s.append(professor.getPreferred());
        s.append(',');
        s.append(comments);

        return s.toString();
    }
}
