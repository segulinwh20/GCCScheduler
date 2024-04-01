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


    @Test
    void search(){
        Search s = new Search();
        assert s!= null;
    }

    @Test
    void  filter(){
        Search y = new Search();
        y.addFilter(Search.Type.STARTHOUR, "10");
        y.addFilter(Search.Type.STARTMINUTE, "05");
        List<Course> e;
        e = y.filterCourses();
        for(Course course: e){
            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
        }



    }

    //Tests multiple filters (time and course code)
    @Test
    void filterCourses() {
        System.out.println("Starting multiple filters test");
        Search y = new Search();
        y.addFilter(Search.Type.STARTHOUR, "9");
        y.addFilter(Search.Type.COURSECODE, "ACCT201");
        List<Course> e;
        e = y.filterCourses();
        for(Course course: e){
            assertEquals("ACCT201 A", (course.getCourseCode()) + " " + course.getSectionLetter());
            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
        }

        //Test based on fall
        System.out.println("Starting fall semester test");
        y = new Search();
        e.clear();
        y.addFilter(Search.Type.SEMESTER, "Fall");
        e = y.filterCourses();
        for(Course course: e){
            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
        }

        //Test based on Spring
        System.out.println("Starting spring semester test");
        y = new Search();
        e.clear();
        y.addFilter(Search.Type.SEMESTER, "Spring");
        e = y.filterCourses();
        for(Course course: e){
            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
        }

        //Test based on 12pm
        System.out.println("Starting 12pm test");
        y = new Search();
        e.clear();
        y.addFilter(Search.Type.STARTHOUR, "24");
        e = y.filterCourses();
        for(Course course: e){
            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
        }

        //Test based on starting at minute :05
        System.out.println("Starting :05 test");
        y = new Search();
        e.clear();
        y.addFilter(Search.Type.STARTMINUTE, "5");
        e = y.filterCourses();
        for(Course course: e){
            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
        }

        //Testing day = R
        System.out.println("Starting Thursday test");
        y = new Search();
        e.clear();
        y.addFilter(Search.Type.DAY, "R");
        e = y.filterCourses();
        for(Course course: e){
            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
        }

        //Testing day = Sunday (Shouldn't return anything)
        System.out.println("Starting Sunday test");
        y = new Search();
        e.clear();
        y.addFilter(Search.Type.DAY, "U");
        e = y.filterCourses();
        for(Course course: e){
            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
            assertEquals("", course.getCourseCode());
        }

        //Testing Title
        System.out.println("Starting Title test");
        y = new Search();
        e.clear();
        y.addFilter(Search.Type.TITLE, "DIGITAL PHOTOGRAPHY");
        e = y.filterCourses();
        for(Course course: e){
            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
            assertEquals("COMM245 A", (course.getCourseCode() + " " + course.getSectionLetter()));
        }

    }


    @Test
    void addFilter() {
        Search y = new Search();
        Map<Search.Type, List<String>> testFilters = new HashMap<>();
        testFilters.computeIfAbsent(Search.Type.STARTHOUR, k -> new ArrayList<>()).add("9");
        y.addFilter(Search.Type.STARTHOUR, "9");
        assertEquals(testFilters.get(Search.Type.STARTHOUR), y.getFilters().get(Search.Type.STARTHOUR));

    }

    @Test
    void removeFilter() {
        Search y = new Search();
        Map<Search.Type, List<String>> testFilters = new HashMap<>();
        testFilters.computeIfAbsent(Search.Type.STARTHOUR, k -> new ArrayList<>()).add("9");
        y.addFilter(Search.Type.STARTHOUR, "9");
        y.removeFilter(Search.Type.STARTHOUR, "9");
        testFilters.remove(Search.Type.STARTHOUR);
        assertEquals(testFilters.get(Search.Type.STARTHOUR), y.getFilters().get(Search.Type.STARTHOUR));


    }



    @Test
    void clearFilters() {
        Search y = new Search();
        Map<Search.Type, List<String>> testFilters = new HashMap<>();
        testFilters.computeIfAbsent(Search.Type.STARTHOUR, k -> new ArrayList<>()).add("9");
        y.addFilter(Search.Type.STARTHOUR, "9");
        y.addFilter(Search.Type.SEMESTER, "Spring");
        y.addFilter(Search.Type.COURSECODE, "ACCT201");
        y.clearFilters();
        testFilters.computeIfAbsent(Search.Type.STARTHOUR, k -> new ArrayList<>()).add("9");
        testFilters.computeIfAbsent(Search.Type.SEMESTER, k -> new ArrayList<>()).add("Spring");
        testFilters.computeIfAbsent(Search.Type.COURSECODE, k -> new ArrayList<>()).add("ACCT201");
        testFilters.clear();
        assertEquals(testFilters.get(Search.Type.STARTHOUR), y.getFilters().get(Search.Type.STARTHOUR));
    }
}