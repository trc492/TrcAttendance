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

/**
 * This class constructs the menu bar of the program. The menu contains only one menu, the
 * File menu.
 */
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

    /**
     * Constructor: Create an instance of the object.
     *
     * @param parent specifies the parent object.
     */
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

        //
        // We start with New/Open enabled and Edit/Close disabled.
        //
        setMenuItemsEnabled(true, true, false, false);
        parent.frame.setJMenuBar(menuBar);
    }   //MenuBar

    /**
     * This method is called to enable/disable the major File menu items.
     *
     * @param newEnabled specifies true to enable File->New menu item, false otherwise.
     * @param openEnabled specifies true to enable File->Open menu item, false otherwise.
     * @param editEnabled specifies true to enable File->Edit menu item, false otherwise.
     * @param closeEnabled specifies true to enable File->Close menu item, false otherwise.
     */
    public void setMenuItemsEnabled(
            boolean newEnabled, boolean openEnabled, boolean editEnabled, boolean closeEnabled)
    {
        fileNewItem.setEnabled(newEnabled);
        fileOpenItem.setEnabled(openEnabled);
        fileEditItem.setEnabled(editEnabled);
        fileCloseItem.setEnabled(closeEnabled);
    }   //setMenuItemsEnabled

    //
    // Implements ActionListener interface.
    //

    /**
     * This method is called when a File menu item is clicked.
     *
     * @param event specifies the event that caused this callback.
     */
    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();

        if (source == fileNewItem)
        {
            //
            // File->New is clicked.
            //
            parent.onFileNew();
        }
        else if (source == fileOpenItem)
        {
            //
            // File->Open is clicked.
            //
            parent.onFileOpen();
        }
        else if (source == fileEditItem)
        {
            //
            // File->Edit is clicked.
            //
            parent.onFileEdit();
        }
        else if (source == fileCloseItem)
        {
            //
            // File->Close is clicked.
            //
            parent.onFileClose();
        }
        else if (source == fileAboutItem)
        {
            //
            // File->About is clicked.
            //
            parent.onFileAbout();
        }
        else if (source == fileExitItem)
        {
            //
            // File->Exit is clicked.
            //
            parent.onFileExit();
        }
    }   //actionPerformed

}   //class MenuBar
