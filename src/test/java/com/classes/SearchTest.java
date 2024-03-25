package com.classes;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {



//class variables for testing
    private Map<String, String> testAddFilters = new HashMap<>();

    private Map<String, String> testRemoveFilters = new HashMap<>();


    private Map<String, String> testModifyFilters = new HashMap<>();

    private Map<String, String> testClearFilters = new HashMap<>();

    public Map<String, String> getTestModifyFilters() {
        return testModifyFilters;
    }

    public Map<String, String> getTestClearFilters() {
        return testClearFilters;
    }

    public Map<String, String> getTestRemoveFilters() {
        return testRemoveFilters;
    }

    public Map<String, String> getTestAddFilters() {
        return testAddFilters;
    }


    @Test
    void search(){
        Search s = new Search();

    }

    //Tests multiple filters
    @Test
    void filterCourses() {
//        Search s = new Search();
//        s.addFilter("day", "W");
//        s.addFilter("courseCode", "HUMA 200");
//        s.addFilter("startHour", "15");
//        s.filterCourses(s.getCourses());
//        for(Course course: s.getCourses()){
//            assertEquals(course.getCourseCode() + " " + course.getSectionLetter(), "HUMA 200 D");
//        }
//
//        Search x = new Search();
//        x.addFilter("day", "W");
//        x.addFilter("day", "T");
//        x.filterCourses(x.getCourses());
//       // System.out.println(x.getCourses().size());
//        for(Course course: x.getCourses()){
//          //  System.out.println(course.getCourseCode());
//        }

        Search y = new Search();
        y.addFilter("startHour", "90");
       // y.addFilter("startHour", "10");
        List<Course> newCourses = new ArrayList<>();
        newCourses = y.filterCourses(y.getCourses());
        for(Course course: newCourses){
           System.out.println(course.getCourseCode());
           for(TimeSlot timeslot: course.getTimes()){
               System.out.println(timeslot.getStartHour());
           }
        }
    }


    @Test
    void addFilter() {
        Search s = new Search();
        testAddFilters.put("courseCode", "ACCT 201");
        s.addFilter("courseCode", "ACCT 201");
        assertEquals(s.getFilters(), getTestAddFilters());


    }

    @Test
    void removeFilter() {
        Search s = new Search();
        testRemoveFilters.put("courseCode", "ACCT 201");
        s.addFilter("courseCode", "ACCT 201");
        testRemoveFilters.remove("courseCode", "ACCT 201");
        s.removeFilter("courseCode", "ACCT 201");
        assertEquals(s.getFilters(), getTestRemoveFilters());



    }


    @Test
    void modifyFilter() {
        Search s = new Search();
        testModifyFilters.put("courseCode", "ACCT 201");
        s.addFilter("courseCode", "ACCT 201");
        testModifyFilters.put("courseCode", "HIST 283");
        s.modifyFilter("courseCode", "HIST 283");
        assertEquals(s.getFilters(), getTestModifyFilters());
    }

    @Test
    void clearFilters() {
        Search s = new Search();
        testClearFilters.put("courseCode", "ACCT 201");
        s.addFilter("courseCode", "ACCT 201");
        testClearFilters.clear();
        s.clearFilters();
        assertEquals(s.getFilters(), getTestClearFilters());
    }
}