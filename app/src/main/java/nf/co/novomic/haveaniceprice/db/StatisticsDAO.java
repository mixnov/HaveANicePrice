package nf.co.novomic.haveaniceprice.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import nf.co.novomic.haveaniceprice.classes.Statistics;

/**
 * Created by Mikhail on 04.05.2016.
 * Data access methods for Statistics entities
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public class StatisticsDAO {

    /**
     * Generate sql query string
     *
     * @param productFilter - Additional where clause
     * @param addStatement - Additional statement
     * @return - The query string
     */
    static String getStatisticsQuery(String productFilter, String addStatement) {
        //SELECT tsh.title, tp.title, ts.date, ts.special, ts.price, ts.std_price, ts.disc_price, ts.old_price, ts.save_price  FROM stat ts JOIN products tp ON ts.product_id = tp._id JOIN shops tsh ON tp.shop_id = tsh._id
        return "SELECT tst." + DBHelper.COLUMN_ID +         // 0
                ", tsh." + DBHelper.COLUMN_SH_TITLE +       // 1
                ", tpr." + DBHelper.COLUMN_PR_TITLE +       // 2
                ", tst." + DBHelper.COLUMN_ST_PRODUCT_ID +  // 3
                ", tst." + DBHelper.COLUMN_ST_DATE +        // 4
                ", tst." + DBHelper.COLUMN_ST_SPECIAL +     // 5
                ", tst." + DBHelper.COLUMN_ST_PRICE +       // 6
                ", tst." + DBHelper.COLUMN_ST_STD_PRICE +   // 7
                ", tst." + DBHelper.COLUMN_ST_DISC_PRICE +  // 8
                ", tst." + DBHelper.COLUMN_ST_OLD_PRICE +   // 9
                ", tst." + DBHelper.COLUMN_ST_SAVE_PRICE +  //10
                " FROM " + DBHelper.TABLE_STATISTICS + " tst " +
                " JOIN " + DBHelper.TABLE_PRODUCTS +
                " tpr ON tst." + DBHelper.COLUMN_ST_PRODUCT_ID +
                " = tpr." + DBHelper.COLUMN_ID +
                " JOIN " + DBHelper.TABLE_SHOPS +
                " tsh ON tpr." + DBHelper.COLUMN_PR_SHOP_ID +
                " = tsh." + DBHelper.COLUMN_ID +
                productFilter + addStatement +
                " ORDER BY tsh." + DBHelper.COLUMN_SH_TITLE + ", tpr." + DBHelper.COLUMN_PR_TITLE;
    }

    /**
     *
     * @param context - App context
     * @param whereClause - The where clause of the query
     * @param addStatement - The additional statement for the query
     * @return - The list of Statistics objects
     */
    public static ArrayList<Statistics> getStatistics(Context context, String whereClause, String addStatement) {
        // open connection to the database
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // create empty array list for Statistics
        ArrayList<Statistics> statisticsList = new ArrayList<>();
        // create custom query to get data from Statistics
        String getStatistics = getStatisticsQuery(whereClause, addStatement);
        // execute query
        Cursor cursor = db.rawQuery(getStatistics, null);

        // loop through the results
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getInt(0);
                String shopTitle    = cursor.getString(1);
                String productTitle = cursor.getString(2);
                long productId       = cursor.getLong(3);
                long date           = cursor.getLong(4);
                String special      = cursor.getString(5);
                Double price        = cursor.getDouble(6);
                Double std_price    = cursor.getDouble(7);
                Double disc_price   = cursor.getDouble(8);
                Double old_price    = cursor.getDouble(9);
                Double save_price   = cursor.getDouble(10);

                // create Statistics object
                Statistics statistics = new Statistics(id, shopTitle, productTitle, productId, date,
                        special, price, std_price, disc_price, old_price, save_price);

                // add Statistics to the list
                statisticsList.add(statistics);
            } while (cursor.moveToNext());
        }
        //close connection
        cursor.close();
        db.close();
        dbHelper.close();

        return statisticsList;
    }

    /**
     * get all Statistics for the Product
     *
     * @param context  - app context
     * @param shopId - ID of the Shop in DB
     * @return list of Statistics records
     */
    public static ArrayList<Statistics> getStatisticsByShop(Context context, long shopId) {
        String whereClause = " WHERE tsh." + DBHelper.COLUMN_ID + " = " + String.valueOf(shopId);
        return getStatistics(context, whereClause, "");
    }

    /**
     * get Statistics by Product
     *
     * @param context  - app context
     * @param productId - ID of the product
     * @return list of Statistics records
     */
    public static ArrayList<Statistics> getStatisticsByProduct(Context context, long productId) {
        String whereClause = " WHERE tpr." + DBHelper.COLUMN_ID + " = " + String.valueOf(productId);
        return getStatistics(context, whereClause, "");
    }

    /**
     * Add Statistics to DB
     *
     * @param statistics - Statistics Object for writing to DB
     */
    public static long addStatistics(Context context, Statistics statistics) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ST_PRODUCT_ID, statistics.getProductId());

        values.put(DBHelper.COLUMN_ST_DATE, System.currentTimeMillis());
        values.put(DBHelper.COLUMN_ST_SPECIAL, statistics.getSpecial());
        values.put(DBHelper.COLUMN_ST_PRICE, statistics.getPrice());
        values.put(DBHelper.COLUMN_ST_STD_PRICE, statistics.getStdPrice());
        values.put(DBHelper.COLUMN_ST_DISC_PRICE, statistics.getDiscPrice());
        values.put(DBHelper.COLUMN_ST_OLD_PRICE, statistics.getOldPrice());
        values.put(DBHelper.COLUMN_ST_SAVE_PRICE, statistics.getSavePrice());

        //insert into db (if id is already there - ignore)
        long id = db.insert(DBHelper.TABLE_STATISTICS, null, values);

        //close connection
        db.close();
        dbHelper.close();

        return id;
    }


    /**
     * Remove Statistics by Product
     *
     * @param productId - Product's id in the database
     */
    static int removeStatisticsByProduct(Context context, long productId) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = DBHelper.COLUMN_ST_PRODUCT_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(productId)};

        //delete from db
        int count = db.delete(DBHelper.TABLE_STATISTICS, whereClause, whereArgs);

        //close connection
        db.close();
        dbHelper.close();

        return count;
    }

    /**
     * Remove Statistics by Shop
     *
     * @param shopId - Shop's id in the database
     */
    static int removeStatisticsByShop(Context context, long shopId) {
//        DBHelper dbHelper = new DBHelper(context);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        String whereClause = DBHelper.COLUMN_ST_PRODUCT_ID + "=?";
//        String[] whereArgs = new String[]{String.valueOf(shopId)};

        //delete from db
//        int count = 0;//db.delete(DBHelper.TABLE_STATISTICS, whereClause, whereArgs);

        // free allocated space
//        db.execSQL("vacuum");
//
//        //close connection
//        db.close();
//        dbHelper.close();

        return 0;
    }

    /**
     * Remove all Statistics
     */
    static void removeAllStatistics(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // delete all
        db.execSQL("delete from " + DBHelper.TABLE_STATISTICS);
        // free allocated space
        db.execSQL("vacuum");
        //close connection
        db.close();
        dbHelper.close();
    }

}
