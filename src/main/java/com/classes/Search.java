package com.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

//Load all of the courses into a giant course list. From there,
//when it comes time to search based on parameters, iterate through each course's "get____" method,
//and return the list of classes containing that match.

public class Search {

    enum Type {
        STARTHOUR,
        STARTMINUTE,
        DAY,
        COURSECODE,
        TITLE

    }


    private Map<Type, List<String>> filters;

    private List<String> timeFilters;

    private List<String> minuteFilters;

    private List<String> dayFilters;

    public Search() {
        filters = new HashMap<>();
        timeFilters = new ArrayList<>();
        dayFilters = new ArrayList<>();
        minuteFilters = new ArrayList<>();
    }


    public Map<Type, List<String>> getFilters() {
        return filters;
    }


    public List<Course> filterCourses() {
        List<Course> data = readCoursesFromFile("data/2020-2021.csv");
        List<Course> filteredCourses = new ArrayList<>();

        for (Course datum : data) {
            boolean match = true;
            for (Map.Entry<Type, List<String>> entry : filters.entrySet()) {
                Type filterType = entry.getKey();
                List<String> filterValues = entry.getValue();

                switch (filterType) {
                    case Type.STARTHOUR:
                        match = false;
                        for (TimeSlot timeslot : datum.getTimes()) {
                            if (filterValues.contains(String.valueOf(timeslot.getStartHour()))) {
                                match = true;
                                break;
                            }
                        }
                        if(datum.getTimes().isEmpty()){
                            match = false;
                            break;
                        }
                        break;
                    case Type.STARTMINUTE:
                        match = false;
                        for (TimeSlot timeslot : datum.getTimes()) {
                            if (filterValues.contains(String.valueOf(timeslot.getStartMinute()))) {
                                match = true;
                                break;
                            }
                        }
                        if(datum.getTimes().isEmpty()){
                            match = false;
                            break;
                        }
                        break;
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
                    case Type.COURSECODE:
                        if (!filterValues.contains(datum.getCourseCode())) {
                            match = false;
                        }
                        break;
                    case Type.TITLE:
                        if (!filterValues.contains(datum.getTitle())) {
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
                filteredCourses.add(datum);
            }
        }
        return filteredCourses;
    }

    public void addFilter(Type filterType, String filterValue) {
        if (filterType.equals(Type.STARTHOUR)) {
            timeFilters.add(filterValue);
            filters.put(filterType, timeFilters);
        } else if(filterType.equals(Type.DAY)){
            dayFilters.add(filterValue);
            filters.put(filterType, dayFilters);
        } else if(filterType.equals(Type.STARTMINUTE)){
            minuteFilters.add(filterValue);
            filters.put(filterType, minuteFilters);
        }
        else {
            filters.computeIfAbsent(filterType, k -> new ArrayList<>()).add(filterValue);
        }
    }

public void removeFilter(Type filterType, String filterValue) {
    if (filterType.equals(Type.STARTHOUR)) {
        if (timeFilters.contains(filterValue)) {
            timeFilters.remove(filterValue);
        } else {
            System.out.println("No such filter present");
        }
    } else if (filterType.equals(Type.DAY)) {
        if (dayFilters.contains(filterValue)) {
            dayFilters.remove(filterValue);
        } else {
            System.out.println("No such filter present");
        }
    } else if (filterType.equals(Type.STARTMINUTE)) {
        if (minuteFilters.contains(filterValue)) {
            minuteFilters.remove(filterValue);
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
