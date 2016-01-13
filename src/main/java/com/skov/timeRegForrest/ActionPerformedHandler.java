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
            String plusName = e.getActionCommand().replace("Plus", "");

            gui.timeRegTimeMap.put(plusName, gui.timeRegTimeMap.get(plusName) + gui.getSubmitDurationMinutes());
            String time = gui.convertMinutesToHouersAndMinutes(gui.timeRegTimeMap.get(plusName));
            String submitTimeTxt = "<html><FONT color=\"#000099\"><U>" + time + "</U></FONT></HTML>";


            gui.timeRegSubmittedTimeMap.get(plusName).setText(submitTimeTxt);

            if (gui.autoMinimizeCheckBox.isSelected() && gui.getMinutesToSubmit() < gui.getSubmitDurationMinutes()) {
                gui.frame.setState(Frame.ICONIFIED);
            }

        } else if (e.getActionCommand().endsWith("Minus")) {
            String minusName = e.getActionCommand().replace("Minus", "");

            gui.timeRegTimeMap.put(minusName, gui.timeRegTimeMap.get(minusName) - gui.getSubmitDurationMinutes());
            gui.timeRegSubmittedTimeMap.get(minusName).setText(gui.convertMinutesToHouersAndMinutes(gui.timeRegTimeMap.get(minusName)));
        } else if (e.getActionCommand().startsWith("XP-") && e.getActionCommand().length() >= 4) {
            System.out.println(e.getActionCommand());
            String shortCutKey = e.getActionCommand();
            JTextField jTextField = gui.jiraNumbersMap.get(shortCutKey.replace("XP-", ""));
            String jiraNumber = jTextField.getText().trim();
            openUri("http://features.nykreditnet.net/browse/" + jiraNumber);
        } else if (e.getActionCommand().startsWith(gui.LIST_JIRAS_BUTTON_PRESSED)) {
            Object[] possibilities = ListOfIssues.getListOfInterestingJirasStr();
            String s = (String)JOptionPane.showInputDialog(
                    gui.frame,
                    "Choose a task:",
                    "Task popup picker",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    possibilities[0]);

            JTextField jTextFieldJira = gui.jiraNumbersMap.get(e.getActionCommand().replace(gui.LIST_JIRAS_BUTTON_PRESSED , ""));
            jTextFieldJira.setText(ListOfIssues.getJiraFromStr(s));

            JTextField jTextFieldDesc = gui.descriptionMap.get(e.getActionCommand().replace(gui.LIST_JIRAS_BUTTON_PRESSED, ""));
            jTextFieldDesc.setText(ListOfIssues.getDescriptionFromStr(s));

        } else if (e.getActionCommand().equals("RESET")) {
            gui.timeRegSubmittedTimeMap.get(1);
            gui.timeRegTimeMap.get(1);

            for (String s : gui.timeRegTimeMap.keySet()) {
                gui.timeRegTimeMap.put(s, 0);
            }

            for (String s : gui.timeRegSubmittedTimeMap.keySet()) {
                JButton jButton = gui.timeRegSubmittedTimeMap.get(s);
                jButton.setText("           ");
                gui.timeRegSubmittedTimeMap.put(s, jButton);
            }


        }

        gui.handleSetTIme();
    }

    private static void openUri(String uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(uri));
            } catch (Exception e) { /* TODO: error handling */ }
        } else { /* TODO: error handling */ }
    }
}
