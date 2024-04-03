package com.classes;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    static Student student;
    static Schedule currentSchedule;
    static Search courseSearch;
    static List<Course> searchResults;
    static Log log = new Log();

    public static void main(String[] args) {
        System.out.println("Welcome to GCC Scheduler");
        System.out.println("Start building your schedule or type 'help' for a list of commands.");
        student = new Student();
        Schedule copyForLog;
        cmdTerminal: do {
            if (currentSchedule != null) {
                System.out.println("Currently editing schedule " + currentSchedule.getName());
                List<Course> courses = currentSchedule.getCourses();
                if (!courses.isEmpty()) {
                    System.out.println("Courses:");
                }
                for (Course course : courses) {
                    System.out.println(course.getCourseCode() + " " + course.getSectionLetter());
                }
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
                    if (cmdItems.length < 2) {
                        System.out.println("Cannot create a schedule with no name.");
                        break;
                    } else if (cmdItems.length < 3) {
                        System.out.println("Cannot create a schedule with no name.");
                        break;
                    }
                    currentSchedule = createSchedule(cmdItems[1], cmdItems[2]);
                    student.addSchedule(currentSchedule);
                    log = new Log(new Schedule(currentSchedule));
                    break;
                case "switchSchedule":
                    if (cmdItems.length < 2) {
                        System.out.println("No schedule specified.");
                        break;
                    }
                    currentSchedule = switchSchedule(cmdItems[1]);
                    if(currentSchedule != null) {
                        log = new Log(new Schedule(currentSchedule));
                    }
                    break;
                case "getSchedules":
                    getSchedules();
                    break;
                case "search":
                    search();
                    break;
                case "calendarView":
                    currentSchedule.viewGrid();
                    break;
                case "save":
                    currentSchedule.save();
                    break;
                case "quit":
                    break cmdTerminal;
                case "undo":
                    if(log.undoLast() == null){
                        System.out.println("Nothing to undo");
                    } else {
                        currentSchedule = log.getLast();
                        log.fix();

                        List<Schedule> schedules = student.getSchedules();
                        for (int i = 0; i<schedules.size(); i++) {
                            if (Objects.equals(currentSchedule.getName(), schedules.get(i).getName())) {
                                student.updateSchedule(i, currentSchedule);
                            }
                        }
                    }
                    break;
                case "redo":
                    if(log.redoLast() == null){
                        System.out.println("Nothing to redo");
                    } else {
                        currentSchedule = log.getLast();
                        log.fix();

                        List<Schedule> schedules = student.getSchedules();
                        for (int i = 0; i<schedules.size(); i++) {
                            if (Objects.equals(currentSchedule.getName(), schedules.get(i).getName())) {
                                student.updateSchedule(i, currentSchedule);
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("Invalid Command, Please Re-Enter Command");
            }
        } while(true);
    }

    static void consoleHelp() {
        System.out.println("'createSchedule' 'name' 'semester': This creates a schedule with the given name and semester.");
        System.out.println("'switchSchedule' 'name': This allows you to change which schedule you are editing.");
        System.out.println("'getSchedules': This gives you a list of the schedules that you have made.");
        System.out.println("'search': This will allow you to open the search menu for courses.");
        System.out.println("'calendarView': This will show a weekly calendar view of your current schedule.");
        System.out.println("'save': This will save the course you are currently editing.");
        System.out.println("'undo': Undoes the last change to schedule.");
        System.out.println("'redo': Redoes the last change to schedule.");
        System.out.println("'quit': This will exit GCC Scheduler");
    }

    static void searchHelp() {
        System.out.println("'filterHelp': This will show you a list of the filterTypes");
        System.out.println("'addFilter' 'filterType' 'filter' ...: This adds any number of filters of the specified type to your search filters.");
        System.out.println("'removeFilter' 'filterType' 'filter': This removes the specified filter.");
        System.out.println("'clearFilters': This resets your search filters.");
        System.out.println("'search': This will search for any course matching your search filters.");
        System.out.println("'addCourse' 'courseCode' 'sectionLetter': This will add the specified course to your schedule.");
        System.out.println("'removeCourse' 'courseCode' 'sectionLetter': This will remove the specified course from your schedule.");
        System.out.println("'back': This will return to the schedule management section");
    }

    static Schedule createSchedule(String name, String semester) {
        return new Schedule(name, semester);
    }

    static Schedule switchSchedule(String name) {
        if (Objects.equals(currentSchedule.getName(), name)) {
            System.out.println("You are already editing schedule " + name);
            return null;
        }
        List<Schedule> schedules = student.getSchedules();
        for (Schedule schedule : schedules) {
            if (Objects.equals(name, schedule.getName())) {
                System.out.println("You are now editing schedule " + name);
                return schedule;
            }
        }
        System.out.println("Schedule " + name + " does not exist.");
        return  null;
    }

    static void getSchedules() {
        List<Schedule> schedules = student.getSchedules();
        if (schedules.isEmpty()) {
            System.out.println("You do not have any schedules.");
        }
        for (int i = 0; i < schedules.size(); i++) {
            System.out.println("Schedule " + (i + 1) + ": " + schedules.get(i).getName());
        }
    }

    static void search() {
        System.out.println("Start searching for courses or type 'help' for a list of commands.");
        courseSearch = new Search();
        courseSearch.addFilter(Search.Type.SEMESTER, currentSchedule.getSemester());
        searchTerminal: do {
            searchResults = courseSearch.filterCourses();
            System.out.print("Search: ");
            Scanner scan = new Scanner(System.in);
            String cmdLine = scan.nextLine();
            String[] cmdItems = cmdLine.split(" ");
            switch (cmdItems[0]) {
                case "help":
                    searchHelp();
                    break;
                case "filterHelp":
                    filterHelp();
                    break;
                case "addFilter":
                    if (cmdItems.length <= 2) {
                        System.out.println("No filter specified.");
                        break;
                    }
                    addFilter(cmdItems);
                    break;
                case "removeFilter":
                    if (cmdItems.length <= 2) {
                        System.out.println("No filter specified.");
                        break;
                    }
                    removeFilter(cmdItems);
                    break;
                case "clearFilters":
                    courseSearch.clearFilters();
                    break;
//                case "modifyFilter":
                case "search":
                    if (courseSearch.getFilters().isEmpty()) {
                        System.out.println("You have no filters selected, this will return every class, are you sure?");
                        System.out.println("Y/N");
                        String killSwitch = scan.nextLine();
                        if (Objects.equals(killSwitch, "Y") || Objects.equals(killSwitch, "y")) {
                        } else {continue; }
                    }
                    searchResults = courseSearch.filterCourses();
                    for(Course course: searchResults){
                        System.out.println(course.getCourseCode() +" " + course.getSectionLetter());
                    }
                    break;
                case "addCourse":
                    if (cmdItems.length < 2) {
                        System.out.println("No course specified.");
                        break;
                    }
                    addCourse(cmdItems, searchResults);
                    break;
                case "removeCourse":
                    if (cmdItems.length < 2) {
                        System.out.println("No course specified.");
                        break;
                    }
                    removeCourse(cmdItems);
                    break;
                case "back":
                    log.setProblm();
                    log.redoLast();
                    break searchTerminal;
                default:
                    System.out.println("Invalid Command, Please Re-Enter Command");
            }
            if (!courseSearch.getFilters().isEmpty()) {
                System.out.println(courseSearch.getFilters());
            }
        } while(true);
    }

    static void addFilter(String[] filterParam) {
        Search.Type[] filterType = stringToFilterType(filterParam[1]);
        if (filterType[0] == null) {
            System.out.println("Filter " + filterParam[1] + " does not exist exist.");
            return;
        }
        for (int i = 2; i < filterParam.length; i++) {
            courseSearch.addFilter(filterType[0], filterParam[i]);
        }
    }

    static void removeFilter(String[] filterParam) {
        Search.Type[] filterType = stringToFilterType(filterParam[1]);
        if (filterType[0] == null) {
            System.out.println("Filter " + filterParam[1] + " does not exist exist.");
            return;
        }
        for (int i = 2; i < filterParam.length; i++) {
            courseSearch.removeFilter(filterType[0], filterParam[i]);
        }
    }

    static Search.Type[] stringToFilterType(String stringFilter) {
        return switch (stringFilter) {
            case "startTime" -> new Search.Type[]{Search.Type.TIME};
            case "day" -> new Search.Type[]{Search.Type.DAY};
            case "courseCode" -> new Search.Type[]{Search.Type.COURSECODE};
            case "title" -> new Search.Type[]{Search.Type.TITLE};
            default -> null;
        };
    }

    static void addCourse(String[] courseData, List<Course> searchResults) {
        for (Course searchResult : searchResults) {
            if (Objects.equals(courseData[1], searchResult.getCourseCode()) && Objects.equals(courseData[2].charAt(0), searchResult.getSectionLetter())) {
                currentSchedule.addCourse(searchResult);
                return;
            }
        }
        System.out.println("Course " + courseData[1] + " " + courseData[2] + " does not exist.");
    }

    static void removeCourse(String[] courseData) {
        List<Course> currentCourses = currentSchedule.getCourses();
        for (Course course : currentCourses) {
            if (Objects.equals(courseData[1], course.getCourseCode()) && Objects.equals(courseData[2].charAt(0), course.getSectionLetter())) {
                currentSchedule.removeCourse(course);
                return;
            }
        }
        System.out.println("Course " + courseData[1] + " " + courseData[2] + " is not in your schedule.");
    }

    static void filterHelp() {
        System.out.println("Enter a filterType to learn more:");
        System.out.println("startTime\nday\ncourseCode\ntitle\n");
        helpTerminal: do {
            System.out.print("Command: ");
            Scanner scan = new Scanner(System.in);
            String cmdLine = scan.nextLine();
            String[] cmdItems = cmdLine.split(" ");
            switch (cmdItems[0]) {
                case "startTime":
                    System.out.println("startHours are 8:00 AM to 3:00 PM as well as night classes at 6:00 PM, enter time as 8:00-18:00.");
                    break;
                case "day":
                    System.out.println("days are M T W R F, each corresponding to Monday, Tuesday, Wednesday, Thursday, and Friday respectively.");
                    break;
                case "courseCode":
                    System.out.println("courseCode is the department followed by the courseID, ex. COMP350.");
                    break;
                case "title":
                    System.out.println("title is the name of the course, ex. Software Engineering.");
                    break;
                case "back":
                    break helpTerminal;
                default:
                    System.out.println("Invalid Command, Please Re-Enter Command");
            }
        } while(true);
    }
}