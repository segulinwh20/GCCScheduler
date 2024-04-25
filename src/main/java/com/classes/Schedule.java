package com.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Schedule {
    private List<Event> events;
    private List<Course> courses;
    private String semester;
    private Log log = new Log();
    private String name;
    private final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private final String[] TIMES = {"8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM",
            "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM",
            "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM",
            "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM",
            "8:00 PM", "8:30 PM", "9:00 PM", "9:30 PM", "10:00 PM", "10:30 PM", "11:00 PM", "11:30 PM"};

    public Schedule(String name, String semester){
        this.name = name;
        this.semester = semester;
        this.courses = new ArrayList<Course>();
        this.events = new ArrayList<Event>();
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
        this.events = new ArrayList<Event>();
        this.events.addAll(s.events);
    }

    public String getName(){
        return name;
    }

    public String getSemester() {return semester;}

    public List<Course> getCourses(){
        return courses;
    }

    public List<Event> getEvents(){
        return events;
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

    public boolean addEvent(Event e){
        if(conflicts(e)){
            System.out.println("Event cannot be added because it conflicts with another course or event in the schedule");
            return false;
        }
        events.add(e);
        log.addAction(new Schedule(this));
        return true;
    }

    public boolean removeEvent(Event e){
        return false;
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
        for (Event current: events){
            if(TimeSlot.conflicts(current.getTimes(), c.getTimes())){
                return true;
            }
        }
        return false;
    }


    private boolean conflicts(Event e){
        for (Event current: events){
            if(TimeSlot.conflicts(current.getTimes(), e.getTimes())){
                return true;
            }
        }
        for(Course current: courses){
            if(TimeSlot.conflicts(current.getTimes(), e.getTimes())){
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
                if (courseName != null) {
                    System.out.printf("%-13s", courseName);
                }
                StringBuilder eventName = getEventSlot(events, day, time);
                if (eventName != null) {
                    System.out.printf("%-13s", eventName.toString());
                } else {
                    System.out.printf("%-13s", "");
                }
            }
            System.out.println();
        }
    }

    private String getCourseSlot(List<Course> courses, String day, String time) {
        //ISSUE: day.charAt(0) takes first letter of day of week, but Tuesday and Thursday have same starting letter
        for (Course course : courses) {
            for (TimeSlot timeSlot : course.getTimes()) {
                if (timeSlot.getDayOfWeek() == day.charAt(0)) {
                    System.out.println("dayOfWeek matches");
                    if(isTimeInSlot(timeSlot, time)){
                        System.out.println("in time slot");
                        return course.getCourseCode() + " " + course.getSectionLetter();
                    }
                  //  return course.getCourseCode() + " " + course.getSectionLetter();
                }
            }
        }
        return null;
    }

    private StringBuilder getEventSlot(List<Event>  events, String day, String time){
        for (Event event: events){
            for(TimeSlot timeSlot: event.getTimes()){
                System.out.println("timeSlot day: " + timeSlot.getDayOfWeek());
                System.out.println("day.charAt(0) " + day.charAt(0));
                if(timeSlot.getDayOfWeek()== day.charAt(0)){
                    System.out.println("dayOfWeek matches");
                    if(isTimeInSlot(timeSlot, time)){
                        System.out.println("in time slot");
                        return event.getTitle();
                    }
                }

            }
        }
        return null;
    }

    private boolean isTimeInSlot(TimeSlot timeSlot, String time) {
        String[] parts = time.split(":");
        int hour;
        if (parts[1].contains("PM")) {
            hour = Integer.parseInt(parts[0]) + 12;
        } else {
            hour = Integer.parseInt(parts[0]);
        }
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
}
