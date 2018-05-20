/*
 * Copyright (c) 2016 Titan Robotics Club (http://www.titanrobotics.net)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import attendance.AttendanceLog;
import attendance.Attendant;

/**
 * This is the main class of the Attendance Logger program. It contains the main method.
 * To minimize the amount of typing required when starting a meeting session with the program,
 * it is recommended to compile this program to a jar file and put it into the same folder
 * where the log data files are stored. On the Windows desktop, it is recommended to create
 * a shortcut that contains the following line in the "Target:" field:
 *  java -jar TrcAttendance.jar log=<LogFileName> place=<MeetingPlace>
 *  where:
 *  <LogFileName>   - The file name of the log data (e.g. Frc2016Attendance.csv). The log data
 *                    is in the format of an Excel CSV (text file with fields separated by
 *                    commas).
 *  <MeetingPlace>  - Specifies the default meeting place. 
 *
 * The parameters are optional. By specifying them, the program will start with the correct
 * log file opened and the meeting place filled in.
 * In the "Start in:" field of the shortcut, put in the location of the folder where the jar
 * file and the log data are stored.
 * In the "Run:" field of the shortcut, select "Minimized". This will cause the command console
 * associated with the java program to be minimized.
 *
 * With the above recommendation, you can create multiple shortcuts on the desktop. For example,
 * we have both an FRC and FTC teams. We created two shortcuts "Frc2016Attendance" and
 * "Ftc2016Attendance" each has different parameters specifying a different log file and meeting
 * place.
 */
public class TrcAttendance extends JComponent implements WindowListener
{
    private static final long serialVersionUID = 1L;
    private static final String PROGRAM_TITLE = "Trc Attendance Logger";
    private static final String COPYRIGHT_MSG = "Copyright (c) Titan Robotics Club";
    private static final String PROGRAM_VERSION = "[version 1.0.0]";
    private static final String SESSION_LOG_FILE_NAME = "SessionLog.txt";

    public static String logFileName = null;
    public static String placeName = "";

    public JFrame frame;

    public Font smallFont;
    public Font mediumFont;
    public Font bigFont;

    public MenuBar menuBar;
    public MeetingPane meetingPane;
    public AttendancePane attendancePane;
    public EditorDialog editorDialog;
    public AttendanceLog attendanceLog = null;

    private JFileChooser fileChooser = new JFileChooser();

