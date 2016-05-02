package com.skov.timeRegForrest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ALSK on 06-01-2016.
 */
public class ListOfIssues {

    public static List<String> getListOfInterestingJirasList() {
        List<String> jiras = new ArrayList<String>();

        jiras.add("XP-1604 - Static issue: Kanban møder");
        jiras.add("XP-1615 - Static issue: Pokerplanning");
        jiras.add("XP-1614 - Static issue: SagsC - Forretningsprioriteret forvaltning");
        jiras.add("XP-1613 - Static issue: SagsC - IT-prioriteret forvaltning");
        jiras.add("XP-1005 - Static issue: Egen administration vedr. Xportalen");
        jiras.add("XP-885 - Static issue: Other");
        jiras.add("XP-884 - Static issue: External project");
        jiras.add("XP-883 - Static issue: Driftstabilitet i Xportalen");
        jiras.add("XP-881 - Static issue: Dokumentation");
        jiras.add("XP-880 - Static issue: Teaching Xportal");
        jiras.add("XP-879 - Static issue: Learning Xportal");
        jiras.add("XP-878 - Static issue: Ikke TK prioriterede opgaver");
        jiras.add("XP-877 - Static issue: TK prioriterede ændringsønsker");
        jiras.add("XP-876 - Static issue: TK prioriterede fejlrettelser");
        jiras.add("XP-874 - Static issue: Rejsetid");
        jiras.add("XP-872 - Static issue: Hjælp til projekter og andre forvaltninger");
        jiras.add("XP-871 - Static issue: Testmiljøer");
        jiras.add("XP-870 - Static issue: Møder Xportalen");
        jiras.add("XP-869 - Static issue: Test + testcases");
        jiras.add("XP-867 - Static issue: Grooming af opgaver");
        jiras.add("XP-864 - Static Issue: Daglig forvaltning - daily maintance");
        jiras.add("XP-1488 - Static issue: Uddannelsesmiljø");
        jiras.add("XP-882 - Static issue: SagsC - Daglig support");
        jiras.add("XP-1610 - Static issue: ServiceManager opfølgning IT");
        jiras.add("XP-1605 - Static issue: ServiceManageropfølgning TK");

        jiras.add("---");

        jiras.add("XP-1343 - prj. TK-erhverv Collaboration");
        jiras.add("XP-1344 - Static issue: Projektsamarbejde P5722 TK-Erhv & P5753 Ny gar.model (5753-10-605)");

        jiras.add("---");

        jiras.add("XP-1611 - Static issue: Områdeansvarlig");
        jiras.add("XP-1607 - Static issue: Releasemaster - ekstern");
        jiras.add("XP-1606 - Static issue: Releasemaster - intern");
        jiras.add("XP-875 - Static issue: Forvaltningsrapportering");
        jiras.add("XP-868 - Static issue: Arkitekturansvar");
        jiras.add("XP-866 - Static issue: Systemansvarlig - opgaver");
        jiras.add("XP-865 - Static issue: Releasemaster");
        jiras.add("XP-1334 - Static issue: P5765 samarbejde (\"5765 Optimeret Ejendomsvurdering\")");
        jiras.add("XP-1240 - Static issue: Projektsamarbejde P4965 Prioritetslån (4965-0372 '13 Forvaltning (P4965: Samarbejde mellem forvaltning og projekt - \"Prioritetslån\")')");
        jiras.add("XP-1238 - Static issue: Projektsamarbejde P5788 Etablering af XPortal 2.0 (5788-10-600 ’ Møder – sparring’)");
        jiras.add("XP-1237 - Static issue: Projektsamarbejde P5673 Digital signatur af TKdokumenter (5673-20-550 'P5673 Co-operation between maintenance and project Digital signatur af TKdokumenter')");



        return jiras;

    }


    public static String[] getListOfInterestingJirasStr() {

        return getListOfInterestingJirasList().toArray(new String[getListOfInterestingJirasList().size()]);
    }

    public static String getJiraFromStr(String str) {

        str.replace("XP-", "");
        return str.split(" - ")[0];

    }

    public static String getDescriptionFromStr(String str) {

        str.replace("XP-", "");
        return str.split(" - ")[1];

    }

}
