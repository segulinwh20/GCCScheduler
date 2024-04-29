package com.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleFromLog {
    @Test
    void loadFromLog(){
        Schedule s = new Schedule("", "","");
        s.loadFromLog("log");
        System.out.println("Name: " + s.getName());
        System.out.println("Semester: "+ s.getSemester());
    }

    @Test
    void courseToString(){
        Schedule s = new Schedule("", "","");
        Course c = s.stringToCourse("ACCT201A");
        assertNotNull(s.stringToCourse("ACCT201A"));

        System.out.println(c.getSemester());

    }


}