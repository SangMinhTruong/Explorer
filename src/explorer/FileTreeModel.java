/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explorer;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
 
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
/**
 *
 * @author Max
 */
public class FileTreeModel implements TreeModel {
    
    private File root;
    public FileTreeModel(File file) {
	this.root = file;
    }
    public FileTreeModel() {
	this.root = new File("Dummy");
    }
    @Override
    public Object getRoot() {
        return this.root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (root.getPath().equals(((File)parent).getPath()))
        {
            return File.listRoots()[index];
        }
        else
        {
            File f = (File) parent;
            
            return f.listFiles(new FileModelFilter())[index];
        }
    }

    @Override
    public int getChildCount(Object parent) {
        if (root.getPath().equals(((File)parent).getPath()))
        {
            return File.listRoots().length;
        }
        else
        {
            File f = (File) parent;

            try {
                if (!f.isDirectory() && f.list() != null) {
                        return 0;
                } else {
                        return f.listFiles(new FileModelFilter()).length;
                }
            } 
            catch (NullPointerException ex) {
                return 0;
            }
        }
    }

    @Override
    public boolean isLeaf(Object node) {
        File f = (File) node;
        return !f.isDirectory() && !root.getPath().equals(f.getPath());
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
            // TODO Auto-generated method stub

    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (root.getPath().equals(((File)parent).getPath()))
        {
            File drive = (File) child;
            return Arrays.asList(File.listRoots()).indexOf(drive);
        }
        else
        {
            File par = (File) parent;
            File ch = (File) child;
            return Arrays.asList(par.listFiles()).indexOf(ch);
        }
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
            // TODO Auto-generated method stub
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
            // TODO Auto-generated method stub
    }
 
}
class FileModelFilter implements FileFilter {
    @Override
    public boolean accept(File current) {
        return current.isDirectory() && !current.isHidden();
    }
}