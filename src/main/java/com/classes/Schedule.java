package com.classes;

import java.io.File;
import java.util.List;

public class Schedule {
    private List<Course> courses;
    private String semester;
    private Log log;
    private String name;

    public void addCourse(Course c) {

    }

    public boolean removeCourse(Course c) {
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
