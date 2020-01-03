/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explorer;

import java.io.File;
import javax.swing.ImageIcon;
/**
 *
 * @author Max
 */
public class ListEntry {
    private File file;
    private ImageIcon icon;
  
    public ListEntry(File file, ImageIcon icon) {
       this.file = file;
       this.icon = icon;
    }

    public File getValue() {
       return file;
    }

    public ImageIcon getIcon() {
       return icon;
    }

    public String toString() {
       return file.getName();
    }
}
