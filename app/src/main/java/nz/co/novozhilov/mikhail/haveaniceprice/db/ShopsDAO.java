package nz.co.novozhilov.mikhail.haveaniceprice.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import nz.co.novozhilov.mikhail.haveaniceprice.Shop;


/**
 * Data access methods for Question entities
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public class ShopsDAO {


    /**
     * get all categories for test type
     *
     * @param context  - app context
     * @return list of questions
     */
    public static ArrayList<Shop> getShopsList(Context context) {
        // open connection to the database
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // create empty array list for categories
        ArrayList<Shop> shops = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TABLE_SHOPS,
                new String[]{DBHelper.COLUMN_ID, DBHelper.COLUMN_SH_URL, DBHelper.COLUMN_SH_TITLE,
                        DBHelper.COLUMN_SH_IMG_URL, DBHelper.COLUMN_SH_SPECIAL, DBHelper.COLUMN_SH_STD_PRICE,
                        DBHelper.COLUMN_SH_DISC_PRICE, DBHelper.COLUMN_SH_OLD_PRICE, DBHelper.COLUMN_SH_SAVE_PRICE},
                null, null, null, null, DBHelper.COLUMN_ID);
        Log.d("SQL", "EXECUTED!");
        // loop through the results
        if (cursor.moveToFirst()) {
            do {
                int sId = cursor.getInt(0);
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
}
