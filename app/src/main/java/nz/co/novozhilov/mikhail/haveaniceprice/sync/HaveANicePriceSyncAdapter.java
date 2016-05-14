package nz.co.novozhilov.mikhail.haveaniceprice.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import nz.co.novozhilov.mikhail.haveaniceprice.Product;
import nz.co.novozhilov.mikhail.haveaniceprice.R;
import nz.co.novozhilov.mikhail.haveaniceprice.Shop;
import nz.co.novozhilov.mikhail.haveaniceprice.Statistics;
import nz.co.novozhilov.mikhail.haveaniceprice.Utility;
import nz.co.novozhilov.mikhail.haveaniceprice.db.ProductsDAO;
import nz.co.novozhilov.mikhail.haveaniceprice.db.ShopsDAO;
import nz.co.novozhilov.mikhail.haveaniceprice.db.StatisticsDAO;

public class HaveANicePriceSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String LOG_TAG = HaveANicePriceSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 5;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    private static HashMap<Long, Shop> shops;

    private static ArrayList<Product> products;

    private static ArrayList<Statistics> statisticss;
    private static Statistics statistics;

    public HaveANicePriceSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");

        products = ProductsDAO.getTrackedProducts(getContext());
        shops = ShopsDAO.getShopsHashMap(getContext());


        for (Product product : products) {
            String[] list = new String[8];
            list[0] = product.getUrl();
            Utility.getParseParansList(list, shops.get(product.getShopId()));
            parseProductPage(getContext(), product, list);
        }
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime, Account newAccount) {
        Log.v(LOG_TAG, "syncImmediately");
//        Account account = newAccount;
//                getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // we can enable inexact timers in our periodic sync
//            SyncRequest request = new SyncRequest.Builder().
//                    syncPeriodic(syncInterval, flexTime).
//                    setSyncAdapter(account, authority).
//                    setExtras(new Bundle()).build();
//            ContentResolver.requestSync(request);
//        } else {
        ContentResolver.addPeriodicSync(newAccount, authority, new Bundle(), syncInterval);
//        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context, Account newAccount) {
        Log.v(LOG_TAG, "syncImmediately(Context context) {");

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(newAccount,
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        Log.v(LOG_TAG, "getSyncAccount");
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

        }
        onAccountCreated(newAccount, context);
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        HaveANicePriceSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME, newAccount);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context, newAccount);
    }

    public static void initializeSyncAdapter(Context context) {
        Log.v(LOG_TAG, "initializeSyncAdapter");
        getSyncAccount(context);
    }

    private static ArrayList<Product> getProducts() {

        return new ArrayList<>();
    }

    private static Shop getShop(long shopId) {

        return new Shop(0, "", "");

    }

    /**
     * AsyncTask to parse the web pages
     */
    private static void parseProductPage(Context context, Product product,String... params) {


        Log.v(LOG_TAG, String.valueOf(product.getShopId()));
        Document document;
        statistics = new Statistics();
        statisticss = new ArrayList<>();
        Shop shop = shops.get(product.getShopId());
        try {
            String param = params[0];
            document = Jsoup.connect(param).get();
            for (int i = 1; i < params.length; i++) {
                param = params[i];
                String value;
                if (!param.equals("title")) {
                    String[] paramss = param.split("###");
                    Elements elements = document.select("." + paramss[0]);
                    for (Element element : elements) {

                        if (paramss.length == 1) {
                            value = element.text();
                            if (!value.isEmpty()) {
                                if (param.equals(shop.getSpecial())) {
                                    statistics.setSpecial(value);
                                }

                                if (param.equals(shop.getStdPrice())) {
                                    value = Utility.onlyNumbers(value);
                                    statistics.setStdPrice(Double.parseDouble(value));
                                    statistics.setPrice(Double.parseDouble(value));
                                }

                                if (param.equals(shop.getDiscPrice())) {
                                    value = Utility.onlyNumbers(value);
                                    statistics.setDiscPrice(Double.parseDouble(value));
                                    statistics.setPrice(Double.parseDouble(value));
                                }

                                if (param.equals(shop.getOldPrice())) {
                                    value = Utility.onlyNumbers(value);
                                    statistics.setOldPrice(Double.parseDouble(value));
                                }

                                if (param.equals(shop.getSavePrice())) {
                                    value = Utility.onlyNumbers(value);
                                    statistics.setSavePrice(Double.parseDouble(value));
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        statistics.setShopTitle(shop.getTitle());
        statistics.setProductTitle(product.getTitle());
        statistics.setProductId(product.getId());
        statistics.setDateTime(System.currentTimeMillis());
//        statistics.setDateTime(System.currentTimeMillis());
        //statisticss.add(statistics);
        statistics.print();
        StatisticsDAO.addStatistics(context, statistics);
    }
}