/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explorer;

import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;

/**
 *
 * @author Max
 */
public class RightClickMenuListener extends MouseAdapter {
    
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
            rightClick(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger())
            rightClick(e);
    }

    private void rightClick(MouseEvent e) {
        
        RightClickMenu menu = new RightClickMenu();
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
