package nz.co.novozhilov.mikhail.haveaniceprice;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nz.co.novozhilov.mikhail.haveaniceprice.fargments.MainMenuFragment;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            fragment = new MainMenuFragment();
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

