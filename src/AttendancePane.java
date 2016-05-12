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

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class AttendancePane implements ActionListener
{
    private TrcAttendance parent;
    private JPanel panel = new JPanel();

    private JButton checkInButton = new JButton(" Check in ");
    private JComboBox<Attendant> checkInList = new JComboBox<Attendant>();
    private JButton checkOutButton = new JButton("Check out");
    private JComboBox<Attendant> checkOutList = new JComboBox<Attendant>();

    public AttendancePane(TrcAttendance parent)
    {
        this.parent = parent;

        panel.setBorder(BorderFactory.createTitledBorder("Attendance"));
        ((TitledBorder)panel.getBorder()).setTitleFont(parent.smallFont);
        checkInButton.setFont(parent.smallFont);
        checkOutButton.setFont(parent.smallFont);
        checkInList.setFont(parent.bigFont);
        checkOutList.setFont(parent.bigFont);

        checkInButton.addActionListener(this);
        checkInList.setEditable(false);
        checkOutButton.addActionListener(this);
        checkOutList.setEditable(false);

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);;
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(checkInButton)
                    .addComponent(checkOutButton))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(checkInList)
                    .addComponent(checkOutList)));
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(checkInButton)
                    .addComponent(checkInList))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(checkOutButton)
                    .addComponent(checkOutList)));

        parent.frame.add(panel);
        clearPanel();
    }   //AttendancePane

    public void clearPanel()
    {
        checkInList.removeAllItems();
        checkOutList.removeAllItems();
        setEnabled(false);
    }   //clearPanel

    public void setEnabled(boolean enabled)
    {
        panel.setEnabled(enabled);
        checkInList.setEnabled(enabled);
        checkInButton.setEnabled(enabled);
        checkOutList.setEnabled(enabled);
        checkOutButton.setEnabled(enabled);
    }   //setEnabled

    public void updateCheckInList(AttendanceLog log)
    {
        checkInList.removeAllItems();

        int numAttendants = log.getNumAttendants();
        for (int i = 0; i < numAttendants; i++)
        {
            checkInList.addItem(log.getAttendant(i));
        }
    }   //updateCheckInList

    public void checkOutAll()
    {
        while (checkOutList.getItemCount() > 0)
        {
            checkOutAttendant(checkOutList.getItemAt(0));
        }
    }   //checkOutAll

    private void checkInAttendant(Attendant attendant)
    {
        if (attendant != null)
        {
            attendant.checkIn();
            checkInList.removeItem(attendant);
            checkOutList.addItem(attendant);
            parent.attendanceLog.setFileDirty();
        }
    }   //checkInAttendant

    private void checkOutAttendant(Attendant attendant)
    {
        if (attendant != null)
        {
            attendant.checkOut();
            checkOutList.removeItem(attendant);
            checkInList.addItem(attendant);
            parent.attendanceLog.setFileDirty();
        }
    }   //checkOutAttendant

    //
    // Implements ActionListener interface.
    //

    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();

        if (source == checkInButton)
        {
            checkInAttendant((Attendant)checkInList.getSelectedItem());
        }
        else if (source == checkOutButton)
        {
            checkOutAttendant((Attendant)checkOutList.getSelectedItem());
        }
    }   //actionPerformed

}   //class AttendancePane
