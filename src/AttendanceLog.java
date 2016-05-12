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

public class AttendanceLog
{
    private File logFile;
    private ArrayList<Attendant> attendantsList = new ArrayList<Attendant>();
    private ArrayList<Session> sessionsList = new ArrayList<Session>();
    private boolean fileDirty = false;
    private Session currentSession = null;

    public AttendanceLog(File file, boolean newFile) throws FileNotFoundException, ParseException
    {
        logFile = file;

        if (!newFile)
        {
            Scanner input = new Scanner(file);
            String[] fields = input.nextLine().trim().split(
                    ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            int numFields = fields.length;

            if (numFields > 5)
            {
                for (int i = 5; i < fields.length; i++)
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

            while (input.hasNextLine())
            {
                fields = input.nextLine().trim().split(",");

                if (fields.length == 0) continue;   //skipping blank line

                if (fields.length == numFields)
                {
                    sessionsList.add(
                            new Session(fields[0], fields[1], fields[2], fields[3], fields[4]));
                    for (int i = 0; i < attendantsList.size(); i++)
                    {
                        attendantsList.get(i).addSession(Long.parseLong(fields[5 + i]));
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

    public void createSession(
            String date, String startTime, String endTime, String place, String meeting)
    {
        if (currentSession == null)
        {
            currentSession = new Session(date, startTime, endTime, place, meeting);
        }
        else
        {
            throw new RuntimeException("A meeting has already been created.");
        }
    }   //createSession

    public int getNumAttendants()
    {
        return attendantsList.size();
    }   //getNumAttendants

    public Attendant getAttendant(int index)
    {
        return attendantsList.get(index);
    }   //getAttendant

    public void updateAttendants(String[] attendants)
    {
        ArrayList<Attendant> newList = new ArrayList<Attendant>();
        int numTransfers = 0;

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

    public boolean isFileDirty()
    {
        return fileDirty;
    }   //isFileDirty

    public void setFileDirty()
    {
        fileDirty = true;
    }   //setFileDirty

    public void closeLogFile() throws FileNotFoundException
    {
        PrintStream output = new PrintStream(logFile);
        output.print("Date,Start Time,End Time,Place,Meeting");

        for (int i = 0; i < attendantsList.size(); i++)
        {
            output.print(",\"" + attendantsList.get(i) + "\"");
        }
        output.println();

        for (int i = 0; i < sessionsList.size(); i++)
        {
            output.print(sessionsList.get(i));
            for (int j = 0; j < attendantsList.size(); j++)
            {
                output.print("," + attendantsList.get(j).getSessionMinutes(i));
            }
            output.println();
        }

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
