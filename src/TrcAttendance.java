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
import java.text.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TrcAttendance extends JComponent implements WindowListener
{
    private static final long serialVersionUID = 1L;
    private static final String programTitle = "TrcAttendance";
    private static final String programName = "Trc Attendance Logger";
    private static final String copyRight = "Copyright (c) Titan Robotics Club";
    private static final String programVersion = "v0.9.0";

    public static String logFileName = null;
    public static String placeName = "";

    public JFrame frame;

    public Font defaultFont;
    public Font smallFont;
    public Font mediumFont;
    public Font bigFont;

    public MenuBar menuBar;
    public MeetingPane meetingPane;
    public AttendancePane attendancePane;

    public AttendanceLog attendanceLog = null;

    public static void main(String[] args)
    {
        parseArgs(args);
        EventQueue.invokeLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        JFrame frame = new JFrame(programName);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
                        frame.setSize(800, 480);
                        frame.setResizable(false);
                        frame.add(new TrcAttendance(frame));
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    }
                });
    }   //main

    private static void parseArgs(String[] args)
    {
        for (int i = 0; i < args.length; i++)
        {
            String[] fields = args[i].trim().split("=");
            if (fields.length == 2)
            {
                if (fields[0].equalsIgnoreCase("file"))
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

    private TrcAttendance(JFrame frame)
    {
        this.frame = frame;

        defaultFont = UIManager.getDefaults().getFont("TabbedPane.font");
        int defaultSize = defaultFont.getSize();
        smallFont = defaultFont.deriveFont(Font.PLAIN, defaultSize*1.5f);
        mediumFont = defaultFont.deriveFont(Font.PLAIN, defaultSize*2.0f);
        bigFont = defaultFont.deriveFont(Font.PLAIN, defaultSize*4.0f);

        menuBar = new MenuBar(this);
        meetingPane = new MeetingPane(this);
        attendancePane = new AttendancePane(this);

        if (logFileName != null)
        {
            openLogFile(new File(logFileName));
        }

        frame.addWindowListener(this);
    }   //TrcAttendance

    public void onFileNew()
    {
        //TODO:
    }   //onFileNew

    public void onFileOpen()
    {
        //
        // File->Open is clicked, use the FileChooser to prompt for a file.
        //
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter(
                "Spreadsheet text data file (*.csv)", "csv"));
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            //
            // The user clicked "open" approving the file choice.
            //
            openLogFile(fc.getSelectedFile());
        }
    }   //onFileOpen

    public void onFileEdit()
    {
        //TODO:
    }   //onFileEdit

    public void onFileClose()
    {
        //
        // File->Close is clicked, save the file and close the program.
        //
        if (saveLogFile(JOptionPane.YES_NO_CANCEL_OPTION) != JOptionPane.CANCEL_OPTION)
        {
            meetingPane.clearPanel();
            attendancePane.clearPanel();
            menuBar.setMenuItemsEnabled(true, true, false, false);
            attendanceLog = null;
            frame.setTitle(programName);
        }
    }   //onFileClose

    public void onFileAbout()
    {
        //
        // File->About is clicked, display the About message.
        //
        String msg =
                programName + " " + programVersion + "\n" + copyRight + "\n";
        JOptionPane.showMessageDialog(
                null, msg, programTitle, JOptionPane.INFORMATION_MESSAGE);
    }   //onFileAbout

    public void onFileExit()
    {
        //
        // File->Exit is clicked, confirm to save file and close the program.
        //
        if (saveLogFile(JOptionPane.YES_NO_CANCEL_OPTION) != JOptionPane.CANCEL_OPTION)
        {
            System.exit(0);
        }
    }   //onFileExit

    public void onCreateMeeting(
            String date, String startTime, String endTime, String place, String meeting)
    {
        attendanceLog.createSession(date, startTime, endTime, place, meeting);
        menuBar.setMenuItemsEnabled(false, false, false, true);
        meetingPane.setEnabled(false);
        attendancePane.setEnabled(true);
    }   //onCreateMeeting

    private void openLogFile(File file)
    {
        try
        {
            attendanceLog = new AttendanceLog(file);
            int numAttendants = attendanceLog.getNumAttendants();
            for (int i = 0; i < numAttendants; i++)
            {
                attendancePane.addToCheckInList(attendanceLog.getAttendant(i));
            }
            frame.setTitle(programName + " - " + file.getName());
            meetingPane.setDefaultDateTimePlace();
            menuBar.setMenuItemsEnabled(false, false, true, true);
            meetingPane.setEnabled(true);
        }
        catch (FileNotFoundException e)
        {
            menuBar.setMenuItemsEnabled(true, true, false, false);
            String msg = String.format("%s does not exist.", file);
            JOptionPane.showMessageDialog(
                    null, msg, programTitle, JOptionPane.ERROR_MESSAGE);
        }
        catch (ParseException e)
        {
            menuBar.setMenuItemsEnabled(true, true, false, false);
            String msg = String.format("Invalid data format in %s.", file);
            JOptionPane.showMessageDialog(
                    null, msg, programTitle, JOptionPane.ERROR_MESSAGE);
        }
    }   //openLogFile

    private int saveLogFile(int option)
    {
        int reply = JOptionPane.YES_OPTION;

        if (attendanceLog != null && attendanceLog.isFileDirty())
        {
            reply = JOptionPane.showConfirmDialog(
                    null, "Do you want to save the data before exiting?", programTitle, option);
        }

        if (reply == JOptionPane.YES_OPTION && attendanceLog != null && attendanceLog.isFileDirty())
        {
            attendancePane.checkOutAll();

            try
            {
                attendanceLog.closeLogFile();
                attendanceLog = null;
            }
            catch (FileNotFoundException e)
            {
                throw new RuntimeException("Failed saving file.");
            }
        }

        return reply;
    }   //saveLogFile

    //
    // Implements WindowListener interface.
    //

    public void windowActivated(WindowEvent e)
    {
    }   //windowActivated

    public void windowClosed(WindowEvent e)
    {
    }   //windowClosed

    public void windowClosing(WindowEvent e)
    {
        saveLogFile(JOptionPane.YES_NO_OPTION);
    }   //windowClosing

    public void windowDeactivated(WindowEvent e)
    {
    }   //windowDeactivated

    public void windowDeiconified(WindowEvent e)
    {
    }   //windowDeiconified

    public void windowIconified(WindowEvent e)
    {
    }   //windowIconified

    public void windowOpened(WindowEvent e)
    {
    }   //windowOpened

}   //class TrcAttendance
