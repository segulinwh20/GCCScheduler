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
        assertEquals(log.getLast(), s);

        log.addAction(s1);
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
        Schedule s = new Schedule("", "");
        Schedule s1 = new Schedule("", "");
        Schedule first = new Schedule("","");
        Log log = new Log(first);

        log.addAction(s);
        log.addAction(s);
        log.addAction(s1);

        assertEquals(log.getLast(), s1); // log.getLast() should equal the last item added
        log.undoLast();
        log.undoLast();
        log.undoLast();
        assertEquals(log.getLast(), first); // should be at the first index

        log.addAction(s1);
        assertNull(log.redoLast()); // should be at the end of redo
        log.undoLast();
        assertEquals(log.getLast(), first); //should be at the first index



    }
}