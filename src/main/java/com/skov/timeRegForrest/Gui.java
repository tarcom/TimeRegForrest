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
    static JTextField txtFieldInOffice, txtFieldOutOffice;
    static JLabel timeInfoLabel;
    static JComboBox popupIntervalComboBox, submitDurationMinutesComboBox;
    static JCheckBox autoMinimizeCheckBox, autoUpdateOfficeOutCheckBox;
//    static HashMap<Integer, Integer> taskIds = new HashMap<Integer, Integer>();
//    static int taskIdNext = 0;


    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public Gui() {
        super.setLayout(new GridLayout(0, 1));

        //--
        JPanel officeInPanel = new JPanel(new GridLayout(1,2));

        officeInPanel.add(new JLabel("Office IN:"));

        Calendar officeInCalendar = Calendar.getInstance();
        officeInCalendar.set(Calendar.HOUR_OF_DAY, 8);
        officeInCalendar.set(Calendar.MINUTE, 00);

        String officeInCalendarStr = sdf.format(officeInCalendar.getTime());

        txtFieldInOffice = new JTextField(officeInCalendarStr + " ?");
        txtFieldInOffice.setBackground(new Color(255, 255, 0));

        officeInPanel.add(txtFieldInOffice);
        add(officeInPanel);
        //--


        //--
        JPanel officeOutPanel = new JPanel(new GridLayout(1,2));

        txtFieldOutOffice = new JTextField(sdf.format(Calendar.getInstance().getTime()));

        autoUpdateOfficeOutCheckBox = new JCheckBox("Office OUT:");
        autoUpdateOfficeOutCheckBox.setSelected(true);
        officeOutPanel.add(autoUpdateOfficeOutCheckBox);

        officeOutPanel.add(txtFieldOutOffice);
        add(officeOutPanel);
        //--


        //--
        String minutes = " minutes";
        String[] minutValuesArr = {"1" + minutes, "5" + minutes, "10" + minutes, "15" + minutes, "30" + minutes, "45" + minutes, "60" + minutes, "90" + minutes, "120 minute", "180" + minutes, "240" + minutes};
        popupIntervalComboBox = new JComboBox(minutValuesArr);
        popupIntervalComboBox.setSelectedIndex(4);

        JPanel popupIntervalPanel = new JPanel(new GridLayout());
        popupIntervalPanel.add(new JLabel("Gui popup interval: "));
        popupIntervalPanel.add(popupIntervalComboBox);
        add(popupIntervalPanel);

        //--

        //--
        submitDurationMinutesComboBox = new JComboBox(minutValuesArr);
        submitDurationMinutesComboBox.setSelectedIndex(3);

        JPanel submitDurationPanel = new JPanel(new GridLayout());
        submitDurationPanel.add(new JLabel("Submit duration: "));
        submitDurationPanel.add(submitDurationMinutesComboBox);
        add(submitDurationPanel);
        //--

        //--
        autoMinimizeCheckBox = new JCheckBox("Auto minimize");
        autoMinimizeCheckBox.setSelected(true);
        add(autoMinimizeCheckBox);

        //--


        add(new JLabel(""));

        timeInfoLabel = new JLabel("Please set office int time");
        add(timeInfoLabel);



        addButton("Daglig forvaltning", KeyEvent.VK_1);
        addButton("Egen administration", KeyEvent.VK_2);
        addButton("Other", KeyEvent.VK_3);
        addButton("Moeder Xportalen", KeyEvent.VK_4);
        addButton("TK moeder", KeyEvent.VK_5);
        addButton("Frokost & pauser", KeyEvent.VK_6);
        addButton("SagsC daglig forv.", KeyEvent.VK_7);
        addButton("ny 1", KeyEvent.VK_8);
        addButton("ny 2", KeyEvent.VK_9);
        addButton("ny 3", KeyEvent.VK_0);
        addButton("ny 4", KeyEvent.VK_A);
        addButton("ny 5", KeyEvent.VK_B);
        addButton("ny 6", KeyEvent.VK_C);

        add(new JLabel("github.com/tarcom/TimeRegForrest"));


    }

    protected static void updateTxtFieldOutOffice() {
        if (autoUpdateOfficeOutCheckBox.isSelected()) {
            txtFieldOutOffice.setText(sdf.format(Calendar.getInstance().getTime()));
        }
    }

    protected static int getPopupIntervalMinutes() {
        return Integer.valueOf(((String) Gui.popupIntervalComboBox.getSelectedItem()).replace(" minutes", "").trim());
    }

    protected static int getSubmitDurationMinutes() {
        return Integer.valueOf(((String) Gui.submitDurationMinutesComboBox.getSelectedItem()).replace(" minutes", "").trim());
    }

    void addButton(String name, int shortcutKey) {

        JPanel rowPanel = new JPanel(new GridLayout());

        JTextField textField = new JTextField(name);
        rowPanel.add(textField);

        JButton plusButton = new JButton("+");
        plusButton.setVerticalTextPosition(AbstractButton.CENTER);
        plusButton.setHorizontalTextPosition(AbstractButton.LEADING);
        plusButton.setMnemonic(KeyEvent.VK_D);

        plusButton.setActionCommand(name + "Plus");
        plusButton.setToolTipText("Submit 15 minutes");
        plusButton.addActionListener(this);
        plusButton.setMnemonic(shortcutKey);

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

        JPanel p = new JPanel(new GridBagLayout());
        p.add(new JLabel(String.valueOf("ALT+" + KeyEvent.getKeyText(shortcutKey))));
        p.add(rowPanel);
        add(p);

        timeRegNameMap.put(name, plusButton);
        timeRegSubmittedTimeMap.put(name, timeSubmittedLabel);
        timeRegTimeMap.put(name, 0);

    }

    public void actionPerformed(ActionEvent e) {


        if (e.getActionCommand().endsWith("Plus")) {
            String plusName = e.getActionCommand().replace("Plus", "");

            timeRegTimeMap.put(plusName, timeRegTimeMap.get(plusName) + getSubmitDurationMinutes());
            timeRegSubmittedTimeMap.get(plusName).setText(convertMinutesToHouersAndMinutes(timeRegTimeMap.get(plusName)));

            if (autoMinimizeCheckBox.isSelected() && getMinutesToSubmit() < getSubmitDurationMinutes()) {
                frame.setState(Frame.ICONIFIED);
            }

        } else if (e.getActionCommand().endsWith("Minus")) {
            String minusName = e.getActionCommand().replace("Minus", "");

            timeRegTimeMap.put(minusName, timeRegTimeMap.get(minusName) - getSubmitDurationMinutes());
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

    /**
     * DO HANDLE NULL!!!
     * @return
     */
    private static Calendar getDateTimeOfficeOut() {
        Calendar dateTimeOfficeOut = Calendar.getInstance();
        try {
            Date dateTimeOfficeOutDate = sdf.parse(txtFieldOutOffice.getText().trim());
            dateTimeOfficeOut.setTime(dateTimeOfficeOutDate);

        } catch (ParseException e1) {
            System.out.println("no go getting office OUT: " + e1);

            e1.printStackTrace();
            txtFieldOutOffice.setBackground(new Color(255, 0, 0));
            return null;
        }
        txtFieldOutOffice.setBackground(new Color(255, 255, 255));
        return dateTimeOfficeOut;
    }

    /**
     * DO HANDLE NULL!!
     * @return
     */
    private static Calendar getDateTimeOfficeIn() {
        Calendar dateTimeOfficeIn = Calendar.getInstance();
        try {
            Date dateTimeOfficeInDate = sdf.parse(txtFieldInOffice.getText().trim());
            dateTimeOfficeIn.setTime(dateTimeOfficeInDate);

        } catch (ParseException e1) {
            System.out.println("no go getting office IN: " + e1);

            e1.printStackTrace();
            txtFieldInOffice.setBackground(new Color(255, 0, 0));
            return null;
        }
        return dateTimeOfficeIn;
    }

    private static void updateTimeInfoLabel() {
        long minutesToSubmit = getMinutesToSubmit();
        timeInfoLabel.setText("<html><font color='red'>Please submit " + convertMinutesToHouersAndMinutes(minutesToSubmit) +
                " minuts</font> (total: " + convertMinutesToHouersAndMinutes(getTotalSubmittedMinutes()) + ")</html>");
    }

    protected static int getTotalSubmittedMinutes() {
        int totalSubmittedMinutes = 0;
        for (Integer t : timeRegTimeMap.values()) {
            totalSubmittedMinutes += t;
        }
        return totalSubmittedMinutes;
    }

    protected static long getMinutesToSubmit() {
        Calendar now = getDateTimeOfficeOut();
        if (now == null) {
            return 0;
        }
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