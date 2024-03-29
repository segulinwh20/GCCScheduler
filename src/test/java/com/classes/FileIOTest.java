package com.classes;

import org.junit.jupiter.api.Test;

import java.util.List;

class FileIOTest {

    @Test
    void readCoursesFromFile() {

        List<Course> c = Search.readCoursesFromFile("data/2020-2021.csv");
      
        System.out.println(c.get(0).getCourseCode());
        System.out.println(c.get(0).getTimes());

        for (Course course : c) {
            System.out.println(course.toCSVFormat());
        }
        System.out.println(c.get(0).toCSVFormat());
    }
}