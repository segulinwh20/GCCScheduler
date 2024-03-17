package com.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Search {
    private List<Course> courses;
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

    }

    public static List<Course> readCoursesFromFile(String filepath) {
        List<Course> c = new ArrayList<Course>();
        try(Scanner inFile = new Scanner(new File(filepath))){
            String line = inFile.nextLine(); // removes header line
            String semester;

            while(inFile.hasNextLine()){
                line = inFile.nextLine();
                String[] fields = line.split(",");

                if(fields.length != 20){ // fixed errors due to empty values at end of csv
                    String[] hold = new String[20];
                    for (int i = 0; i < fields.length; i++) {
                        hold[i] = fields[i];
                    }
                    fields = new String[20];
                    for (int i = 0; i < 20; i++) {
                        fields[i] = hold[i];

                    }
                }

                if(Integer.parseInt(fields[1]) == 10) {
                    semester = "fall";
                } else {
                    semester = "spring";
                }

                Professor prof = new Professor();

                prof.setFirst(fields[18]);
                prof.setLast(fields[17]);
                prof.setDepartment(fields[2]);

                String[] beginTimeData = fields[14].split(":");
                String[] endTimeData = fields[15].split(":");

                if(beginTimeData[0].isEmpty()){ // fixed error were time slots were empty
                    beginTimeData = new String[3];
                    beginTimeData[0] = "0";
                    beginTimeData[1] = "0";
                    beginTimeData[2] = "NONE";
                }

                int startHour = Integer.parseInt(beginTimeData[0]);

                if(beginTimeData[2].charAt(3) == 'P'){
                    startHour += 12;
                }

                if(endTimeData[0].isEmpty()) {
                    endTimeData = new String[3];
                    endTimeData[0] = "0";
                    endTimeData[1] = "0";
                    endTimeData[2] = "NONE";
                }

                int endHour = Integer.parseInt(endTimeData[0]);

                if(endTimeData[2].charAt(3) == 'P'){
                    endTimeData[0] += 12;
                }


                // note, days of the week are field[9] - field[13]
                String daysOfWeek = fields[9] + fields[10] + fields[11] + fields[12] + fields[13];
                System.out.println(daysOfWeek);
                //TODO: Fix TimeSlot to allow for multiple days of the week
                TimeSlot timeslot = new TimeSlot('M', startHour,
                        Integer.parseInt(beginTimeData[1]),endHour,
                        Integer.parseInt(endTimeData[1]));

                List<TimeSlot> time = new ArrayList<TimeSlot>();
                time.add(timeslot);


                char sectionLetter; // fixed error when the section letter is empty
                if(fields[4].isEmpty()){
                    sectionLetter = ' ';
                } else {
                    sectionLetter = fields[4].charAt(0);
                }

                // made course constructor with all information in CSV that lined up with variables already present
                Course course = new Course(Integer.parseInt(fields[3]), fields[5],
                        Integer.parseInt(fields[7]), fields[2],fields[19], fields[0],
                        semester, prof, time, sectionLetter, Integer.parseInt(fields[7]));


                c.add(course);


            }
            return c;
        }
        catch (FileNotFoundException e){
            throw new RuntimeException("NO FILE");
        }
    }
}
