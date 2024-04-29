package com.classes;

import java.io.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.util.*;

public class Schedule {
    private List<Event> events;
    private List<Course> courses;
    private String semester;

    private String year;
    private Log log = new Log();
    private String name;
    private final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private final String[] TIMES = {"8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM",
            "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM",
            "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM",
            "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM",
            "8:00 PM", "8:30 PM", "9:00 PM", "9:30 PM", "10:00 PM", "10:30 PM", "11:00 PM", "11:30 PM"};

    public Schedule(String name, String semester, String year){
        this.name = name;
        this.semester = semester;
        this.year = year;
        this.courses = new ArrayList<Course>();
        this.events = new ArrayList<Event>();
    }

    public Schedule(String filepath) {
        String[] path = filepath.split("/");
        String[] scheduleData = path[path.length - 1].split("\\|");
        this.name = scheduleData[0];
        this.semester = scheduleData[1];
        this.courses = Search.WebScraper().getClasses();
        this.year = year;
    }

    public Schedule(Schedule s){
        this.name = s.name;
        this.courses = new ArrayList<Course>();
        this.courses.addAll(s.courses);
        this.semester = s.semester;
        this.events = new ArrayList<Event>();
        this.events.addAll(s.events);
        this.year = year;
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
        if(events.isEmpty()){
            System.out.println("You have no events in your schedule.");
            return false;
        }
        if(events.remove(e)){
            System.out.println("Event: " + e.getTitle() + " has been removed from your schedule");
            log.addAction(new Schedule(this));
            return true;
        }
        System.out.println("Event: " + e.getTitle() + " is not in your schedule");
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
                System.out.println(current.getCourseCode());
                System.out.println(c.getCourseCode());
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

    public void export()  {
        try (PDDocument document = new PDDocument()) {
            // Define custom landscape page dimensions
            PDPage page = new PDPage(new PDRectangle(792, 800)); // Width: 792 points, Height: 612 points (A4 in landscape)
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Define cell width and height
            float cellWidth = 80;
            float cellHeight = 20;

            String title = "Schedule: " + this.name + ", for " + this.semester + " " + this.year; // PDF Title basically
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18); // Adjust font and size as needed
            contentStream.newLineAtOffset(50, 750); // Adjust x and y coordinates as needed
            contentStream.showText(title);
            contentStream.endText();

            // Adjust y-coordinate to leave space for times before 10:00 AM
            float startY = 1400 - (TIMES.length + 2) * cellHeight; // Start below the times (plus some additional space)

            // Table header
            drawCell(contentStream, 50, startY, cellWidth, cellHeight, "Time", true);
            for (int i = 0; i < DAYS.length; i++) {
                drawCell(contentStream, 50 + (i + 1) * cellWidth, startY, cellWidth, cellHeight, DAYS[i], true);
            }

            // Table content
            for (int i = 0; i < TIMES.length; i++) {
                drawCell(contentStream, 50, startY - (i + 1) * cellHeight, cellWidth, cellHeight, TIMES[i], true);
                for (int j = 0; j < DAYS.length; j++) {
                    String courseName = getCourseSlot(courses, DAYS[j], TIMES[i]);
                    StringBuilder eventName = getEventSlot(events, DAYS[j], TIMES[i]);
                    String content = "";
                    if (courseName != null) {
                        content += courseName + "\n";
                    }
                    if (eventName != null) {
                        content += eventName.toString() + "\n";
                    }
                    drawCell(contentStream, 50 + (j + 1) * cellWidth, startY - (i + 1) * cellHeight, cellWidth, cellHeight, content, false);
                }
            }

            contentStream.close();

            // Save the PDF document
            File file = new File(this.name + ".pdf");
            document.save(file);
            System.out.println("PDF created successfully.");
        } catch (IOException e) {
            System.err.println("Error creating PDF: " + e.getMessage());
        }
    }

    // Method to draw a cell in the PDF
    private static void drawCell(PDPageContentStream contentStream, float x, float y, float width, float height, String content, boolean bold) throws IOException {
        // Filter out control characters
        content = content.replaceAll("\\p{Cntrl}", "");

        contentStream.setFont(PDType1Font.HELVETICA, 9);
        if (bold) {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        }
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(content);
        contentStream.endText();

        // Adjust cell width and height to fixed values

        contentStream.addRect(x, y - height, width, height);
        contentStream.stroke();
    }

    public void viewGrid()  {
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
                    if(isTimeInSlot(timeSlot, time)){
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
                if(timeSlot.getDayOfWeek()== day.charAt(0)){
                    if(isTimeInSlot(timeSlot, time)){
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
            for(Event e: events){
                p.println(e.toCSVFormat());
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
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
