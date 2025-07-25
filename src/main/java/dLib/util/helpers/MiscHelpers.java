package dLib.util.helpers;

import dLib.util.DLibLogger;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class MiscHelpers {
    public static void copyToClipboard(String s) {
        StringSelection selection = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    public static String readStringFromUri(String uri){
        try {
            java.net.URL url = new java.net.URL(uri);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder content = new StringBuilder();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine).append("\n");
            }
            in.close();

            return content.toString().trim();
        } catch (Exception e) {
            DLibLogger.logError("Failed to fetch content from " + uri, DLibLogger.ErrorType.NON_FATAL);
            e.printStackTrace();
            return null;
        }
    }


    public static Date generateDateTime(int day, int month, int year,
                                        int hour, int minute,
                                        String zone) {
        ZoneId zoneId = ZoneId.of(zone);

        final LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, minute);
        ZonedDateTime zdt = ldt.atZone(zoneId);

        return Date.from(zdt.toInstant());
    }
}
