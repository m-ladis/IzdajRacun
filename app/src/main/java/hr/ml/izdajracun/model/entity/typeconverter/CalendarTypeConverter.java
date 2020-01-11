package hr.ml.izdajracun.model.entity.typeconverter;

import androidx.room.TypeConverter;

import java.util.Calendar;

public class CalendarTypeConverter {
    @TypeConverter
    public static Calendar calendarFromTimestamp(Long timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        return calendar;
    }

    @TypeConverter
    public static Long timestampFromCalendar(Calendar calendar){
        return calendar.getTimeInMillis();
    }
}
