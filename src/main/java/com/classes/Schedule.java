package com.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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

    public void save() {
        try {
            PrintWriter p = new PrintWriter("data/" + name + ".csv");
            for (Course c : courses) {
                p.print(c.toCSVFormat());
            }
            p.close();
        } catch (FileNotFoundException e) {
            System.out.println("Schedule unable to be saved");
        }

    }
}
