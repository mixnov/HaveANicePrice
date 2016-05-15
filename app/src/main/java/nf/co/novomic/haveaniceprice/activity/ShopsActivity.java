package nf.co.novomic.haveaniceprice.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nf.co.novomic.haveaniceprice.R;
import nf.co.novomic.haveaniceprice.fragments.ShopsListFragment;

/**
 * Activity with list of shops
 * Created by Mikhail on 29.04.2016.
 */
public class ShopsActivity  extends AppCompatActivity {
    public static final String EXTRA_SHOP = "SHOP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Intent alarmIntent = new Intent(this, HaveANicePriceService.AlarmReceiver.class);
//
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, pi);
//         alarmIntent


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);
        setTitle("S H O P S");

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.mmf);

        // add up navigation between fragments
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                placeUpButton();
            }
        });

        if (fragment == null) {
            fragment = new ShopsListFragment();
            fm.beginTransaction()
                    .add(R.id.mmf, fragment)
                    .commit();
        } else {
            //re-create navigation button for fragments after rotation
            placeUpButton();
        }

    }

    /**
     * Find action bar in a fragment and add up navigation
     * if fragment is not first oin stack
     */
    void placeUpButton(){
        int stackHeight = getFragmentManager().getBackStackEntryCount();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            if (stackHeight > 0) {
                // show navigation button
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            } else {
                // hide navigation button
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeButtonEnabled(false);
            }
        }
    }

    /**
     * to navigate from contact details fragment back to list fragment
     */
    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
