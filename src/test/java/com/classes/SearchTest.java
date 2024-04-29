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
    void  WebScraper(){
        //WebScraper();
    }
    @Test
    void search(){
        Search s = new Search();
        assert s!= null;
    }

    //to test time + minute
    @Test
    void  filterTimeTest(){
        //time as 10:05
        System.out.println(" \n Starting 10:05 test \n");
        Search y = new Search();
        y.addFilter(Search.Type.TIME, "10:05");
        List<Course> e;
        e = y.filterCourses();
        for(Course course: e){
            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
        }

        //time as 9:30
        System.out.println("Starting 9:30 test \n");
        y.addFilter(Search.Type.SEMESTER, "Fall");
        y.addFilter(Search.Type.YEAR, "2023");
        y.clearFilters();
        y.addFilter(Search.Type.TIME, "11:30");
        e.clear();
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
        y.addFilter(Search.Type.TIME, "9:00");
        y.addFilter(Search.Type.COURSECODE, "ACCT201");
        List<Course> e;
        e = y.filterCourses();
        for(Course course: e){
            assertEquals("ACCT201 A", (course.getCourseCode()) + " " + course.getSectionLetter());
            System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
        }

        //Test based on 10:05
        System.out.println("Starting fall semester test");
        y = new Search();
        e.clear();
        y.addFilter(Search.Type.SEMESTER, "Fall");
        e = y.filterCourses();
        for(Course course: e){
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
        testFilters.computeIfAbsent(Search.Type.TIME, k -> new ArrayList<>()).add("9:00");
        y.addFilter(Search.Type.TIME, "9:00");
        assertEquals(testFilters.get(Search.Type.TIME), y.getFilters().get(Search.Type.TIME));

    }

    @Test
    void removeFilter() {
        Search y = new Search();
        Map<Search.Type, List<String>> testFilters = new HashMap<>();
        testFilters.computeIfAbsent(Search.Type.TIME, k -> new ArrayList<>()).add("9:00");
        y.addFilter(Search.Type.TIME, "9:00");
        y.removeFilter(Search.Type.TIME, "9:00");
        testFilters.remove(Search.Type.TIME);
        assertEquals(testFilters.get(Search.Type.TIME), y.getFilters().get(Search.Type.TIME));


    }



    @Test
    void clearFilters() {
        Search y = new Search();
        Map<Search.Type, List<String>> testFilters = new HashMap<>();
        testFilters.computeIfAbsent(Search.Type.TIME, k -> new ArrayList<>()).add("9:00");
        y.addFilter(Search.Type.TIME, "9:00");
        y.addFilter(Search.Type.SEMESTER, "Spring");
        y.addFilter(Search.Type.COURSECODE, "ACCT201");
        y.addFilter(Search.Type.YEAR, "2023");
        y.clearFilters();
        testFilters.computeIfAbsent(Search.Type.TIME, k -> new ArrayList<>()).add("9:00");
        testFilters.computeIfAbsent(Search.Type.SEMESTER, k -> new ArrayList<>()).add("Spring");
        testFilters.computeIfAbsent(Search.Type.COURSECODE, k -> new ArrayList<>()).add("ACCT201");
        testFilters.clear();
        assertEquals(testFilters.get(Search.Type.TIME), y.getFilters().get(Search.Type.TIME));
    }

    @Test
    void viewMajor(){
        Search s = new Search();
        s.viewMajorMinor("Computer Science BA");
    }

    @Test
    void getMajorsMinors(){
        ArrayList<String> majMin = Search.getMajorsMinors();
        for(String str: majMin){
            System.out.println(str.substring(8));

        }
    }
}