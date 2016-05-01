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
    public static ArrayList<Shop> getAllShops(Context context) {
        // open connection to the database
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // create empty array list for categories
        ArrayList<Shop> shops = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TABLE_SHOPS,
                new String[]{DBHelper.COLUMN_ID, DBHelper.COLUMN_SH_TITLE, DBHelper.COLUMN_SH_URL,
                        DBHelper.COLUMN_SH_DIV_TITLE, DBHelper.COLUMN_SH_TITLE_IN, DBHelper.COLUMN_SH_STD_PRICE,
                        DBHelper.COLUMN_SH_DISC_PRICE, DBHelper.COLUMN_ST_OLD_PRICE},
                null, null, null, null, DBHelper.COLUMN_ID);
        Log.d("SQL", "EXECUTED!");
        // loop through the results
        if (cursor.moveToFirst()) {
            do {
                Log.d("SQL", "LOOP!");
                int sId = cursor.getInt(0);
                String sTitle = cursor.getString(1);
                String sUrl = cursor.getString(2);
                String sDivTitle = cursor.getString(3);
                String sTitleIn = cursor.getString(4);
                Double sStdPrice = cursor.getDouble(5);
                Double sDiscPrice = cursor.getDouble(6);
                Double sOldPrice = cursor.getDouble(7);

                // create question
                Shop category = new Shop(sId, sTitle, sUrl, sDivTitle, sTitleIn, sStdPrice, sDiscPrice, sOldPrice);
                // add question to the list of questions
                shops.add(category);
            } while (cursor.moveToNext());
        }
        Log.d("SQL", "FINISHED!");
        //close connection
        cursor.close();
        db.close();
        dbHelper.close();
        return shops;
    }
}
