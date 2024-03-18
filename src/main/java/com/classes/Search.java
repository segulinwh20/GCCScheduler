package com.classes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Search {

    //List of courses to be input from file
    private List<Course> courses;

    //List of filters that I will modify
    private List<String> filters;

    public List<Course> search() {
        return null;
    }

    public List<Course> addFilter(String filter) {
        return null;
    }

    public List<Course> removeFilter(String filter) {
        return null;
    }

    public List<Course> modifyFilter(String filter) {
        return null;
    }

    public void clearFilters() {
        filters.clear();
    }

    public List<Course> readCoursesFromFile(String filepath) throws IOException{
        String filepathString = Files.readString(Path.of("searchTest.txt"));
        Scanner scn = new Scanner(filepathString);
        while (scn.hasNextLine()){
            String[] tokens = scn.nextLine().split(",");
            String semester = tokens[0];
            String department = tokens[1];
            int id = Integer.parseInt(tokens[2]);
            char sectionLetter = tokens[3].charAt(0);
            String courseCode = tokens[1] + " " + tokens[2] + " " + tokens[3];
            String title = tokens[4];
            int credits = Integer.parseInt(tokens[5]);
            int seats = (Integer.parseInt(tokens[6])-Integer.parseInt(tokens[7]));
            String M = tokens[8];
            String T = tokens[9];
            String W = tokens[10];
            String R = tokens[11];
            String F = tokens[12];
            String start = tokens[13];
            String end = tokens[14];
            String professor =  tokens[15] + ", "  + tokens[16];
            String description = tokens[18];
            //TODO: Create the timeslot here (check that a day isn't empty, parse time)
        }
        return null;
    }
}
