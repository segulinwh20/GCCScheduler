package com.classes;

import org.junit.jupiter.api.Test;

import java.util.List;

class FileIOTest {

    @Test
    void readCoursesFromFile() {

        List<Course> c = Search.readCoursesFromFile("data/2020-2021.csv");
        System.out.println();

    }
}