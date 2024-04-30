package com.classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    private static List<Schedule> actions = new ArrayList<Schedule>();
    private static int index = 0;
    private static Schedule problemAction;
    private static int problemIndex = 0;
    public static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Log(){

    }
    public Log(String location){
        try {
            FileHandler fileHandler = new FileHandler(location);
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.setUseParentHandlers(false);
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    public Log(Schedule s){
        resetLog();
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
        if(index == (actions.size()-1) || actions.isEmpty()){
            return null;
        }
        index++;
        return actions.get(index);
    }

    public void addAction(Schedule action) {
        if(actions.isEmpty()){
            actions.add(action);
        } else {
            if (index != actions.size() - 1) { // if index isn't at the end of the log, remove everything after it
                while (index != actions.size() - 1) {
                    actions.removeLast();
                }
            }
            index++;
            actions.add(action);
        }
    }
    public void resetLog(){
        actions.clear();
        index = 0;
    }
    public void setProblemIndex(){
        problemIndex = index;
    }
    public void setProblemAction(){
        problemAction = new Schedule(actions.get(problemIndex));
    }
    public void setErrorIndex(){
        if(problemAction == null){
            return;
        }
        actions.set(problemIndex, problemAction);
    }
    public void setProblems(){
        setProblemIndex();
        setProblemAction();
    }
}
