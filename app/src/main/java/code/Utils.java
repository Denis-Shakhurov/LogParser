package code;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static Date stringToDate(String strDate) {
        DateFormat format = new SimpleDateFormat("d.M.yyyy H:m:s");
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    public static boolean dateBetweenDates(Date current, Date after, Date before) {
        if (after == null) {
            after = new Date(0);
        }
        if (before == null) {
            before = new Date(Long.MAX_VALUE);
        }
        return current.after(after) && current.before(before);
    }

    public static String normalizeWord(String word) {
        String result = word.trim();
        if (result.startsWith("\"") && result.endsWith("\"")) {
            return result.substring(1, result.length() - 1);
        }
        return result;
    }
}
