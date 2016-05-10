package nz.co.novozhilov.mikhail.haveaniceprice.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import nz.co.novozhilov.mikhail.haveaniceprice.Product;

/**
 * Data access methods for Products entities
 *
 * Created by Mikhail on 03.05.2016.
 */
public class ProductsDAO {


    /**
     * Generate sql query string
     *
     * @param filter - filter by shop
     * @return string of query
     */
    static String getProductsQuery(String filter) {
        return "SELECT " + DBHelper.COLUMN_ID +         //  0
                ", " + DBHelper.COLUMN_ID +             //  1
                ", " + DBHelper.COLUMN_P_URL +          //  2
                ", " + DBHelper.COLUMN_P_TITLE +        //  3
                ", " + DBHelper.COLUMN_P_IMG_URL +      //  4
                " FROM " + DBHelper.TABLE_PRODUCTS +
                filter + " ORDER BY " + DBHelper.COLUMN_P_TITLE;
    }

    /**
     *
     * @param context - App context
     * @param whereClause - The where clause of the query
     * @return - The list of Products
     */
    static ArrayList<Product> getProducts(Context context, String whereClause) {
        // open connection to the database
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        // create empty array list for products
        ArrayList<Product> products = new ArrayList<>();
        // create custom query to get data from products
        String selectProducts = getProductsQuery(whereClause);
        // execute query
        Cursor cursor = db.rawQuery(selectProducts, null);

        long current_id = -1;
        Product product;

        // loop through the results
        if (cursor.moveToFirst()) {
            do {
                long qId = cursor.getLong(0);
                if (qId != current_id) {
                    long pShopId = cursor.getLong(1);
                    String pUrl = cursor.getString(2);
                    String pTitle = cursor.getString(3);
                    String pImgUrl = cursor.getString(4);

                    // create product
                    product = new Product(qId, pShopId, pUrl, pTitle, pImgUrl);

                    // add product to the products list
                    products.add(product);
                    current_id = qId;
                }
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
     * @param context  - app context
     * @return list of Products
     */
    static ArrayList<Product> getAllProducts(Context context) {
        return getProducts(context, "");
    }

    /**
     * get product by url
     *
     * @param context  - app context
     * @param url - url of the product.
     * @return id
     */
    public static long getProductsByUrl(Context context, String url) {
        String whereClause = " WHERE " + DBHelper.COLUMN_P_URL + " = '" + url + "'";
        ArrayList<Product> products = getProducts(context, whereClause);

        return products.size();
    }

    /**
     * get discounted products
     *
     * @param context  - app context
     * @return list of products
     */
    static ArrayList<Product> getDiscountedProducts(Context context) {
        return getProducts(context, "");
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
        values.put(DBHelper.COLUMN_P_SHOP_ID, product.getShopId());
        values.put(DBHelper.COLUMN_P_URL, product.getUrl());
        values.put(DBHelper.COLUMN_P_TITLE, product.getTitle());
        values.put(DBHelper.COLUMN_P_IMG_URL, product.getImgUrl());

        //insert into db
        long id = db.insert(DBHelper.TABLE_PRODUCTS, null, values);

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
    static void removeProduct(Context context, long productID) {
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
     * @param context - App context
     */
    static void removeAllProducts(Context context) {
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
