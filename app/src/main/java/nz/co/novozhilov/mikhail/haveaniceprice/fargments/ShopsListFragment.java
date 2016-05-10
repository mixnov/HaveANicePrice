package nz.co.novozhilov.mikhail.haveaniceprice.fargments;


import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import nz.co.novozhilov.mikhail.haveaniceprice.R;
import nz.co.novozhilov.mikhail.haveaniceprice.Shop;
import nz.co.novozhilov.mikhail.haveaniceprice.ShopActivity;
import nz.co.novozhilov.mikhail.haveaniceprice.ShopsActivity;
import nz.co.novozhilov.mikhail.haveaniceprice.db.ShopsDAO;


/**
 * Fragment with a list of category
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public final class ShopsListFragment extends ListFragment{

    private ArrayList<Shop> mShops;
    private ShopAdapter mShopAdapter;

    /**
     * Create new instance of the fragment with defined extra
     *
     * @return built fragment
     */
    public static ShopsListFragment newInstance() {
        Bundle args = new Bundle();

        return new ShopsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // will retain instance after rotation
        setRetainInstance(true);

        // initialize variables
        mShops = new ArrayList<>();
        mShopAdapter = null;
        // start async task to get shops and set adapter
        DownloadShopsTask downloadTask = new DownloadShopsTask();
        downloadTask.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // create new question activity with selected category
        Intent shopActivity = new Intent(v.getContext(), ShopActivity.class);
        int shopId = (int) mShopAdapter.getItem(position).getId();
        Shop shop = mShops.get(shopId-1);
        shopActivity.putExtra(ShopsActivity.EXTRA_SHOP, shop);
        v.getContext().startActivity(shopActivity);
    }

    /**
     * Async loader of shops
     * after execution sets adapter to the fragment
     */
    private class DownloadShopsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            mShops = ShopsDAO.getShopsList(getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (mShopAdapter == null) {
                //if not defined add adapter with array list of shops
                mShopAdapter = new ShopAdapter(mShops);
                setListAdapter(mShopAdapter);
            } else {
                //update data
                mShopAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Array adapter to show list of shops
     */
    private class ShopAdapter extends ArrayAdapter<Shop> {

        public ShopAdapter(ArrayList<Shop> shops) {
            super(getActivity(), R.layout.menu_list_item, shops);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // If we get view, then fill it
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.menu_list_item,parent,false);
            }
            // Set view for category (Shop object)
            Shop c = getItem(position);
//            TextView titleTextView =
//                    (TextView)convertView.findViewById(R.id.shops_list_item_title);
//            titleTextView.setText(c.getTitle());
            String drawTitle = c.getTitle().toLowerCase();
            drawTitle = drawTitle.replaceAll(" ", "");
            int resID = getResources().getIdentifier(drawTitle, "drawable", getActivity().getPackageName());
            if (resID > 0) {
                //add image if found
                ImageView itemImage = (ImageView) convertView.findViewById(R.id.iv_pic);
                itemImage.setImageResource(resID);
            }

            return convertView;
        }

    }
}
