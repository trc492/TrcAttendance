# TrcAttendance
###Attendance Logger - Tool to log students' participation in meetings.

In our team, each student is required to participate a minimum number of hours of meetings
during the build season in order to participate in competitions. At our meeting place, we
put out a laptop with touch screen at the door running this program. Each student when walked
in will select their name on the check-in list and click the check-in button. When leaving,
they will select their name in the check-out list and click the check-out button. This allows
us to keep track of the minutes they have attended meetings. The program keeps track of this
info in an Excel spreadsheet which can sum up the total hours of each student if needed.

To minimize the amount of typing required when starting a meeting session with the program,
it is recommended to compile this program to a jar file and put it into the same folder
where the log data files are stored. On the Windows desktop, it is recommended to create
a shortcut that contains the following line in the `Target:` field:
```
java -jar TrcAttendance.jar log=<LogFileName> place=<MeetingPlace>
```
where:
```
<LogFileName>   - The file name of the log data (e.g. Frc2016Attendance.csv). The log data is in the format
                  of an Excel CSV (text file with fields separated by commas).

<MeetingPlace>  - Specifies the default meeting place.
```
The parameters are optional. By specifying them, the program will start with the correct
log file opened and the meeting place filled in.

In the `Start in:` field of the shortcut, put in the location of the folder where the jar
file and the log data are stored.

In the `Run:` field of the shortcut, select `Minimized`. This will cause the command console
associated with the java program to be minimized.

With the above recommendation, you can create multiple shortcuts on the desktop. For example,
we have both an FRC and FTC teams. We created two shortcuts `Frc2016Attendance` and
`Ftc2016Attendance` each has different parameters specifying a different log file and meeting
place.
