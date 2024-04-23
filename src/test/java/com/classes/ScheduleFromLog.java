package com.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleFromLog {
    @Test
    void loadFromLog(){
        Schedule s = new Schedule("", "");
        s.loadFromLog();
        System.out.println(s.getCourses());
    }

    @Test
    void courseToString(){
        Schedule s = new Schedule("", "");
        Course c = s.stringToCourse("ACCT201A");
        assertNotNull(s.stringToCourse("ACCT201A"));

        System.out.println(c.getSemester());

    }


}