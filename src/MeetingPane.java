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

import java.awt.event.*;
import java.text.*;
import java.time.LocalDateTime;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;

/**
 * This class constructs the Meeting pane. It contains the meeting info such as meeting type,
 * meeting date, start time, end time and meeting place.
 */
public class MeetingPane implements ActionListener
{
    private TrcAttendance parent;
    private JPanel panel = new JPanel();

    //
    // Meeting type.
    //
    private JCheckBox mechanicalCheckBox = new JCheckBox("Mechanical", false);
    private JCheckBox programmingCheckBox = new JCheckBox("Programming", false);
    private JCheckBox driveCheckBox = new JCheckBox("Drive", false);
    private JCheckBox otherCheckBox = new JCheckBox("Other", false);

    //
    // Meeting date.
    //
    private JLabel dateLabel = new JLabel("Date");
    private JFormattedTextField date = new JFormattedTextField();
//    private DatePicker datePicker = new DatePicker(LocalDate.now());

    //
    // Meeting start time.
    //
    private SpinnerModel startHourModel = new SpinnerNumberModel(0, 0, 23, 1);
    private SpinnerModel startMinuteModel = new SpinnerNumberModel(0, 0, 59, 15);
    private JLabel startTimeLabel = new JLabel("Start Time");
    private JSpinner startTimeHour = new JSpinner(startHourModel);
    private JLabel startTimeColonLabel = new JLabel(":");
    private JSpinner startTimeMinute = new JSpinner(startMinuteModel);

    //
    // Meeting end time.
    //
    private SpinnerModel endHourModel = new SpinnerNumberModel(0, 0, 23, 1);
    private SpinnerModel endMinuteModel = new SpinnerNumberModel(0, 0, 59, 15);
    private JLabel endTimeLabel = new JLabel("End Time");
    private JSpinner endTimeHour = new JSpinner(endHourModel);
    private JLabel endTimeColonLabel = new JLabel(":");
    private JSpinner endTimeMinute = new JSpinner(endMinuteModel);

    //
    // Meeting place.
    //
    private JLabel placeLabel = new JLabel("Place");
    private JTextField place = new JTextField();

    private JButton createMeetingButton = new JButton("Create Meeting");

