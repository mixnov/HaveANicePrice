package nz.co.novozhilov.mikhail.haveaniceprice;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Mikhail on 06.05.2016.
 *
 * Class contains the additional methods
 */
public class Utility {
    /**
     *
     * @param currentTime Time in mills
     * @return Formated string
     */
    public static String longToDate(long currentTime){
//        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss,SSS", Locale.US);

        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("NZST"));
        calendar.setTimeInMillis(currentTime);
        return sdf.format(calendar.getTime());
    }

    /**
     *
     * @param object Object
     * @param fieldName Name of the field we want to set
     * @param value The
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setProperty(Object object, String fieldName, String value) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        if (field.getType() == Character.TYPE) {field.set(object.getClass(), value.charAt(0)); return;}
        if (field.getType() == Short.TYPE) {field.set(object.getClass(), Short.parseShort(value)); return;}
        if (field.getType() == Integer.TYPE) {field.set(object.getClass(), Integer.parseInt(value)); return;}
        if (field.getType() == Long.TYPE) {field.set(object.getClass(), Long.parseLong(value)); return;}
        if (field.getType() == Float.TYPE) {field.set(object.getClass(), Float.parseFloat(value)); return;}
        if (field.getType() == Double.TYPE) {field.set(object.getClass(), Double.parseDouble(value)); return;}
        if (field.getType() == Byte.TYPE) {field.set(object.getClass(), Byte.parseByte(value)); return;}
        if (field.getType() == Boolean.TYPE) {field.set(object.getClass(), Boolean.parseBoolean(value)); return;}
        Field[] fields = object.getClass().getFields();
        field.set(object.getClass(), value);
    }

    /**
     *
     * @param list The list we need to fill
     * @param shop The Shop object where I get the parameters to fill thi list
     */
    public static void getParseParansList(String[] list, Shop shop){

        list[1] = "title";
        list[2] = shop.getImgUrl();
        list[3] = shop.getSpecial();
        list[4] = shop.getStdPrice();
        list[5] = shop.getDiscPrice();
        list[6] = shop.getOldPrice();
        list[7] = shop.getSavePrice();
    }

    /**
     *
     * @param list The list of fields
     * @param object Object
     */
    public static void getFields(ArrayList<String> list, Object object){
        Field[] fields = object.getClass().getDeclaredFields();
        String _package =  object.getClass().getPackage().getName();
        for (Field field : fields) {
            String fieldName = field.toString();
            if (!fieldName.contains("private")) continue;
            fieldName = field.getName();
            if (fieldName.charAt(0)=='_') continue;
            if (list.indexOf(fieldName) > -1) continue;
            list.add(fieldName);
        }
    }

    /**
     *
     * @param number - The string with number
     */
    public static String onlyNumbers(String number){
        String result="";
        byte codes[] = number.getBytes();
        for(int i = 0; i<number.length(); i++){
            if (((codes[i] <= 57) && (codes[i] >= 48)) || (codes[i] == 46)){
                result += number.charAt(i);
            }
        }
        return result;
    }
}
