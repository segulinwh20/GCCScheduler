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
//        List<Course> q;5
//        q = s.filterCourses();
//        for(Course course: q){
//            assertEquals(course.getCourseCode() + " " + course.getSectionLetter(), "HUMA 200 D");
//        }

        Search x = new Search();
        x.addFilter(Search.Type.DAY, "W");
        x.addFilter(Search.Type.DAY, "R");
        List<Course> w;
        w = x.filterCourses();
        for(Course course: w){
          System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
        }

//        Search y = new Search();
//        y.addFilter("startHour", "9");
//        y.addFilter("startHour", "10");
//       // y.addFilter("startHour", "10");
//        List<Course> e;
//        e = y.filterCourses();
//        for(Course course: e){
//            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
//        }
    }


    @Test
    void addFilter() {

    }

    @Test
    void removeFilter() {
        Search x = new Search();
        x.addFilter(Search.Type.DAY, "W");
        x.addFilter(Search.Type.DAY, "R");
        x.addFilter(Search.Type.STARTHOUR, "9");
        System.out.println("Before: " + x.getFilters());
//        List<Course> w;
//        w = x.filterCourses();
//        for(Course course: w){
//            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
//        }
        x.removeFilter(Search.Type.DAY, "R");
        x.removeFilter(Search.Type.STARTHOUR, "9");
        System.out.println("After: " + x.getFilters());
//        List<Course> courses;
//        courses = x.filterCourses();
//        for(Course course: courses){
//            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
//        }




    }


    @Test
    void modifyFilter() {

    }

    @Test
    void clearFilters() {

    }
}