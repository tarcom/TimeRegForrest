package com.skov.timeRegForrest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * yo
 */
public class Gui extends JPanel implements ActionListener {

    static JFrame frame;
    static HashMap<String, Integer> timeRegTimeMap = new HashMap<String, Integer>();
    static HashMap<String, JButton> timeRegNameMap = new HashMap<String, JButton>();
    static HashMap<String, JLabel> timeRegSubmittedTimeMap = new HashMap<String, JLabel>();
    static JTextField txtFieldInOffice;
    static JLabel timeInfoLabel;

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public Gui() {

        super.setLayout(new GridLayout(0, 1));


        JPanel officeInPanel = new JPanel(new GridLayout(1,2));

        officeInPanel.add(new JLabel("When did you meet today?"));


        Calendar officeInCalendar = Calendar.getInstance();
        officeInCalendar.set(Calendar.HOUR_OF_DAY, 8);
        officeInCalendar.set(Calendar.MINUTE, 00);

        String officeInCalendarStr = sdf.format(officeInCalendar.getTime());

        txtFieldInOffice = new JTextField(officeInCalendarStr + " ?");
        txtFieldInOffice.setBackground(new Color(255, 255, 0));

        officeInPanel.add(txtFieldInOffice);
        add(officeInPanel);

        timeInfoLabel = new JLabel("Please set office int time");
        add(timeInfoLabel);

        add(new JLabel(""));


        addButton("Daglig forvaltning");
        addButton("Egen administration");
        addButton("Other");
        addButton("Moeder Xportalen");
        addButton("TK moeder");
        addButton("Frokost & pauser");
        addButton("SagsC daglig forv.");
        addButton("");
        addButton("");
        addButton("");
        addButton("");
        addButton("");
        addButton("");


    }

    void addButton(String name) {

        JPanel rowPanel = new JPanel(new GridLayout(1,3));

        JTextField textField = new JTextField(name);
        rowPanel.add(textField);

        JButton plusButton = new JButton("+");
        plusButton.setVerticalTextPosition(AbstractButton.CENTER);
        plusButton.setHorizontalTextPosition(AbstractButton.LEADING);
        plusButton.setMnemonic(KeyEvent.VK_D);

        plusButton.setActionCommand(name + "Plus");
        plusButton.setToolTipText("Submit 15 minutes");
        plusButton.addActionListener(this);

        JButton minusButton = new JButton("-");
        minusButton.setVerticalTextPosition(AbstractButton.CENTER);
        minusButton.setHorizontalTextPosition(AbstractButton.LEADING);
        minusButton.setMnemonic(KeyEvent.VK_D);

        minusButton.setActionCommand(name + "Minus");
        minusButton.setToolTipText("Submit 15 minutes");
        minusButton.addActionListener(this);

        JLabel timeSubmittedLabel = new JLabel("");

        JPanel plusMinusPanel = new JPanel(new GridLayout(1,3));
        plusMinusPanel.add(plusButton);
        plusMinusPanel.add(minusButton);
        plusMinusPanel.add(timeSubmittedLabel);
        rowPanel.add(plusMinusPanel);
        add(rowPanel);

        timeRegNameMap.put(name, plusButton);
        timeRegSubmittedTimeMap.put(name, timeSubmittedLabel);
        timeRegTimeMap.put(name, 0);

    }

    public void actionPerformed(ActionEvent e) {


        if (e.getActionCommand().endsWith("Plus")) {
            String plusName = e.getActionCommand().replace("Plus", "");

            timeRegTimeMap.put(plusName, timeRegTimeMap.get(plusName) + 15);
            timeRegSubmittedTimeMap.get(plusName).setText(convertMinutesToHouersAndMinutes(timeRegTimeMap.get(plusName)));
        } else if (e.getActionCommand().endsWith("Minus")) {
            String minusName = e.getActionCommand().replace("Minus", "");

            timeRegTimeMap.put(minusName, timeRegTimeMap.get(minusName) - 15);
            timeRegSubmittedTimeMap.get(minusName).setText(convertMinutesToHouersAndMinutes(timeRegTimeMap.get(minusName)));
        }


        handleSetTIme();

    }

