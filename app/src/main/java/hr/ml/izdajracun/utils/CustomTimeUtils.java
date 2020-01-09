package hr.ml.izdajracun.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CustomTimeUtils {
    public static Integer getCurrentYear(){
        Calendar calendar = new GregorianCalendar(Locale.getDefault());

        return calendar.get(Calendar.YEAR);
    }
}
