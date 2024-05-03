package com.classes;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Course {
    // Ignoring the "date" and "time" fields
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String date;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private String time;

    private int id;
    private String title;
    private int credits;
    private String department;
    private String description;
    private String courseCode;
    private String semester;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    private String year;
    private Professor professor;
    private List<TimeSlot> times;
    private char sectionLetter;
    // private String location;
    private int seats;
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCredits() {
        return credits;
    }

    public String getDepartment() {
        return department;
    }

    public String getDescription() {
        return description;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getSemester() {
        return semester;
    }

    public Professor getProfessor() {
        return professor;
    }

    public List<TimeSlot> getTimes() {
        return times;
    }

    public char getSectionLetter() {
        return sectionLetter;
    }

    public int getSeats() {
        return seats;
    }



    public Course(String year, String semester, String department, int id, char sectionLetter, String courseCode, String title, int credits, int seats, List<TimeSlot> times, Professor professor, String description){
        this.year = year;
        this.semester = semester;
        this.department = department;
        this.courseCode = courseCode;
        this.id = id;
        this.sectionLetter = sectionLetter;
        this.title = title;
        this.credits = credits;
        this.seats = seats;
        this.times = times;
        this.professor = professor;
        this.description = description;
    }

    @JsonCreator
    public Course(@JsonProperty("semester") String semesterYear,
                  @JsonProperty("subject") String department,
                  @JsonProperty("number") int id,
                  @JsonProperty("section") String section,
                  @JsonProperty("courseCode") String courseCode,
                  @JsonProperty("name") String title,
                  @JsonProperty("credits") int credits,
                  @JsonProperty("open_seats") int seats,
                  @JsonProperty("times") List<TimeSlot> times,
                  @JsonProperty("faculty")List<String> faculty,
                  @JsonProperty("location") String description){
        String[] tokens = semesterYear.split("_");
        this.year = tokens[0];
        this.semester = tokens[1];
        this.department = department;
        this.courseCode = department+id;
        this.id = id;
        this.sectionLetter = section.charAt(0);
        this.title = title;
        this.credits = credits;
        this.seats = seats;
        this.times = times;
        this.description = description;
        String prof = faculty.getFirst();
        String[] names = prof.split(",");
        String firstName = "";
        if(names.length > 1){
            firstName = names[1];
        }
        else{
            firstName = names[0];
        }
        String lastName = names[0];
        Professor newProf = new Professor();
        newProf.setFirst(firstName);
        newProf.setLast(lastName);
        newProf.setPreferred(firstName);
        newProf.setDepartment(department);
        this.professor = newProf;
    }

    public String toCSVFormat() {
        StringBuilder s = new StringBuilder();

        List<List<TimeSlot>> groups = getTimeSlotGroups();

        for (List<TimeSlot> group : groups) {
            s.append(year);
            s.append(',');
            s.append(semester.equals("Fall") ? 10 : 30);
            s.append(',');
            s.append(department);
            s.append(',');
            s.append(id);
            s.append(',');
            s.append(sectionLetter);
            s.append(',');
            s.append(title);
            s.append(',');
            s.append(credits);
            s.append(',');
            s.append(seats); // this might be wrong
            s.append(',');
            s.append(seats);
            s.append(',');

            // Time logic
            String daysOfWeek = "";
            for (TimeSlot t : group) {
                daysOfWeek += t.getDayOfWeek();
            }
            if (daysOfWeek.contains("M")) {
                s.append('M');
            }
            s.append(',');
            if (daysOfWeek.contains("T")) {
                s.append('T');
            }
            s.append(',');
            if (daysOfWeek.contains("W")) {
                s.append('W');
            }
            s.append(',');
            if (daysOfWeek.contains("R")) {
                s.append('R');
            }
            s.append(',');
            if (daysOfWeek.contains("F")) {
                s.append('F');
            }
            s.append(',');
            s.append(group.get(0).csvFormattedStartTime());
            s.append(',');
            s.append(group.get(0).csvFormattedEndTime());
            s.append(',');

            s.append(professor.getLast());
            s.append(',');
            s.append(professor.getFirst());
            s.append(',');
            s.append(professor.getPreferred());
            s.append(',');
            s.append(description);
            s.append('\n');
        }

        return s.toString();
    }

    // this is for the toCSVFormat method
    private List<List<TimeSlot>> getTimeSlotGroups() {
        List<List<TimeSlot>> groups = new LinkedList<>();
        for (TimeSlot t : times) {
            boolean updated = false;
            for (int i = 0; i < groups.size(); i++) {
                if(groups.get(i).size() > 0 && t.sameStartEndTime(groups.get(i).get(0))) {
                    groups.get(i).add(t);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                List<TimeSlot> group = new LinkedList<>();
                group.add(t);
                groups.add(group);
            }
        }
        return groups;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Course) {
            Course o = (Course) obj;
            return this.courseCode.equals(o.courseCode) && this.sectionLetter == o.sectionLetter
                    && this.year == o.year && this.semester == o.semester;
        }
        return false;
    }

    public void setTimes(List<TimeSlot> times) {
        this.times = times;
    }

    public boolean meetsOnDay(char c) {
        for (TimeSlot t : times) {
            if (t.getDayOfWeek() == Character.toUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    public boolean meetsAt(int hour, int minute) {
        for (TimeSlot t : times) {
            if (t.timeFallsInRange(hour, minute)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(courseCode);
        s.append(' ');
        s.append(sectionLetter);
        s.append(": ");
        s.append(title);
        s.append(" [");
        List<List<TimeSlot>> timeSlotGroups = getTimeSlotGroups();
        if (timeSlotGroups.size() > 0) {
            for (List<TimeSlot> group : timeSlotGroups) {
                for (TimeSlot t : group) {
                    s.append(t.getDayOfWeek());
                }
                s.append(' ');
                s.append(group.get(0).timeRangeAsString());
                s.append(", ");
            }
            s.delete(s.length() - 2, s.length());
        }
        else {
            s.append("No time listed");
        }
        s.append(']');
        return s.toString();
    }
}
