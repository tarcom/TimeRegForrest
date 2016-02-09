package com.skov.timeRegForrest;

import javax.swing.*;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by ALSK on 22-01-2016.
 */
public class PersistanceDataWrapper implements Serializable {


    private HashMap<String, JTextField> jiraNumbersMap = new HashMap<String, JTextField>();
    private HashMap<String, JButton> timeRegSubmittedTimeMap = new HashMap<String, JButton>();
    private HashMap<String, JTextField> descriptionMap = new HashMap<String, JTextField>();
    private HashMap<String, JButton> timeRegNameMap = new HashMap<String, JButton>();
    private HashMap<String, Integer> timeRegTimeMap = new HashMap<String, Integer>();

    public PersistanceDataWrapper() {
    }

    public HashMap<String, JTextField> getJiraNumbersMap() {
        return jiraNumbersMap;
    }





    public void setJiraNumbersMap(HashMap<String, JTextField> jiraNumbersMap) {
        this.jiraNumbersMap = jiraNumbersMap;
    }

    public HashMap<String, JButton> getTimeRegSubmittedTimeMap() {
        return timeRegSubmittedTimeMap;
    }

    public void setTimeRegSubmittedTimeMap(HashMap<String, JButton> timeRegSubmittedTimeMap) {
        this.timeRegSubmittedTimeMap = timeRegSubmittedTimeMap;
    }

    public HashMap<String, JTextField> getDescriptionMap() {
        return descriptionMap;
    }

    public void setDescriptionMap(HashMap<String, JTextField> descriptionMap) {
        this.descriptionMap = descriptionMap;
    }

    public HashMap<String, JButton> getTimeRegNameMap() {
        return timeRegNameMap;
    }

    public void setTimeRegNameMap(HashMap<String, JButton> timeRegNameMap) {
        this.timeRegNameMap = timeRegNameMap;
    }

    public HashMap<String, Integer> getTimeRegTimeMap() {
        return timeRegTimeMap;
    }

    public void setTimeRegTimeMap(HashMap<String, Integer> timeRegTimeMap) {
        this.timeRegTimeMap = timeRegTimeMap;
    }
}
