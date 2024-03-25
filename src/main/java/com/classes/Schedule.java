package com.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Course> courses;
    private String semester;
    private Log log;
    private String name;

    public Schedule(String name, String semester){
        this.name = name;
        //TODO: semester
        //TODO: log
        this.courses = new ArrayList<Course>();
    }

    public Schedule(String filepath) {
        String[] path = filepath.split("/");
        this.name = path[path.length - 1];
        //TODO: semester
        //TODO: log
        this.courses = Search.readCoursesFromFile(filepath);
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
        if (courses.size() == 0) {
            System.out.println("You have no courses in your schedule.");
            return false;
        }
        if (courses.remove(c)) {
            System.out.println(c + " has been removed from your schedule.");
            return true;
        }
        System.out.println(c + " is not in your schedule.");
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

    public void setName(String name) {
        this.name = name;
    }
}
