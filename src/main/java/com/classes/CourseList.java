package com.classes;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseList {
    public List<Course> getClasses() {
        return classes;
    }

    public void setClasses(List<Course> classes) {
        this.classes = classes;
    }

    private List<Course> classes;

    // Ignoring the "date" and "time" fields
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String date;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private String time;


}