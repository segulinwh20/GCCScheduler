package com.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    @Test
    void conflicts() {
        TimeSlot a = new TimeSlot('M', 1, 00, 1, 50);
        TimeSlot b = new TimeSlot('W', 1, 00, 1, 50);
        TimeSlot c = new TimeSlot('M', 1, 30, 2, 30);
        TimeSlot d = new TimeSlot('M', 1, 51, 2, 50);
        TimeSlot e = new TimeSlot('M', 2, 00, 2, 50);

        System.out.println(TimeSlot.conflicts(a, b));
        System.out.println(TimeSlot.conflicts(a, c));
        System.out.println(TimeSlot.conflicts(a, d));
        System.out.println(TimeSlot.conflicts(a, e));

        assertFalse(TimeSlot.conflicts(a, b));
        assertTrue(TimeSlot.conflicts(a, c));
        assertFalse(TimeSlot.conflicts(a, d));
        assertFalse(TimeSlot.conflicts(a, e));
    }

    void timeFallsInRange() {
        TimeSlot t = new TimeSlot('M', 1, 00, 1, 50);

        assertFalse(t.timeFallsInRange(2, 00));
        assertTrue(t.timeFallsInRange(1, 30));
    }

    @Test
    void testConflicts() {
    }
}