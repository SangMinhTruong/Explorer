/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explorer;

import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Max
 */
public class RightClickMenu extends JPopupMenu {
    JMenuItem copyItem;
    JMenuItem deleteItem;
    JMenuItem renameItem;
    JMenuItem pasteItem;
    public RightClickMenu() {
        MainForm topFrame = (MainForm) Frame.getFrames()[0];
        FileMenuBar fileMenuBar = topFrame.getFileMenuBar();
        copyItem = fileMenuBar.getCopyItem();
        pasteItem = fileMenuBar.getPasteItem();
        deleteItem = fileMenuBar.getDeleteItem();
        renameItem = fileMenuBar.getRenameItem();
        
        add(copyItem);
        add(pasteItem);
        add(deleteItem);
        add(renameItem);
    }
}

