package com.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

//Load all of the courses into a giant course list. From there,
//when it comes time to search based on parameters, iterate through each course's "get____" method,
//and return the list of classes containing that match.

public class Search {
    private List<Course> courses;
    //private List<String> filters = new ArrayList<>();
    private Map<String, String> filters;

    public Search() {
        courses = new ArrayList<>();
        filters = new HashMap<>();
    }

    //Testing  purposes
    public List<Course> getCourses() {
        return courses;
    }

    public Map<String, String> getFilters() {
        return filters;
    }



    public List<Course> filterCourses(List<Course> courses){
        List<Course> data = readCoursesFromFile("data/2020-2021.csv");

        for (Course datum : data) {
            boolean match = true;
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                String filterType = entry.getKey();
                String filterValue = entry.getValue();
                switch (filterType) {
                    case "startHour":
                        for(TimeSlot timeslot: datum.getTimes()){
                            //System.out.println(datum.getCourseCode() + " "  + datum.getSectionLetter() + " " + timeSlot.getStartHour());
                            if(timeslot.getStartHour() != Integer.parseInt(filterValue)){
                                match = false;
                                break;
                            }
                        }

                    if(datum.getTimes().isEmpty()){
                        match = false;
                    }

                        break;
                    case "day":
                        match = false;
                        for(TimeSlot timeSlot: datum.getTimes()){
                            if(timeSlot.getDayOfWeek() == filterValue.charAt(0)){
                                match = true;
                                break;
                            }
                        }
                        break;
                    case "courseCode":
                        if (!datum.getCourseCode().equals(filterValue)) {
                            match = false;
                        }
                        break;
                    case "title":
                        if (!datum.getTitle().equals(filterValue)) {
                            match = false;
                        }
                        break;
                    default:
                        System.out.println("Invalid filter type");
                        break;
                }
                if (!match) {
                    break;
                }
            }
            if (match) {
                courses.add(datum);
                System.out.println(datum.getTimes());
                for(TimeSlot timeslot: datum.getTimes()){
                    System.out.println(datum.getCourseCode() + " " + datum.getSectionLetter() + " " + timeslot.getStartHour());
                }

            }

        }
        return courses;
    }

    //Syntax [FilterType]:"filterName"
    public void addFilter(String filterType, String filterValue) {
        filters.put(filterType, filterValue);

    }



    public void removeFilter(String filterType, String filterValue) {
        filters.remove(filterType, filterValue);
    }

    public void modifyFilter(String filterType, String filterValue) {
        if(filters.containsKey(filterType)){
            filters.put(filterType, filterValue);
        } else {
            System.out.println("No such filter has been set");
        }
    }

    public void clearFilters() {
        filters.clear();
    }

    public static List<Course> readCoursesFromFile(String filepath) {
        // account 201 in one
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
                    semester = "Fall";
                } else {
                    semester = "Spring";
                }

                Professor prof = new Professor();

                prof.setFirst(fields[17]);
                prof.setLast(fields[16]);
                prof.setPreferred(fields[18]);
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
                    endHour += 12;
                }


                // days of the week are field[9] - field[13]
                List<TimeSlot> time = new ArrayList<TimeSlot>();
                String daysOfWeek = fields[9] + fields[10] + fields[11] + fields[12] + fields[13];

                for(int i = 0; i < daysOfWeek.length(); i++){
                    TimeSlot timeslot = new TimeSlot(daysOfWeek.charAt(i), startHour,
                            Integer.parseInt(beginTimeData[1]),endHour,
                            Integer.parseInt(endTimeData[1]));
                    time.add(timeslot);
                }

                char sectionLetter; // fixed error when the section letter is empty
                if(fields[4].isEmpty()){
                    sectionLetter = ' ';
                } else {
                    sectionLetter = fields[4].charAt(0);
                }

                int year = Integer.parseInt(fields[0]);
                String department = fields[2];
                int id = Integer.parseInt(fields[3]);
                String courseCode = department + " " + id;
                String title = fields[5];
                int credits = Integer.parseInt(fields[6]);
                int seats = Integer.parseInt(fields[7]);
                String desc = fields[19];


                // made course constructor with all information in CSV that lined up with variables already present
                Course course = new Course(year, semester, department, id, sectionLetter, courseCode, title, credits, seats, time, prof, desc);


                c.add(course);


            }

            List<Course> combined = new ArrayList<>(c.size());

            for (int i = 0; i < c.size(); i++) {
                boolean addedToList = false;
                for (int j = 0; j < combined.size(); j++) {
                    if (c.get(i).equals(combined.get(j))) {         // if there is a matching course, combine the times
                        Course old = c.get(i);
                        Course keep = combined.get(j);
                        List<TimeSlot> newTimes = keep.getTimes();
                        newTimes.addAll(old.getTimes());
                        keep.setTimes(newTimes);
                        addedToList = true;
                        break;
                    }
                }
                if (!addedToList) {
                    combined.add(c.get(i));
                }
            }

            return combined;
        }
        catch (FileNotFoundException e){
            throw new RuntimeException("NO FILE");
        }
    }
}
