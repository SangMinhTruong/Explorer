/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explorer;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 *
 * @author Max
 */
public class FileMenuBar extends JMenuBar {
    
    private JMenu fileMenu, editMenu, aboutMenu;
    private int editOption = 0;
    private File curFile;
    public FileMenuBar()
    {
        super();
        initMenu();
        this.add(fileMenu);
        this.add(editMenu);
        this.add(aboutMenu);
    }
    private void initMenu()
    {
        initFileMenu();
        initEditMenu();
        aboutMenu = new JMenu("About");
    }
    private void initFileMenu()
    {
        fileMenu = new JMenu("File");
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);
    }
    
    private void initEditMenu()
    {
        editMenu = new JMenu("Edit");
        
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainForm topFrame = (MainForm) Frame.getFrames()[0];
                ListView list = topFrame.getListView();
                if (!list.isSelectionEmpty())
                {
                    curFile = ((ListEntry) list.getSelectedValue()).getValue();
                    editOption = 1;
                }
                else
                    return;
            }
        });
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainForm topFrame = (MainForm) Frame.getFrames()[0];
                ListView list = topFrame.getListView();
                if (editOption != 0)
                {
                    Path source = Paths.get(curFile.getAbsolutePath());
                    File newFile = new File(topFrame.getCurFolder() + curFile.getName());
                    try {
                        if (editOption == 1)
                            Files.copy(source, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        else if (editOption == 2)
                            curFile.renameTo(newFile);
                    } catch (IOException ex) {
                        Logger.getLogger(FileMenuBar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    list.updateContent(newFile.getParentFile());
                }
                else
                    return;
            }
        });
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                int a = JOptionPane.showConfirmDialog(null, "Delete this file?", "Delete this file?", JOptionPane.YES_NO_OPTION);
                if (a == JOptionPane.YES_OPTION)
                {
                    MainForm topFrame = (MainForm) Frame.getFrames()[0];
                    ListView list = topFrame.getListView();

                    File file;
                    if (!list.isSelectionEmpty())
                        file = ((ListEntry) list.getSelectedValue()).getValue();
                    else
                        return;
                    
                    try {
                        file.delete();
                    } catch (SecurityException ex) {
                        Logger.getLogger(FileMenuBar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    list.updateContent(file.getParentFile());
                }
            }
        });
        JMenuItem renameItem = new JMenuItem("Rename");
        renameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainForm topFrame = (MainForm) Frame.getFrames()[0];
                ListView list = topFrame.getListView();
                
                File file;
                if (!list.isSelectionEmpty())
                    file = ((ListEntry) list.getSelectedValue()).getValue();
                else
                    return;
                
                Path source = Paths.get(file.getAbsolutePath());
                try {
                    String name = (String)JOptionPane.showInputDialog("Rename to:");
                    Files.move(source, source.resolveSibling(name));
                } catch (IOException ex) {
                    Logger.getLogger(FileMenuBar.class.getName()).log(Level.SEVERE, null, ex);
                }
                list.updateContent(file.getParentFile());
            }
        });
        
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.add(deleteItem);
        editMenu.add(renameItem);
    }
}