    /**
     * This is the entry point of the program. It created the main window of the program,
     * set the proper size and location and initialized all the UI elements.
     *
     * @param args specifies the command line parameters.
     */
    public static void main(String[] args)
    {
        parseArgs(args);
        EventQueue.invokeLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        JFrame frame = new JFrame(PROGRAM_TITLE);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
                        frame.setSize(800, 500);
                        frame.setResizable(false);
                        frame.add(new TrcAttendance(frame));
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    }
                });
    }   //main

    /**
     * This method parses the command line parameters. The parameters are optional. If they
     * exist, it will initialize the corresponding variables. Each parameter is in the format
     * of <parameter>=<argument>. At this time, the supported parameters are:
     *  log=<LogFileName>
     *  place=<MeetingPlace>
     *  where:
     *  <LogFileName>   - specifies the name of the log file. It can be a full path if the
     *                    log file is not in the same current folder of the program.
     *  <MeetingPlace>  - specifies the default meeting place.
     *
     * @param args specifies the command line parameters.
     */
    private static void parseArgs(String[] args)
    {
        for (int i = 0; i < args.length; i++)
        {
            String[] fields = args[i].trim().split("=");
            if (fields.length == 2)
            {
                if (fields[0].equalsIgnoreCase("log"))
                {
                    logFileName = fields[1];
                }
                else if (fields[0].equalsIgnoreCase("place"))
                {
                    placeName = fields[1];
                }
                else
                {
                    throw new IllegalArgumentException(
                            "Invalid command line parameter \"" + fields[0] + "\".");
                }
            }
            else
            {
                throw new IllegalArgumentException(
                        "Invalid command line syntax \"" + args[i] + "\".");
            }
        }
    }   //parseArgs

    /**
     * Constructor: Create an instance of the object.
     * Note that the constructor is private so that the class cannot be instantiated by anybody
     * other than the main method.
     *
     * @param frame specifies the main window of the program.
     */
    private TrcAttendance(JFrame frame)
    {
        this.frame = frame;

        Font defaultFont = UIManager.getDefaults().getFont("TabbedPane.font");
        int defaultSize = defaultFont.getSize();
        //
        // To make the program "touch friendly", make the fonts bigger.
        //
        smallFont = defaultFont.deriveFont(Font.PLAIN, defaultSize*1.5f);
        mediumFont = defaultFont.deriveFont(Font.PLAIN, defaultSize*2.0f);
        bigFont = defaultFont.deriveFont(Font.PLAIN, defaultSize*4.0f);

        //
        // Create and initialize all the UI elements.
        //
        menuBar = new MenuBar(this);
        meetingPane = new MeetingPane(this);
        attendancePane = new AttendancePane(this);
        editorDialog = new EditorDialog(this, "Edit Attendants List - " + PROGRAM_TITLE, true);

        //
        // Create and initialize a global FileChooser object for "New" and "Open".
        //
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Spreadsheet text data file (*.csv)", "csv"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        //
        // If a log file is specified as a command parameter, open it.
        //
        if (logFileName != null)
        {
            openLogFile(new File(logFileName));
        }

        frame.addWindowListener(this);
    }   //TrcAttendance

    /**
     * This method is called when the File->New menu item is clicked. It will pop up the
     * FileChooser dialog allowing the user to select the folder and to specify the new
     * log file name.
     */
    public void onFileNew()
    {
        //
        // Clear the selectedFile field from the previous session.
        //
        fileChooser.setSelectedFile(new File(""));
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            //
            // Check if the user selected an existing log file. If so, prompt the user
            // for permission to overwrite it.
            //
            File file = fileChooser.getSelectedFile();
            returnVal = JOptionPane.OK_OPTION;
            if (file.exists() && !file.isDirectory())
            {
                returnVal = JOptionPane.showConfirmDialog(
                        this,
                        file.getPath() + " already exists, overwrite?",
                        PROGRAM_TITLE,
                        JOptionPane.OK_CANCEL_OPTION);
            }

            if (returnVal == JOptionPane.OK_OPTION)
            {
                //
                // Create the new log file, open the editor dialog allowing the user to
                // enter the names of the new attendants and update the program state.
                //
                try
                {
                    //
                    // Create the new log file.
                    //
                    attendanceLog = new AttendanceLog(file, true);
                }
                catch (Exception e)
                {
                    //
                    // Should never come here.
                    // The AttendanceLog may throw a few exceptions when parsing an existing
                    // log file but since we are creating a new log file, we should never get
                    // any exceptions. In case we do for whatever reason, we throw a
                    // RuntimeException as an internal error.
                    //
                    throw new RuntimeException(e.getMessage());
                }
                //
                // Update the Window title showing the new log file.
                //
                frame.setTitle(file.getName() + " - " + PROGRAM_TITLE);
                //
                // Open the Editor dialog as if the user has clicked File->Edit allowing
                // the user to enter the names of the new attendants.
                //
                onFileEdit();
                //
                // Auto fill-in the default meeting date/time/place.
                //
                meetingPane.setDefaultDateTimePlace();
                //
                // Update the program state by disabling New/Open and enabling Edit/Close
                // and the meeting pane.
                //
                menuBar.setMenuItemsEnabled(false, false, true, true);
                meetingPane.setEnabled(true);
            }
        }
    }   //onFileNew

    /**
     * This method is called when the File->Open menu item is clicked. It will pop up the
     * FileChooser dialog allowing the user to select the folder and log file to open.
     */
    public void onFileOpen()
    {
        fileChooser.setSelectedFile(new File(""));
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            //
            // The user clicked "open" approving the file choice.
            //
            openLogFile(fileChooser.getSelectedFile());
        }
    }   //onFileOpen

    /**
     * This method is called when the File->Edit menu item is clicked. It will pop up the
     * Editor dialog allowing the user to edit the attendants list.
     */
    public void onFileEdit()
    {
        editorDialog.open(attendanceLog);
    }   //onFileEdit

    /**
     * This method is called when the File->Close menu item is clicked. It will close the
     * log file. If the log file has changes, it will prompt the user to save the changes
     * before closing the file. The user has the options to save the changes, discard the
     * changes or cancel the close operation.
     */
    public void onFileClose()
    {
        if (closeLogFile(JOptionPane.YES_NO_CANCEL_OPTION) != JOptionPane.CANCEL_OPTION)
        {
            //
            // Update the program state by clearing and disabling the meeting and attendance
            // panes, disabling the Edit/Close menu items and enabling the New/Open menu items.
            // It also updates the Window title showing no log file opened.
            //
            meetingPane.clearPanel();
            attendancePane.clearPanel();
            menuBar.setMenuItemsEnabled(true, true, false, false);
            attendanceLog = null;
            frame.setTitle(PROGRAM_TITLE);
        }
    }   //onFileClose

    /**
     * This method is called when the File->About menu item is clicked. It will pop up the
     * about message dialog displaying the program name, version number and copyright notice.
     */
    public void onFileAbout()
    {
        JOptionPane.showMessageDialog(
                this,
                PROGRAM_TITLE + " " + PROGRAM_VERSION + "\n" + COPYRIGHT_MSG + "\n",
                PROGRAM_TITLE,
                JOptionPane.INFORMATION_MESSAGE);
    }   //onFileAbout

    /**
     * This method is called when the File->Exit menu item is clicked. It will exit the program.
     * If the log file has changes, it will prompt the user to save the changes before exiting.
     * The user has the options to save the changes and exit, discard the changes and exit, or
     * cancel the exit operation.
     */
    public void onFileExit()
    {
        if (closeLogFile(JOptionPane.YES_NO_CANCEL_OPTION) != JOptionPane.CANCEL_OPTION)
        {
            System.exit(0);
        }
    }   //onFileExit

    /**
     * This method is called when the "Create Meeting" button is clicked. It will create a
     * new meeting session and update the program state accordingly.
     */
    public void onCreateMeeting(
            String date, String startTime, String endTime, String place, String meeting)
    {
        String[] sessionHeader = {date, startTime, endTime, place, meeting};
        attendanceLog.createSession(sessionHeader);
        //
        // Disable New/Open menu items. Enable Edit/Close menu items.
        // Disable Meeting pane and enable Attendance pane.
        //
        menuBar.setMenuItemsEnabled(false, false, true, true);
        meetingPane.setEnabled(false);
        attendancePane.setEnabled(true);
    }   //onCreateMeeting

    /**
     * This method opens the specified log file. It will also populate the check-in list with
     * all the attendants and update the program state accordingly.
     *
     * @param file specifies the log file to open.
     */
    private void openLogFile(File file)
    {
        try
        {
            //
            // Create the attendance log and populate it with the attendance info from log file.
            //
            attendanceLog = new AttendanceLog(file, false);
            //
            // Populate the check-in list with all the attendants in the log file.
            //
            attendancePane.clearLists();
            attendancePane.updateLists(attendanceLog);
            //
            // Update the Window title showing the opened log file.
            //
            frame.setTitle(file.getName() + " - " + PROGRAM_TITLE);
            //
            // Disable New/Open menu items. Enable Edit/Close menu items.
            //
            menuBar.setMenuItemsEnabled(false, false, true, true);

            //
            // Check for existing session log. If found, recover the existing session.
            //
            if (readExistingSessionLog(SESSION_LOG_FILE_NAME))
            {
                meetingPane.setEnabled(false);
            }
            else
            {
                //
                // Auto fill-in the default meeting date/time/place.
                //
                meetingPane.setDefaultDateTimePlace();
                //
                // Enable Meeting pane.
                //
                meetingPane.setEnabled(true);
            }
        }
        catch (FileNotFoundException e)
        {
            //
            // The specified log file does not exist.
            // Enable New/Open menu items. Disable Edit/Close menu items.
            //
            menuBar.setMenuItemsEnabled(true, true, false, false);
            String msg = String.format("%s does not exist.", file);
            JOptionPane.showMessageDialog(
                    this, msg, PROGRAM_TITLE, JOptionPane.ERROR_MESSAGE);
        }
        catch (IllegalArgumentException e)
        {
            //
            // The specified log file contains invalid data.
            // Enable New/Open menu items. Disable Edit/Close menu items.
            //
            menuBar.setMenuItemsEnabled(true, true, false, false);
            String msg = String.format("Invalid data format in %s.", file);
            JOptionPane.showMessageDialog(
                    this, msg, PROGRAM_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }   //openLogFile

    /**
     * This method closes the log file. If the log file has changes, it will prompt the user to
     * save the changes before closing.
     *
     * @param option specifies the choice options for the Confirmation dialog. If allowing
     *               cancel, use JOptionPane.YES_NO_CANCEL_OPTION, otherwise use
     *               JOptionPane.YES_NO_OPTION.
     * @return user choice response: JOptionPane.YES_OPTION, JOptionPane.NO_OPTION,
     *         JOptionPane.CANCEL_OPTION.
     */
    private int closeLogFile(int option)
    {
        int reply = JOptionPane.YES_OPTION;

        //
        // If a log file is opened and there are changes, prompt for the user's confirmation
        // to save the changes before closing.
        //
        if (attendanceLog != null && attendanceLog.isFileDirty())
        {
            reply = JOptionPane.showConfirmDialog(
                    this, "Do you want to save the data before exiting?", PROGRAM_TITLE, option);
        }

        //
        // A log file is opened, contains changes and the user has confirmed to save the changes.
        //
        if (reply == JOptionPane.YES_OPTION && attendanceLog != null && attendanceLog.isFileDirty())
        {
            //
            // Check out all remaining attendants and close the log file.
            //
            attendancePane.checkOutAll();

            try
            {
                attendanceLog.closeLogFile();
                attendanceLog = null;
            }
            catch (FileNotFoundException e)
            {
                //
                // This should never happen.
                // We already verified the log file exists when opening it.
                //
                throw new RuntimeException("Failed saving file.");
            }
        }

        //
        // We are updating and closing the attendance log, so we can now delete the session log if there is one.
        //
        File sessionLog = new File(SESSION_LOG_FILE_NAME);
        if (sessionLog.exists())
        {
            sessionLog.delete();
        }

        return reply;
    }   //closeLogFile

    /**
     * This method reads the session log file if there is one. It will recreate the meeting from the session log.
     *
     * @param sessionLogName specifies the session log file name.
     * @return true if there is a session log file, false otherwise.
     * @throws FileNotFoundException
     */
    private boolean readExistingSessionLog(String sessionLogName) throws FileNotFoundException
    {
        boolean success = false;

        File sessionLogFile = new File(sessionLogName);
        if (sessionLogFile.exists())
        {
            Scanner sessionLog = new Scanner(sessionLogFile);
            String[] sessionInfo = sessionLog.nextLine().trim().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            if (sessionInfo.length != 5)
            {
                sessionLog.close();
                throw new IllegalArgumentException("Invalid meeting info.");
            }

            onCreateMeeting(sessionInfo[0], sessionInfo[1], sessionInfo[2], sessionInfo[3], sessionInfo[4]);
            meetingPane.setMeetingInfo(sessionInfo[0], sessionInfo[1], sessionInfo[2], sessionInfo[3], sessionInfo[4]);
            while (sessionLog.hasNextLine())
            {
                String[] transaction = sessionLog.nextLine().trim().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                Attendant attendant = attendanceLog.findAttendant(
                    transaction[1].substring(1, transaction[1].length() - 1));

                if (transaction[0].equals("CheckIn"))
                {
                    attendancePane.checkInAttendant(attendant, Long.parseLong(transaction[2]), false);
                }
                else if (transaction[0].equals("CheckOut"))
                {
                    attendancePane.checkOutAttendant(attendant, Long.parseLong(transaction[2]), false);
                }
            }
            sessionLog.close();
            success = true;
        }

        return success;
    }   //readExistingSessionLog

    /**
     * This method writes a transaction entry to the session log.
     *
     * @param checkOut specifies true if it is a check-out transaction, false if it is a check-in transaction.
     * @param attendant specifies the attendant.
     * @param timestamp specifies the transaction time.
     */
    public void logTransaction(boolean checkOut, Attendant attendant, long timestamp)
    {
        File sessionFile = new File(SESSION_LOG_FILE_NAME);
        boolean exist = sessionFile.exists();

        try
        {
            PrintStream sessionLog = new PrintStream(new FileOutputStream(sessionFile, exist));

            if (!exist)
            {
                sessionLog.println(attendanceLog.getCurrentSession());
            }
            sessionLog.printf("%s,\"%s\",%d\n", checkOut? "CheckOut": "CheckIn", attendant, timestamp);
            sessionLog.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }   //logTransaction

    //
    // Implements WindowListener interface.
    //

    @Override
    public void windowActivated(WindowEvent e)
    {
    }   //windowActivated

    @Override
    public void windowClosed(WindowEvent e)
    {
    }   //windowClosed

    /**
     * This method is called when the "X" Window Close button is clicked. This will exit the
     * program. If the log file has changes, it will prompt the user to save the changes before
     * exiting. The user has the options to save the changes and exit or discard the changes
     * and exit.
     */
    @Override
    public void windowClosing(WindowEvent e)
    {
        closeLogFile(JOptionPane.YES_NO_OPTION);
    }   //windowClosing

    @Override
    public void windowDeactivated(WindowEvent e)
    {
    }   //windowDeactivated

    @Override
    public void windowDeiconified(WindowEvent e)
    {
    }   //windowDeiconified

    @Override
    public void windowIconified(WindowEvent e)
    {
    }   //windowIconified

    @Override
    public void windowOpened(WindowEvent e)
    {
    }   //windowOpened

}   //class TrcAttendance
