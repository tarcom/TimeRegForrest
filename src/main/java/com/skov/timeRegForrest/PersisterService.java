package com.skov.timeRegForrest;

import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.joda.time.DateTime;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
            PersistenceDataWrapper latestPersistenceDataWrapper = doLoad(PersisterService.PREFIX_FILENAME + latestDate.toString(DATE_PATTERN));

            DateTime yesterday = new DateTime();
            yesterday = yesterday.minusDays(1);

            if (latestDate.isBefore(yesterday)) {
                System.out.println("found latest file, but it is not from today, resetting time, keeping text.");
                //only use text, reset times.
                latestPersistenceDataWrapper.getTimeRegSubmittedTimeMap().get(1);
                latestPersistenceDataWrapper.getTimeRegTimeMap().get(1);

                for (String s : latestPersistenceDataWrapper.getTimeRegTimeMap().keySet()) {
                    latestPersistenceDataWrapper.getTimeRegTimeMap().put(s, 0);
                }

                for (String s : latestPersistenceDataWrapper.getTimeRegSubmittedTimeMap().keySet()) {
                    JButton jButton = latestPersistenceDataWrapper.getTimeRegSubmittedTimeMap().get(s);
                    jButton.setText("           ");
                    latestPersistenceDataWrapper.getTimeRegSubmittedTimeMap().put(s, jButton);
                }
                //done resetting time

                //lets also set office in time to now.
                Calendar officeInCalendar = Calendar.getInstance();
                // for testing: officeInCalendar.set(Calendar.MINUTE, 41);

                int unroundedMinutes = officeInCalendar.get(Calendar.MINUTE);
                int mod = unroundedMinutes % 15;
                officeInCalendar.add(Calendar.MINUTE, mod < 14 ? -mod : (15-mod));
                //officeInCalendar.set(Calendar.MINUTE, unroundedMinutes + mod);

                String officeInCalendarStr = Gui.sdf.format(officeInCalendar.getTime()) + " ?";

                latestPersistenceDataWrapper.setOfficeIn(officeInCalendarStr);



            } else {
                System.out.println("found latest file, it is from today, NOT resetting time and keeping text.");
            }

            return latestPersistenceDataWrapper;
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
