package nz.co.novozhilov.mikhail.haveaniceprice;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Mikhail on 06.05.2016.
 */
public class Utility {
    public static String longToDate(Long currentTime){
//        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss,SSS", Locale.US);

        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("NZST"));
        calendar.setTimeInMillis(currentTime);
        return sdf.format(calendar.getTime());
    }
}
