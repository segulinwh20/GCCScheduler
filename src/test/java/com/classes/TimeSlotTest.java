package com.classes;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    @Test
    void testConflicts() {
        TimeSlot a = new TimeSlot('M', 1, 00, 1, 50);
        TimeSlot b = new TimeSlot('W', 1, 00, 1, 50);
        TimeSlot c = new TimeSlot('M', 1, 30, 2, 30);
        TimeSlot d = new TimeSlot('M', 1, 51, 2, 50);
        TimeSlot e = new TimeSlot('M', 2, 00, 2, 50);

        assertFalse(TimeSlot.conflicts(a, b));
        assertTrue(TimeSlot.conflicts(a, c));
        assertFalse(TimeSlot.conflicts(a, d));
        assertFalse(TimeSlot.conflicts(a, e));
    }

    @Test
    void testTimeFallsInRange() {
        TimeSlot t = new TimeSlot('M', 1, 00, 1, 50);

        assertFalse(t.timeFallsInRange(2, 00));
        assertTrue(t.timeFallsInRange(1, 30));
        assertTrue(t.timeFallsInRange(1, 00));
        assertTrue(t.timeFallsInRange(1, 50));
        assertFalse(t.timeFallsInRange(1, 51));
    }

    @Test
    void testConflictsList() {
        TimeSlot a = new TimeSlot('M', 1, 00, 1, 50);
        TimeSlot b = new TimeSlot('W', 1, 00, 1, 50);
        TimeSlot c = new TimeSlot('M', 1, 30, 2, 30);
        TimeSlot d = new TimeSlot('M', 1, 51, 2, 50);
        TimeSlot e = new TimeSlot('M', 2, 00, 2, 50);
        List<TimeSlot> l1 = new LinkedList<>();
        l1.add(a);
        l1.add(b);
        List<TimeSlot> l2 = new LinkedList<>();
        l2.add(c);
        l2.add(d);
        l2.add(e);

        assertTrue(TimeSlot.conflicts(l1, l2));

        l2.remove(c);

        assertFalse(TimeSlot.conflicts(l1, l2));

        System.out.println(a);
    }
}