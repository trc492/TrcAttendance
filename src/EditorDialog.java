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

import javax.swing.*;

/**
 * This class constructs an Editor dialog and populates it with the list of attendants allowing
 * the user to edit the list, adding or removing attendants. The Editor dialog contains an
 * editable TextArea, a Save button and a Cancel button. The TextArea is filled with names of
 * the attendants, one per line. The Save and Cancel buttons will close the Editor dialog. The
 * Save button will update the Attendants list with the edited list whereas the Cancel button
 * will discard the changes and leaves the attendant list unchanged.
 */
public class EditorDialog extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private TrcAttendance parent;
    //
    // Text pane controls.
    //
    private JPanel textPane = new JPanel();
    private JTextArea textArea = new JTextArea(20, 36);
    //
    // Button pane controls.
    //
    private JPanel buttonPane = new JPanel();
    private JLabel instructionsText = new JLabel("Enter new attendant names, one per line.");
    private JButton saveButton = new JButton("Save");
    private JButton cancelButton = new JButton("Cancel");

    /**
     * Constructor: Creates an instance of the object.
     *
     * @param parent specifies the parent object.
     * @param title specifies the title of the dialog window.
     * @param modal specifies true if the dialog window is modal, false otherwise.
     */
    public EditorDialog(TrcAttendance parent, String title, boolean modal)
    {
        super(parent.frame, title, modal);
        this.parent = parent;
        //
        // Initialize the Text pane controls.
        //
        textArea.setEditable(true);
        textArea.setFont(parent.smallFont);
        JScrollPane scrollPane = new JScrollPane(
                textArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textPane.add(scrollPane);
        //
        // Initialize the Button pane controls.
        //
        instructionsText.setFont(parent.smallFont);
        saveButton.setFont(parent.smallFont);
        saveButton.addActionListener(this);
        cancelButton.setFont(parent.smallFont);
        cancelButton.addActionListener(this);
        buttonPane.add(instructionsText);
        buttonPane.add(saveButton);
        buttonPane.add(cancelButton);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(textPane);
        contentPane.add(buttonPane);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        //
        // Determine the proper size of the dialog.
        //
        setSize(textPane.getPreferredSize().width + 30,
                textPane.getPreferredSize().height + buttonPane.getSize().height + 90);
    }   //EditorDialog

    /**
     * This method opens the Editor dialog by populating it with all the attendant names from
     * the AttendanceLog.
     *
     * @param log specifies the AttendanceLog object.
     */
    public void open(AttendanceLog log)
    {
        if (log != null)
        {
            int numAttendants = log.getNumAttendants();

            textArea.setText("");
            for (int i = 0; i < numAttendants; i++)
            {
                Attendant attendant = log.getAttendant(i);
                textArea.append(attendant.toString() + "\n");
            }

            //
            // Determines the location of the Editor dialog.
            //
            Point parentLocation = parent.frame.getLocationOnScreen();
            setLocation(parentLocation.x + 30, parentLocation.y + 30);
            setVisible(true);
        }
    }   //open

    //
    // Implements ActionListener interface.
    //

    /**
     * This method is called when a Editor Dialog control is clicked.
     *
     * @param event specifies the event that caused this callback.
     */
    @Override
    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();

        if (source == saveButton)
        {
            //
            // Close the Editor and save the changes.
            //
            String[] attendants = textArea.getText().split("\n");
            parent.attendanceLog.updateAttendants(attendants);
            parent.attendancePane.updateLists(parent.attendanceLog);
            setVisible(false);
        }
        else if (source == cancelButton)
        {
            //
            // Close the Editor and discard the changes.
            //
            setVisible(false);
        }
    }   //actionPerformed

}   //class EditorDialog
