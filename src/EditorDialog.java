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
import java.util.Arrays;

import javax.swing.*;

public class EditorDialog extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private TrcAttendance parent;
    private JPanel textPane = new JPanel();
    private JPanel buttonPane = new JPanel();
    private JTextArea textArea = new JTextArea(20, 36);
    private JLabel instructionsText = new JLabel("Enter new attendant names, one per line.");
    private JButton saveButton = new JButton("Save");
    private JButton cancelButton = new JButton("Cancel");

    public EditorDialog(TrcAttendance parent, String title, boolean modal)
    {
        super(parent.frame, title, modal);
        this.parent = parent;
        textArea.setEditable(true);
        textArea.setFont(parent.smallFont);
        instructionsText.setFont(parent.smallFont);
        saveButton.setFont(parent.smallFont);
        cancelButton.setFont(parent.smallFont);
        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);
        JScrollPane scrollPane = new JScrollPane(
                textArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(textPane);
        contentPane.add(buttonPane);
        textPane.add(scrollPane);
        buttonPane.add(instructionsText);
        buttonPane.add(saveButton);
        buttonPane.add(cancelButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(textPane.getPreferredSize().width + 30,
                textPane.getPreferredSize().height + buttonPane.getSize().height + 90);
    }   //EditorDialog

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

            Point parentLocation = parent.frame.getLocationOnScreen();
            setLocation(parentLocation.x + 30, parentLocation.y + 30);
            setVisible(true);
        }
    }   //open

    private void close(boolean save)
    {
        if (save)
        {
            String[] attendants = textArea.getText().split("\n");
            Arrays.sort(attendants);
            parent.attendanceLog.updateAttendants(attendants);
            parent.attendancePane.updateCheckInList(parent.attendanceLog);
        }
        setVisible(false);
    }   //close

    //
    // Implements ActionListener interface.
    //

    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();

        if (source == saveButton)
        {
            close(true);
        }
        else if (source == cancelButton)
        {
            close(false);
        }
    }   //actionPerformed

}   //class EditorDialog
