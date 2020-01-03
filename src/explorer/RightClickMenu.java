/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explorer;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.apache.commons.io.FileUtils;
/**
 *
 * @author Max
 */
public class RightClickMenu extends JPopupMenu {
    JMenuItem copyItem;
    JMenuItem cutItem;
    JMenuItem deleteItem;
    JMenuItem renameItem;
    JMenuItem pasteItem;
    
    private int editOption = 0;
    private File curFile;
    
    public RightClickMenu() {
        
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
                    topFrame.getFileMenuBar().getPasteItem().setEnabled(true);
                    topFrame.getFileMenuBar().setEditOption(1);
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
                    topFrame.getFileMenuBar().getPasteItem().setEnabled(true);
                    topFrame.getFileMenuBar().setEditOption(2);
                }
            }
        });
        pasteItem = new JMenuItem("Paste");
        MainForm topFrame = (MainForm) Frame.getFrames()[0];
        if (topFrame.getCurCopyFile() != null)
        {
            editOption = 1;
            pasteItem.setEnabled(true);
        }
        else if (topFrame.getCurCutFile() != null)
        {
            editOption = 2;
            pasteItem.setEnabled(true);
        }
        else
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
                topFrame.getFileMenuBar().getPasteItem().setEnabled(false);
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
        
        
        add(copyItem);
        add(cutItem);
        add(pasteItem);
        add(deleteItem);
        add(renameItem);
    }
}

