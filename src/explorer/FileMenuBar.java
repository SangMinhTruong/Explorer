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
import org.apache.commons.io.FileUtils;
/**
 *
 * @author Max
 */
public class FileMenuBar extends JMenuBar {
    
    private JMenu fileMenu, editMenu, aboutMenu;

    private JMenuItem copyItem, cutItem, deleteItem, renameItem, pasteItem;

    public JMenuItem getCopyItem() {
        return copyItem;
    }

    public JMenuItem getCutItem() {
        return cutItem;
    }
    
    public JMenuItem getDeleteItem() {
        return deleteItem;
    }

    public JMenuItem getRenameItem() {
        return renameItem;
    }

    public JMenuItem getPasteItem() {
        return pasteItem;
    }
    
    public void setEditOption(int option) {
        editOption = option;
    }
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
        initHelpMenu();
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
        
        copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainForm topFrame = (MainForm) Frame.getFrames()[0];
                ListView list = topFrame.getListView();
                if (!list.isSelectionEmpty())
                {
                    curFile = ((ListEntry) list.getSelectedValue()).getValue();
                    editOption = 1;
                    topFrame.setCurCopyFile(curFile);
                    topFrame.setCurCutFile(null);
                    pasteItem.setEnabled(true);
                }
            }
        });
        cutItem = new JMenuItem("Cut");
        cutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainForm topFrame = (MainForm) Frame.getFrames()[0];
                ListView list = topFrame.getListView();
                if (!list.isSelectionEmpty())
                {
                    curFile = ((ListEntry) list.getSelectedValue()).getValue();
                    editOption = 2;
                    topFrame.setCurCopyFile(null);
                    topFrame.setCurCutFile(curFile);
                    pasteItem.setEnabled(true);
                }
            }
        });
        pasteItem = new JMenuItem("Paste");
        pasteItem.setEnabled(false);
        pasteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainForm topFrame = (MainForm) Frame.getFrames()[0];
                ListView list = topFrame.getListView();
                File newFile = topFrame.getCurFolder();
                try {
                    if (editOption == 1)
                    {   
                        Path source = Paths.get(topFrame.getCurCopyFile().getAbsolutePath());
                        newFile = new File(topFrame.getCurFolder() + topFrame.getCurCopyFile().getName());
                        if (!topFrame.getCurCopyFile().isDirectory())
                            Files.copy(source, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        else
                            FileUtils.copyDirectory(topFrame.getCurCopyFile(), newFile);
                    }
                    else if (editOption == 2)
                    {
                        Path source = Paths.get(topFrame.getCurCutFile().getAbsolutePath());
                        newFile = new File(topFrame.getCurFolder() + topFrame.getCurCutFile().getName());
                        if (!topFrame.getCurCutFile().isDirectory())
                            topFrame.getCurCutFile().renameTo(newFile);
                        else
                            FileUtils.moveDirectory(topFrame.getCurCutFile(), newFile);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(FileMenuBar.class.getName()).log(Level.SEVERE, null, ex);
                }
                list.updateContent(newFile.getParentFile());
                topFrame.setCurCopyFile(null);
                topFrame.setCurCutFile(null);
                pasteItem.setEnabled(false);
            }
        });
        deleteItem = new JMenuItem("Delete");
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
                        if (!file.isDirectory())
                            file.delete();
                        else
                            FileUtils.deleteDirectory(file);
                    } catch (SecurityException ex) {
                        Logger.getLogger(FileMenuBar.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(FileMenuBar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    list.updateContent(file.getParentFile());
                }
            }
        });
        renameItem = new JMenuItem("Rename");
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
        editMenu.add(cutItem);
        editMenu.add(pasteItem);
        editMenu.add(deleteItem);
        editMenu.add(renameItem);
    }
    private void initHelpMenu()
    {
        aboutMenu = new JMenu("Help");
        
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                JOptionPane.showMessageDialog(null, "This program was made by: \n"
                        + "Trương Minh Sang \n"
                        + "Nguyễn Quốc Nam Sang \n"
                        ,"About"
                        ,JOptionPane.INFORMATION_MESSAGE);
            }
        });
        aboutMenu.add(aboutItem);
    }
}