    /**
     * Constructor: Create an instance of the object.
     *
     * @param parent specifies the parent object.
     */
    public MeetingPane(TrcAttendance parent)
    {
        this.parent = parent;

        //
        // Initialize group panel.
        //
        panel.setBorder(BorderFactory.createTitledBorder("Meeting"));
        ((TitledBorder)panel.getBorder()).setTitleFont(parent.smallFont);
        //
        // Initialize meeting type check boxes.
        //
        mechanicalCheckBox.setFont(parent.mediumFont);
        programmingCheckBox.setFont(parent.mediumFont);
        driveCheckBox.setFont(parent.mediumFont);
        otherCheckBox.setFont(parent.mediumFont);
        //
        // Initialize meeting date.
        //
        dateLabel.setFont(parent.smallFont);
        dateLabel.setLabelFor(date);
        date.setFont(parent.mediumFont);
        try
        {
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('0');
            DefaultFormatterFactory dateFormatterFactory =
                    new DefaultFormatterFactory(dateFormatter);
            date.setFormatterFactory(dateFormatterFactory);
        }
        catch (ParseException e)
        {
            //
            // Do nothing. If it failed, it just means we don't have nice format, no big deal.
            //
        }
        //
        // Initialize meeting start and end times.
        //
        startTimeLabel.setFont(parent.smallFont);
        startTimeLabel.setLabelFor(startTimeHour);
        startTimeColonLabel.setFont(parent.mediumFont);
        startTimeColonLabel.setLabelFor(startTimeMinute);
        getSpinnerTextField(startTimeHour).setFont(parent.mediumFont);
        getSpinnerTextField(startTimeMinute).setFont(parent.mediumFont);
        endTimeLabel.setFont(parent.smallFont);
        endTimeLabel.setLabelFor(endTimeHour);
        endTimeColonLabel.setFont(parent.mediumFont);
        endTimeColonLabel.setLabelFor(endTimeMinute);
        getSpinnerTextField(endTimeHour).setFont(parent.mediumFont);
        getSpinnerTextField(endTimeMinute).setFont(parent.mediumFont);
        try
        {
            MaskFormatter timeFormatter = new MaskFormatter("##");
            timeFormatter.setPlaceholderCharacter('0');
            DefaultFormatterFactory timeFormatterFactory =
                    new DefaultFormatterFactory(timeFormatter);
            getSpinnerTextField(startTimeMinute).setFormatterFactory(timeFormatterFactory);
            getSpinnerTextField(endTimeMinute).setFormatterFactory(timeFormatterFactory);
        }
        catch (ParseException e)
        {
            //
            // Do nothing. If it failed, it just means we don't have nice format, no big deal.
            //
        }
        //
        // Initialize meeting place.
        //
        placeLabel.setFont(parent.smallFont);
        placeLabel.setLabelFor(place);
        place.setFont(parent.mediumFont);
        //
        // Initialize Create Meeting button.
        //
        createMeetingButton.setFont(parent.smallFont);

        //
        // Initialize component layout.
        //
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);;
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(mechanicalCheckBox)
                    .addComponent(programmingCheckBox)
                    .addComponent(driveCheckBox)
                    .addComponent(otherCheckBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(dateLabel)
                    .addComponent(startTimeLabel)
                    .addComponent(placeLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(date)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startTimeHour)
                        .addComponent(startTimeColonLabel)
                        .addComponent(startTimeMinute)
                        .addComponent(endTimeLabel)
                        .addComponent(endTimeHour)
                        .addComponent(endTimeColonLabel)
                        .addComponent(endTimeMinute))
                    .addComponent(place)
                    .addComponent(createMeetingButton)));
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(mechanicalCheckBox)
                    .addComponent(dateLabel)
                    .addComponent(date))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(programmingCheckBox)
                    .addComponent(startTimeLabel)
                    .addComponent(startTimeHour)
                    .addComponent(startTimeColonLabel)
                    .addComponent(startTimeMinute)
                    .addComponent(endTimeLabel)
                    .addComponent(endTimeHour)
                    .addComponent(endTimeColonLabel)
                    .addComponent(endTimeMinute))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(driveCheckBox)
                    .addComponent(placeLabel)
                    .addComponent(place))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(otherCheckBox)
                    .addComponent(createMeetingButton)));

        createMeetingButton.addActionListener(this);
        parent.frame.add(panel);
        clearPanel();
    }   //MeetingPane

    /**
     * This method clears all fields in the Meeting pane and disables all controls.
     */
    public void clearPanel()
    {
        mechanicalCheckBox.setSelected(false);
        programmingCheckBox.setSelected(false);
        driveCheckBox.setSelected(false);
        otherCheckBox.setSelected(false);

        date.setValue(null);
        getSpinnerTextField(startTimeHour).setValue(0);
        getSpinnerTextField(startTimeMinute).setValue(0);
        getSpinnerTextField(endTimeHour).setValue(0);
        getSpinnerTextField(endTimeMinute).setValue(0);
        place.setText("");

        setEnabled(false);
    }   //clearPanel

    /**
     * This method enables/disables all controls in the Meeting pane.
     *
     * @param enabled specifies true to enable all controls, false otherwise.
     */
    public void setEnabled(boolean enabled)
    {
        panel.setEnabled(enabled);
        dateLabel.setEnabled(enabled);
        date.setEnabled(enabled);
        startTimeLabel.setEnabled(enabled);
        startTimeHour.setEnabled(enabled);
        startTimeColonLabel.setEnabled(enabled);
        startTimeMinute.setEnabled(enabled);
        endTimeLabel.setEnabled(enabled);
        endTimeHour.setEnabled(enabled);
        endTimeColonLabel.setEnabled(enabled);
        endTimeMinute.setEnabled(enabled);
        placeLabel.setEnabled(enabled);
        place.setEnabled(enabled);
        mechanicalCheckBox.setEnabled(enabled);
        programmingCheckBox.setEnabled(enabled);
        driveCheckBox.setEnabled(enabled);
        otherCheckBox.setEnabled(enabled);
        createMeetingButton.setEnabled(enabled);
    }   //setEnabled

    /**
     * This method auto fills in the meeting date/time/place with default values. Default date
     * is TODAY. Default start time is the current time rounded to the nearest half hour. Default
     * end time is 2 hours from the start time. Default place is the place specified in the
     * command line parameter if one is provided. It's null otherwise.
     */
    public void setDefaultDateTimePlace()
    {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date.setText(dateFormat.format(Calendar.getInstance().getTime()));

        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        if (minute < 15)
        {
            minute = 0;
        }
        else if (minute < 45)
        {
            minute = 30;
        }
        else
        {
            minute = 0;
            hour++;
        }

        getSpinnerTextField(startTimeHour).setValue(hour);
        getSpinnerTextField(startTimeMinute).setValue(minute);
        getSpinnerTextField(endTimeHour).setValue((hour + 2)%24);
        getSpinnerTextField(endTimeMinute).setValue(minute);

        place.setText(TrcAttendance.placeName);
    }   //setDefaultDateTimePlace

    /**
     * This method updates the UI elements with the given meeting info.
     *
     * @param dateText specifies the meeting date.
     * @param startTimeText specifies the meeting start time.
     * @param endTimeText specifies the meeting end time.
     * @param placeText specifies the meeting place.
     * @param meetingTypes specifies the meeting types.
     */
    public void setMeetingInfo(
        String dateText, String startTimeText, String endTimeText, String placeText, String meetingTypes)
    {
        date.setText(dateText);

        String[] startTime = startTimeText.split(":");
        getSpinnerTextField(startTimeHour).setValue(Integer.parseInt(startTime[0]));
        getSpinnerTextField(startTimeMinute).setValue(Integer.parseInt(startTime[1]));

        String[] endTime = endTimeText.split(":");
        getSpinnerTextField(endTimeHour).setValue(Integer.parseInt(endTime[0]));
        getSpinnerTextField(endTimeMinute).setValue(Integer.parseInt(endTime[1]));

        place.setText(placeText);

        String[] types = meetingTypes.split("/");
        for (int i = 0; i < types.length; i++)
        {
            if (types[i].equals("Mechanical"))
            {
                mechanicalCheckBox.setSelected(true);
            }
            else if (types[i].equals("Programming"))
            {
                programmingCheckBox.setSelected(true);
            }
            else if (types[i].equals("Drive"))
            {
                driveCheckBox.setSelected(true);
            }
            else
            {
                otherCheckBox.setSelected(true);
            }
        }
    }   //setMeetingInfo

    /**
     * This method returns the TextField object of the Spinner control.
     *
     * @param spinner specifies the Spinner control for retrieving its TextField object.
     * @return TextField object of the Spinner control.
     */
    private JFormattedTextField getSpinnerTextField(JSpinner spinner)
    {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor)
        {
            return ((JSpinner.DefaultEditor)editor).getTextField();
        }
        else
        {
            throw new RuntimeException("Unexpected editor type: " + editor.getClass());
        }
    }   //getSpinnerTextField

    //
    // Implements ActionListener interface.
    //

    /**
     * This method is called when a Meeting pane control is clicked.
     *
     * @param event specifies the event that caused this callback.
     */
    @Override
    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();

        if (source == createMeetingButton)
        {
            String meeting = "";

            if (mechanicalCheckBox.isSelected()) meeting += "Mechanical";

            //
            // Construct the meeting type string.
            //
            if (programmingCheckBox.isSelected())
            {
                if (meeting.length() > 0) meeting += "/";
                meeting += "Programming";
            }

            if (driveCheckBox.isSelected())
            {
                if (meeting.length() > 0) meeting += "/";
                meeting += "Drive";
            }

            if (otherCheckBox.isSelected())
            {
                if (meeting.length() > 0) meeting += "/";
                meeting += "Other";
            }

            if (meeting.length() == 0) meeting += "Other";

            //
            // Construct the start time and end time strings.
            //
            String startTime = String.format("%02d:%02d",
                    getSpinnerTextField(startTimeHour).getValue(),
                    getSpinnerTextField(startTimeMinute).getValue());
            String endTime = String.format("%02d:%02d",
                    getSpinnerTextField(endTimeHour).getValue(),
                    getSpinnerTextField(endTimeMinute).getValue());

            //
            // Create the new meeting session.
            //
            parent.onCreateMeeting(date.getText(), startTime, endTime, place.getText(), meeting);
        }
    }   //actionPerformed

}   //class MeetingPane
