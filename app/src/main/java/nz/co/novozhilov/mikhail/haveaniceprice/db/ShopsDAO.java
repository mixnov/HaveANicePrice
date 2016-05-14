package nz.co.novozhilov.mikhail.haveaniceprice.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import nz.co.novozhilov.mikhail.haveaniceprice.Shop;

/**
 * Data access methods for Shops entities
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public class ShopsDAO {

    /**
     * Generate sql query string
     *
     * @param filter - filter by shop
     * @return string of query
     */
    static String getShopsQuery(String filter, String addStatement) {
        return "SELECT " + DBHelper.COLUMN_ID +         //  0
                ", " + DBHelper.COLUMN_SH_URL +         //  1
                ", " + DBHelper.COLUMN_SH_TITLE +       //  2
                ", " + DBHelper.COLUMN_SH_IMG_URL +     //  3
                ", " + DBHelper.COLUMN_SH_SPECIAL +     //  4
                ", " + DBHelper.COLUMN_SH_STD_PRICE +   //  5
                ", " + DBHelper.COLUMN_SH_DISC_PRICE +  //  6
                ", " + DBHelper.COLUMN_SH_OLD_PRICE +   //  7
                ", " + DBHelper.COLUMN_SH_SAVE_PRICE +  //  8
                " FROM " + DBHelper.TABLE_SHOPS +
                filter + addStatement + " ORDER BY " + DBHelper.COLUMN_SH_TITLE;
    }

    /**
     * get all Shops
     *
     * @param context - App context
     * @return - The list of Shops
     */
    static ArrayList<Shop> getShops(Context context, String whereClause, String addStatement) {
        // open connection to the database
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // create empty array list for Shops
        ArrayList<Shop> shops = new ArrayList<>();
        // create custom query to get data from products
        String selectShops = getShopsQuery(whereClause, addStatement);
        // execute query
        Cursor cursor = db.rawQuery(selectShops, null);

        // loop through the results
        if (cursor.moveToFirst()) {
            do {
                long sId = cursor.getInt(0);
                String sUrl = cursor.getString(1);
                String sTitle = cursor.getString(2);
                String sImgUrl = cursor.getString(3);
                String sSpecial = cursor.getString(4);
                String sStdPrice = cursor.getString(5);
                String sDiscPrice = cursor.getString(6);
                String sOldPrice = cursor.getString(7);
                String sSavePrice = cursor.getString(8);

                // create shop
                Shop shop = new Shop(sId, sUrl, sTitle, sImgUrl, sSpecial, sStdPrice, sDiscPrice, sOldPrice, sSavePrice);
                // add shop to the list of shops
                shops.add(shop);
            } while (cursor.moveToNext());
        }
        //close connection
        cursor.close();
        db.close();
        dbHelper.close();
        return shops;
    }

    /**
     * get all Shops
     *
     * @param context - App context
     * @return - The list of Shops
     */
    public static ArrayList<Shop> getShopsList(Context context) {
        return getShops(context, "", "");
    }

    /**
     * get Products Shops
     *
     * @param context - App context
     * @return - The list of Shops
     */
    public static ArrayList<Shop> getProductsShops(Context context) {
        return getShops(context, "", "");
    }


    public static Shop getShopById(Context context, long id){
        String whereClause = " WHERE " + DBHelper.COLUMN_ID + " = " + String.valueOf(id);
        ArrayList<Shop> shops = getShops(context, whereClause, "");

        return shops.get(0);
    }

    public static HashMap<Long, Shop> getShopsHashMap(Context context){
        HashMap<Long, Shop> hm = new HashMap<>();
        ArrayList<Shop> shops = getShops(context, "", "");

        for(Shop shop : shops) {
            hm.put(shop.getId(), shop);
        }

        return hm;
    }
}
