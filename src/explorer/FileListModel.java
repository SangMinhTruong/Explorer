/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.AbstractListModel;

/**
 *
 * @author Max
 */
public class FileListModel extends AbstractListModel {
    
    ArrayList<ListEntry> files;
    public FileListModel()
    {
        files = new ArrayList<>();
    }
    public FileListModel(ArrayList<ListEntry> array)
    {
        files = array;
    }
    public void addElement(ListEntry f){
        files.add(f);
    }
    @Override
    public int getSize() {
        return files.size();
    }

    @Override
    public Object getElementAt(int index) {
        return (ListEntry)files.get(index);
    }
    
    public void sort(){
        Collections.sort(files, new ListComparator());
    }
}

class ListComparator implements Comparator<ListEntry> {

    @Override
    public int compare(ListEntry f1, ListEntry f2) {
        if (f1.getValue().isDirectory())
        {
            if (f2.getValue().isDirectory())
            {
                return f1.getValue().getName().compareTo(f2.getValue().getName());
            }
            else
            {
                return -1;
            }
        }
        else
        {
            if (f2.getValue().isDirectory())
            {
                return 1;
            }
            else
            {
                return f1.getValue().getName().compareTo(f2.getValue().getName());
            }
        }
    }
}