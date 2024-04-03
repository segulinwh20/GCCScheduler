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
    static RawLog rawlog = new RawLog();

    public static void main(String[] args) {
        System.out.println("Welcome to GCC Scheduler");
        System.out.println("Start building your schedule or type 'help' for a list of commands.");
        student = new Student();
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
                    RawLog.logger.info("Opened Schedule Help Menu");
                    consoleHelp();
                    break;
                case "createSchedule":
                    RawLog.logger.info("Creating New Schedule");
                    if (cmdItems.length < 2) {
                        RawLog.logger.warning("Tried to Create a Schedule With No Name");
                        System.out.println("Cannot create a schedule with no name.");
                        break;
                    } else if (cmdItems.length < 3) {
                        RawLog.logger.warning("Tried to Create a Schedule With No Name");
                        System.out.println("Cannot create a schedule with no name.");
                        break;
                    }
                    currentSchedule = createSchedule(cmdItems[1], cmdItems[2]);
                    student.addSchedule(currentSchedule);
                    log = new Log(new Schedule(currentSchedule));
                    RawLog.logger.info("Schedule Successfully Created");
                    break;
                case "switchSchedule":
                    RawLog.logger.info("Switching to new Schedule");
                    if (cmdItems.length < 2) {
                        RawLog.logger.warning("No Schedule was Specified");
                        System.out.println("No schedule specified.");
                        break;
                    }
                    currentSchedule = switchSchedule(cmdItems[1]);
                    if(currentSchedule != null) {
                        log = new Log(new Schedule(currentSchedule));
                    }
                    RawLog.logger.info("Successfully Switched to new Schedule");
                    break;
                case "getSchedules":
                    RawLog.logger.info("Displayed All Schedules");
                    getSchedules();
                    break;
                case "search":
                    RawLog.logger.info("Opened Search Menu");
                    search();
                    break;
                case "calendarView":
                    RawLog.logger.info("Opened Calender View");
                    currentSchedule.viewGrid();
                    break;
                case "save":
                    RawLog.logger.info("Saved Schedule");
                    currentSchedule.save();
                    break;
                case "quit":
                    RawLog.logger.info("Exiting Program");
                    break cmdTerminal;
                case "undo":
                    if(log.undoLast() == null){
                        RawLog.logger.warning("Tried to Undo Nothing");
                        System.out.println("Nothing to undo");
                    } else {
                        currentSchedule = log.getLast();
                        log.setProblems();

                        List<Schedule> schedules = student.getSchedules();
                        for (int i = 0; i<schedules.size(); i++) {
                            if (Objects.equals(currentSchedule.getName(), schedules.get(i).getName())) {
                                student.updateSchedule(i, currentSchedule);
                            }
                        }
                    }
                    RawLog.logger.info("Last Action Undone");
                    break;
                case "redo":
                    if(log.redoLast() == null){
                        RawLog.logger.warning("Tried to Redo Nothing");
                        System.out.println("Nothing to redo");
                    } else {
                        currentSchedule = log.getLast();
                        log.setProblems();

                        List<Schedule> schedules = student.getSchedules();
                        for (int i = 0; i<schedules.size(); i++) {
                            if (Objects.equals(currentSchedule.getName(), schedules.get(i).getName())) {
                                student.updateSchedule(i, currentSchedule);
                            }
                        }
                    }
                    RawLog.logger.info("Last Action Redone");
                    break;
                default:
                    RawLog.logger.info("Invalid Command Entered in Schedule Menu");
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
                    RawLog.logger.info("Opened Search Help Menu");
                    searchHelp();
                    break;
                case "filterHelp":
                    RawLog.logger.info("Opened Filter Help Menu");
                    filterHelp();
                    break;
                case "addFilter":
                    RawLog.logger.info("Add Filter");
                    if (cmdItems.length <= 2) {
                        RawLog.logger.warning("No filter specified");
                        System.out.println("No filter specified.");
                        break;
                    }
                    RawLog.logger.info("Successfully Added Filter");
                    addFilter(cmdItems);
                    break;
                case "removeFilter":
                    RawLog.logger.info("Remove Filter");
                    if (cmdItems.length <= 2) {
                        RawLog.logger.warning("No filter specified");
                        System.out.println("No filter specified.");
                        break;
                    }
                    RawLog.logger.info("Successfully Removed Filter");
                    removeFilter(cmdItems);
                    break;
                case "clearFilters":
                    RawLog.logger.info("Cleared Filters");
                    courseSearch.clearFilters();
                    break;
//                case "modifyFilter":
                case "search":
                    RawLog.logger.info("Searching for courses");
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
                    RawLog.logger.info("Add Course");
                    if (cmdItems.length < 2) {
                        RawLog.logger.warning("No course specified");
                        System.out.println("No course specified.");
                        break;
                    }
                    addCourse(cmdItems, searchResults);
                    break;
                case "removeCourse":
                    RawLog.logger.info("Remove Course");
                    if (cmdItems.length < 2) {
                        RawLog.logger.warning("No course specified");
                        System.out.println("No course specified.");
                        break;
                    }
                    removeCourse(cmdItems);
                    break;
                case "back":
                    RawLog.logger.info("Going Back to Schedule Menu");
                    log.setErrorIndex();
                    log.redoLast();
                    break searchTerminal;
                default:
                    RawLog.logger.warning("Invalid Command Entered in Search Menu");
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
            RawLog.logger.warning("Attempted to Add Filter That Does Not Exist");
            System.out.println("Filter " + filterParam[1] + " does not exist exist.");
            return;
        }
        for (int i = 2; i < filterParam.length; i++) {
            RawLog.logger.info("Filter Successfully Added");
            courseSearch.addFilter(filterType[0], filterParam[i]);
        }
    }

    static void removeFilter(String[] filterParam) {
        Search.Type[] filterType = stringToFilterType(filterParam[1]);
        if (filterType[0] == null) {
            RawLog.logger.warning("Attempted to Remove Filter That Does Not Exist");
            System.out.println("Filter " + filterParam[1] + " does not exist exist.");
            return;
        }
        for (int i = 2; i < filterParam.length; i++) {
            RawLog.logger.info("Filter Successfully Removed");
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
                RawLog.logger.info("Successfully Added Course");
                return;
            }
        }
        RawLog.logger.warning("Attempting to add a course that does not exist");
        System.out.println("Course " + courseData[1] + " " + courseData[2] + " does not exist.");
    }

    static void removeCourse(String[] courseData) {
        List<Course> currentCourses = currentSchedule.getCourses();
        for (Course course : currentCourses) {
            if (Objects.equals(courseData[1], course.getCourseCode()) && Objects.equals(courseData[2].charAt(0), course.getSectionLetter())) {
                currentSchedule.removeCourse(course);
                RawLog.logger.info("Course Successfully Removed");
                return;
            }
        }
        RawLog.logger.warning("Course Not in Schedule");
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
                    RawLog.logger.info("Displaying Start Time Filter Info");
                    System.out.println("startHours are 8:00 AM to 3:00 PM as well as night classes at 6:00 PM, enter time as 8:00-18:00.");
                    break;
                case "day":
                    RawLog.logger.info("Displaying Day Filter Info");
                    System.out.println("days are M T W R F, each corresponding to Monday, Tuesday, Wednesday, Thursday, and Friday respectively.");
                    break;
                case "courseCode":
                    RawLog.logger.info("Displaying Course Code Filter Info");
                    System.out.println("courseCode is the department followed by the courseID, ex. COMP350.");
                    break;
                case "title":
                    RawLog.logger.info("Displaying Title Filter Info");
                    System.out.println("title is the name of the course, ex. Software Engineering.");
                    break;
                case "back":
                    RawLog.logger.info("Going Back to Search Menu");
                    break helpTerminal;
                default:
                    RawLog.logger.warning("Invalid Filter Entered");
                    System.out.println("Invalid Command, Please Re-Enter Command");
            }
        } while(true);
    }
}