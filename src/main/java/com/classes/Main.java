package com.classes;


import com.sun.net.httpserver.Authenticator;

import java.io.IOException;

import java.sql.Array;
import java.util.*;

public class Main {
    static Student student;
    static Schedule currentSchedule;
    static Search courseSearch;
    static List<Course> courseList;
    static List<Course> searchResults;
    static Log log = new Log("data/log.log");
    static Account act = new Account();

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to GCC Scheduler");
        Login();

        System.out.println("Start building your schedule or type 'help' for a list of commands.");
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
                List<Event> events = currentSchedule.getEvents();
                if(!events.isEmpty()){
                    System.out.println("Events: ");
                    for (Event event: events){
                        System.out.println(event.getTitle());
                    }
                }

            }
            System.out.print("Command: ");
            Scanner scan = new Scanner(System.in);
            String cmdLine = scan.nextLine();
            String[] cmdItems = cmdLine.split(" ");
            switch (cmdItems[0]) {
                case "help":
                    Log.logger.info("Opened Schedule Help Menu");
                    consoleHelp();
                    break;
                case "event":
                    Log.logger.info("Creating New Event");
                    if(currentSchedule != null) {
                        createEvent();
                    } else {
                        System.out.println("Create a schedule before adding an event");
                    }

                    break;
                case "export":
                    Log.logger.info("Exporting");
                    export();
                    break;
                case "createSchedule":
                    Log.logger.info("Making New Schedule");
                    if (cmdItems.length < 2) {
                        Log.logger.warning("Tried to Create a Schedule With No Name");
                        System.out.println("Cannot create a schedule with no name.");
                        break;
                    }
                    currentSchedule = createSchedule(cmdItems);
                    student.addSchedule(currentSchedule);
                    log = new Log(new Schedule(currentSchedule));
                    new Log("data/" + currentSchedule.getName() + ".log");
                    Log.logger.info("Successfully Created " + currentSchedule.getName() + " " + currentSchedule.getSemester() + " " + currentSchedule.getYear());
                    break;
                case "switchSchedule":
                    Log.logger.info("Switching to new Schedule");
                    if (cmdItems.length < 2) {
                        Log.logger.warning("No Schedule was Specified");
                        System.out.println("No schedule specified.");
                        break;
                    }
                    currentSchedule = switchSchedule(cmdItems);
                    if(currentSchedule != null) {
                        log = new Log(new Schedule(currentSchedule));
                    }
                    Log.logger.info("Successfully Switched " + currentSchedule.getName());
                    new Log("data/" + currentSchedule.getName() + ".log");
                    break;
                case "getSchedules":
                    Log.logger.info("Displayed All Schedules");
                    getSchedules();
                    break;
                case "search":
                    if (currentSchedule == null) {
                        Log.logger.info("No Schedule Selected");
                        System.out.println("Cannot search without a schedule.");
                        break;
                    }
                    Log.logger.info("Opened Search Menu");
                    search();
                    break;
                case "calendarView":
                    Log.logger.info("Opened Calender View");
                    currentSchedule.viewGrid();
                    break;
                case "save":
                    if (currentSchedule.getCourses().isEmpty()){
                        Log.logger.info("Failed to save schedule");
                        System.out.println("Cannot save a schedule with no courses.");
                        break;
                    }
                    currentSchedule.save();
                    Log.logger.info("Saved Schedule");
                    System.out.println(currentSchedule.getName() + " has been saved.");
                    break;
                case "quit":
                    Log.logger.info("Exiting Program");
                    break cmdTerminal;
                case "undo":
                    if(log.undoLast() == null){
                        Log.logger.warning("Tried to Undo Nothing");
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
                        Log.logger.info("Last Action Undone");
                    }
                    break;
                case "redo":
                    if(log.redoLast() == null){
                        Log.logger.warning("Tried to Redo Nothing");
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
                        Log.logger.info("Last Action Redone");
                    }
                    break;
                case "load":
                    Log.logger.info("Loading new Schedule");
                    if (cmdItems.length < 2) {
                        Log.logger.warning("No Schedule was Specified");
                        System.out.println("No schedule specified.");
                        break;
                    }
                    boolean load = loadSchedule(cmdItems[1]);
                    if (!load) {
                        Log.logger.info("Failed To Load Schedule");
                        break;
                    }
                    if(currentSchedule != null) {
                        log = new Log(new Schedule(currentSchedule));
                    }
                    System.out.println("Successfully loaded schedule.");
                    Log.logger.info("Successfully loaded Schedule");
                    break;
                case "loadLog":
                    System.out.println("Enter the schedule you want to load");
                    String name = scan.nextLine();

                    if(currentSchedule == null) {
                        currentSchedule = new Schedule("", "", "");
                    }

                    if(currentSchedule.loadFromLog(name)){
                        System.out.println("Successfully loaded schedule.");
                        Log.logger.info("Loaded " + name + " from log");
                        //new Log("data/"+name+".log");
                    } else {
                        System.out.println("Failed to load schedule");
                    }
                    break;
                case "viewSupportedMajors":
                    ArrayList<String> list = Search.getMajorsMinors();
                    for(String str: list){
                        System.out.println(str.substring(8));
                    }
                    break;
                case "viewMajor":
                    System.out.print("Enter the schedule you want to load: ");
                    String maj = scan.nextLine();
                    Search.viewMajorMinor(maj);
                    break;
                default:
                    Log.logger.info("Invalid Command Entered in Schedule Menu");
                    System.out.println("Invalid Command, Please Re-Enter Command");
            }
        } while(true);
    }

    static boolean loadSchedule(String name) {
        List<Course> coursesToAdd = Search.readCoursesFromFile("data/" + name + ".csv");
        if (coursesToAdd == null) {
            System.out.println("Schedule " + name + " does not exist.");
            return false;
        }
        currentSchedule = new Schedule(name, coursesToAdd.getFirst().getSemester(), coursesToAdd.getFirst().getYear());
        student.addSchedule(currentSchedule);
        for (int i = 0; i < coursesToAdd.size(); i++) {
            String[] courseParams = new String[3];
            courseParams[1] = coursesToAdd.get(i).getCourseCode();
            courseParams[2] = String.valueOf(coursesToAdd.get(i).getSectionLetter());
            addCourse(courseParams, coursesToAdd);
        }
        return true;
    }

    static boolean studentBuilder(String[] studentParams) {
        if (studentParams.length <= 2) {
            return false;
        }
        student = new Student(studentParams);
        return true;
    }

    static void consoleHelp() {
        System.out.println("'createSchedule' 'name' : This creates a schedule with the given name.");
        System.out.println("'switchSchedule' 'name': This allows you to change which schedule you are editing.");
        System.out.println("'getSchedules': This gives you a list of the schedules that you have made.");
        System.out.println("'search': This will allow you to open the search menu for courses.");
        System.out.println("'calendarView': This will show a weekly calendar view of your current schedule.");
        System.out.println("'save': This will save the course you are currently editing.");
        System.out.println("'load' 'schedule': This will load the specified schedule.");
        System.out.println("'undo': This will undo the last change to schedule.");
        System.out.println("'redo': This will redo the last change to schedule.");
        System.out.println("'quit': This will exit GCC Scheduler");
        System.out.println("'viewSupportedMajors': Lists the major requirements that are currently available");
        System.out.println("'viewMajor': Opens a pdf version of major the requirement sheets");
        System.out.println("'export': This will export your schedule in calendar format as a PDF");
        System.out.println("'event': This brings you to the event menu");
    }

    static void searchHelp() {
        System.out.println("'filterHelp': This will show you a list of the filterTypes");
        System.out.println("'addFilter' 'filterType' 'filter' ...: This adds any number of filters of the specified type to your search filters.");
        System.out.println("'removeFilter' 'filterType' 'filter': This removes the specified filter.");
        System.out.println("'clearFilters': This resets your search filters.");
        System.out.println("'search': This will search for any course matching your search filters.");
        System.out.println("'addCourse' 'courseCode' 'sectionLetter': This will add the specified course to your schedule. Enter 0 for sectionLetter if there is none.");
        System.out.println("'removeCourse' 'courseCode' 'sectionLetter': This will remove the specified course from your schedule. Enter 0 for sectionLetter if there is none.");
        System.out.println("'back': This will return to the schedule management section");
    }

    static void eventHelp(){
        System.out.println("'newEvent': This will start the process to create a new event, prompting you to type a title as well as event times. \n Format: 'newEvent: [title]'");
        System.out.println("'removeEvent': This will remove the event matching the title given. Format: 'removeEvent: [title]'");
    }

    static Schedule createSchedule(String[] name) {
        StringBuilder scheduleName = new StringBuilder();
        scheduleName.append(name[1]);
        for (int i = 2; i < name.length; i++) {
            scheduleName.append(" ").append(name[i]);
        }
        Scanner scan = new Scanner(System.in);
        do {
            System.out.print("Enter Semester: ");
            String semester = scan.nextLine();
            if (semester.equals("Fall") || semester.equals("Spring")) {
                do{
                    System.out.println("Enter Year: ");
                    String year = scan.nextLine();
                    if(year.equals("2023") || year.equals("2024") || year.equals("2025")){
                        return new Schedule(String.valueOf(scheduleName), semester, year);
                    }
                } while(true);
            }
            System.out.println(semester + " is not a valid semester.");
        } while (true);
    }

    static Schedule switchSchedule(String[] name) {
        StringBuilder scheduleName = new StringBuilder();
        scheduleName.append(name[1]);
        for (int i = 2; i < name.length; i++) {
            scheduleName.append(" ").append(name[i]);
        }
        if (Objects.equals(currentSchedule.getName(), String.valueOf(scheduleName))) {
            System.out.println("You are already editing schedule " + scheduleName);
            return currentSchedule;
        }
        List<Schedule> schedules = student.getSchedules();
        for (Schedule schedule : schedules) {
            if (Objects.equals((String.valueOf(scheduleName)), schedule.getName())) {
                System.out.println("You are now editing schedule " + scheduleName);
                return schedule;
            }
        }
        System.out.println("Schedule " + scheduleName + " does not exist.");
        return currentSchedule;
    }
    static void export() throws IOException {
        currentSchedule.export();
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

    static void createEvent(){
        eventTerminal: do {
            System.out.println("event: ");
            Scanner scan = new Scanner(System.in);
            String cmdLine = scan.nextLine();
            String[] cmdItems = cmdLine.split(" ");
            switch(cmdItems[0]){
                case "help":
                    Log.logger.info("Opened Event Help Menu");
                    eventHelp();
                    break;
                case "newEvent":
                    Log.logger.info("Creating New Event");

                    newEvent();
                    break;
                case "removeEvent":
                    Log.logger.info("Remove Event");
                    if(cmdItems.length<= 1){
                        Log.logger.warning("No title specified for removal");
                        System.out.println("No title specified for removal");
                        break;
                    }
                    removeEvent(cmdItems);
                    Log.logger.info("Successfully removed event");
                case "back":
                    Log.logger.info("Going Back to Schedule Menu");
                    log.setErrorIndex();
                    log.redoLast();
                    break eventTerminal;
                default:
                    Log.logger.warning("Invalid Command Entered in Event Menu");
                    System.out.println("Invalid Command, Please Re-Enter Command");
            }
        } while(true);
    }

    static void newEvent(){
        boolean goodLength = false;
        Scanner scan = new Scanner(System.in);
        System.out.println("What's the name of this event?");
        StringBuilder eventName = new StringBuilder();
        while(!goodLength){
            System.out.println("Name must be limited to 15 characters");
            eventName.append(scan.nextLine());
            if(eventName.length() <= 15){
                goodLength =  true;
                Log.logger.info("Successfully created event title");
            }
            else{
                Log.logger.warning("Tried to Create an Event With Invalid Name");
                eventName = new StringBuilder();
            }
        }
//        eventName.append(titleParam[1]);
//        for (int i = 2; i < titleParam.length; i++) {
//            eventName.append(" ").append(titleParam[i]);
//        }
        System.out.println("Please input the days in which this event occurs, separated by commas, in the following format: \n" +
                "M,T,W,R,F,S,U: ");
        String line = scan.nextLine().trim();
        String[] days = line.split(",");
        while (!line.matches("^[MTWRFSU](,[MTWRFSU])*$")) {
            System.out.println("Invalid entry: Please type days as 'M,T,W,R,F,S,U' separated by commas");
            Log.logger.warning("Invalid Day Entered in newEvent menu");
            line = scan.nextLine().trim();
            days = line.split(",");
        }
        int startHour = -1;
        int startMinute = -1;
        int endHour = -1;
        int endMinute = -1;

        boolean validStartTime = false;
        boolean validEndTime = false;

        while (!validStartTime || !validEndTime) {
            // Input and validation for start time
            while (!validStartTime) {
                System.out.println("Please input the time the event starts in meridian format, but NOT using AM or PM (yet)");
                String input = scan.nextLine();
                String[] tokens = input.split(":");
                try {
                    if (tokens.length != 2) {
                        throw new NumberFormatException();
                    }
                    startHour = Integer.parseInt(tokens[0]);
                    startMinute = Integer.parseInt(tokens[1]);
                    if (startHour < 1 || startHour > 12 || startMinute < 0 || startMinute > 59) {
                        throw new NumberFormatException();
                    }
                    validStartTime = true;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter valid integers for hour and minute");
                    Log.logger.warning("Non-integer entered for startHour or startMinute");
                }
            }

            // Input and validation for meridian of start time
            System.out.println("Please enter 'AM' or 'PM' for the starting time");
            String startMeridian = scan.nextLine();
            if (!startMeridian.equals("AM") && !startMeridian.equals("PM")) {
                System.out.println("Please enter AM or PM");
                Log.logger.warning("Something other than AM or PM entered for startTime");
                validStartTime = false; // Resetting validStartTime to false to repeat the loop
            } else {
                // Convert start time to 24-hour format
                if (startMeridian.equals("PM") && startHour != 12) {
                    startHour += 12;
                } else if (startMeridian.equals("AM") && startHour == 12) {
                    startHour = 0;
                }

                validStartTime = true; // Start time input is valid
            }

            // Input and validation for end time
            while (!validEndTime) {
                System.out.println("Please input the time the event ends in meridian format, but NOT using AM or PM (yet)");
                String input = scan.nextLine();
                String[] tokens = input.split(":");
                try {
                    if (tokens.length != 2) {
                        throw new NumberFormatException();
                    }
                    endHour = Integer.parseInt(tokens[0]);
                    endMinute = Integer.parseInt(tokens[1]);
                    if (endHour < 1 || endHour > 12 || endMinute < 0 || endMinute > 59) {
                        throw new NumberFormatException();
                    }

                    // Input and validation for meridian of end time
                    System.out.println("Please enter 'AM' or 'PM' for the ending time");
                    String endMeridian = scan.nextLine();
                    if (!endMeridian.equals("AM") && !endMeridian.equals("PM")) {
                        System.out.println("Please enter AM or PM");
                        Log.logger.warning("Something other than AM or PM entered for endTime");
                        continue; // Restart the loop to re-enter end time
                    }

                    // Convert end time to 24-hour format
                    if (endMeridian.equals("PM") && endHour != 12) {
                        endHour += 12;
                    } else if (endMeridian.equals("AM") && endHour == 12) {
                        endHour = 0;
                    }

                    // Check if end time is after start time
                    if (endHour < startHour || (endHour == startHour && endMinute < startMinute)) {
                        System.out.println("Event end time cannot be before start time");
                        Log.logger.warning("End time before start time");
                        continue; // Restart the loop to re-enter end time
                    }

                    validEndTime = true; // End time input is valid
                } catch (NumberFormatException e) {
                    System.out.println("Please enter valid integers for hour and minute");
                    Log.logger.warning("Non-integer entered for endHour or endMinute");
                }
            }
        }
        if(endMinute == 30){
            endMinute = 29;
        }
        if(endMinute == 0 && endHour > startHour){
            if(endHour == 0){
                endHour = 23;
                endMinute = 59;
            } else {
                endHour--;
                endMinute = 59;
            }
        }

        //Creates timeslot(s) for the event
        String s = "";
        for (String n:days)
            s+= n;
        char[] charDays = s.toCharArray();
        List<TimeSlot> times = new ArrayList<TimeSlot>();
        for (int i = 0; i < charDays.length; i++) {
            TimeSlot timeslot = new TimeSlot(charDays[i], startHour, startMinute, endHour, endMinute);
            times.add(timeslot);
        }
        for (int i = 0; i < times.size(); i++) {
            System.out.println(times.get(i).toString());
        }
        Event e = new Event(eventName, times);
        if(currentSchedule.addEvent(e)){
            System.out.println("Successfully added event");
            Log.logger.info("Successfully Added event " + e.toLogFormat());
        }
        else{
            System.out.println("Failed to add event");
            createEvent();
        }
    }

    static void removeEvent(String[] titleParam){
        StringBuilder eventName = new StringBuilder();
        eventName.append(titleParam[1]);
        for (int i = 2; i < titleParam.length; i++) {
            eventName.append(" ").append(titleParam[i]);
        }
    }

    static void search() {
        System.out.println("Start searching for courses or type 'help' for a list of commands.");
        courseSearch = new Search();
        courseSearch.addFilter(Search.Type.SEMESTER, currentSchedule.getSemester());
        courseSearch.addFilter(Search.Type.YEAR, currentSchedule.getYear());
        courseList = courseSearch.filterCourses();
        searchTerminal: do {
            System.out.println(courseSearch.getFilters());
            System.out.print("Search: ");
            Scanner scan = new Scanner(System.in);
            String cmdLine = scan.nextLine();
            String[] cmdItems = cmdLine.split(" ");
            switch (cmdItems[0]) {
                case "help":
                    Log.logger.info("Opened Search Help Menu");
                    searchHelp();
                    break;
                case "filterHelp":
                    Log.logger.info("Opened Filter Help Menu");
                    filterHelp();
                    break;
                case "addFilter":
                    Log.logger.info("Add Filter");
                    if (cmdItems.length <= 2) {
                        Log.logger.warning("No filter specified");
                        System.out.println("No filter specified.");
                        break;
                    }
                    Log.logger.info("Successfully Added Filter");
                    addFilter(cmdItems);
                    break;
                case "removeFilter":
                    Log.logger.info("Remove Filter");
                    if (cmdItems.length <= 2) {
                        Log.logger.warning("No filter specified");
                        System.out.println("No filter specified.");
                        break;
                    }
                    Log.logger.info("Successfully Removed Filter");
                    removeFilter(cmdItems);
                    break;
                case "clearFilters":
                    Log.logger.info("Cleared Filters");
                    courseSearch.clearFilters();
                    break;
//                case "modifyFilter":
                case "search":
                    Log.logger.info("Searching for courses");
                    if (courseSearch.getFilters().isEmpty()) {
                        System.out.println("You have no filters selected, this will return every class, are you sure?");
                        System.out.println("Y/N");
                        String killSwitch = scan.nextLine();
                        if (Objects.equals(killSwitch, "Y") || Objects.equals(killSwitch, "y")) {
                            break;
                        } else {continue; }
                    }
                    searchResults = courseSearch.filterCourses();
                    for(Course course: searchResults){
                        System.out.println(course.getCourseCode() +" " + course.getSectionLetter());
                    }
                    break;
                case "addCourse":
                    Log.logger.info("Add Course");
                    if (cmdItems.length < 2) {
                        Log.logger.warning("No course specified");
                        System.out.println("No course specified.");
                        break;
                    }
                    if (cmdItems.length < 3) {
                        Log.logger.warning("No section letter specified");
                        System.out.println("No section letter specified.");
                        break;
                    }
                    addCourse(cmdItems, courseList);
                    break;
                case "removeCourse":
                    Log.logger.info("Remove Course");
                    if (cmdItems.length < 2) {
                        Log.logger.warning("No course specified");
                        System.out.println("No course specified.");
                        break;
                    }
                    if (cmdItems.length < 3) {
                        Log.logger.warning("No section letter specified");
                        System.out.println("No section letter specified.");
                        break;
                    }
                    removeCourse(cmdItems);
                    break;
                case "back":
                    Log.logger.info("Going Back to Schedule Menu");
                    log.setErrorIndex();
                    log.redoLast();
                    break searchTerminal;
                default:
                    Log.logger.warning("Invalid Command Entered in Search Menu");
                    System.out.println("Invalid Command, Please Re-Enter Command");
            }
        } while(true);
    }

    static void addFilter(String[] filterParam) {
        Search.Type[] filterType = stringToFilterType(filterParam[1]);
        if (filterType[0] == null) {
            Log.logger.warning("Attempted to Add Filter That Does Not Exist");
            System.out.println("Filter " + filterParam[1] + " does not exist exist.");
            return;
        }
        for (int i = 2; i < filterParam.length; i++) {
            Log.logger.info("Filter Successfully Added");
            courseSearch.addFilter(filterType[0], filterParam[i]);
        }
    }

    static void removeFilter(String[] filterParam) {
        Search.Type[] filterType = stringToFilterType(filterParam[1]);
        if (filterType[0] == null) {
            Log.logger.warning("Attempted to Remove Filter That Does Not Exist");
            System.out.println("Filter " + filterParam[1] + " does not exist exist.");
            return;
        }
        for (int i = 2; i < filterParam.length; i++) {
            Log.logger.info("Filter Successfully Removed");
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
            if (courseData[2].equals("0")) {
                if (Objects.equals(courseData[1], searchResult.getCourseCode())) {
                    currentSchedule.addCourse(searchResult);
                    Log.logger.info("Successfully Added " + courseData[1] + courseData[2]);
                    return;
                }
            }
            if (Objects.equals(courseData[1], searchResult.getCourseCode()) && Objects.equals(courseData[2].charAt(0), searchResult.getSectionLetter())) {
                currentSchedule.addCourse(searchResult);
                Log.logger.info("Successfully Added " + courseData[1] + courseData[2]);
                return;
            }
        }
        Log.logger.warning("Attempting to add a course that does not exist");
        System.out.println("Course " + courseData[1] + " " + courseData[2] + " does not exist.");
    }

    static void removeCourse(String[] courseData) {
        List<Course> currentCourses = currentSchedule.getCourses();
        for (Course course : currentCourses) {
            if (courseData[2].equals("0")) {
                if (Objects.equals(courseData[1], course.getCourseCode())) {
                    currentSchedule.removeCourse(course);
                    Log.logger.info("Course Successfully Removed");
                    return;
                }
            }
            if (Objects.equals(courseData[1], course.getCourseCode()) && Objects.equals(courseData[2].charAt(0), course.getSectionLetter())) {
                currentSchedule.removeCourse(course);
                Log.logger.info("Course Successfully Removed");
                return;
            }
        }
        Log.logger.warning("Course Not in Schedule");
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
                    Log.logger.info("Displaying Start Time Filter Info");
                    System.out.println("startHours are 8:00 AM to 3:00 PM as well as night classes at 6:00 PM, enter time as 8:00-18:00.");
                    break;
                case "Year":
                    Log.logger.info("Displaying year time filter info");
                    System.out.println("years are 2023, 2024, and 2025");
                case "day":
                    Log.logger.info("Displaying Day Filter Info");
                    System.out.println("days are M T W R F, each corresponding to Monday, Tuesday, Wednesday, Thursday, and Friday respectively.");
                    break;
                case "courseCode":
                    Log.logger.info("Displaying Course Code Filter Info");
                    System.out.println("courseCode is the department followed by the courseID, ex. COMP350.");
                    break;
                case "title":
                    Log.logger.info("Displaying Title Filter Info");
                    System.out.println("title is the name of the course, ex. Software Engineering.");
                    break;
                case "back":
                    Log.logger.info("Going Back to Search Menu");
                    break helpTerminal;
                default:
                    Log.logger.warning("Invalid Filter Entered");
                    System.out.println("Invalid Command, Please Re-Enter Command");
            }
        } while(true);
    }
     static void Login() {
         Scanner scanner = new Scanner(System.in);
         boolean validAccount = false;
         while (!validAccount) {
             System.out.println("Do you already have an account? (Y/N)");
             if (scanner.nextLine().equalsIgnoreCase("y")) {
                 System.out.println("Please enter your username");
                 String username = scanner.nextLine();
                 if(act.userExists(username)) {
                     System.out.println("Please enter your password");
                     String password = scanner.nextLine();
                     if (act.Login(username, password)) {
                         System.out.println("Login successful");
                         break;
                     } else {
                         System.out.println("Failed login");
                         System.out.println("Do you want to create a new password? (Y/N)");
                         String answer = scanner.nextLine();
                         if (answer.equalsIgnoreCase("Y")) {
                             act.resetPassword(username);
                             break;
                         }
                     }
                 } else{
                     System.out.println("Username doesn't exist");
                 }
             } else {
                 do {
                     System.out.println("Please enter your first and last name and your major separated by a space.");

                     String studentData = scanner.nextLine();
                     String[] studentParam = studentData.split(" ");
                     if (studentBuilder(studentParam)) {
                         act.createAccount(student);
                         validAccount = true;
                         break;
                     }
                     System.out.println("Please fill out all three fields.");
                 } while (true);
             }
         }
     }
}