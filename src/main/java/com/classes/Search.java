package com.classes;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.util.*;

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
        TIME,
        YEAR,
        DEPARTMENT

    }


    //various data structures for search algorithm
    private Map<Type, List<String>> filters;
    private List<String> timeFilters;
    private List<String> dayFilters;

    private Set<String> departments;

    //search constructor
    public Search() {
        filters = new HashMap<>();
        timeFilters = new ArrayList<>();
        dayFilters = new ArrayList<>();
        departments = new HashSet<>();
        for (Course c : filterCourses()) {
            departments.add(c.getDepartment());
        }
    }

    //getter for filters

    public Map<Type, List<String>> getFilters() {
        return filters;
    }


    //main searching algorithm. Reads data from file, creates a new list of courses, and searches for matches of each type
    //of enumerator.
    public List<Course> filterCourses() {
        CourseList courseList = WebScraper(); // Fetch courses from the web API
        if (courseList == null) {
            System.out.println("Failed to fetch course data from the web API.");
            return new ArrayList<>(); // Return an empty list if fetching data fails
        }
        List<Course> data = courseList.getClasses();
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
                    case Type.YEAR:
                        if(!filterValues.contains(datum.getYear())){
                            match = false;
                        }
                        break;
                    case Type.DEPARTMENT:
                        if(!filterValues.contains(datum.getDepartment())) {
                            match = false;
                        }
                        break;
                    default:
                        System.out.println("Invalid filter type!!!");
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
        List<String> year = filters.get(Type.YEAR);
        dayFilters.clear();
        timeFilters.clear();
        filters.clear();
        addFilter(Type.SEMESTER, semester.getFirst());
        addFilter(Type.YEAR, year.getFirst());
    }

    public static CourseList WebScraper(){
        CourseList courseList = null;
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
                courseList = mapper.readValue(response.toString(), CourseList.class);
            }
            else {
                System.out.println("HTTP request failed with response code: " + responseCode);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
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

                String year = (fields[0]);
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

    public static void viewMajorMinor(String name) {
        String path;
        if(name.endsWith(".pdf")){
            path = "MamPDFs/" + name;
        } else {
            path = "MamPDFs/" + name + ".pdf";
        }
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(path);
                if(myFile.exists()) {
                    Desktop.getDesktop().open(myFile);
                    Log.logger.info("Viewing pdf " + myFile);
                } else{
                    System.out.println("File does not exist");
                    Log.logger.info("Tried to view a pdf that does not exist");
                }
            } catch (IOException ex) {
                System.out.println("Action not supported on device");
                Log.logger.info("'Desktop' is not supported on this device");
            }
        }
    }
    public static ArrayList<String> getMajorsMinors(){
        ArrayList<String> fileNames = new ArrayList<>();
        File folder = new File("MamPDFs/");
        File[] files = folder.listFiles();
        for(File f: files){
            fileNames.add(f.toString());
        }
        return fileNames;

    public List<Course> smartSearch(String input) {
        clearFilters();

        String[] tokens = input.toUpperCase().split(" ");

        List<Character> daysOfWeek = new ArrayList<>();
        List<int[]> times = new ArrayList<>();
        List<String> depts = new ArrayList<>();
        List<Integer> courseCodes = new ArrayList<>();
        List<Character> sectionLetters = new ArrayList<>();
        List<String> genStrings = new ArrayList<>();

        // determine what type of information the token is
        for (String token : tokens) {
            if (token.matches("\\d+")) {
                int num = parseInteger(token);
                if (num < 24 && num > 0) {
                    int[] time1 = {num, 0};
                    times.add(time1);
                    if (num < 12) {
                        int[] time2 = {num+12, 0};
                        times.add(time2);
                    }

                }
                else if (num < 2400) {
//                    if (num / 100 < 24 && num / 100 > 0 && num % 100 < 60) {
//                        int[] time1 = {num/100, num%100};
//                        times.add(time1);
//                        if ((num / 100) + 12 < 24) {
//                            int[] time2 = {(num/100)+12, num%100};
//                            times.add(time2);
//                        }
//                    }
                    if (num >= 100 && num < 600) {
                        courseCodes.add(200);
                    }
                }
            }
            else if (token.matches("[MTWRF]+")) {
                for (int i = 0; i < token.length(); i++) {
                    if (!daysOfWeek.contains(token.charAt(i))) {
                        daysOfWeek.add(token.charAt(i));
                    }
                }
            }
            else if (departments.contains(token)) {
                depts.add(token);
            }
            else if (token.matches("[A-Z]")) {
                sectionLetters.add(token.charAt(0));
            }
            else if (token.matches("[A-Z]{1,4}\\d{3}")) {
                String[] strings = token.split("(?<=\\D)(?=\\d)");
                depts.add(strings[0]);
                courseCodes.add(parseInteger(strings[1]));
            }
            else if (token.matches("\\d{1,2}:\\d{1,2}")) {
                String[] time = token.split(":");
                int hour = parseInteger(time[0]);
                int minute = parseInteger(time[1]);
                if (hour < 24 && hour > 0 && minute < 60) {
                    int[] time1 = {hour, minute};
                    times.add(time1);
                    if (hour < 12) {
                        int[] time2 = {hour+12, minute};
                        times.add(time2);
                    }
                }
            }
            if (token.matches("[A-Z]+")) {
                genStrings.add(token);
            }
        }

        // filter the courses
        for (Character day : daysOfWeek) {
            addFilter(Type.DAY, day.toString());
        }
        for (int[] time : times) {
            addFilter(Type.TIME, time[0] + ":" + time[1]);
        }
        for (String dept : depts) {
            addFilter(Type.DEPARTMENT, dept);
        }

        List<Course> courses = filterCourses();

        // calculate the heuristic
        Map<Course, Integer> courseHeuristic = new HashMap<>();
        for (Course course : courses) {
            int h = 0;
            for (String dept : depts) {
                if (jaroWinkler(dept, course.getDepartment()) > .7) {
                    h += 1000;
                }
            }
            for (int courseCode : courseCodes) {
                if (String.valueOf(courseCode).equals(course.getCourseCode().split("(?<=\\D)(?=\\d)")[1])) {
                    h += 1000;
                }
                if (String.valueOf(courseCode).substring(0,1).equals(course.getCourseCode().substring(0,1))) {
                    h += 100;
                }
            }
            for (char sectionLetter : sectionLetters) {
                if (sectionLetter == course.getSectionLetter()) {
                    h += 250;
                }
            }
            for (String s : genStrings) {
                for (String word : course.getTitle().split(" ")) {
                    if (word.length() > 4) {
                        double jaroWinkler = jaroWinkler(s, word);
                        if (jaroWinkler > .95) {
                            h += 5000;
                        }
                        else if (jaroWinkler > .9) {
                            h += 1000;
                        }
                        else if (jaroWinkler > .8) {
                            h += 500;
                        }
                        else {
                            h += 500 * jaroWinkler(s, word);
                        }
                    }
                }
                if (jaroWinkler(s, course.getProfessor().getLast()) > .7) {
                    h += 500;
                }
            }
            courseHeuristic.put(course, h);
        }

        return sortCourseHeuristic(courseHeuristic);
    }

    private double jaroWinkler(String s1, String s2) {
        s1 = s1.toUpperCase();
        s2 = s2.toUpperCase();
        double jaro = jaro(s1, s2);
        int MAX_PREFIX_LENGTH = 4;
        int commonPrefixLength = 0;
        double prefixScalar = 0.2;  // Can't exceed 1 div MAX_PREFIX_LENGTH

        for (int i = 0; i < Integer.min(MAX_PREFIX_LENGTH, Integer.min(s1.length(), s2.length())); i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                commonPrefixLength++;
            }
            else {
                break;
            }
        }

        return jaro + commonPrefixLength * prefixScalar * (1 - jaro);
    }

    private double jaro(String s1, String s2) {
        char[] a = s1.toUpperCase().toCharArray();
        char[] b = s2.toUpperCase().toCharArray();
        int matchDistance = Integer.max(a.length, b.length) / 2 - 1;
        double matches = 0;
        int transpositions = 0;
        if (a.length < b.length) {
            for (int i = 1; i < a.length; i++) {
                if (a[i-1] == b[i] && a[i] == b[i-1] && a[i] != a[i-1]) {
                    transpositions++;
                }
            }
            for (int i = 0; i < a.length; i++) {
                for (int j = Integer.max(0, i - matchDistance); j <= Integer.min(b.length-1, i + matchDistance); j++) {
                    if (a[i] == b[j]) {
                        matches++;
                        b[j] = Character.toLowerCase(b[j]);
                        break;
                    }
                }
            }
        }
        else {
            for (int i = 1; i < b.length; i++) {
                if (b[i-1] == a[i] && b[i] == a[i-1] && b[i] != b[i-1]) {
                    transpositions++;
                }
            }
            for (int i = 0; i < b.length; i++) {
                for (int j = Integer.max(0, i - matchDistance); j <= Integer.min(a.length-1, i + matchDistance); j++) {
                    if (b[i] == a[j]) {
                        matches++;
                        a[j] = Character.toLowerCase(a[j]);
                        break;
                    }
                }
            }
        }
        if (matches == 0) {
            return 0;
        }

        return ((matches / a.length) + (matches / b.length) + ((matches - transpositions) / matches)) / 3;
    }

    private int parseInteger(String s) {
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            n *= 10;
            n += (s.charAt(i) - '0');
        }
        return n;
    }

    private List<Course> sortCourseHeuristic(Map<Course, Integer> heuristics) {
        List<Course> courses = new LinkedList<>();
        Set<Course> keys = heuristics.keySet();
        for (Course c : keys) {
            courses.add(c);
        }
        Course[] arr = new Course[courses.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = courses.remove(0);
        }
        arr = mergeSort(arr, heuristics);
        List<Course> sorted = new LinkedList<>();
        for (int i = 0; i < arr.length; i++) {
            sorted.add(arr[i]);
        }
        return sorted;
    }

    private Course[] mergeSort(Course[] courses, Map<Course, Integer> heuristics) {
        if (courses.length <= 1) {
            return courses;
        }
        int middle = courses.length / 2;
        Course[] a = new Course[middle];
        for (int i = 0; i < a.length; i++) {
            a[i] = courses[i];
        }
        Course[] b = new Course[courses.length - middle];
        for (int i = 0; i < b.length; i++) {
            b[i] = courses[middle+i];
        }
        a = mergeSort(a, heuristics);
        b = mergeSort(b, heuristics);
        return merge(a, b, heuristics);
    }

    private Course[] merge(Course[] a, Course[] b, Map<Course, Integer> heuristics) {
        Course[] sorted = new Course[a.length + b.length];
        int i = 0, ai = 0, bi = 0;
        for (; i < sorted.length && ai < a.length && bi < b.length; i++) {
            if (heuristics.get(a[ai]) < heuristics.get(b[bi])) {
                sorted[i] = b[bi++];
            }
            else {
                sorted[i] = a[ai++];
            }
        }
        if (ai == a.length) {
            for (; i < sorted.length; i++) {
                sorted[i] = b[bi++];
            }
        }
        if (bi == b.length) {
            for (; i < sorted.length; i++) {
                sorted[i] = a[ai++];
            }
        }
        return sorted;
    }
}
