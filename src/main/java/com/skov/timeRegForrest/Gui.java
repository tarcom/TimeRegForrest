package com.skov.timeRegForrest;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * yo
 */
public class Gui extends JPanel {

    static ActionPerformedHandler actionPerformedHandler;

    public static final String LIST_JIRAS_BUTTON_PRESSED = "listJirasButtonPressed";
    public static final String LOAD_FILE = "Load file";
    static JFrame frame;
    static JTextField txtFieldInOffice, txtFieldOutOffice;

    static JLabel timeInfoLabel;
    static JComboBox popupIntervalComboBox, submitDurationMinutesComboBox, chooseSavedDataComboBox;
    static JComboBox popupIntervalMorningComboBox, popupIntervalLunchComboBox, popupIntervalAfternoonComboBox;
    static JCheckBox autoMinimizeCheckBox, autoUpdateOfficeOutCheckBox;

    //public static String FROKOST_PAUSER = "Frokost & pauser";
//    static HashMap<Integer, Integer> taskIds = new HashMap<Integer, Integer>();
//    static int taskIdNext = 0;

    public static PersistanceDataWrapper persistanceDataWrapper;


    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public Gui() {

        persistanceDataWrapper = new PersistanceDataWrapper();

        super.setLayout(new GridLayout(0, 1));

        //--

        this.actionPerformedHandler = new ActionPerformedHandler(this);

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





        JPanel breakPanel = new JPanel(new GridLayout(1,1));
        breakPanel.add(new JLabel("Break section:"));
        breakPanel.setBackground(new Color(200,200,200));
        officeInPanel.add(breakPanel);



        add(officeInPanel);

        //--

        JPanel officeOutPanel = new JPanel(new GridLayout(1,2));

        txtFieldOutOffice = new JTextField(sdf.format(Calendar.getInstance().getTime()));

        autoUpdateOfficeOutCheckBox = new JCheckBox("Office OUT:");
        autoUpdateOfficeOutCheckBox.setSelected(true);
        officeOutPanel.add(autoUpdateOfficeOutCheckBox);

        officeOutPanel.add(txtFieldOutOffice);


        //officeOutPanel.add(new JLabel(""));
        JPanel breakPanelMorning = new JPanel(new GridLayout(1,2));
        breakPanelMorning.add(new JLabel("Morning:"));
        breakPanelMorning.setBackground(new Color(200, 200, 200));

        ArrayList<String> minValuesArr = new ArrayList<String>();
        for (int i = 0; i < 301 ; i = i + 5) {
            minValuesArr.add(i + " min");
        }

        popupIntervalMorningComboBox = new JComboBox(minValuesArr.toArray());
        popupIntervalMorningComboBox.setSelectedIndex(0);
        breakPanelMorning.add(popupIntervalMorningComboBox);

        officeOutPanel.add(breakPanelMorning);




        add(officeOutPanel);

        //--

        String minutes = " minutes";
        String[] minutValuesArr = {"1" + minutes, "5" + minutes, "10" + minutes, "15" + minutes, "30" + minutes, "45" + minutes, "60" + minutes, "90" + minutes, "120 minute", "180" + minutes, "240" + minutes};
        popupIntervalComboBox = new JComboBox(minutValuesArr);
        popupIntervalComboBox.setSelectedIndex(4);

        JPanel popupIntervalPanel = new JPanel(new GridLayout());
        popupIntervalPanel.add(new JLabel("Gui popup interval: "));
        popupIntervalPanel.add(popupIntervalComboBox);



        //popupIntervalPanel.add(new JLabel(""));
        JPanel breakPanelLunch = new JPanel(new GridLayout(1,2));
        breakPanelLunch.add(new JLabel("Lunch:"));
        breakPanelLunch.setBackground(new Color(200, 200, 200));

        popupIntervalLunchComboBox = new JComboBox(minValuesArr.toArray());
        popupIntervalLunchComboBox.setSelectedIndex(6);
        breakPanelLunch.add(popupIntervalLunchComboBox);

        popupIntervalPanel.add(breakPanelLunch);




        add(popupIntervalPanel);

        //--


        submitDurationMinutesComboBox = new JComboBox(minutValuesArr);
        submitDurationMinutesComboBox.setSelectedIndex(3);

        JPanel submitDurationPanel = new JPanel(new GridLayout());
        submitDurationPanel.add(new JLabel("Submit duration: "));
        submitDurationPanel.add(submitDurationMinutesComboBox);





        //submitDurationPanel.add(new JLabel(""));

        JPanel breakPanelDinner = new JPanel(new GridLayout(1,2));
        breakPanelDinner.add(new JLabel("Afternoon:"));
        breakPanelDinner.setBackground(new Color(200, 200, 200));

        popupIntervalAfternoonComboBox = new JComboBox(minValuesArr.toArray());
        popupIntervalAfternoonComboBox.setSelectedIndex(0);
        breakPanelDinner.add(popupIntervalAfternoonComboBox);

        submitDurationPanel.add(breakPanelDinner);






        add(submitDurationPanel);
        //--


        //--
        String[] savedFiles = PersisterService.getAvaiableFilesStrArr();
        chooseSavedDataComboBox = new JComboBox(savedFiles);
        chooseSavedDataComboBox.setSelectedIndex(PersisterService.getAvaiableFiles().size()-1);
        chooseSavedDataComboBox.setActionCommand(LOAD_FILE);
        chooseSavedDataComboBox.addActionListener(actionPerformedHandler);

        JPanel chooseSavedDataPanel = new JPanel(new GridLayout());
        chooseSavedDataPanel.add(new JLabel("Saved datafile: (" + savedFiles.length + " avail.)"));
        chooseSavedDataPanel.add(chooseSavedDataComboBox);

        chooseSavedDataPanel.add(new JLabel(""));
//        JButton savedFileButton = new JButton(LOAD_FILE);
//        savedFileButton.setActionCommand(LOAD_FILE);
//        savedFileButton.addActionListener(actionPerformedHandler);
//        chooseSavedDataPanel.add(savedFileButton);


        add(chooseSavedDataPanel);

        //--


        //--

        JPanel autoMinimizeAndResetPanel = new JPanel(new GridLayout());
        autoMinimizeCheckBox = new JCheckBox("Auto minimize");
        autoMinimizeCheckBox.setSelected(true);
        autoMinimizeAndResetPanel.add(autoMinimizeCheckBox);

        JButton resetButton = new JButton("Reset all time");
        resetButton.setActionCommand("RESET");
        resetButton.addActionListener(actionPerformedHandler);
        autoMinimizeAndResetPanel.add(resetButton);

        autoMinimizeAndResetPanel.add(new JLabel(""));

        add(autoMinimizeAndResetPanel);
        //--

        add(new JLabel(""));

        timeInfoLabel = new JLabel("Please set office int time");
        add(timeInfoLabel);

        JiraGuiRow jiraGuiRow = new JiraGuiRow(this, actionPerformedHandler);

        jiraGuiRow.addButton("Møder: Kanban", 1604);
        jiraGuiRow.addButton("Møder Xportalen ('alt andet')", 870);
        jiraGuiRow.addButton("Intern tid - sidemandsoplæring, kurser, vidensopbygning, erfa (Viden-16)", 1709);
      //jiraGuiRow.addButton("Intern tid - Afdelingsmøder (Afd-16)", 1708);
        jiraGuiRow.addButton("Egen administration", 1005);
        jiraGuiRow.addButton("Ikke TK prioriterede opgaver", 878);
        jiraGuiRow.addButton("Other - ikke i timesheet (konferencer/kurser)", 885);
        jiraGuiRow.addButton("Daglig forvaltning", 864);
        jiraGuiRow.addButton("Sagscontainer daglig forv.", 882);
        jiraGuiRow.addButton("Documentation", 881);

        jiraGuiRow.addButton("Polen mentoring", 1813);
        jiraGuiRow.addButton("Auto-test", 1881);
        jiraGuiRow.addButton("TK erhverv samarbejde", 1343);
        jiraGuiRow.addButton("Ny gar model samarbejde", 1345);
        jiraGuiRow.addButton("Hjælp til projekter", 872);
        jiraGuiRow.addButton("systemansvar", 866);
        jiraGuiRow.addButton("ADM-16", 1708);
        jiraGuiRow.addButton("");
        jiraGuiRow.addButton("");
        jiraGuiRow.addButton("");
        jiraGuiRow.addButton("");
        jiraGuiRow.addButton("");
        jiraGuiRow.addButton("");
        jiraGuiRow.addButton("");

        add(new JLabel("github.com/tarcom/TimeRegForrest"));

        try {
            ActionPerformedHandler.handleLoadFile();
        } catch (NullPointerException npe) {
            PersisterService.doPersist(); //initial persist, so that we can load though empty values later
        }
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
                " minuts</font> (total: " + convertMinutesToHouersAndMinutes(getTotalSubmittedMinutesNotPauser()) + ")</html>");
    }

    protected static int getTotalSubmittedMinutes() {
        int totalSubmittedMinutes = 0;
        for (Integer t : persistanceDataWrapper.getTimeRegTimeMap().values()) {
            totalSubmittedMinutes += t;
        }
        return totalSubmittedMinutes;
    }

    protected static int getTotalSubmittedMinutesNotPauser() {
        int totalSubmittedMinutes = 0;
        for (String s : persistanceDataWrapper.getTimeRegTimeMap().keySet()) {
//            if (!FROKOST_PAUSER.equalsIgnoreCase(s)) {
                totalSubmittedMinutes += persistanceDataWrapper.getTimeRegTimeMap().get(s);
//            }
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

        String minStr = ((String) popupIntervalMorningComboBox.getSelectedItem()).replace("min", "").trim();
        minutesToSubmit -= Integer.valueOf(minStr);

        minStr = ((String) popupIntervalAfternoonComboBox.getSelectedItem()).replace("min", "").trim();
        minutesToSubmit -= Integer.valueOf(minStr);


        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 00);
        if (getDateTimeOfficeOut().after(c)) {
            minStr = ((String) popupIntervalLunchComboBox.getSelectedItem()).replace("min", "").trim();
            minutesToSubmit -= Integer.valueOf(minStr);
        }

        return minutesToSubmit;
    }


    static long getAllreadySubmittetMinutes() {
        long duration = 0l;
        for (int time : persistanceDataWrapper.getTimeRegTimeMap().values()) {
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
                    PersisterService.doPersist();
                    System.out.println("bye.");
                    System.exit(0);
                }
            }
        });

        try {
            File pathToFile = new File("C:\\projects\\TimeRegForrest\\doc\\icon.png");
            Image img = ImageIO.read(pathToFile);
            frame.setIconImage(img);
        } catch (Exception e) {
            System.out.println(e);
        }
        //frame.setSize(1000, 1400);

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