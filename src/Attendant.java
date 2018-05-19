/*
 * Titan Robotics Attendance Logger
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

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class implements an attendant. An attendant has a name and the attendance history of
 * all the past meeting sessions. It also records the state of the current meeting session.
 */
public class Attendant
{
    /**
     * This class implements a Comparator comparing the names of two Attendants.
     */
    public static class NameComparator implements Comparator<Attendant>
    {
        /**
         * This method compares the name of attendant1 to the name of attendant2 for
         * sorting purpose.
         *
         * @param attendant1 specifies attendant1 for the comparison.
         * @param attendant2 specifies attendant2 for the comparison.
         * @return positive value if attendant1's name is alphabetically higher than attendant2's
         *         name, 0 if they are equal or negative if attendant1's name is alphabetically
         *         lower than attendant2's name.
         */
        @Override
        public int compare(Attendant attendant1, Attendant attendant2)
        {
            return attendant1.toString().compareTo(attendant2.toString());
        }   //compare

    }   //class NameComparator

    private String name;
    private ArrayList<Long> sessionTimes = new ArrayList<Long>();
    private long currentSessionTime = 0;
    private long checkInTime = 0;

    /**
     * Constructor: Creates an instance of the object.
     *
     * @param name specifies the name of the attendant.
     */
    public Attendant(String name)
    {
        //
        // Remove the quotes if any.
        //
        if (name.charAt(0) == '"' && name.charAt(name.length() - 1) == '"')
        {
            this.name = name.substring(1, name.length() - 1);
        }
        else
        {
            this.name = name;
        }
    }   //Attendant

    /**
     * This method returns the name of the attendant.
     *
     * @return name of the attendant.
     */
    public String toString()
    {
        return name;
    }   //toString

    /**
     * This method is called to check-in the attendant. It records the check-in time for the
     * current meeting session.
     *
     * @param timestamp specifies the check-in time.
     */
    public void checkIn(long timestamp)
    {
        if (checkInTime == 0)
        {
            checkInTime = timestamp;
        }
        else
        {
            throw new IllegalStateException("Attendant " + name + " already checked in.");
        }
    }   //checkIn

    /**
     * This method is called to check-out the attendant. It records the attendance duration
     * for the current meeting session.
     *
     * @param timestamp specifies the check-out time.
     */
    public void checkOut(long timestamp)
    {
        if (checkInTime != 0)
        {
            currentSessionTime += timestamp - checkInTime;
            checkInTime = 0;
        }
        else
        {
            throw new IllegalStateException("Attendant " + name + " was not checked in.");
        }
    }   //checkOut

    /**
     * This method returns the attendance duration of the current meeting session in minutes.
     *
     * @return attendance duration of current meeting session in minutes.
     */
    public long getCurrentSessionMinutes()
    {
        return Math.round(currentSessionTime/60000.0);
    }   //getCurrentSessionMinutes

    /**
     * This method returns the attendance duration of the specified past meeting session
     * in minutes.
     *
     * @param index specifies the index of the past meeting session.
     * @return attendance duration in minutes of specified past meeting sessino.
     */
    public long getSessionMinutes(int index)
    {
        return index < sessionTimes.size()? sessionTimes.get(index): 0;
    }   //getSessionMinutes

    /**
     * This method adds a new session to the session list.
     *
     * @param minutes specifies the attendance duration in minutes of the new session.
     */
    public void addSession(long minutes)
    {
        sessionTimes.add(minutes);
    }   //addSession

}   //class Attendant