    protected static String convertMinutesToHouersAndMinutes(long minutes) {
        if (minutes < 0) {
            return String.valueOf(minutes);
        }

        String hours = String.valueOf(minutes / 60); //since both are ints, you get an int
        String minutesLeft = String.valueOf(minutes % 60);

        if (minutesLeft.length() == 1) {
            minutesLeft = "0" + minutesLeft;
        }

        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        return hours + ":" + minutesLeft;
    }

    static protected void handleSetTIme() {
        Calendar dateTimeOfficeIn = getDateTimeOfficeIn();
        if (dateTimeOfficeIn == null) {
            return;
        }

        if (txtFieldInOffice.getText().contains("?")) {
            txtFieldInOffice.setBackground(new Color(255, 255, 0));
        } else {
            txtFieldInOffice.setBackground(new Color(0, 255, 0));
        }


        updateTimeInfoLabel();

        return;
    }

    private static Calendar getDateTimeOfficeIn() {
        Calendar dateTimeOfficeIn = Calendar.getInstance();
        try {
            Date dateTimeOfficeInDate = sdf.parse(txtFieldInOffice.getText().trim());
            dateTimeOfficeIn.setTime(dateTimeOfficeInDate);

        } catch (ParseException e1) {
            System.out.println("no go: " + e1);

            e1.printStackTrace();
            txtFieldInOffice.setBackground(new Color(255, 0, 0));
            return null;
        }
        return dateTimeOfficeIn;
    }

    private static void updateTimeInfoLabel() {
        long minutesToSubmit = getMinutesToSubmit();
        timeInfoLabel.setText("Please submit " + convertMinutesToHouersAndMinutes(minutesToSubmit) + " minuts");
    }

    protected static long getMinutesToSubmit() {
        Calendar now = Calendar.getInstance();
        Calendar dateTimeOfficeIn = getDateTimeOfficeIn();
        if (dateTimeOfficeIn == null) {
            return -1l;
        }
        long minutesToSubmit = (now.getTimeInMillis() - dateTimeOfficeIn.getTimeInMillis()) / 1000 / 60;

        minutesToSubmit -= getAllreadySubmittetMinutes();
        return minutesToSubmit;
    }


    static long getAllreadySubmittetMinutes() {
        long duration = 0l;
        for (int time : timeRegTimeMap.values()) {
            duration += time;
        }

        return duration;
    }

    protected static void createGUI() {
        Gui buttonContentPane = new Gui();
        buttonContentPane.setOpaque(true);


        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("TimeRegForrest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.getRootPane().setDefaultButton(button);
        frame.setContentPane(buttonContentPane);

        frame.pack();
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                String ObjButtons[] = {"Yes", "No"};
                int PromptResult = JOptionPane.showOptionDialog(null, "You normally do NOT want to exit this app, but just minimize it. Are you sure you want to exit? ", "Exit?", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
                if (PromptResult == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

    }


    protected static void setGUIInForground() {
        frame.setVisible(true);
        int state = frame.getExtendedState();
        state &= ~JFrame.ICONIFIED;
        frame.setExtendedState(state);
        frame.setAlwaysOnTop(true);
        frame.toFront();
        frame.requestFocus();
        frame.setAlwaysOnTop(false);
    }

    protected static void doBlink() throws InterruptedException {
        for (int i = 0 ; i < 5 ; i++) {
            txtFieldInOffice.setBackground(new Color(255, 0, 0));
            Thread.sleep(100);
            txtFieldInOffice.setBackground(new Color(0, 255, 0));
            Thread.sleep(100);
            txtFieldInOffice.setBackground(new Color(0, 255, 255));

            handleSetTIme();
        }
    }


}