package com.classes;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    static Student student;
    static Schedule currentSchedule;
    static Search courseSearch;
    static List<Course> searchResults;

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
                case "calendarView":
                    currentSchedule.viewGrid();
                    break;
                case "save":
                    currentSchedule.save();
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
        System.out.println("'calendarView': This will show a weekly calendar view of your current schedule.");
        System.out.println("'save': This will save the course you are currently editing.");
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

    static Schedule createSchedule(String name) {
        return new Schedule(name, null);
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
                    addCourse(cmdItems, searchResults);
                    break;
                case "removeCourse":
                    removeCourse(cmdItems);
                    break;
                case "back":
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
        String[] timeParts = filterParam[2].split(":");
        for (int i = 2; i < filterParam.length; i++) {
            if (filterType.length == 1) {
                courseSearch.addFilter(filterType[0], filterParam[i]);
            } else if (filterType.length == 2) {
                courseSearch.addFilter(filterType[0], timeParts[0]);
                courseSearch.addFilter(filterType[1], timeParts[1]);
            }
        }
    }

    static void removeFilter(String[] filterParam) {
        Search.Type[] filterType = stringToFilterType(filterParam[1]);
        String[] timeParts = filterParam[2].split(":");
        for (int i = 2; i < filterParam.length; i++) {
            if (filterType.length == 1) {
                courseSearch.removeFilter(filterType[0], filterParam[i]);
            } else if (filterType.length == 2) {
                courseSearch.removeFilter(filterType[0], timeParts[0]);
                courseSearch.removeFilter(filterType[1], timeParts[1]);
            }
        }
    }

    static Search.Type[] stringToFilterType(String stringFilter) {
        return switch (stringFilter) {
            case "startTime" -> new Search.Type[]{Search.Type.STARTHOUR, Search.Type.STARTMINUTE};
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
                    startTimeHelp();
                    break;
                case "day":
                    dayHelp();
                    break;
                case "courseCode":
                    courseCodeHelp();
                    break;
                case "title":
                    titleHelp();
                    break;
                case "back":
                    break helpTerminal;
                default:
                    System.out.println("Invalid Command, Please Re-Enter Command");
            }
        } while(true);
    }

    static void startTimeHelp() {
        System.out.println("startHours are 8:00 AM to 3:00 PM as well as night classes at 6:00 PM, enter time as 8:00-18:00.");
    }

    static void dayHelp() {
        System.out.println("days are M T W R F, each corresponding to Monday, Tuesday, Wednesday, Thursday, and Friday respectively.");
    }

    static void courseCodeHelp() {
        System.out.println("courseCode is the department followed by the courseID, ex. COMP350.");
    }

    static void titleHelp() {
        System.out.println("title is the name of the course, ex. Software Engineering.");
    }
}