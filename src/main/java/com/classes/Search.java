package com.classes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.util.*;

import java.util.List;

//Load all the courses into a giant course list. From there,
//when it comes time to search based on parameters, iterate through each course's "get____" method,
//and return the list of classes containing that match.

public class Search {


//enumerators for search variables
    enum Type {
        DAY,
        COURSECODE,
        TITLE,
        SEMESTER,
        TIME

    }


    //various data structures for search algorithm
    private Map<Type, List<String>> filters;

    private List<String> timeFilters;

    private List<String> minuteFilters;

    private List<String> dayFilters;

    //search constructor
    public Search() {
        filters = new HashMap<>();
        timeFilters = new ArrayList<>();
        dayFilters = new ArrayList<>();
    }

    //getter for filters

    public Map<Type, List<String>> getFilters() {
        return filters;
    }


    //main searching algorithm. Reads data from file, creates a new list of courses, and searches for matches of each type
    //of enumerator.
    public List<Course> filterCourses() {
        List<Course> data = readCoursesFromFile("data/2020-2021.csv");
        List<Course> filteredCourses = new ArrayList<>();
        //iterating through every course
        for (Course datum : data) {
            boolean match = true;
            //iterates through each filter
            for (Map.Entry<Type, List<String>> entry : filters.entrySet()) {
                Type filterType = entry.getKey();
                List<String> filterValues = entry.getValue();
                switch (filterType) {
                    //match is true if TIME matches
                    case Type.TIME:
                        match = false;
                        for(String time: filterValues){
                            String minute = "";
                            String hour = "";
                            String[] tokens = time.split(":");
                            hour = tokens[0];
                            minute = tokens[1];
                            for(TimeSlot timeslot: datum.getTimes()){
                                if(timeslot.getStartMinute() == Integer.parseInt(minute) && timeslot.getStartHour() == Integer.parseInt(hour)){
                                    match = true;
                                    break;
                                }
                            }
                        }
                        if (datum.getTimes().isEmpty()) {
                            match = false;
                        }
                        break;
                        //match is true if DAY matches
                    case Type.DAY:
                        match = false;
                        for (TimeSlot timeSlot : datum.getTimes()) {
                            if (filterValues.contains(String.valueOf(timeSlot.getDayOfWeek()))) {
                                match = true;
                                break;
                            }
                        }
                        if(datum.getTimes().isEmpty()){
                            match = false;
                            break;
                        }
                        break;
                        //no match if courseCode doesn't match
                    case Type.COURSECODE:
                        if (!filterValues.contains(datum.getCourseCode())) {
                            match = false;
                        }
                        break;
                        //no match if title doesn't match
                    case Type.TITLE:
                        if (!filterValues.contains(datum.getTitle())) {
                            match = false;
                        }
                        break;
                        //no match if semester doesn't match
                    case Type.SEMESTER:
                        if (!filterValues.contains(datum.getSemester())) {
                            match = false;
                        }
                        break;
                        //error handling
                    default:
                        System.out.println("Invalid filter type");
                        break;
                }
                //doesn't add course to search list if no match
                if (!match) {
                    break;
                }
            }
            //Adds course to search list if there is a match
            if (match) {
                filteredCourses.add(datum);
            }
        }
        return filteredCourses;
    }

    //Adds filter to the filter list
    public void addFilter(Type filterType, String filterValue) {
       if(filterType.equals(Type.DAY)){
            dayFilters.add(filterValue);
            filters.put(filterType, dayFilters);
        }
        else {
            filters.computeIfAbsent(filterType, k -> new ArrayList<>()).add(filterValue);
        }

    }

    //Removes filter from the filter list
public void removeFilter(Type filterType, String filterValue) {
   if (filterType.equals(Type.DAY)) {
        if (dayFilters.contains(filterValue)) {
            dayFilters.remove(filterValue);
        } else {
            System.out.println("No such filter present");
        }
    }
        List<String> values = filters.get(filterType);
        if (values != null) {
            values.remove(filterValue);
            if (values.isEmpty()) {
                filters.remove(filterType);
            }
        }
}

//Clears all filters from the list
    public void clearFilters() {
        List<String> semester = filters.get(Type.SEMESTER);
        dayFilters.clear();
        timeFilters.clear();
        filters.clear();
        addFilter(Type.SEMESTER, semester.getFirst());
    }

    public static void WebScraper(){
        // Sample time string
        String timeString = "15:30:00";

        // Parse the time string into a LocalTime object
        LocalTime time = LocalTime.parse(timeString);

        // Get the hour and minute from the LocalTime object
        int hour = time.getHour();
        int minute = time.getMinute();

        // Print the hour and minute
        System.out.println("Hour: " + hour);
        System.out.println("Minute: " + minute);
        try {
            URL url = new URL("http://10.18.110.187/api/classes.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse JSON response
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
                mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
                mapper.enable(MapperFeature.ALLOW_COERCION_OF_SCALARS);
                CourseList courseList = mapper.readValue(response.toString(), CourseList.class);


                // Process Course objects
                for (Course course : courseList.getClasses()) {
                    System.out.println(course.getCourseCode());
                    System.out.print(course.getTitle());
                    System.out.print(" ");
                    System.out.print(course.getSectionLetter());
                    System.out.print(" ");
                    Professor prof = course.getProfessor();
                    System.out.println();
                    System.out.println(prof.getFirst() + " " + prof.getLast());
                    List<TimeSlot> times = course.getTimes();
                    if (times.isEmpty()) {
                        System.out.println("No times scheduled for this course");
                    } else {
                        System.out.println("Times:");
                        for (TimeSlot timeSlot : times) {
                            System.out.println("- Day: " + timeSlot.getDayOfWeek());
                            System.out.println("  Start Hour: " + timeSlot.getStartHour());
                            System.out.println("  Start Minute: " + timeSlot.getStartMinute());
                            System.out.println("  End Hour: " + timeSlot.getEndHour());
                            System.out.println("  End Minute: " + timeSlot.getEndMinute());
                            System.out.println();
//                            System.out.println(timeSlot.getStart_time());
//                            System.out.println(timeSlot.getEnd_time());
                        }
                    }
                    System.out.println();
                    // You can access other properties of the course object here
                }
            } else {
                System.out.println("HTTP request failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //File-parsing.
    public static List<Course> readCoursesFromFile(String filepath) {
        // account 201 in one
        List<Course> c = new ArrayList<Course>();
        File file = new File(filepath);
        if (!file.exists()) {
            return null;
        }
        try(Scanner inFile = new Scanner(file)){
            String line = inFile.nextLine(); // removes header line
            String semester;

            while(inFile.hasNextLine()){
                line = inFile.nextLine();
                String[] fields = line.split(",");


                if(fields.length != 20){ // fixed errors due to empty values at end of csv
                    String[] hold = new String[20];
                    System.arraycopy(fields, 0, hold, 0, fields.length);
                    fields = new String[20];
                    System.arraycopy(hold, 0, fields, 0, 20);
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
                String courseCode = department + id;
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
