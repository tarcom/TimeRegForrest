package com.skov.timeRegForrest;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ALSK on 22-01-2016.
 */
public class PersisterService {

    public final static String PREFIX_FILENAME = "TimeRegForrestData_";

    public static void doPersist() {

        PersistanceDataWrapper persistanceDataWrapper = Gui.persistanceDataWrapper;
        String filename = (String) Gui.chooseSavedDataComboBox.getSelectedItem();

        System.out.println("persist file=" + filename + "...");
        try {
            FileOutputStream fos = new FileOutputStream(PREFIX_FILENAME + filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(persistanceDataWrapper);
            oos.close();
        }catch (Exception e) {
            System.out.println("Cannot persist file!!!" + e);
        }
    }


    public static PersistanceDataWrapper doLoad(Gui gui) {
        String filename = PersisterService.PREFIX_FILENAME + gui.chooseSavedDataComboBox.getSelectedItem();
        System.out.println("loading file=" + filename + "...");
        PersistanceDataWrapper persistanceDataWrapper = null;
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            persistanceDataWrapper = (PersistanceDataWrapper) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println("Cannot load file!!! filename=" + filename + ", e=" + e);
        }
        return persistanceDataWrapper;
    }

    public static List<String> getAvaiableFiles() {
        List<String> list = new ArrayList<String>();

        File folder = new File(".");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                if (listOfFiles[i].getName().contains(PREFIX_FILENAME)) {
                    list.add(listOfFiles[i].getName().replace(PREFIX_FILENAME, ""));
                }
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }

        if (!list.contains(generateTodaysFile())) {
            list.add(generateTodaysFile());
        }

        return list;
    }

    public static String[] getAvaiableFilesStrArr() {
        List<String> avaiableFiles = getAvaiableFiles();
        String[] array = avaiableFiles.toArray(new String[avaiableFiles.size()]);
        return array;
    }

    private static String generateTodaysFile() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

}
