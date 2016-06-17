package com.skov.timeRegForrest;

import org.joda.time.DateTime;

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
    private static final String EARLY_DATE = "2000-01-01";
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static PersisterService instance = new PersisterService();

    private PersisterService() {

    }

    public static PersisterService getInstance() {
        return instance;
    }

    public void createToday() {
        doPersist();
    }

    public void doPersist() {
        doPersist(PREFIX_FILENAME + Gui.chooseSavedDataComboBox.getSelectedItem().toString());
    }

    public void doPersist(String filename) {

        PersistenceDataWrapper persistenceDataWrapper = Gui.persistenceDataWrapper;

        persistenceDataWrapper.setOfficeIn(Gui.txtFieldInOffice.getText());
        persistenceDataWrapper.setOfficeOut(Gui.txtFieldOutOffice.getText());

        persistenceDataWrapper.setBreakMorning(Gui.popupIntervalMorningComboBox.getSelectedIndex());
        persistenceDataWrapper.setBreakLunch(Gui.popupIntervalLunchComboBox.getSelectedIndex());
        persistenceDataWrapper.setBreakAfternoon(Gui.popupIntervalAfternoonComboBox.getSelectedIndex());

        System.out.println("persist file=" + filename + "...");
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(persistenceDataWrapper);
            oos.close();
        }catch (Exception e) {
            System.out.println("Cannot persist file!!!" + e);
        }
    }

    public PersistenceDataWrapper doLoadLatest() {
        List<String> storedFiles = getStoredFiles();
        if (storedFiles.size() == 0) {
            return null;
        }

        DateTime latestDate = DateTime.parse(EARLY_DATE);
        for(String storedFile : storedFiles) {
            DateTime storedDate = DateTime.parse(storedFile);
            if (storedDate.isAfter(latestDate)) {
                latestDate = storedDate;
            }
        }

        if (latestDate.isAfter(DateTime.parse(EARLY_DATE))) {
            return doLoad(PersisterService.PREFIX_FILENAME + latestDate.toString(DATE_PATTERN));
        }

        return null;
    }

    public PersistenceDataWrapper doLoad(Gui gui) {
        String filename = PersisterService.PREFIX_FILENAME + gui.chooseSavedDataComboBox.getSelectedItem();
        return doLoad(filename);
    }

    public PersistenceDataWrapper doLoad(String filename) {
        System.out.println("loading file=" + filename + "...");
        PersistenceDataWrapper persistenceDataWrapper = null;
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            persistenceDataWrapper = (PersistenceDataWrapper) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println("Cannot load file!!! filename=" + filename + ", e=" + e);
        }

        return persistenceDataWrapper;
    }

    public List<String> getAvailableFiles() {
        List<String> list = getStoredFiles();

        if (!list.contains(generateTodaysFile())) {
            list.add(generateTodaysFile());
        }

        return list;
    }

    private List<String> getStoredFiles() {
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
        return list;
    }

    public String[] getAvailableFilesStrArr() {
        List<String> availableFiles = getAvailableFiles();
        String[] array = availableFiles.toArray(new String[availableFiles.size()]);
        return array;
    }

    private String generateTodaysFile() {
        return new SimpleDateFormat(DATE_PATTERN).format(new Date());
    }

}
