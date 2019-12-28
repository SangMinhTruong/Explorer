/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explorer;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.SwingUtilities;

/**
 *
 * @author Max
 */
public class ListView extends JList {
    
    public ListView()
    {
        super();
        addControls();
        addListeners();
    }
    public void addControls() {
        
    }
    public void addListeners() {
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listMouseClicked(evt);
            }
        });
        this.addMouseListener(new RightClickMenuListener());
    }
    private void listMouseClicked(java.awt.event.MouseEvent evt) {                                    
        JList list = (JList)evt.getSource();
        if (evt.getClickCount() == 2) {

            ListEntry item = (ListEntry)list.getSelectedValue();
            if (item.getValue().isDirectory())
            {
                MainForm topFrame = (MainForm) Frame.getFrames()[0];
                
                updateContent(item.getValue());
            }
        }
    }    
    public void updateContent(File directory)
    {   
        updateList(directory);
        
        MainForm topFrame = (MainForm) Frame.getFrames()[0];
        topFrame.getDirectoryTextField().setText(directory.getAbsolutePath());
        

        topFrame.getBackStack().push(topFrame.getCurFolder());
        topFrame.getForwardStack().clear();
        topFrame.getBackButton().setEnabled(true);
        topFrame.getForwardButton().setEnabled(false);
        
        topFrame.setCurFolder(directory);
        
    }
    public void updateContentStack(File directory)
    {
        updateList(directory);
        MainForm topFrame = (MainForm) Frame.getFrames()[0];
        topFrame.getDirectoryTextField().setText(directory.getAbsolutePath());
        topFrame.setCurFolder(directory);
    }
    private void updateList(File directory)
    {
        FileListModel model = new FileListModel();
        for (File file : directory.listFiles())
        {
            String resourceStr;
            if (file.isFile())
                resourceStr = "resources/file.png";
            else
                resourceStr = "resources/folder.png";
            model.addElement(new ListEntry(file, new ImageIcon(getClass().getClassLoader().getResource(resourceStr))));
        }
        
        model.sort();
        this.setCellRenderer(new ListIconCellRenderer());
        this.setModel(model);
    }
}
