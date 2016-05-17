package nf.co.novomic.haveaniceprice.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import nf.co.novomic.haveaniceprice.classes.Product;

/**
 * Data access methods for Products entities
 * <p/>
 * Created by Mikhail on 03.05.2016.
 */
public class ProductsDAO {

    private static final String LOG_TAG = ProductsDAO.class.getSimpleName();

    /**
     * Generate sql query string
     *
     * @param filter - filter by shop
     * @return string of query
     */
    static String getProductsQuery(String filter, String addStatement) {
        return "SELECT " + DBHelper.COLUMN_ID +         //  0
                ", " + DBHelper.COLUMN_PR_SHOP_ID +     //  1
                ", " + DBHelper.COLUMN_PR_URL +         //  2
                ", " + DBHelper.COLUMN_PR_TITLE +       //  3
                ", " + DBHelper.COLUMN_PR_IMG_URL +     //  4
                ", " + DBHelper.COLUMN_PR_TRACK +       //  5
                ", " + DBHelper.COLUMN_PR_SPECIAL +     //  6
                ", " + DBHelper.COLUMN_PR_PRICE +       //  7
                ", " + DBHelper.COLUMN_PR_STD_PRICE +   //  8
                ", " + DBHelper.COLUMN_PR_MIN_PRICE +   //  9
                ", " + DBHelper.COLUMN_PR_MAX_PRICE +   // 10
                ", " + DBHelper.COLUMN_PR_DISC_PRICE +  // 11
                ", " + DBHelper.COLUMN_PR_OLD_PRICE +   // 12
                " FROM " + DBHelper.TABLE_PRODUCTS +
                filter + addStatement + " ORDER BY " + DBHelper.COLUMN_PR_TITLE;
    }

    /**
     * Generate sql query string
     *
     * @param filter - filter by shop
     * @return string of query
     */
    static String getProductsWithShopsQuery(String filter, String addStatement) {
        return "SELECT " + DBHelper.COLUMN_ID +         //  0
                ", " + DBHelper.COLUMN_PR_SHOP_ID +     //  1
                ", " + DBHelper.COLUMN_PR_URL +         //  2
                ", " + DBHelper.COLUMN_PR_TITLE +       //  3
                ", " + DBHelper.COLUMN_PR_IMG_URL +     //  4
                ", " + DBHelper.COLUMN_PR_TRACK +       //  5
                ", " + DBHelper.COLUMN_PR_SPECIAL +     //  6
                ", " + DBHelper.COLUMN_PR_PRICE +       //  7
                ", " + DBHelper.COLUMN_PR_STD_PRICE +   //  8
                ", " + DBHelper.COLUMN_PR_MIN_PRICE +   //  9
                ", " + DBHelper.COLUMN_PR_MAX_PRICE +   // 10
                ", " + DBHelper.COLUMN_PR_DISC_PRICE +  // 11
                ", " + DBHelper.COLUMN_PR_OLD_PRICE +   // 12
                " FROM " + DBHelper.TABLE_PRODUCTS +
                filter + addStatement + " ORDER BY " + DBHelper.COLUMN_PR_TITLE;
    }

    /**
     * @param context     - App context
     * @param whereClause - The where clause of the query
     * @return - The list of Products
     */
    static ArrayList<Product> getProducts(Context context, String whereClause, String addStatement) {
        Log.v(LOG_TAG, whereClause);
        // open connection to the database
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // create empty array list for products
        ArrayList<Product> products = new ArrayList<>();
        // create custom query to get data from products
        String selectProducts = getProductsQuery(whereClause, addStatement);
        Log.v(LOG_TAG, selectProducts);
        // execute query
        Cursor cursor = db.rawQuery(selectProducts, null);

        // loop through the results
        if (cursor.moveToFirst()) {
            do {

                long pId            = cursor.getLong(0);
                long pShopId        = cursor.getLong(1);
                String pUrl         = cursor.getString(2);
                String pTitle       = cursor.getString(3);
                String pImgUrl      = cursor.getString(4);
                int pTrack          = cursor.getInt(5);
                String special      = cursor.getString(6);
                Double price        = cursor.getDouble(7);
                Double std_price    = cursor.getDouble(8);
                Double min_price   = cursor.getDouble(9);
                Double max_price   = cursor.getDouble(10);
                Double disc_price   = cursor.getDouble(11);
                Double old_price    = cursor.getDouble(12);

                // create product
                Product product = new Product(pId, pShopId, pUrl, pTitle, pImgUrl, pTrack, special,
                        price, std_price, min_price, max_price, disc_price, old_price);

                // add product to the products list
                products.add(product);

            } while (cursor.moveToNext());
        }
        //close connection
        cursor.close();
        db.close();
        dbHelper.close();

        return products;
    }

    /**
     * get all Products
     *
     * @param context - app context
     * @return list of Products
     */
    public static ArrayList<Product> getAllProducts(Context context) {
        return getProducts(context, "", "");
    }

    /**
     * get Tracked Products
     *
     * @param context - app context
     * @return list of Products
     */
    public static ArrayList<Product> getProductsByShop(Context context, long shopId) {
        String whereClause = " WHERE " + DBHelper.COLUMN_PR_SHOP_ID + " = '" + String.valueOf(shopId) + "'";
        return getProducts(context, whereClause, "");
    }

