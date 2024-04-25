package com.classes;

import java.sql.Array;
import java.util.*;

public class Main {
    static Student student;
    static Schedule currentSchedule;
    static Search courseSearch;
    static List<Course> courseList;
    static List<Course> searchResults;
    static Log log = new Log();
    static RawLog rawlog = new RawLog();

    public static void main(String[] args) {
        //TODO: edit main to add year as well!
        Search.WebScraper();
        System.out.println("Welcome to GCC Scheduler");
        do{
            System.out.println("Please enter your first and last name and your major separated by a space.");
            Scanner scanner = new Scanner(System.in);
            String studentData = scanner.nextLine();
            String[] studentParam = studentData.split(" ");
            if (studentBuilder(studentParam)) {
                break;
            }
            System.out.println("Please fill out all three fields.");
        } while (true);
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
                    RawLog.logger.info("Opened Schedule Help Menu");
                    consoleHelp();
                    break;
                case "createEvent":
                    RawLog.logger.info("Creating New Event");
                    createEvent();
                    break;
                case "createSchedule":
                    RawLog.logger.info("Creating New Schedule");
                    if (cmdItems.length < 2) {
                        RawLog.logger.warning("Tried to Create a Schedule With No Name");
                        System.out.println("Cannot create a schedule with no name.");
                        break;
                    }
                    currentSchedule = createSchedule(cmdItems);
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
                    currentSchedule = switchSchedule(cmdItems);
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
                    if (currentSchedule == null) {
                        RawLog.logger.info("No Schedule Selected");
                        System.out.println("Cannot search without a schedule.");
                        break;
                    }
                    RawLog.logger.info("Opened Search Menu");
                    search();
                    break;
                case "calendarView":
                    RawLog.logger.info("Opened Calender View");
                    currentSchedule.viewGrid();
                    break;
                case "save":
                    if (currentSchedule.getCourses().isEmpty()){
                        RawLog.logger.info("Failed to save schedule");
                        System.out.println("Cannot save a schedule with no courses.");
                        break;
                    }
                    currentSchedule.save();
                    RawLog.logger.info("Saved Schedule");
                    System.out.println(currentSchedule.getName() + " has been saved.");
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
                case "load":
                    RawLog.logger.info("Loading new Schedule");
                    if (cmdItems.length < 2) {
                        RawLog.logger.warning("No Schedule was Specified");
                        System.out.println("No schedule specified.");
                        break;
                    }
                    boolean load = loadSchedule(cmdItems[1]);
                    if (!load) {
                        RawLog.logger.info("Failed To Load Schedule");
                        break;
                    }
                    if(currentSchedule != null) {
                        log = new Log(new Schedule(currentSchedule));
                    }
                    System.out.println("Successfully loaded schedule.");
                    RawLog.logger.info("Successfully loaded Schedule");
                    break;
                default:
                    RawLog.logger.info("Invalid Command Entered in Schedule Menu");
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
        currentSchedule = new Schedule(name, coursesToAdd.getFirst().getSemester());
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
        System.out.println("'createEvent': this creates a custom event, prompting you with day(s) and time(s)'");
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
                return new Schedule(String.valueOf(scheduleName), semester);
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
            System.out.println("createEvent: ");
            Scanner scan = new Scanner(System.in);
            String cmdLine = scan.nextLine();
            String[] cmdItems = cmdLine.split(" ");
            switch(cmdItems[0]){
                case "help":
                    RawLog.logger.info("Opened Event Help Menu");
                    eventHelp();
                    break;
                case "newEvent":
                    RawLog.logger.info("Creating New Event");
                    if (cmdItems.length < 2) {
                        RawLog.logger.warning("Tried to Create an Event With No Name");
                        System.out.println("Cannot create an event with no name.");
                        break;
                    }
                    RawLog.logger.info("Successfully created event title");
                    newEvent(cmdItems);
                    break;
                case "removeEvent":
                    RawLog.logger.info("Remove Event");
                    if(cmdItems.length<= 1){
                        RawLog.logger.warning("No title specified for removal");
                        System.out.println("No title specified for removal");
                        break;
                    }
                    removeEvent(cmdItems);
                    RawLog.logger.info("Successfully removed event");
                case "back":
                    RawLog.logger.info("Going Back to Schedule Menu");
                    log.setErrorIndex();
                    log.redoLast();
                    break eventTerminal;
                default:
                    RawLog.logger.warning("Invalid Command Entered in Event Menu");
                    System.out.println("Invalid Command, Please Re-Enter Command");
            }
        } while(true);
    }

    static void newEvent(String[] titleParam){
        String[] newDays = new String[0];
        StringBuilder eventName = new StringBuilder();
        eventName.append(titleParam[1]);
        for (int i = 2; i < titleParam.length; i++) {
            eventName.append(" ").append(titleParam[i]);
        }
        System.out.println("Please input the days in which this event occurs, separated by commas, in the following format: \n" +
                "M,T,W,R,F,S,U: ");
        Scanner scan = new Scanner(System.in);
        String line = scan.nextLine().trim();
        //TODO: Error handling
        String[] days = line.split(",");
        if (!line.matches("^[MTWRFSU](,[MTWRFSU])*$")) {
            System.out.println("Invalid entry: Please type days as 'M,T,W,R,F,S,U' separated by commas");
            RawLog.logger.warning("Invalid Day Entered in newEvent menu");
            newEvent(titleParam);
        } else {
            // Use a HashSet to store unique days
            Set<String> uniqueDays = new HashSet<>();
            Collections.addAll(uniqueDays, days);

            // Convert the HashSet back to an array
            newDays = uniqueDays.toArray(new String[0]);

            System.out.println("Days: ");
            for (String day : days) {
                System.out.println(day);
            }
            //TODO: fix because updated days reverses order
            System.out.println("Updated Days: ");
            for (String newDay : newDays) {
                System.out.println(newDay);
            }

            // Continue processing the days
     //       for (String day : newDays) {
                // Process each unique day
     //       }
        }

        boolean correctInput = false;
        int startHour = -1;
        int startMinute = -1;
        int endHour = -1;
        int endMinute = -1;
        while(!correctInput){
            //TODO: change to do-while
            System.out.println("Please input the hour the event starts in military-time format: ");
            if(scan.hasNextInt()){
                startHour = scan.nextInt();
                correctInput = true;
            }
            else{
                System.out.println("Please enter an integer");
                RawLog.logger.warning("Non-integer entered for startHour");
                scan.next();
            }
        }
        correctInput = false;
        while(!correctInput){
            System.out.println("Please input the minute the event starts: ");
            if(scan.hasNextInt()){
                startMinute = scan.nextInt();
                correctInput = true;
            }
            else{
                System.out.println("Please enter an integer");
                RawLog.logger.warning("Non-integer entered for startMinute");
                scan.next();
            }
        }
        correctInput = false;
        while(!correctInput){
            System.out.println("Please input the hour the event ends in military-time format: ");
            if(scan.hasNextInt()){
                endHour = scan.nextInt();
                correctInput = true;
            }
            else{
                System.out.println("Please enter an integer");
                RawLog.logger.warning("Non-integer entered for endHour");
                scan.next();
            }
            if(correctInput && endHour < startHour){
                System.out.println("Event can't end before it starts!");
                RawLog.logger.warning("endHour before startHour");
                correctInput = false;
            }

        }
        correctInput = false;
        while(!correctInput){
            System.out.println("Please input the minute the event ends: ");
            if(scan.hasNextInt()){
                endMinute = scan.nextInt();
                correctInput = true;
            }
            else{
                System.out.println("Please enter an integer");
                RawLog.logger.warning("Non-integer entered for endMinute");
                scan.next();
            }
            if(correctInput && startHour == endHour && endMinute < startMinute){
                System.out.println("Event can't end before it started!");
                RawLog.logger.warning("endMinute before startMinute & startHour");
                correctInput = false;
                scan.next();
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
        courseList = courseSearch.filterCourses();
        searchTerminal: do {
            System.out.println(courseSearch.getFilters());
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
                            break;
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
                    if (cmdItems.length < 3) {
                        RawLog.logger.warning("No section letter specified");
                        System.out.println("No section letter specified.");
                        break;
                    }
                    addCourse(cmdItems, courseList);
                    break;
                case "removeCourse":
                    RawLog.logger.info("Remove Course");
                    if (cmdItems.length < 2) {
                        RawLog.logger.warning("No course specified");
                        System.out.println("No course specified.");
                        break;
                    }
                    if (cmdItems.length < 3) {
                        RawLog.logger.warning("No section letter specified");
                        System.out.println("No section letter specified.");
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
            if (courseData[2].equals("0")) {
                if (Objects.equals(courseData[1], searchResult.getCourseCode())) {
                    currentSchedule.addCourse(searchResult);
                    RawLog.logger.info("Successfully Added Course");
                    return;
                }
            }
            if (Objects.equals(courseData[1], searchResult.getCourseCode()) && Objects.equals(courseData[2].charAt(0), searchResult.getSectionLetter())) {
                currentSchedule.addCourse(searchResult);
                RawLog.logger.info("Successfully Added Course");
                System.out.println(searchResult.getTimes());
                return;
            }
        }
        RawLog.logger.warning("Attempting to add a course that does not exist");
        System.out.println("Course " + courseData[1] + " " + courseData[2] + " does not exist.");
    }

    static void removeCourse(String[] courseData) {
        List<Course> currentCourses = currentSchedule.getCourses();
        for (Course course : currentCourses) {
            if (courseData[2].equals("0")) {
                if (Objects.equals(courseData[1], course.getCourseCode())) {
                    currentSchedule.removeCourse(course);
                    RawLog.logger.info("Course Successfully Removed");
                    return;
                }
            }
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