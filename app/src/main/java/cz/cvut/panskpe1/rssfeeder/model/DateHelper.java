package cz.cvut.panskpe1.rssfeeder.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by petr on 4/17/16.
 */
public class DateHelper {

    private final static String formatType = "dd/MM/yyyy hh:mm:ss.SSS";
    private final static SimpleDateFormat formatter = new SimpleDateFormat(formatType);

    public static String convertMiliSecondsToDate(Long time) {
        return formatter.format(new Date(time));
    }

    public static long getTimeInMiliSeconds(String dateString) {
        Date date = null;
        try {
            date = formatter.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            return 0;
        }
    }
}
