package com.skov.timeRegForrest;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.input.WorklogInput;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Effect;
import org.joda.time.DateTime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ALSK on 13-01-2016.
 */
public class ActionPerformedHandler  implements ActionListener {
    private static final String JIRA_URL = "http://features-direct";

    static Gui gui;

    JiraRestClient jiraRestClient;

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
        } else if (e.getActionCommand().equals(Gui.SUBMIT_ALL_TO_JIRA)) {
            handleSubmitToJira();
        }

        gui.handleSetTIme();
    }

    private void handleSubmitToJira() {
        try {
            String username = Gui.txtFieldJiraUsername.getText();
            String password = new String(Gui.txtFieldJiraPassword.getPassword());
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "JIRA username and password are required", "Missing credentials", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (jiraRestClient == null) {
                BasicHttpAuthenticationHandler authenticationHandler = new BasicHttpAuthenticationHandler(username, password);
                JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
                jiraRestClient = factory.create(new URI(JIRA_URL), authenticationHandler);
            }

            final HashMap<String, Integer> workLogMap = new HashMap<String, Integer>();

            String issueKeys = null;
            for (Map.Entry<String, JTextField> entry : Gui.persistenceDataWrapper.getJiraNumbersMap().entrySet()) {
                final int minutes = Gui.persistenceDataWrapper.getTimeRegTimeMap().get(entry.getKey());
                final String issueKey = entry.getValue().getText();

                if (minutes <= 0) continue;

                workLogMap.put(issueKey.toUpperCase(), minutes);
                if (issueKeys == null) {
                    issueKeys = issueKey;
                } else {
                    issueKeys += "," + issueKey;
                }
            }

            final String query = "key in (" + issueKeys + ")";
            jiraRestClient.getSearchClient().searchJql(query).done(new Effect<SearchResult>() {
                public void apply(SearchResult searchResult) {
                    Iterable<Issue> results = searchResult.getIssues();
                    for (Issue issue : results) {
                        URI workLogUri = issue.getWorklogUri();
                        int minutes = workLogMap.get(issue.getKey());
                        jiraRestClient.getIssueClient().addWorklog(workLogUri,
                                new WorklogInput(null, null, null, null, "Automatically inserted", new DateTime(), minutes, null, WorklogInput.AdjustEstimate.AUTO, null));
                    }
                    JOptionPane.showMessageDialog(null, "Work logs created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }).fail(new Effect<Throwable>() {
                public void apply(Throwable throwable) {

                    if (throwable instanceof RestClientException) {
                        RestClientException e = (RestClientException) throwable;
                        if (e.getStatusCode().get() == 401) {
                            JOptionPane.showMessageDialog(null, "Incorrect username or password", "Invalid credentials", JOptionPane.ERROR_MESSAGE);
                        } else if (e.getStatusCode().get() == 403) {
                            String msg = "Username and password is correct, but the system might have detected too may consecutive requests.\n" +
                                    "Please login to JIRA in a browser session and try again.";
                            JOptionPane.showMessageDialog(null, msg, "JIRA access forbidden", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    throwable.printStackTrace(pw);
                    JOptionPane.showMessageDialog(null, "JQL query failed: '" + query + "'.\n" +
                            (throwable.getMessage() == null ? "" : throwable.getMessage()) + "\n"
                            + sw.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void handleLoadFile() {
        PersistenceDataWrapper persistanceDataWrapperCOPY = PersisterService.doLoad(gui);

        for (String key : persistanceDataWrapperCOPY.getTimeRegTimeMap().keySet()) {
            Gui.persistenceDataWrapper.getTimeRegTimeMap().put(key, persistanceDataWrapperCOPY.getTimeRegTimeMap().get(key));
        }

        for (String key : persistanceDataWrapperCOPY.getTimeRegSubmittedTimeMap().keySet()) {
            Gui.persistenceDataWrapper.getTimeRegSubmittedTimeMap().get(key).setText(persistanceDataWrapperCOPY.getTimeRegSubmittedTimeMap().get(key).getText());
        }


        for (String key : persistanceDataWrapperCOPY.getTimeRegNameMap().keySet()) {
            Gui.persistenceDataWrapper.getTimeRegNameMap().put(key, persistanceDataWrapperCOPY.getTimeRegNameMap().get(key));
        }


        for (String key : persistanceDataWrapperCOPY.getJiraNumbersMap().keySet()) {
            Gui.persistenceDataWrapper.getJiraNumbersMap().get(key).setText(persistanceDataWrapperCOPY.getJiraNumbersMap().get(key).getText());
        }


        for (String key : persistanceDataWrapperCOPY.getDescriptionMap().keySet()) {
            Gui.persistenceDataWrapper.getDescriptionMap().get(key).setText(persistanceDataWrapperCOPY.getDescriptionMap().get(key).getText());
        }
    }

    private void handleResetPressed() {
        Gui.persistenceDataWrapper.getTimeRegSubmittedTimeMap().get(1);
        Gui.persistenceDataWrapper.getTimeRegTimeMap().get(1);

        for (String s : Gui.persistenceDataWrapper.getTimeRegTimeMap().keySet()) {
            Gui.persistenceDataWrapper.getTimeRegTimeMap().put(s, 0);
        }

        for (String s : Gui.persistenceDataWrapper.getTimeRegSubmittedTimeMap().keySet()) {
            JButton jButton = Gui.persistenceDataWrapper.getTimeRegSubmittedTimeMap().get(s);
            jButton.setText("           ");
            Gui.persistenceDataWrapper.getTimeRegSubmittedTimeMap().put(s, jButton);
        }
        System.out.println("reset pressed!!");
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

        JTextField jTextFieldJira = Gui.persistenceDataWrapper.getJiraNumbersMap().get(e.getActionCommand().replace(gui.LIST_JIRAS_BUTTON_PRESSED, ""));
        jTextFieldJira.setText(ListOfIssues.getJiraFromStr(s));

        JTextField jTextFieldDesc = Gui.persistenceDataWrapper.getDescriptionMap().get(e.getActionCommand().replace(gui.LIST_JIRAS_BUTTON_PRESSED, ""));
        jTextFieldDesc.setText(ListOfIssues.getDescriptionFromStr(s));
    }

    private void handleOpenJira(ActionEvent e) {
        System.out.println(e.getActionCommand());
        String shortCutKey = e.getActionCommand();
        JTextField jTextField = Gui.persistenceDataWrapper.getJiraNumbersMap().get(shortCutKey.replace("XP-", ""));
        String jiraNumber = jTextField.getText().trim();
        openUri("http://features.nykreditnet.net/browse/" + jiraNumber);
    }

    private void handleMinus(ActionEvent e) {
        String minusName = e.getActionCommand().replace("Minus", "");

        Gui.persistenceDataWrapper.getTimeRegTimeMap().put(minusName, Gui.persistenceDataWrapper.getTimeRegTimeMap().get(minusName) - gui.getSubmitDurationMinutes());
        Gui.persistenceDataWrapper.getTimeRegSubmittedTimeMap().get(minusName).setText(gui.convertMinutesToHouersAndMinutes(Gui.persistenceDataWrapper.getTimeRegTimeMap().get(minusName)));
    }

    private void handlePlus(ActionEvent e) {
        String plusName = e.getActionCommand().replace("Plus", "");

        Gui.persistenceDataWrapper.getTimeRegTimeMap().put(plusName, Gui.persistenceDataWrapper.getTimeRegTimeMap().get(plusName) + gui.getSubmitDurationMinutes());
        String time = gui.convertMinutesToHouersAndMinutes(Gui.persistenceDataWrapper.getTimeRegTimeMap().get(plusName));
        String submitTimeTxt = "<html><FONT color=\"#000099\"><U>" + time + "</U></FONT></HTML>";


        Gui.persistenceDataWrapper.getTimeRegSubmittedTimeMap().get(plusName).setText(submitTimeTxt);

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
