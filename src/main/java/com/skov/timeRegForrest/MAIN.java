package com.skov.timeRegForrest;

import java.util.Date;

/**
 * yo
 */
public class MAIN {

    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Gui.createGUI();
                Gui.handleSetTIme();
            }
        });


        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1 * 1000);
                        System.out.println("updating time...");
                        Gui.updateTxtFieldOutOffice();

                        Gui.handleSetTIme();

                        if ((Gui.getMinutesToSubmit() % Gui.getPopupIntervalMinutes()) == 0 && Gui.getMinutesToSubmit() > 1) { //on every 15 sharp minute, lets bring the GUI in front
                            if (new Date().getSeconds() == 0) {
                                Gui.setGUIInForground();
                                System.out.println("setting GUI in forground now");
                                Gui.doBlink();
                            }
                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

}