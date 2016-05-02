package com.skov.timeRegForrest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

/**
 * Created by ALSK on 13-01-2016.
 */
public class ActionPerformedHandler  implements ActionListener {

    static Gui gui;

    public ActionPerformedHandler(Gui gui) {
        this.gui = gui;
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().endsWith("Plus")) {
            handlePlus(e);
            PersisterService.doPersist();
        } else if (e.getActionCommand().endsWith("Minus")) {
            handleMinus(e);
            PersisterService.doPersist();
        } else if (e.getActionCommand().startsWith("XP-") && e.getActionCommand().length() >= 4) {
            handleOpenJira(e);
        } else if (e.getActionCommand().startsWith(gui.LIST_JIRAS_BUTTON_PRESSED)) {
            handleShowJiraListPopup(e);
        } else if (e.getActionCommand().equals("RESET")) {
            handleResetPressed();
        } else if (e.getActionCommand().equals(Gui.LOAD_FILE)) {
            handleLoadFile();
        }

        gui.handleSetTIme();
    }

    public static void handleLoadFile() {
        PersistanceDataWrapper persistanceDataWrapperCOPY = PersisterService.doLoad(gui);

        for (String key : persistanceDataWrapperCOPY.getTimeRegTimeMap().keySet()) {
            Gui.persistanceDataWrapper.getTimeRegTimeMap().put(key, persistanceDataWrapperCOPY.getTimeRegTimeMap().get(key));
        }

        for (String key : persistanceDataWrapperCOPY.getTimeRegSubmittedTimeMap().keySet()) {
            Gui.persistanceDataWrapper.getTimeRegSubmittedTimeMap().get(key).setText(persistanceDataWrapperCOPY.getTimeRegSubmittedTimeMap().get(key).getText());
        }


        for (String key : persistanceDataWrapperCOPY.getTimeRegNameMap().keySet()) {
            Gui.persistanceDataWrapper.getTimeRegNameMap().put(key, persistanceDataWrapperCOPY.getTimeRegNameMap().get(key));
        }


        for (String key : persistanceDataWrapperCOPY.getJiraNumbersMap().keySet()) {
            Gui.persistanceDataWrapper.getJiraNumbersMap().get(key).setText(persistanceDataWrapperCOPY.getJiraNumbersMap().get(key).getText());
        }


        for (String key : persistanceDataWrapperCOPY.getDescriptionMap().keySet()) {
            Gui.persistanceDataWrapper.getDescriptionMap().get(key).setText(persistanceDataWrapperCOPY.getDescriptionMap().get(key).getText());
        }
    }

    private void handleResetPressed() {
        Gui.persistanceDataWrapper.getTimeRegSubmittedTimeMap().get(1);
        Gui.persistanceDataWrapper.getTimeRegTimeMap().get(1);

        for (String s : Gui.persistanceDataWrapper.getTimeRegTimeMap().keySet()) {
            Gui.persistanceDataWrapper.getTimeRegTimeMap().put(s, 0);
        }

        for (String s : Gui.persistanceDataWrapper.getTimeRegSubmittedTimeMap().keySet()) {
            JButton jButton = Gui.persistanceDataWrapper.getTimeRegSubmittedTimeMap().get(s);
            jButton.setText("           ");
            Gui.persistanceDataWrapper.getTimeRegSubmittedTimeMap().put(s, jButton);
        }
    }

    private void handleShowJiraListPopup(ActionEvent e) {
        Object[] possibilities = ListOfIssues.getListOfInterestingJirasStr();
        String s = (String) JOptionPane.showInputDialog(
                gui.frame,
                "Choose a task:",
                "Task popup picker",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                possibilities[0]);

        JTextField jTextFieldJira = Gui.persistanceDataWrapper.getJiraNumbersMap().get(e.getActionCommand().replace(gui.LIST_JIRAS_BUTTON_PRESSED, ""));
        jTextFieldJira.setText(ListOfIssues.getJiraFromStr(s));

        JTextField jTextFieldDesc = Gui.persistanceDataWrapper.getDescriptionMap().get(e.getActionCommand().replace(gui.LIST_JIRAS_BUTTON_PRESSED, ""));
        jTextFieldDesc.setText(ListOfIssues.getDescriptionFromStr(s));
    }

    private void handleOpenJira(ActionEvent e) {
        System.out.println(e.getActionCommand());
        String shortCutKey = e.getActionCommand();
        JTextField jTextField = Gui.persistanceDataWrapper.getJiraNumbersMap().get(shortCutKey.replace("XP-", ""));
        String jiraNumber = jTextField.getText().trim();
        openUri("http://features.nykreditnet.net/browse/" + jiraNumber);
    }

    private void handleMinus(ActionEvent e) {
        String minusName = e.getActionCommand().replace("Minus", "");

        Gui.persistanceDataWrapper.getTimeRegTimeMap().put(minusName, Gui.persistanceDataWrapper.getTimeRegTimeMap().get(minusName) - gui.getSubmitDurationMinutes());
        Gui.persistanceDataWrapper.getTimeRegSubmittedTimeMap().get(minusName).setText(gui.convertMinutesToHouersAndMinutes(Gui.persistanceDataWrapper.getTimeRegTimeMap().get(minusName)));
    }

    private void handlePlus(ActionEvent e) {
        String plusName = e.getActionCommand().replace("Plus", "");

        Gui.persistanceDataWrapper.getTimeRegTimeMap().put(plusName, Gui.persistanceDataWrapper.getTimeRegTimeMap().get(plusName) + gui.getSubmitDurationMinutes());
        String time = gui.convertMinutesToHouersAndMinutes(Gui.persistanceDataWrapper.getTimeRegTimeMap().get(plusName));
        String submitTimeTxt = "<html><FONT color=\"#000099\"><U>" + time + "</U></FONT></HTML>";


        Gui.persistanceDataWrapper.getTimeRegSubmittedTimeMap().get(plusName).setText(submitTimeTxt);

        if (gui.autoMinimizeCheckBox.isSelected() && gui.getMinutesToSubmit() < gui.getSubmitDurationMinutes()) {
            gui.frame.setState(Frame.ICONIFIED);
        }
    }

    private static void openUri(String uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(uri));
            } catch (Exception e) { /* TODO: error handling */ }
        } else { /* TODO: error handling */ }
    }
}
