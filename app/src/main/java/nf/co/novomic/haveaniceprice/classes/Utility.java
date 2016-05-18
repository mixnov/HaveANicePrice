package nf.co.novomic.haveaniceprice.classes;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Mikhail on 06.05.2016.
 * <p/>
 * Class contains the additional methods
 */
public class Utility {
    /**
     * @param currentTime Time in mills
     * @return Formated string
     */
    public static String longToDate(long currentTime) {
//        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss,SSS", Locale.US);

        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("NZST"));
        calendar.setTimeInMillis(currentTime);
        return sdf.format(calendar.getTime());
    }

    /**
     * @param list The list we need to fill
     * @param shop The Shop object where I get the parameters to fill thi list
     */
    public static void getParseParansList(String[] list, Shop shop) {
        list[1] = "title";
        list[2] = shop.getImgUrl();
        list[3] = shop.getSpecial();
        list[4] = shop.getStdPrice();
        list[5] = shop.getDiscPrice();
        list[6] = shop.getOldPrice();
        list[7] = shop.getSavePrice();
    }

    /**
     * @param number - The string with number
     */
    public static String onlyNumbers(String number) {
        String result = "";
        byte codes[] = number.getBytes();
        for (int i = 0; i < number.length(); i++) {
            if (((codes[i] <= 57) && (codes[i] >= 48)) || (codes[i] == 46)) {
                result += number.charAt(i);
            }
        }
        return result;
    }

    public static boolean compareProductsForStat(Product product, Product newProduct) {

        boolean result = product.getSpecial().equals(newProduct.getSpecial());
        result = result && product.getPrice() == newProduct.getPrice();
        result = result && product.getStdPrice() == newProduct.getStdPrice();
        result = result && product.getMinPrice() == newProduct.getMinPrice();
        result = result && product.getMaxPrice() == newProduct.getMaxPrice();
        result = result && product.getDiscPrice() == newProduct.getDiscPrice();
        result = result && product.getOldPrice() == newProduct.getOldPrice();
        return result;
    }

    public static boolean compareProducts(Product product, Product newProduct) {
        boolean result = product.getUrl().equals(newProduct.getUrl());
        result = result && product.getTitle().equals(newProduct.getTitle());
        result = result && product.getImgUrl().equals(newProduct.getImgUrl());
        result = result && product.getSpecial().equals(newProduct.getSpecial());
        result = result && product.getPrice() == newProduct.getPrice();
        result = result && product.getStdPrice() == newProduct.getStdPrice();
        result = result && product.getMinPrice() == newProduct.getMinPrice();
        result = result && product.getMaxPrice() == newProduct.getMaxPrice();
        result = result && product.getDiscPrice() == newProduct.getDiscPrice();
        result = result && product.getOldPrice() == newProduct.getOldPrice();
        return result;
    }

    public static Product copyObject(Product product) {
        Product newProduct = new Product(product.getId(), product.getShopId(), product.getUrl(), product.getTrack(),
                product.getMinPrice(), product.getMaxPrice());
        newProduct.setTitle("");
        newProduct.setImgUrl("");
        newProduct.setSpecial("");
        return newProduct;
    }

    public static String notNull(String value){
        if(value == null ) value = "";
        return value;
    }
}
