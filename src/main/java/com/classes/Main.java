package com.classes;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    static Student student;
    static Schedule currentSchedule;
    static Search courseSearch;


    public static void main(String[] args) {
        System.out.println("Welcome to GCC Scheduler");
        System.out.println("Start building your schedule or type 'help' for a list of commands.");
        student = new Student();
        cmdTerminal: do {
            if (currentSchedule != null) {
                System.out.println("Currently editing schedule " + currentSchedule.getName());
            }
            List<Course> courses = currentSchedule.getCourses();
            for (int i = 0; i < ; i++) {

            }
            System.out.print("Command: ");
            Scanner scan = new Scanner(System.in);
            String cmdLine = scan.nextLine();
            String[] cmdItems = cmdLine.split(" ");
            switch (cmdItems[0]) {
                case "help":
                    consoleHelp();
                    break;
                case "createSchedule":
                    currentSchedule = createSchedule(cmdItems[1]);
                    student.addSchedule(currentSchedule);
                    break;
                case "switchSchedule":
                    currentSchedule = switchSchedule(cmdItems[1]);
                    break;
                case "getSchedules":
                    getSchedules();
                    break;
                case "search":
                    search();
                    break;
                case "quit":
                    break cmdTerminal;
                default:
                    System.out.println("Invalid Command, Please Re-Enter Command");
            }
        } while(true);
    }

    static void consoleHelp() {
        System.out.println("'createSchedule' 'name': This creates a schedule with the given name.");
        System.out.println("'switchSchedule' 'name': This allows you to change which schedule you are editing.");
        System.out.println("'getSchedules': This gives you a list of the schedules that you have made.");
        System.out.println("'search': This will allow you to open the search menu for courses.");
    }

    static void searchHelp() {
        System.out.println();
    }

    static Schedule createSchedule(String name) {
        return new Schedule(name, null);
    }

    static Schedule switchSchedule(String name) {
        if (Objects.equals(currentSchedule.getName(), name)) {
            System.out.println("You are already editing schedule " + name);
            return null;
        }
        List<Schedule> schedules = student.getSchedules();
        for (int i = 0; i < schedules.size(); i++) {
            if (Objects.equals(name, schedules.get(i).getName())) {
                System.out.println("You are now editing schedule " + name);
                return schedules.get(i);
            }
        }
        System.out.println("Schedule " + name + " does not exist.");
        return  null;
    }

    static void getSchedules() {
        List<Schedule> schedules = student.getSchedules();
        if (schedules.size() == 0) {
            System.out.println("You do not have any schedules.");
        }
        for (int i = 0; i < schedules.size(); i++) {
            System.out.println("Schedule " + (i + 1) + ": " + schedules.get(i).getName());
        }
    }

    static void search() {
        System.out.println("Start searching for courses or type 'help' for a list of search filters.");
        courseSearch = new Search();
        searchTerminal: do {
            if (courseSearch.getFilters().size() > 0) {
                System.out.println(courseSearch.getFilters());
            }
            System.out.print("Search: ");
            Scanner scan = new Scanner(System.in);
            String cmdLine = scan.nextLine();
            String[] cmdItems = cmdLine.split(" ");
            switch (cmdItems[0]) {
                case "help":
                    searchHelp();
                    break;
                case "addFilter":
                    addFilter(cmdItems);
                    break;
                case "removeFilter":
                    removeFilter(cmdItems);
                    break;
                case "clearFilters":
                    courseSearch.clearFilters();
                    break;
//                case "modifyFilter":
                case "search":
                    if (courseSearch.getFilters().size() == 0) {
                        System.out.println("You have no filters selected, this will return every class, are you sure?");
                        System.out.println("Y/N");
                        String killSwitch = scan.nextLine();
                        if (killSwitch == "Y" || killSwitch == "y") {
                            courseSearch.filterCourses(courseSearch.getCourses());
                        } else {continue; }
                    }
                    courseSearch.filterCourses(courseSearch.getCourses());
                    break;
                case "quit":
                    break searchTerminal;
                default:
                    System.out.println("Invalid Command, Please Re-Enter Command");
            }
        } while(true);
    }

    static void addFilter(String[] filterParam) {
        String filterType = filterParam[1];
        for (int i = 2; i < filterParam.length; i++) {
            courseSearch.addFilter(filterType, filterParam[i]);
        }
    }

    static void removeFilter(String[] filterParam) {
        String filterType = filterParam[1];
        for (int i = 2; i < filterParam.length; i++) {
            courseSearch.removeFilter(filterType, filterParam[i]);
        }
    }
}