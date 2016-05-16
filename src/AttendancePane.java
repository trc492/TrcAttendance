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
import java.util.Comparator;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * This class constructs the Attendance pane. It contains a check-in list and a check-out list.
 * When the log file is first opened, all attendants are put into the check-in list. When an
 * attendant checks in, he/she will be moved to the check-out list. When the attendant checks
 * out, the attendance time is recorded and the attendant will be moved back to the check-in
 * list.
 */
public class AttendancePane implements ActionListener
{
    /**
     * This class implements the SortedComboBoxModel that allows the elements in a ComboBox to
     * be added in sorted order.
     */
    private class SortedComboBoxModel<E> extends DefaultComboBoxModel<E>
    {
        private static final long serialVersionUID = 1L;
        private Comparator<E> comparator;

        /**
         * Constructor: Create an instance of the object.
         *
         * @param comparator specifies the custom comparator.
         */
        public SortedComboBoxModel(Comparator<E> comparator)
        {
            super();
            this.comparator = comparator;
        }   //SortedComboBoxModel

        /**
         * This method inserts the specified element into the ComboBox at the sorted position.
         *
         * @param element specifies the element to be inserted into the ComboBox.
         */
        @Override
        public void addElement(E element)
        {
            int size = getSize();
            int pos;
            //
            // Find the appropriate position to insert the element.
            //
            for (pos = 0; pos < size; pos++)
            {
                E e = getElementAt(pos);
                if (comparator.compare(element, e) < 0)
                {
                    break;
                }
            }
            super.insertElementAt(element, pos);
        }   //addElement

    }   //class SortedComboBoxModel

    private TrcAttendance parent;
    private JPanel panel = new JPanel();
    //
    // Check-in controls.
    //
    private JButton checkInButton = new JButton(" Check in ");
    private SortedComboBoxModel<Attendant> checkInListModel =
                new SortedComboBoxModel<Attendant>(new Attendant.NameComparator());
    private JComboBox<Attendant> checkInList = new JComboBox<Attendant>(checkInListModel);
    //
    // Check-out controls.
    //
    private JButton checkOutButton = new JButton("Check out");
    private SortedComboBoxModel<Attendant> checkOutListModel =
                new SortedComboBoxModel<Attendant>(new Attendant.NameComparator());
    private JComboBox<Attendant> checkOutList = new JComboBox<Attendant>(checkOutListModel);

    /**
     * Constructor: Create an instance of the object.
     *
     * @param parent specifies the parent object.
     */
    public AttendancePane(TrcAttendance parent)
    {
        this.parent = parent;
        //
        // Initialize group panel.
        //
        panel.setBorder(BorderFactory.createTitledBorder("Attendance"));
        ((TitledBorder)panel.getBorder()).setTitleFont(parent.smallFont);
        //
        // Initialize check-in controls.
        //
        checkInButton.setFont(parent.smallFont);
        checkInButton.addActionListener(this);
        checkInList.setFont(parent.bigFont);
        checkInList.setEditable(false);
        //
        // Initialize check-out controls.
        //
        checkOutButton.setFont(parent.smallFont);
        checkOutButton.addActionListener(this);
        checkOutList.setFont(parent.bigFont);
        checkOutList.setEditable(false);

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

    /**
     * This method clears the check-in and check-out lists. It also disables all controls.
     */
    public void clearPanel()
    {
        checkInList.removeAllItems();
        checkOutList.removeAllItems();
        setEnabled(false);
    }   //clearPanel

    /**
     * This method enables/disables all controls in the Attendance pane.
     *
     * @param enabled specifies true to enable all controls, false otherwise.
     */
    public void setEnabled(boolean enabled)
    {
        panel.setEnabled(enabled);
        checkInList.setEnabled(enabled);
        checkInButton.setEnabled(enabled);
        checkOutList.setEnabled(enabled);
        checkOutButton.setEnabled(enabled);
    }   //setEnabled

    /**
     * This method clears the check-in and check-out lists.
     */
    public void clearLists()
    {
        checkInList.removeAllItems();
        checkOutList.removeAllItems();
    }   //clearLists

    /**
     * This method updates the check-in and check-out lists according to the attendants in the
     * AttendanceLog.
     *
     * @param log specifies the AttendanceLog object.
     */
    public void updateLists(AttendanceLog log)
    {
        int numAttendants = log.getNumAttendants();

        for (int i = 0; i < numAttendants; i++)
        {
            Attendant attendant = log.getAttendant(i);
            if (checkInListModel.getIndexOf(attendant) == -1 &&
                checkOutListModel.getIndexOf(attendant) == -1)
            {
                //
                // Add new attendant.
                //
                checkInList.addItem(attendant);
            }
        }

        for (int i = checkInList.getItemCount() - 1; i >= 0; i--)
        {
            if (!parent.attendanceLog.contains(checkInList.getItemAt(i)))
            {
                //
                // The attendant has been removed, get rid of it from the check-in list.
                //
                checkInList.removeItemAt(i);
            }
        }

        for (int i = checkOutList.getItemCount() - 1; i >= 0; i--)
        {
            if (!parent.attendanceLog.contains(checkOutList.getItemAt(i)))
            {
                //
                // The attendant has been removed, get rid of it from the check-out list.
                //
                checkOutList.removeItemAt(i);
            }
        }

        if (checkInList.getItemCount() > 0)
        {
            checkInList.setSelectedIndex(0);
        }

        if (checkOutList.getItemCount() > 0)
        {
            checkOutList.setSelectedIndex(0);
        }
    }   //updateLists

    /**
     * This method checks out all the attendants from the check-out list.
     */
    public void checkOutAll()
    {
        while (checkOutList.getItemCount() > 0)
        {
            checkOutAttendant(checkOutList.getItemAt(0));
        }
    }   //checkOutAll

    /**
     * This method checks in the selected attendant by moving the attendant from the check-in
     * list to the check-out list and mark the log file as dirty.
     *
     * @param attendant
     */
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

    /**
     * This method checks out the selected attendant by moving the attendant from the check-out
     * list back to the check-in list and mark the log file as dirty.
     *
     * @param attendant
     */
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

    /**
     * This method is called when a Attendance pane control is clicked.
     *
     * @param event specifies the event that caused this callback.
     */
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
