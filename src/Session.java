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

/**
 * This class implements a meeting session. A meeting session contains 5 pieces of information:
 * meeting date, meeting start time, meeting end time, meeting place and the type of meeting.
 */
public class Session
{
    public static final String[] header = {"Date", "Start Time", "End Time", "Place", "Meeting"};
    private String[] sessionInfo = new String[header.length];

    /**
     * Constructor: Create an instance of the object.
     *
     * @param info specifies the meeting info fields.
     */
    public Session(String[] info)
    {
        //
        // Store the meeting info.
        //
        for (int i = 0; i < header.length; i++)
        {
            sessionInfo[i] = info[i];
        }
    }   //Meeting

    /**
     * This method is called to get a string representation of the meeting info. It concatenates
     * all the info separated by commas. This is primarily used for formatting the meeting info
     * in the CSV log file.
     */
    public String toString()
    {
        String info = sessionInfo[0];
        for (int i = 1; i < header.length; i++)
        {
            info += "," + sessionInfo[i];
        }

        return info;
    }   //toString

    public static String getHeaderString()
    {
        String headerString = header[0];
        for (int i = 1; i < header.length; i++)
        {
            headerString += "," + header[i];
        }

        return headerString;
    }   //getHeaderString

}   //class Meeting
