package com.classes;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    @Test
    void testAddCourse() {
        Schedule scheduleA = new Schedule("myFirstSchedule", "Spring");

        Course courseA = new Course(2020, "Spring", "COMP", 350, 'B',

                "COMP350", "Software Engineering", 3, 30, null, null, "");
        scheduleA.addCourse(courseA);
        List<Course> testCourses = new ArrayList<>();
        testCourses.add(courseA);
        assertEquals(scheduleA.getCourses(), testCourses);
    }

    @Test
    void testRemoveCourse() {
        Schedule scheduleA = new Schedule("myFirstSchedule", "Spring");

        Course courseA = new Course(2020, "Spring", "COMP", 350, 'A',
                "COMP350", "Software Engineering", 3, 30, null, null, "");
        Course courseB = new Course(2020, "Spring", "COMP", 350, 'B',

                "COMP350", "Software Engineering", 3, 30, null, null, "");
        scheduleA.addCourse(courseA);
        scheduleA.addCourse(courseB);
        scheduleA.removeCourse(courseA);
        List<Course> testCourses = new ArrayList<>();
        testCourses.add(courseB);
        assertEquals(scheduleA.getCourses(), testCourses);
    }

    @Test
    void testExport() {
    }

    @Test
    void testViewGrid() {
    }
}