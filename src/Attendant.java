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

public class Attendant
{
    private String name;
    private ArrayList<Long> sessionTimes = new ArrayList<Long>();
    private long currentSessionTime = 0;
    private long checkInTime = 0;

    public Attendant(String name)
    {
        if (name.charAt(0) == '"' && name.charAt(name.length() - 1) == '"')
        {
            this.name = name.substring(1, name.length() - 1);
        }
        else
        {
            this.name = name;
        }
    }   //Attendant

    public String toString()
    {
        return name;
    }   //toString

    public void checkIn()
    {
        if (checkInTime == 0)
        {
            checkInTime = System.currentTimeMillis();
        }
        else
        {
            throw new IllegalStateException("Attendant " + name + " already checked in.");
        }
    }   //checkIn

    public void checkOut()
    {
        if (checkInTime != 0)
        {
            currentSessionTime += System.currentTimeMillis() - checkInTime;
            checkInTime = 0;
        }
        else
        {
            throw new IllegalStateException("Attendant " + name + " was not checked in.");
        }
    }   //checkOut

    public long getCurrentSessionMinutes()
    {
        return Math.round(currentSessionTime/60000.0);
    }   //getCurrentSessionMinutes

    public long getSessionMinutes(int index)
    {
        return sessionTimes.get(index);
    }   //getSessionMinutes

    public void addSession(long minutes)
    {
        sessionTimes.add(minutes);
    }   //addSession

}   //class Attendant
