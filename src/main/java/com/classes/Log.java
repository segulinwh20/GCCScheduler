package com.classes;

import java.util.ArrayList;
import java.util.List;

public class Log {
    private List<Schedule> actions = new ArrayList<Schedule>();
    private int index = 0;
    public Log() {
        Schedule s = new Schedule();
        actions.add(s);
    }

    public Log(Schedule s){

        actions.add(s);
    }

    public Schedule getLast() {

        return actions.get(index);
    }

    public Schedule undoLast() {
        if(index == 0){
            return null;
        }
        index -= 1;

        return actions.get(index);
    }

    public Schedule redoLast() {
        if(index == (actions.size()-1)){
            return null;
        }
        index++;
        return actions.get(index);
    }

    public void addAction(Schedule action) {
        actions.add(action);
    }
}
