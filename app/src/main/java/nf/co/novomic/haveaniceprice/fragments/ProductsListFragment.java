package nf.co.novomic.haveaniceprice.fragments;

import android.app.ListFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;

import nf.co.novomic.haveaniceprice.R;
import nf.co.novomic.haveaniceprice.activity.ShopsActivity;
import nf.co.novomic.haveaniceprice.classes.Product;
import nf.co.novomic.haveaniceprice.classes.Shop;
import nf.co.novomic.haveaniceprice.db.ProductsDAO;

/**
 * Fragment for Products List
 *
 * Created by Mikhail on 16.05.2016.
 */
public class ProductsListFragment extends ListFragment {

    private ArrayList<Product> mProducts;
    private ProductAdapter mProductAdapter;

    /**
     * Create new instance of the fragment with defined extra
     *
     * @return built fragment
     */
    public static ProductsListFragment newInstance() {

        return new ProductsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // will retain instance after rotation
        setRetainInstance(true);

        Bundle bundle = getArguments();
        Shop shop = bundle.getParcelable(ShopsActivity.EXTRA_SHOP);
        // initialize variables
        mProducts = new ArrayList<>();
        mProductAdapter = null;

        // start async task to get shops and set adapter
        DownloadProductsTask downloadTask = new DownloadProductsTask();
        assert shop != null;
        downloadTask.execute(shop.getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // create new Shops activity
//        Intent shopActivity = new Intent(v.getContext(), ShopActivity.class);
//        Intent productActivity = new Intent(v.getContext(), ProductsActivity.class);
//        int shopId = (int) mShopAdapter.getItem(position).getId();
//        Product Product = mProducts.get((int) id);
//        productActivity.putExtra(ProductsActivity.EXTRA_SHOP, Product);
//        v.getContext().startActivity(productActivity);
    }

    /**
     * Async loader of shops
     * after execution sets adapter to the fragment
     */
    private class DownloadProductsTask extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... shopsId) {
            long shopId = shopsId[0];
            mProducts = ProductsDAO.getProductsByShop(getActivity(), shopId);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (mProductAdapter == null) {
                //if not defined add adapter with array list of shops
                mProductAdapter = new ProductAdapter(mProducts);
                setListAdapter(mProductAdapter);
            } else {
                //update data
                mProductAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Array adapter to show list of shops
     */
    private class ProductAdapter extends ArrayAdapter<Product> {

        /**
         * @param products - The Object for the adapter
         */
        public ProductAdapter(ArrayList<Product> products) {
            super(getActivity(), R.layout.products_list_item, products);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // If we get view, then fill it
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.products_list_item, parent, false);
            }
            // Set view for Product object
            Product product = getItem(position);
            String productTitle = product.getTitle();
            TextView txtView, txtViewTitle, txtView1, txtViewTitle1;
            txtView = (TextView) convertView.findViewById(R.id.txtTitle);
            txtView.setText(productTitle);
            // show The Image in a ImageView
            new DownloadImageTask((ImageView) convertView.findViewById(R.id.iv_product))
                    .execute(product.getImgUrl());
            NumberFormat formatVal = NumberFormat.getCurrencyInstance();
            NumberFormat formatProc = NumberFormat.getPercentInstance();
            txtView = (TextView) convertView.findViewById(R.id.txtPriceNow);
            txtViewTitle = (TextView) convertView.findViewById(R.id.txtPriceNowTitle);
            txtView1 = (TextView) convertView.findViewById(R.id.txtDiscount);
            txtViewTitle1 = (TextView) convertView.findViewById(R.id.txtDiscountTitle);
            txtView.setText(formatVal.format(product.getPrice()));
            if(product.getPrice()<product.getStdPrice()){
                int red = Color.parseColor("#FF0000");
                txtView.setTextColor(Color.RED);
                txtViewTitle.setTextColor(Color.RED);
                txtView1.setTextColor(Color.RED);
                txtViewTitle1.setTextColor(Color.RED);
                txtView1.setText(formatProc.format((product.getStdPrice() - product.getPrice())/product.getStdPrice()));
                txtViewTitle1.setText(product.getSpecial());
            } else {
                int black = Color.parseColor("#FFFFFF");
                txtView.setTextColor(Color.BLACK);
                txtViewTitle.setTextColor(Color.BLACK);
                txtView1.setTextColor(Color.BLACK);
                txtViewTitle1.setTextColor(Color.BLACK);
                txtView1.setText("0.0%");
                txtViewTitle1.setText(R.string.discount);
            }
            txtView = (TextView) convertView.findViewById(R.id.txtPriceStd);
            txtView.setText(formatVal.format(product.getStdPrice()));
            txtView = (TextView) convertView.findViewById(R.id.txtPriceMin);
            txtView.setText(formatVal.format(product.getMinPrice()));
            txtView = (TextView) convertView.findViewById(R.id.txtPriceMax);
            txtView.setText(formatVal.format(product.getMaxPrice()));
//            drawTitle = drawTitle.replaceAll(" ", "");
//            int resID = getResources().getIdentifier(drawTitle, "drawable", getActivity().getPackageName());
//            if (resID > 0) {
//                //add image if found
//                ImageView itemImage = (ImageView) convertView.findViewById(R.id.iv_pic);
//                itemImage.setImageResource(resID);
//            }

            return convertView;
        }

        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }
    }
}
