package nz.co.novozhilov.mikhail.haveaniceprice.db;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.sql.SQLException;
import android.annotation.SuppressLint;
import android.util.Log;

/**
 * SQLite helper class for database
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public final class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getName();

    private static final int DB_VERSION = 1; // DB version (used on upgrade)
    private static String DB_PATH = "/data/data/";
    private static String DB_NAME = "havaniceprice.db";     // Database Name
    private static final int SCHEMA = 1; // версия базы данных

    private SQLiteDatabase mDataBase = null;
    private Context mContext;

    // Tables
    public static final String TABLE_PRODUCTS       = "products";
    public static final String TABLE_SHOPS          = "shops";
    public static final String TABLE_STAT           = "stat";
    public static final String TABLE_USER_PROFILE   = "user_profile";

    // Common columns names
    public static final String COLUMN_ID            = "_id";

    //  Products table columns
    public static final String COLUMN_P_SHOP_ID     = "shop_id";
    public static final String COLUMN_P_IMG_URL     = "img_url";
    public static final String COLUMN_P_TITLE       = "title";
    public static final String COLUMN_P_URL         = "url";

    //  Shops table columns
    public static final String COLUMN_SH_TITLE      = "title";
    public static final String COLUMN_SH_URL        = "url";
    public static final String COLUMN_SH_DIV_TITLE  = "div_title";
    public static final String COLUMN_SH_TITLE_IN   = "title_in";
    public static final String COLUMN_SH_STD_PRICE  = "std_price";
    public static final String COLUMN_SH_DISC_PRICE = "disc_price";
    public static final String COLUMN_SH_OLD_PRICE  = "old_price";

    // Stat table columns
    public static final String COLUMN_ST_PRODUCT_ID = "product_id";
    public static final String COLUMN_ST_DATE       = "date";
    public static final String COLUMN_ST_STD_PRICE  = "std_price";
    public static final String COLUMN_ST_DISC_PRICE = "disc_price";
    public static final String COLUMN_ST_OLD_PRICE  = "old_price";

    // User profile table columns
    public static final String COLUMN_UP_NAME = "name";
    public static final String COLUMN_UP_PASS = "pass";


    @SuppressLint("SdCardPath")
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.mContext = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        openDataBase();
    }

    /**
     * Run copy database from assets if not exists
     */
    public void createDB(){
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
//            File file = new File(DB_PATH + DB_NAME);
//            if (!file.exists()) {
                this.getReadableDatabase();
                //получаем локальную бд как поток
                myInput = mContext.getAssets().open(DB_NAME);
                // Путь к новой бд
                String outFileName = DB_PATH + DB_NAME;

                // Открываем пустую бд
                myOutput = new FileOutputStream(outFileName);

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
//            }
        }
        catch(IOException ex){

        }

//        try {
//            copyDataBase();
//        } catch (IOException e) {
//            Log.e(TAG, "Copy database failed!");
//        }
//        boolean dbExist = checkDataBase();
//        if(dbExist){
//            Log.v(TAG, "database does exist");
//        }else{
//            Log.v(TAG, "database does not exist");
//            this.getReadableDatabase();
//            try {
//                copyDataBase();
//            } catch (IOException e) {
//                Log.e(TAG, "Copy database failed!");
//            }
//        }
    }

    /**
     * Copy database from assets to the app data
     * @throws IOException
     */
    private void copyDataBase() throws IOException{
        // get source and target streams
        InputStream myInput = mContext.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        // copy file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        // close streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * Check if source db is created
     *
     * @return true if exists
     */
    private boolean checkDataBase(){
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    /**
     * Allows to write and read to/from the database
     *
     * @return database opened for writing and reading
     */
    public boolean openDataBase() {
        String mPath = DB_PATH + DB_NAME;
        createDB();
        if (!checkDataBase()) {
            createDB();
        }
        if (mDataBase == null) {
            mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
        }

        return mDataBase.isOpen();
    }

    /**
     * Close db connection
     */
    @Override
    public synchronized void close() {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
