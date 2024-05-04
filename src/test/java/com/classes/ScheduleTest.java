package com.classes;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    @Test
    void testAddCourse() {
        Schedule scheduleA = new Schedule("myFirstSchedule", "Spring", "2023");

        Course courseA = new Course("2020", "Spring", "COMP", 350, 'B',

                "COMP350", "Software Engineering", 3, 30, null, null, "");
        scheduleA.addCourse(courseA);
        List<Course> testCourses = new ArrayList<>();
        testCourses.add(courseA);
        assertEquals(scheduleA.getCourses(), testCourses);
    }

    @Test
    void testAddEvent(){
        Schedule scheduleA = new Schedule("Baby's First Schedule", "Spring", "2023");
        TimeSlot t = new TimeSlot('M', 8,0,9,0);
        List<TimeSlot> tList = new LinkedList<>();
        tList.add(t);
        Event eventA = new Event(new StringBuilder("Title"), tList);
        scheduleA.addEvent(eventA);
        List<Event> testEvent = new ArrayList<>();
        testEvent.add(eventA);
        assertEquals(scheduleA.getEvents(), testEvent);
    }
    @Test
    void testRemoveEvent(){
        TimeSlot t = new TimeSlot('M', 8,0,9,0);
        TimeSlot s = new TimeSlot('W',9,0,10,0);
        List<TimeSlot> tList = new LinkedList<>();
        tList.add(t);
        List<TimeSlot> sList = new LinkedList<>();
        sList.add(s);
        Schedule scheduleA = new Schedule("myFirstSchedule", "Spring", "2023");

        Event eventA = new Event(new StringBuilder("Title"), tList);
        Event eventB = new Event(new StringBuilder("yolo"), sList);

        scheduleA.addEvent(eventA);
        scheduleA.addEvent(eventB);
        scheduleA.removeEvent(eventA);
        List<Event> testEvent = new ArrayList<>();
        testEvent.add(eventB);
        assertEquals(scheduleA.getEvents(), testEvent);

    }

    @Test
    void testConflictEventCourse(){
        TimeSlot t = new TimeSlot('M', 8,0,9,0);
        TimeSlot s = new TimeSlot('W',9,0,10,0);
        List<TimeSlot> tList = new LinkedList<>();
        tList.add(t);
        List<TimeSlot> sList = new LinkedList<>();
        sList.add(s);
        Schedule scheduleA = new Schedule("myFirstSchedule", "Spring", "2023");

        Course courseA = new Course("2023", "Fall", "ACCT", 201, 'A', "ACCT201", "PRINCPLES OF ACCOUNTING I", 3, 1, tList, null, "");
        Event eventA = new Event(new StringBuilder("Title"), tList);
        Event eventB = new Event(new StringBuilder("Title"), sList);


        scheduleA.addCourse(courseA);
        scheduleA.addEvent(eventB);
        scheduleA.addEvent(eventA);
        List<Event> testEvent = new ArrayList<>();
        testEvent.add(eventB);
        assertEquals(scheduleA.getEvents(), testEvent);

    }



    @Test
    void testRemoveCourse() {
        TimeSlot t = new TimeSlot('M', 8,0,9,0);
        TimeSlot s = new TimeSlot('W',9,0,10,0);
        List<TimeSlot> tList = new LinkedList<>();
        tList.add(t);
        List<TimeSlot> sList = new LinkedList<>();
        sList.add(s);
        Schedule scheduleA = new Schedule("myFirstSchedule", "Spring", "2023");

        Course courseA = new Course("2023", "Fall", "ACCT", 201, 'A', "ACCT201", "PRINCPLES OF ACCOUNTING I", 3, 1, tList, null, "");
        Course courseB = new Course("2023", "Fall", "ACCT", 201, 'A', "ACCT202", "PRINCPLES OF ACCOUNTING I", 3, 1, sList, null, "");

        scheduleA.addCourse(courseA);
        scheduleA.addCourse(courseB);
        scheduleA.removeCourse(courseA);
        List<Course> testCourses = new ArrayList<>();
        testCourses.add(courseB);
        assertEquals(scheduleA.getCourses(), testCourses);
    }

    @Test
    void testExport() {
        Schedule A = new Schedule("","","");
        A.export();
    }

    @Test
    void testViewGrid() {
        Schedule A = new Schedule("","","");
        A.viewGrid();
    }
}