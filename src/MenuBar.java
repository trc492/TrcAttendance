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

public class MenuBar implements ActionListener
{
    private TrcAttendance parent;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem fileNewItem = new JMenuItem("New");
    private JMenuItem fileOpenItem = new JMenuItem("Open");
    private JMenuItem fileEditItem = new JMenuItem("Edit");
    private JMenuItem fileCloseItem = new JMenuItem("Close");
    private JMenuItem fileAboutItem = new JMenuItem("About");
    private JMenuItem fileExitItem = new JMenuItem("Exit");

    public MenuBar(TrcAttendance parent)
    {
        this.parent = parent;

        fileMenu.setFont(parent.smallFont);
        fileNewItem.setFont(parent.smallFont);
        fileOpenItem.setFont(parent.smallFont);
        fileEditItem.setFont(parent.smallFont);
        fileCloseItem.setFont(parent.smallFont);
        fileAboutItem.setFont(parent.smallFont);
        fileExitItem.setFont(parent.smallFont);

        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        fileNewItem.setMnemonic(KeyEvent.VK_N);
        fileNewItem.addActionListener(this);
        fileMenu.add(fileNewItem);

        fileOpenItem.setMnemonic(KeyEvent.VK_O);
        fileOpenItem.addActionListener(this);
        fileMenu.add(fileOpenItem);

        fileEditItem.setMnemonic(KeyEvent.VK_E);
        fileEditItem.addActionListener(this);
        fileMenu.add(fileEditItem);

        fileCloseItem.setMnemonic(KeyEvent.VK_C);
        fileCloseItem.addActionListener(this);
        fileMenu.add(fileCloseItem);

        fileMenu.addSeparator();

        fileAboutItem.setMnemonic(KeyEvent.VK_A);
        fileAboutItem.addActionListener(this);
        fileMenu.add(fileAboutItem);

        fileExitItem.setMnemonic(KeyEvent.VK_X);
        fileExitItem.addActionListener(this);
        fileMenu.add(fileExitItem);

        setMenuItemsEnabled(true, true, false, false);
        parent.frame.setJMenuBar(menuBar);
    }   //createMenuBar

    public void setMenuItemsEnabled(
            boolean newEnabled, boolean openEnabled, boolean editEnabled, boolean closeEnabled)
    {
//        fileNewItem.setEnabled(newEnabled);
      fileNewItem.setEnabled(false);
        fileOpenItem.setEnabled(openEnabled);
//        fileEditItem.setEnabled(editEnabled);
      fileEditItem.setEnabled(false);
        fileCloseItem.setEnabled(closeEnabled);
    }   //setMenuItemsEnabled

    //
    // Implements ActionListener interface.
    //

    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();

        if (source == fileNewItem)
        {
            parent.onFileNew();
        }
        else if (source == fileOpenItem)
        {
            parent.onFileOpen();
        }
        else if (source == fileEditItem)
        {
            parent.onFileEdit();
        }
        else if (source == fileCloseItem)
        {
            parent.onFileClose();
        }
        else if (source == fileAboutItem)
        {
            parent.onFileAbout();
        }
        else if (source == fileExitItem)
        {
            parent.onFileExit();
        }
    }   //actionPerformed

}   //class MenuBar
