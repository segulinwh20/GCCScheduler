package com.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

    Search s = new Search();


    @Test
    void search(){

    }
    @Test
    void filterCourses() {
        s.addFilter("day", "W");
        s.addFilter("day", "T");
        System.out.println(s.getFilters());
        s.filterCourses(s.getCourses());
        for(Course course: s.getCourses()){
            System.out.println(course.getCourseCode() +" " + course.getSectionLetter());
        }

    }

    @Test
    void addFilter() {
//        s.addFilter("courseCode", "ACCT 201");
//        System.out.println(s.getFilters());


    }

    @Test
    void removeFilter() {
//        s.removeFilter("courseCode", "ACCT 201");
//        System.out.println(s.getFilters());
    }

    @Test
    void modifyFilter() {
//        s.modifyFilter("courseCode", "CHEM 105");
//        System.out.println(s.getFilters());
    }

    @Test
    void clearFilters() {
//        s.clearFilters();
//        System.out.println(s.getFilters());
    }
}