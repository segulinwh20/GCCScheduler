package com.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Schedule {
    private List<Course> courses;
    private String semester;
    private Log log = new Log();
    private String name;
    private final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private final String[] TIMES = {"8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM",
            "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM",
            "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM",
            "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM",
            "8:00 PM", "8:30 PM", "9:00 PM"};

    public Schedule(String name, String semester){
        this.name = name;
        this.semester = semester;
        this.courses = new ArrayList<Course>();
    }

    public Schedule(String filepath) {
        String[] path = filepath.split("/");
        String[] scheduleData = path[path.length - 1].split("\\|");
        this.name = scheduleData[0];
        this.semester = scheduleData[1];
        this.courses = Search.readCoursesFromFile(filepath);
    }

    public Schedule(Schedule s){
        this.name = s.name;
        this.courses = new ArrayList<Course>();
        this.courses.addAll(s.courses);
        this.semester = s.semester;
    }

    public String getName(){
        return name;
    }

    public String getSemester() {return semester;}

    public List<Course> getCourses(){
        return courses;
    }

    public boolean addCourse(Course c) {
        if (conflicts(c)) {
            System.out.println("Class cannot be added because it conflicts with another course in the schedule.");
            return false;
        }
        courses.add(c);
        log.addAction(new Schedule(this));
        return true;
    }

    public boolean removeCourse(Course c) {
        if (courses.isEmpty()) {
            System.out.println("You have no courses in your schedule.");
            return false;
        }
        if (courses.remove(c)) {

            System.out.println(c.getCourseCode() + " " + c.getSectionLetter() + " has been removed from your schedule.");
            log.addAction(new Schedule(this));
            return true;
        }
        System.out.println(c.getCourseCode() + " " + c.getSectionLetter() + " is not in your schedule.");
        return false;
    }

    private boolean conflicts(Course c) {
        for (Course current : courses) {
            if (TimeSlot.conflicts(current.getTimes(), c.getTimes()) || current.getCourseCode().equals(c.getCourseCode())) {
                return true;
            }
        }
        return false;
    }

    public File export() {
        return null;
    }

    public void viewGrid() {
        System.out.printf("%-15s", "");
        for (String day : DAYS) {
            System.out.printf("%-13s", day);
        }
        System.out.println();
        for (String time : TIMES) {
            System.out.printf("%-15s", time);
            for (String day : DAYS) {
                String courseName = getCourseSlot(courses, day, time);
                System.out.printf("%-13s", courseName == null ? "" : courseName);
            }
            System.out.println();
        }
    }

    private String getCourseSlot(List<Course> courses, String day, String time) {
        for (Course course : courses) {
            for (TimeSlot timeSlot : course.getTimes()) {
                if (timeSlot.getDayOfWeek() == day.charAt(0) && isTimeInSlot(timeSlot, time)) {
                    return course.getCourseCode() + " " + course.getSectionLetter();
                }
            }
        }
        return null;
    }

    private boolean isTimeInSlot(TimeSlot timeSlot, String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]) + 12;
        String stringMinute = parts[1];
        if (!timeCheck(stringMinute, timeSlot.toString())) {
            return false;
        }
        int minute = Integer.parseInt(stringMinute.split(" ")[0]);
        return timeSlot.timeFallsInRange(hour, minute);
    }

    public static boolean timeCheck(String time1, String time2) {
        String meridiem1 = time1.substring(time1.length() - 2);
        String meridiem2 = time2.substring(time2.length() - 2);
        return meridiem1.equals(meridiem2);
    }

    public void save() {
        try {
            PrintWriter p = new PrintWriter("data/" + name + ".csv");
            p.println(semester);
            for (Course c : courses) {
                p.print(c.toCSVFormat());
            }
            p.flush();
            p.close();
        } catch (FileNotFoundException e) {
            System.out.println("Schedule unable to be saved");
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void loadFromLog(){
        String line;
        File file = new File("data/log.log");
        try(Scanner inFile = new Scanner(file)){
            while(inFile.hasNextLine()) {
                inFile.nextLine();
                line = inFile.nextLine();
                String[] fields = line.split(" ");
                if(fields.length >= 4) {
                    if (fields[1].equalsIgnoreCase("Successfully")) {
                        switch (fields[2]){
                            case "Added":
                                System.out.println(line);
                                this.addCourse(stringToCourse(fields[3]));
                                break;
                            case "Removed":
                                this.removeCourse(stringToCourse(fields[3]));
                                break;
                            case "Created":
                                this.name = fields[3];
                                this.semester = fields[4];
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Unable to find log");
        }

    }

    public Course stringToCourse(String str){
        List<Course> list = Search.readCoursesFromFile("data/2020-2021.csv");
        String s = str.substring(0,str.length()-1);
        for (Course item : list) {
            if (Objects.equals(s, item.getCourseCode()) && Objects.equals(str.charAt(str.length()-1), item.getSectionLetter())) {
                return item;
            }
        }
        return null;
    }

}
