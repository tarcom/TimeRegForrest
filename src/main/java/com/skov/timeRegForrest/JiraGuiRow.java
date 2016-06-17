package com.skov.timeRegForrest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by ALSK on 13-01-2016.
 */
public class JiraGuiRow {

    static Gui gui;
    static ArrayList<Integer> shortCutList = new ArrayList<Integer>();
    static ActionPerformedHandler actionPerformedHandler;

    public JiraGuiRow(Gui gui, ActionPerformedHandler actionPerformedHandler) {
        this.gui = gui;
        this.actionPerformedHandler = actionPerformedHandler;

        shortCutList.add(KeyEvent.VK_0);
        shortCutList.add(KeyEvent.VK_1);
        shortCutList.add(KeyEvent.VK_2);
        shortCutList.add(KeyEvent.VK_3);
        shortCutList.add(KeyEvent.VK_4);
        shortCutList.add(KeyEvent.VK_5);
        shortCutList.add(KeyEvent.VK_6);
        shortCutList.add(KeyEvent.VK_7);
        shortCutList.add(KeyEvent.VK_8);
        shortCutList.add(KeyEvent.VK_9);
        shortCutList.add(KeyEvent.VK_A);
        shortCutList.add(KeyEvent.VK_B);
        shortCutList.add(KeyEvent.VK_C);
        shortCutList.add(KeyEvent.VK_D);
        shortCutList.add(KeyEvent.VK_E);
        shortCutList.add(KeyEvent.VK_F);
        shortCutList.add(KeyEvent.VK_G);
        shortCutList.add(KeyEvent.VK_H);
        shortCutList.add(KeyEvent.VK_I);
        shortCutList.add(KeyEvent.VK_J);
        shortCutList.add(KeyEvent.VK_K);
        shortCutList.add(KeyEvent.VK_L);
        shortCutList.add(KeyEvent.VK_M);
        shortCutList.add(KeyEvent.VK_N);


    }

    void addButton(String name, int... jiraNumber) {

        JPanel rowPanel = new JPanel(new GridBagLayout());

        int shortcutKey = shortCutList.remove(0);
        String shortcutKeyStr = String.valueOf(shortcutKey);

        //--

        //todo: I wish I coult set default size for this...
        rowPanel.add(new JLabel(String.valueOf(" ALT+" + KeyEvent.getKeyText(shortcutKey) + " ")));

        //--

        JTextField descriptionTxtField = new JTextField(name, 25);
        rowPanel.add(descriptionTxtField);

        Gui.persistenceDataWrapper.getDescriptionMap().put(shortcutKeyStr, descriptionTxtField);

        //--

        String jiraNumberLink = "XP-";
        if (jiraNumber != null && jiraNumber.length == 1) {
            jiraNumberLink = "XP-" + jiraNumber[0];
        }

        JTextField jiraLinkField = new JTextField(jiraNumberLink, 5);

        String key = shortcutKeyStr;
        Gui.persistenceDataWrapper.getJiraNumbersMap().put(key, jiraLinkField);

        rowPanel.add(jiraLinkField);

        //--

        JButton listJirasButton = new JButton();
        listJirasButton.setPreferredSize(new Dimension(7,19));

        listJirasButton.setActionCommand(gui.LIST_JIRAS_BUTTON_PRESSED + key);
        listJirasButton.setToolTipText("Shows which static jiras issues that you can use");
        listJirasButton.addActionListener(actionPerformedHandler);

        rowPanel.add(listJirasButton);

        //--

        JButton plusButton = new JButton("+");
        plusButton.setVerticalTextPosition(AbstractButton.CENTER);
        plusButton.setHorizontalTextPosition(AbstractButton.LEADING);
        plusButton.setMnemonic(KeyEvent.VK_D);

        plusButton.setActionCommand(shortcutKeyStr + "Plus");
        plusButton.setToolTipText("Submit 15 minutes");
        plusButton.addActionListener(actionPerformedHandler);
        plusButton.setMnemonic(shortcutKey);

        rowPanel.add(plusButton);

        Gui.persistenceDataWrapper.getTimeRegNameMap().put(shortcutKeyStr, plusButton);

        //--

        JButton minusButton = new JButton("-");
        minusButton.setVerticalTextPosition(AbstractButton.CENTER);
        minusButton.setHorizontalTextPosition(AbstractButton.LEADING);
        minusButton.setMnemonic(KeyEvent.VK_D);

        minusButton.setActionCommand(shortcutKeyStr + "Minus");
        minusButton.setToolTipText("Submit 15 minutes");
        minusButton.addActionListener(actionPerformedHandler);

        rowPanel.add(minusButton);

        //--

        JButton timeSubmittedLabel = new JButton("           ");
        timeSubmittedLabel.setToolTipText("Click to open browser and submit time in JIRA " + jiraNumberLink);
        timeSubmittedLabel.setActionCommand("XP-" + shortcutKeyStr);
        timeSubmittedLabel.setBorderPainted(false);
        timeSubmittedLabel.addActionListener(actionPerformedHandler);
        rowPanel.add(timeSubmittedLabel);

        Gui.persistenceDataWrapper.getTimeRegSubmittedTimeMap().put(shortcutKeyStr, timeSubmittedLabel);
        Gui.persistenceDataWrapper.getTimeRegTimeMap().put(shortcutKeyStr, 0);

        //--

        gui.add(rowPanel);

    }
}