    /**
     * get Tracked Products
     *
     * @param context - app context
     * @return list of Products
     */
    public static ArrayList<Product> getTrackedProducts(Context context) {
        String whereClause = " WHERE " + DBHelper.COLUMN_PR_TRACK + " = 1 ";
        return getProducts(context, whereClause, "");
    }

    /**
     * get Tracked Products
     *
     * @param context - app context
     * @return list of Products
     */
    public static ArrayList<Product> getTrackedProductsByShop(Context context, long shopId) {
        String whereClause = " WHERE " + DBHelper.COLUMN_PR_TRACK + " = 1 AND " +
                DBHelper.COLUMN_PR_SHOP_ID + " = '" + String.valueOf(shopId) + "'";
        return getProducts(context, whereClause, "");
    }

    /**
     * get product by url
     *
     * @param context - app context
     * @param url     - url of the product.
     * @return id
     */
    public static long getProductsByUrl(Context context, String url) {
        String whereClause = " WHERE " + DBHelper.COLUMN_PR_URL + " = '" + url + "'";
        ArrayList<Product> products = getProducts(context, whereClause, "");

        return products.size();
    }

    /**
     * get products with shops
     *
     * @param context - app context
     * @param url     - url of the product.
     * @return id
     */
    public static long getProductsWithShops(Context context, String url) {
        String whereClause = " WHERE " + DBHelper.COLUMN_PR_URL + " = '" + url + "'";
        ArrayList<Product> products = getProducts(context, whereClause, "");

        return products.size();
    }

    /**
     * get discounted products
     *
     * @param context - app context
     * @return list of products
     */
    public static ArrayList<Product> getDiscountedProducts(Context context) {
        return getProducts(context, "", "");
    }


    /**
     * Add Product to the table
     *
     * @param product - product for writing to DB
     */
    public static long addProduct(Context context, Product product) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_PR_SHOP_ID, product.getShopId());
        values.put(DBHelper.COLUMN_PR_URL, product.getUrl());
        values.put(DBHelper.COLUMN_PR_TITLE, product.getTitle());
        values.put(DBHelper.COLUMN_PR_IMG_URL, product.getImgUrl());
        values.put(DBHelper.COLUMN_PR_TRACK, product.getTrack());
        values.put(DBHelper.COLUMN_PR_SPECIAL, product.getSpecial());
        values.put(DBHelper.COLUMN_PR_PRICE, product.getPrice());
        values.put(DBHelper.COLUMN_PR_STD_PRICE, product.getStdPrice());
        values.put(DBHelper.COLUMN_PR_MIN_PRICE, product.getMinPrice());
        values.put(DBHelper.COLUMN_PR_MAX_PRICE, product.getMaxPrice());
        values.put(DBHelper.COLUMN_PR_DISC_PRICE, product.getDiscPrice());
        values.put(DBHelper.COLUMN_PR_OLD_PRICE, product.getOldPrice());

        //insert into db
        long id = db.insert(DBHelper.TABLE_PRODUCTS, null, values);

        //close connection
        db.close();
        dbHelper.close();

        return id;
    }


    /**
     * Add Product in the table
     *
     * @param product - product for writing to DB
     */
    public static long updateProduct(Context context, Product product){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_PR_SHOP_ID, product.getShopId());
        values.put(DBHelper.COLUMN_PR_URL, product.getUrl());
        values.put(DBHelper.COLUMN_PR_TITLE, product.getTitle());
        values.put(DBHelper.COLUMN_PR_IMG_URL, product.getImgUrl());
        values.put(DBHelper.COLUMN_PR_TRACK, product.getTrack());
        values.put(DBHelper.COLUMN_PR_SPECIAL, product.getSpecial());
        values.put(DBHelper.COLUMN_PR_PRICE, product.getPrice());
        values.put(DBHelper.COLUMN_PR_STD_PRICE, product.getStdPrice());
        values.put(DBHelper.COLUMN_PR_MIN_PRICE, product.getMinPrice());
        values.put(DBHelper.COLUMN_PR_MAX_PRICE, product.getMaxPrice());
        values.put(DBHelper.COLUMN_PR_DISC_PRICE, product.getDiscPrice());
        values.put(DBHelper.COLUMN_PR_OLD_PRICE, product.getOldPrice());

        String[] whereArgs = new String[]{
                String.valueOf(product.getId())};
        //insert into db
        long id = db.update (DBHelper.TABLE_PRODUCTS, values, "_id = '?'", whereArgs);

        //close connection
        db.close();
        dbHelper.close();

        return id;
    }

    /**
     * Remove Product by ID
     *
     * @param productID - Product's id in the database
     */
    public static void removeProduct(Context context, long productID) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = DBHelper.COLUMN_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(productID)};

        //delete from db
        db.delete(DBHelper.TABLE_PRODUCTS, whereClause, whereArgs);

        //close connection
        db.close();
        dbHelper.close();
    }

    /**
     * Remove all Products from the table
     *
     * @param context - App context
     */
    public static void removeAllProducts(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // delete all
        db.execSQL("delete from " + DBHelper.TABLE_PRODUCTS);
        // free allocated space
        db.execSQL("vacuum");
        //close connection
        db.close();
        dbHelper.close();
    }
}
