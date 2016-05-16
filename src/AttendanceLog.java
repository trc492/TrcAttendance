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

import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * This class implements an attendance log. It contains an ArrayList of attendants, an ArrayList
 * of past meeting sessions as well as the current meeting session.
 */
public class AttendanceLog
{
    private File logFile;
    private ArrayList<Attendant> attendantsList = new ArrayList<Attendant>();
    private ArrayList<Session> sessionsList = new ArrayList<Session>();
    private boolean fileDirty = false;
    private Session currentSession = null;

    /**
     * Constructor: Create an instance of the object.
     *
     * @param file specifies the log file.
     * @param newFile specifies true if the log file is new, false otherwise.
     * @throws FileNotFoundException if newFile is false but the specified file does not exist.
     * @throws ParseException if the log file contains invalid data.
     */
    public AttendanceLog(File file, boolean newFile) throws FileNotFoundException, ParseException
    {
        logFile = file;

        if (!newFile)
        {
            //
            // Parse the header line into array of fields using comma as separator but
            // ignoring commas inside quotes.
            //
            Scanner input = new Scanner(file);
            String[] fields = input.nextLine().trim().split(
                    ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            int numFields = fields.length;

            //
            // The first 5 fields are: Date/Start Time/End Time/Place/Meeting.
            // The rest are names of attendants.
            //
            if (numFields > Session.header.length)
            {
                //
                // Create a new attendant for each attendant name.
                //
                for (int i = Session.header.length; i < fields.length; i++)
                {
                    attendantsList.add(new Attendant(fields[i]));
                }
            }
            else
            {
                input.close();
                throw new ParseException(
                        "Data file must have at least 6 fields", fields.length);
            }

            //
            // Parse each line in the log file as a meeting session.
            //
            while (input.hasNextLine())
            {
                fields = input.nextLine().trim().split(",");

                // Skip a blank line.
                if (fields.length == 0) continue;

                //
                // The total number of fields should match the number of header fields.
                //
                if (fields.length == numFields)
                {
                    //
                    // Create and add a new meeting session.
                    //
                    sessionsList.add(new Session(fields));
                    //
                    // Add session time of each attendant.
                    //
                    for (int i = 0; i < attendantsList.size(); i++)
                    {
                        attendantsList.get(i).addSession(
                                Long.parseLong(fields[Session.header.length + i]));
                    }
                }
                else
                {
                    input.close();
                    throw new ParseException(
                            "Invalid data file (incorrect number of fields.", fields.length);
                }
            }

            input.close();
        }
    }   //AttendanceLog

    /**
     * This method create a new meeting session with the specified meeting info as the current
     * meeting session.
     *
     * @param info specifies the meeting info.
     */
    public void createSession(String[] info)
    {
        if (currentSession == null)
        {
            currentSession = new Session(info);
        }
        else
        {
            throw new RuntimeException("A meeting has already been created.");
        }
    }   //createSession

    /**
     * This method returns the total number of attendants.
     *
     * @return number of attendants.
     */
    public int getNumAttendants()
    {
        return attendantsList.size();
    }   //getNumAttendants

    /**
     * This method returns the attendant of the specified index.
     *
     * @param index specifies the index of the attendant to be returned.
     * @return attendant of the specified index.
     */
    public Attendant getAttendant(int index)
    {
        return attendantsList.get(index);
    }   //getAttendant

    /**
     * This method determines if the specified attendant is in the attendants list.
     *
     * @param attendant specifies the attendant to look for in the list.
     * @return true if the specified attendant is in the list, false otherwise.
     */
    public boolean contains(Attendant attendant)
    {
        return attendantsList.contains(attendant);
    }   //contains

    /**
     * This method updates the attendants list from the result of the Editor dialog. It sorted
     * the Editor dialog list, eliminated duplicates if any and cross-checked each attendant's
     * name against the existing attendants list. If the attendant is already in the old list,
     * it is transferred to the new list. If it is not found in the old list, a new attendant
     * is created and added to the new list. At the end, the new list replaces the old list.
     *
     * @param attendants specifies the list of attendant names from the Editor dialog.
     */
    public void updateAttendants(String[] attendants)
    {
        ArrayList<Attendant> newList = new ArrayList<Attendant>();
        int numTransfers = 0;

        Arrays.sort(attendants);
        for (int i = 0; i < attendants.length; i++)
        {
            String name = attendants[i].trim();

            //
            // Eliminate duplicates.
            //
            if (i > 0 && attendants[i - 1].trim().equals(name)) continue;

            if (name.length() > 0)
            {
                Attendant existing = findAttendant(name);
                if (existing != null)
                {
                    newList.add(existing);
                    numTransfers++;
                }
                else
                {
                    newList.add(new Attendant(attendants[i]));
                    fileDirty = true;
                }
            }
        }

        if (!fileDirty && numTransfers != attendantsList.size())
        {
            fileDirty = true;
        }

        attendantsList = newList;
    }   //updateAttendants

    /**
     * This method determines if the log file has changed.
     *
     * @return true if the log file has changed, false otherwise.
     */
    public boolean isFileDirty()
    {
        return fileDirty;
    }   //isFileDirty

    /**
     * This method sets the state of the log file to indicate it has changed.
     */
    public void setFileDirty()
    {
        fileDirty = true;
    }   //setFileDirty

    /**
     * This method writes all the info to the log file and closes it.
     *
     * @throws FileNotFoundException
     */
    public void closeLogFile() throws FileNotFoundException
    {
        PrintStream output = new PrintStream(logFile);
        //
        // Print header line.
        //
        output.print(Session.getHeaderString());
        for (int i = 0; i < attendantsList.size(); i++)
        {
            output.print(",\"" + attendantsList.get(i) + "\"");
        }
        output.println();
        //
        // Print each meeting session in a separate line.
        //
        for (int i = 0; i < sessionsList.size(); i++)
        {
            output.print(sessionsList.get(i));
            for (int j = 0; j < attendantsList.size(); j++)
            {
                output.print("," + attendantsList.get(j).getSessionMinutes(i));
            }
            output.println();
        }
        //
        // Print the current meeting session.
        //
        if (currentSession != null)
        {
            output.print(currentSession);
            for (int i = 0; i < attendantsList.size(); i++)
            {
                output.print("," + attendantsList.get(i).getCurrentSessionMinutes());
            }
            output.println();
        }

        output.close();
        fileDirty = false;
        attendantsList = null;
        sessionsList = null;
        currentSession = null;
        logFile = null;
    }   //closeLogFile

    /**
     * This method returns the attendant with the specified name.
     *
     * @param name specifies the name of the attendant to look for.
     * @return attendant with the specified name.
     */
    private Attendant findAttendant(String name)
    {
        for (int i = 0; i < attendantsList.size(); i++)
        {
            Attendant attendant = attendantsList.get(i);
            if (name.equals(attendant.toString()))
            {
                return attendant;
            }
        }

        return null;
    }   //findAttendant

}   //class AttendanceLog
