package com.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Course> courses;
    private String semester;
    private Log log;
    private String name;

    public Schedule(String name, String semester){
        this.name = name;
        this.courses = new ArrayList<Course>();
    }

    public String getName(){
        return name;
    }

    public List<Course> getCourses(){
        return courses;
    }

    public void addCourse(Course c) {
        courses.add(c);
    }

    public boolean removeCourse(Course c) {
        if (courses.remove(c)) {
            return true;
        }
        return false;
    }

    private boolean conflicts(Course c) {
        return false;
    }

    public File export() {
        return null;
    }

    public void viewGrid() {

    }
}
