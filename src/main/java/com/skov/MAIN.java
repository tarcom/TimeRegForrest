package com.skov;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class MAIN extends JPanel implements ActionListener {

    static JFrame frame;
    static HashMap<String, Integer> timeRegTimeMap = new HashMap<String, Integer>();
    static HashMap<String, JButton> timeRegNameMap = new HashMap<String, JButton>();
    static JTextField txtFieldInOffice;
    static JLabel timeInfoLabel;

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public MAIN() {

        super.setLayout(new GridLayout(0, 1));


        add(new JLabel("When did you meet today?"));


        Calendar officeInCalendar = Calendar.getInstance();
        officeInCalendar.set(Calendar.HOUR_OF_DAY, 8);
        officeInCalendar.set(Calendar.MINUTE, 00);

        String officeInCalendarStr = sdf.format(officeInCalendar.getTime());

        txtFieldInOffice = new JTextField(officeInCalendarStr + " ?");
        txtFieldInOffice.setBackground(new Color(255, 255, 0));

        add(txtFieldInOffice);


        timeInfoLabel = new JLabel("Please set office int time");
        add(timeInfoLabel);

        add(new JLabel(""));


        addButton("Frokost (og andre pauser)");
        addButton("TK moeder");
        addButton("TIN oplysninger");
        addButton("Vagt");
        addButton("Release master");


    }

    void addButton(String name) {

        JButton button = new JButton(name + "                     ");
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.LEADING);
        button.setMnemonic(KeyEvent.VK_D);

        button.setActionCommand(name);
        button.setToolTipText("Add 15 minutes");
        button.addActionListener(this);

        add(button);

        timeRegNameMap.put(name, button);
        timeRegTimeMap.put(name, 0);

    }

    public void actionPerformed(ActionEvent e) {

        JButton button = timeRegNameMap.get(e.getActionCommand());
        Integer time = timeRegTimeMap.get(e.getActionCommand());
        timeRegTimeMap.put(e.getActionCommand(), time + 15);
        button.setText(e.getActionCommand() + ": " + timeRegTimeMap.get(e.getActionCommand()) + " min");

        HandleSetTIme();

    }

    static private void HandleSetTIme() {
        txtFieldInOffice.getText();
        Calendar dateTimeOfficeIn = Calendar.getInstance();
        try {
            Date dateTimeOfficeInDate = sdf.parse(txtFieldInOffice.getText().trim());
            dateTimeOfficeIn.setTime(dateTimeOfficeInDate);

        } catch (ParseException e1) {
            System.out.println("no go" + e1);

            e1.printStackTrace();
            txtFieldInOffice.setBackground(new Color(255, 0, 0));
            return;
        }
        txtFieldInOffice.setBackground(new Color(0, 255, 0));

        updateTimeInfoLabel(dateTimeOfficeIn);

        return;
    }

    private static void updateTimeInfoLabel(Calendar dateTimeOfficeIn) {
        if (txtFieldInOffice.getBackground().getGreen() == 255) {
            Calendar now = Calendar.getInstance();
            long minutesToSubmit = (now.getTimeInMillis() - dateTimeOfficeIn.getTimeInMillis()) / 1000 / 60;

            minutesToSubmit -= getAllreadySubmittetMinutes();

            timeInfoLabel.setText("Please submit " + minutesToSubmit + " minuts");
        }

    }


    static long getAllreadySubmittetMinutes() {
        long duration = 0l;
        for (int time : timeRegTimeMap.values()) {
            duration += time;
        }

        return duration;
    }

    private static void createGUI() {
        MAIN buttonContentPane = new MAIN();
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

    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createGUI();
            }
        });


        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(60 * 1000);
                        System.out.println("updating time...");

                        for (int i = 0 ; i < 5 ; i++) {
                            txtFieldInOffice.setBackground(new Color(255, 0, 0));
                            Thread.sleep(100);
                            txtFieldInOffice.setBackground(new Color(0, 255, 0));
                            Thread.sleep(100);
                            txtFieldInOffice.setBackground(new Color(0, 0, 255));
                            Thread.sleep(100);
                        }
                        HandleSetTIme();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2 * 60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    frame.setVisible(true);
                    int state = frame.getExtendedState();
                    state &= ~JFrame.ICONIFIED;
                    frame.setExtendedState(state);
                    frame.setAlwaysOnTop(true);
                    frame.toFront();
                    frame.requestFocus();
                    frame.setAlwaysOnTop(false);

                    System.out.println("setting GUI in forground now");
                }
            }
        }).start();

    }
}