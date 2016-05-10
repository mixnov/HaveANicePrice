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
    public static String longToDate(Long currentTime){
//        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss,SSS", Locale.US);

        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("NZST"));
        calendar.setTimeInMillis(currentTime);
        return sdf.format(calendar.getTime());
    }

    public static Object setProperty(Object object, String fieldName, String value) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        if (field.getType() == Character.TYPE) {field.set(object.getClass(), value.charAt(0)); return object;}
        if (field.getType() == Short.TYPE) {field.set(object.getClass(), Short.parseShort(value)); return object;}
        if (field.getType() == Integer.TYPE) {field.set(object.getClass(), Integer.parseInt(value)); return object;}
        if (field.getType() == Long.TYPE) {field.set(object.getClass(), Long.parseLong(value)); return object;}
        if (field.getType() == Float.TYPE) {field.set(object.getClass(), Float.parseFloat(value)); return object;}
        if (field.getType() == Double.TYPE) {field.set(object.getClass(), Double.parseDouble(value)); return object;}
        if (field.getType() == Byte.TYPE) {field.set(object.getClass(), Byte.parseByte(value)); return object;}
        if (field.getType() == Boolean.TYPE) {field.set(object.getClass(), Boolean.parseBoolean(value)); return object;}
        Field[] fields = object.getClass().getFields();
//        fields[0].
        field.set(object.getClass(), value);
        return object;
    }

    public static void getParseParansList(String[] list, Shop shop){
//        public static ArrayList<String> getParseParansList(ArrayList<String> list, Shop shop){
        //ArrayList<String> list = new ArrayList<>();
        //list = getFields(list, shop);
//        list.add("title");
//        //list.add(shop.getTitle());
//        list.add(shop.getImgUrl());
//        list.add(shop.getSpecial());
//        list.add(shop.getStdPrice());
//        list.add(shop.getDiscPrice());
//        list.add(shop.getOldPrice());
//        list.add(shop.getSavePrice());

        list[1] = "title";
        //list.add(shop.getTitle());
        list[2] = shop.getImgUrl();
        list[3] = shop.getSpecial();
        list[4] = shop.getStdPrice();
        list[5] = shop.getDiscPrice();
        list[6] = shop.getOldPrice();
        list[7] = shop.getSavePrice();
    }

    public static ArrayList<String> getFields(ArrayList<String> list, Object object){
        Field[] fields = object.getClass().getDeclaredFields();
        String _package =  object.getClass().getPackage().getName();
        for (Field field : fields) {
//            for (int i = 0; i < fields.length; i++) {
//            Field field = fields[i];
            String fieldName = field.toString();
            if (!fieldName.contains("private")) continue;
            fieldName = field.getName();
            if (fieldName.charAt(0)=='_') continue;
            if (list.indexOf(fieldName) > -1) continue;
//            fieldName = fieldName.substring(fieldName.indexOf(_package) + _package.length());
            list.add(fieldName);
        }
        return list;
    }

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
