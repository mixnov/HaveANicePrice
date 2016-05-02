package nz.co.novozhilov.mikhail.haveaniceprice.db;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;

import android.os.Environment;

import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * SQLite helper class for database
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public final class DBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DBHelper.class.getName();

    private static final int DB_VERSION = 1; // DB version (used on upgrade)
    private static String DB_PATH = "/data/data/";
    private static String DB_NAME = "havaniceprice.db";     // Database Name
    private static final int SCHEMA = 1; // версия базы данных

    private SQLiteDatabase mDataBase = null;
    private Context mContext;

    // Tables
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_SHOPS = "shops";
    public static final String TABLE_STAT = "stat";
    public static final String TABLE_USER_PROFILE = "user_profile";

    // Common columns names
    public static final String COLUMN_ID = "_id";

    //  Products table columns
    public static final String COLUMN_P_SHOP_ID = "shop_id";
    public static final String COLUMN_P_IMG_URL = "img_url";
    public static final String COLUMN_P_TITLE = "title";
    public static final String COLUMN_P_URL = "url";

    //  Shops table columns
    public static final String COLUMN_SH_TITLE = "title";
    public static final String COLUMN_SH_URL = "url";
    public static final String COLUMN_SH_DIV_TITLE = "div_title";
    public static final String COLUMN_SH_TITLE_IN = "title_in";
    public static final String COLUMN_SH_STD_PRICE = "std_price";
    public static final String COLUMN_SH_DISC_PRICE = "disc_price";
    public static final String COLUMN_SH_OLD_PRICE = "old_price";

    // Stat table columns
    public static final String COLUMN_ST_PRODUCT_ID = "product_id";
    public static final String COLUMN_ST_DATE = "date";
    public static final String COLUMN_ST_STD_PRICE = "std_price";
    public static final String COLUMN_ST_DISC_PRICE = "disc_price";
    public static final String COLUMN_ST_OLD_PRICE = "old_price";

    // User profile table columns
    public static final String COLUMN_UP_NAME = "name";
    public static final String COLUMN_UP_PASS = "pass";

    final String FILENAME = "file";

    final String DIR_SD = "my_files/database/";

    private static String fullDBFileName;


    @SuppressLint("SdCardPath")
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.mContext = context;
        fullDBFileName = "/data/data/" + context.getPackageName() + "/databases/" + DB_NAME;
        openDataBase();
    }

    /**
     * Allows to write and read to/from the database
     *
     * @return database opened for writing and reading
     */
    public boolean openDataBase() {
        //createDB();
        if (!checkDataBase()) {
            createDB();
        }
        if (mDataBase == null) {
            try {
                writeFileSD();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mDataBase = SQLiteDatabase.openDatabase(fullDBFileName, null, SQLiteDatabase.OPEN_READWRITE);
        }

        return mDataBase.isOpen();
    }

    /**
     * Check if source db is created
     *
     * @return true if exists
     */
    private boolean checkDataBase() {
        File dbFile = new File(fullDBFileName);
        return dbFile.exists();
    }

    /**
     * Run copy database from assets if not exists
     */
    public void createDB() {

        try {
            copyDataBase();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Copy database failed!");
        }
        boolean dbExist = checkDataBase();
        if(dbExist){
            Log.v(LOG_TAG, "database does exist");
        }else{
            Log.v(LOG_TAG, "database does not exist");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Copy database failed!");
            }
        }
    }

    /**
     * Copy database from assets to the app data
     *
     * @throws IOException
     */
    private void copyDataBase() throws IOException {
        InputStream myInput = null;
        OutputStream myOutput = null;
        this.getReadableDatabase();
        // get source and target streams
        myInput = mContext.getAssets().open(DB_NAME);
        myOutput = new FileOutputStream(fullDBFileName);

        // copy file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * Close db connection
     */
    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void writeFileSD()  throws IOException {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        boolean res;
        res = sdPath.mkdirs();
        // добавляем свой каталог к пути
        String sdFullDBFileName = sdPath.getAbsolutePath() + "/" + DB_NAME;
        InputStream myInput = null;
        OutputStream myOutput = null;
//        this.getReadableDatabase();
        // get source and target streams
        myInput = new FileInputStream(fullDBFileName);
        myOutput = new FileOutputStream(sdFullDBFileName);

        // copy file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private String getSDcardPath()
    {
        String exts =  Environment.getExternalStorageDirectory().getPath();
        String sdCardPath = null;
        try
        {
            FileReader fr = new FileReader(new File("/proc/mounts"));
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine())!=null)
            {
                if(line.contains("secure") || line.contains("asec"))
                    continue;
                if(line.contains("fat"))
                {
                    String[] pars = line.split("\\s");
                    if(pars.length<2)
                        continue;
                    if(pars[1].equals(exts))
                        continue;
                    sdCardPath =pars[1];
                    break;
                }
            }
            fr.close();
            br.close();
            return sdCardPath;
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            Log.v(LOG_TAG, e.toString());
        }
        return sdCardPath;
    }
}
