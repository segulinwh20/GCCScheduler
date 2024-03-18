package com.classes;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    @Test
    void testAddCourse() {
        Schedule scheduleA = new Schedule("myFirstSchedule", "Spring");
        Course courseA = new Course(12345, "Software Engineering", 3, "COMP",
                null, "350", "Spring", null, null, 'A', 30);
        scheduleA.addCourse(courseA);
        List<Course> testCourses = new ArrayList<>();
        testCourses.add(courseA);
        assertEquals(scheduleA.getCourses(), testCourses);
    }

    @Test
    void testRemoveCourse() {
        Schedule scheduleA = new Schedule("myFirstSchedule", "Spring");
        Course courseA = new Course(12345, "Software Engineering", 3, "COMP",
                null, "350", "Spring", null, null, 'A', 30);
        Course courseB = new Course(12345, "Software Engineering", 3, "COMP",
                null, "350", "Spring", null, null, 'B', 30);
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