package com.classes;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogTest {

    @Test
    void getLast() {
        Schedule s = new Schedule("", "");
        Schedule s1 = new Schedule("", "");
        Log log = new Log(s);
        log.addAction(s1);

        assertEquals(log.getLast(), s);
        assertEquals(log.getLast(), s1);
    }

    @Test
    void undoLast() {
        Schedule s1 = new Schedule("", "");
        Log log = new Log(s1);
        Schedule s = new Schedule("", "");

        assertNull(log.undoLast()); // when last item is not present, return null

        log.addAction(s);

        assertNotNull(log.undoLast()); // if action is added, there is an action behind it
        assertEquals(log.getLast(), s1); // getLast() returns the expected schedule


    }

    @Test
    void redoLast() {
        Schedule s1 = new Schedule("", "");
        Log log = new Log(s1);
        Schedule s2 = new Schedule("", "");

        assertNull(log.redoLast()); // if you try to redo an action that does not exist it returns null
        log.addAction(s2);
        assertNull(log.redoLast()); // adding an action doesn't allow to redo an action

        log.undoLast(); // at s1
        assertNotNull(log.redoLast()); // shouldn't be at the end of the list after an undo (at s2)
        log.undoLast(); // at s1
        assertEquals(log.redoLast(), s2); // a redo should cancel an undo (at s2)
    }

    @Test
    void addAction() {
    }
}