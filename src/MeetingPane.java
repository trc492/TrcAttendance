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

public class MeetingPane implements ActionListener
{
    private TrcAttendance parent;
    private JPanel panel = new JPanel();

    private JCheckBox mechanicalCheckBox = new JCheckBox("Mechanical", false);
    private JCheckBox programmingCheckBox = new JCheckBox("Programming", false);
    private JCheckBox driveCheckBox = new JCheckBox("Drive", false);
    private JCheckBox otherCheckBox = new JCheckBox("Other", false);
    private JButton createMeetingButton = new JButton("Create Meeting");

    private JLabel dateLabel = new JLabel("Date");
    private JFormattedTextField date = new JFormattedTextField();
//    private DatePicker datePicker = new DatePicker(LocalDate.now());

    private SpinnerModel startHourModel = new SpinnerNumberModel(0, 0, 23, 1);
    private SpinnerModel startMinuteModel = new SpinnerNumberModel(0, 0, 59, 15);
    private SpinnerModel endHourModel = new SpinnerNumberModel(0, 0, 23, 1);
    private SpinnerModel endMinuteModel = new SpinnerNumberModel(0, 0, 59, 15);

    private JLabel startTimeLabel = new JLabel("Start Time");
    private JSpinner startTimeHour = new JSpinner(startHourModel);
    private JLabel startTimeColonLabel = new JLabel(":");
    private JSpinner startTimeMinute = new JSpinner(startMinuteModel);

    private JLabel endTimeLabel = new JLabel("End Time");
    private JSpinner endTimeHour = new JSpinner(endHourModel);
    private JLabel endTimeColonLabel = new JLabel(":");
    private JSpinner endTimeMinute = new JSpinner(endMinuteModel);

    private JLabel placeLabel = new JLabel("Place");
    private JTextField place = new JTextField();

    public MeetingPane(TrcAttendance parent)
    {
        this.parent = parent;

        panel.setBorder(BorderFactory.createTitledBorder("Meeting"));
        ((TitledBorder)panel.getBorder()).setTitleFont(parent.smallFont);
        mechanicalCheckBox.setFont(parent.mediumFont);
        programmingCheckBox.setFont(parent.mediumFont);
        driveCheckBox.setFont(parent.mediumFont);
        otherCheckBox.setFont(parent.mediumFont);
        createMeetingButton.setFont(parent.smallFont);

        dateLabel.setFont(parent.smallFont);
        startTimeLabel.setFont(parent.smallFont);
        endTimeLabel.setFont(parent.smallFont);
        placeLabel.setFont(parent.smallFont);

        date.setFont(parent.mediumFont);

        getSpinnerTextField(startTimeHour).setFont(parent.mediumFont);
        startTimeColonLabel.setFont(parent.mediumFont);
        getSpinnerTextField(startTimeMinute).setFont(parent.mediumFont);
        getSpinnerTextField(endTimeHour).setFont(parent.mediumFont);
        endTimeColonLabel.setFont(parent.mediumFont);
        getSpinnerTextField(endTimeMinute).setFont(parent.mediumFont);

        place.setFont(parent.mediumFont);

        dateLabel.setLabelFor(date);
        startTimeLabel.setLabelFor(startTimeHour);
        startTimeColonLabel.setLabelFor(startTimeMinute);
        endTimeLabel.setLabelFor(endTimeHour);
        endTimeColonLabel.setLabelFor(endTimeMinute);
        placeLabel.setLabelFor(place);

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

    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();

        if (source == createMeetingButton)
        {
            String meeting = "";

            if (mechanicalCheckBox.isSelected()) meeting += "Mechanical";

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

            String startTime = String.format("%02d:%02d",
                    getSpinnerTextField(startTimeHour).getValue(),
                    getSpinnerTextField(startTimeMinute).getValue());
            String endTime = String.format("%02d:%02d",
                    getSpinnerTextField(endTimeHour).getValue(),
                    getSpinnerTextField(endTimeMinute).getValue());

            parent.onCreateMeeting(date.getText(), startTime, endTime, place.getText(), meeting);
        }
    }   //actionPerformed

}   //class MeetingPane
