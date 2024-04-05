# GCCScheduler Console How To Guide

## Upon running the program you will be met with the prompt "Please enter your first and last name and your major separated by a space."
##Just enter your first and last name and major, your major can be multiple words if necessary.

## In the command menu, you can enter the following commands
#### All commands are followed by their arguements while entering them:
```
"help" - This command takes no arguements and shows you the syntax and purpose of any other commands
in the current menu.
"createSchedule" - This command takes two consecutive arguements, first is the name of the schedule,
this can be multiple words. After executing the command it will then ask for you for the semester.
"switchSchedule" - This command takes one arguement, the name of the schedule you want to change to,
and it changes the schedule currently being edited.
"getSchedules" - This command takes no arguements and returns every schedule attached to the current
student profile.
"search" - This command takes no arguements and takes you to the search menu, you cannot use it unless
you are currently editing a schedule.
"calendarView" - This command takes no arguements and prints out a one week calendar with all classes
in the current schedule in their proper timeslots.
"save" - This command takes no arguements and saves the current schedule to a csv file in the data
folder.
"load" - This command takes one argumment, the name of the schedule that you want to load from the
data folder, and converts the csv in a schedule.
"undo" - This command takes no arguments and undoes the most recent change to the schedule.
"redo" - This command takes no arguments and redoes the most recent change to the schedule.
"quit" - This command takes no arguements and closes the program.
```

## In the search menu, you can enter the following commands
#### All commands are followed by their arguements while entering them:
```
"help" - This command takes no arguements and shows you the syntax and purpose of any other commands
in the current menu.
"filterHelp" - This command takes no arguements and takes you to the filterHelp menu, it gives you a
list of filters, enter the name of a filter to learn more and enter back to return to the search menu.
"addFilter" - This command takes two arguments, the type of filter and the value of the filter, you
can enter multiple values at once. This command adds the specified filters to your search menu.
"removeFilter" - This command takes two arguements, the type of filter and value of the filter, it
will remove the corresponding filter.
"clearFilters" - This command takes no arguments and removes all filters that you have added.
"search" - This command takes no arguments and prints out all of the classes that match your filters.
"addCourse" - This command takes 2 arguments, the course code (ex. COMP350) and the section letter (ex. B),
enter 0 for the section letter if there is none, and adds the course to your schedule.
"removeCourse" - This command takes 2 arguments, the course code (ex. COMP350) and the section letter (ex. B),
enter 0 for the section letter if there is none, and removes the course from your schedule.
"back" - This command takes no arguments and returns you to the command menu.
```
