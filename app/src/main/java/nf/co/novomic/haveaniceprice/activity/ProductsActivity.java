package nf.co.novomic.haveaniceprice.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import nf.co.novomic.haveaniceprice.R;
import nf.co.novomic.haveaniceprice.classes.Shop;
import nf.co.novomic.haveaniceprice.fragments.ProductsListFragment;

/**
 * Activity for Products List
 *
 * Created by Mikhail on 16.05.2016.
 */
public class ProductsActivity  extends AppCompatActivity {
    public static final String EXTRA_SHOP = "SHOP";
    private static Shop shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle extras = this.getIntent().getExtras();
        shop = extras.getParcelable(EXTRA_SHOP);
        setContentView(R.layout.activity_shops);
        setTitle(shop.getTitle()+ " - Products List");

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

            fragment = new ProductsListFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable(ShopsActivity.EXTRA_SHOP, shop);

            fragment.setArguments(bundle);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.products_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Intent nextActivity = new Intent(this, ShopActivity.class);
            nextActivity.putExtra(ShopsActivity.EXTRA_SHOP, shop);
            startActivity(nextActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
