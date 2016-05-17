package nf.co.novomic.haveaniceprice.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;

import nf.co.novomic.haveaniceprice.classes.Product;
import nf.co.novomic.haveaniceprice.R;
import nf.co.novomic.haveaniceprice.classes.Shop;
import nf.co.novomic.haveaniceprice.classes.Statistics;
import nf.co.novomic.haveaniceprice.classes.Utility;
import nf.co.novomic.haveaniceprice.db.ProductsDAO;
import nf.co.novomic.haveaniceprice.db.StatisticsDAO;

/**
 * Created by Mikhail on 29.04.2016.
 * <p/>
 * Activity of a shop
 */
@SuppressLint("SetJavaScriptEnabled")
public class ShopActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShopActivity.class.getSimpleName();
    private static final String EXTRA_SHOP = "SHOP";
    private static String url;
    private static Shop shop;
    private WebView webView;
    private TextView linkTxt, price, stdPrice, discount;
    private EditText linkEdt;
    private Button add;
    private Product product;
    private Statistics statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        linkTxt = (TextView) findViewById(R.id.txtLink);
        price = (TextView) findViewById(R.id.txtPrice);
        stdPrice = (TextView) findViewById(R.id.txtStdPrice);
        discount = (TextView) findViewById(R.id.txtVDiscount);
        linkEdt = (EditText) findViewById(R.id.edtLink);

        add = (Button) findViewById(R.id.btnAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add.getText().equals("+")) {
                    if (product.isEmpty()) return;
                    long idP = ProductsDAO.addProduct(ShopActivity.this, product);
                    if (idP > 0) {
                        statistics.setProductId(idP);
                        long idS = StatisticsDAO.addStatistics(ShopActivity.this, statistics);
                        if (idS > 0) {
                            Snackbar.make(v, "'" + product.getTitle() + "' was added to DB successfully...",
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            add.setText("-");
                        }
                    }
                } else {
                    Snackbar.make(v, "Sorry! You could not delete the product in this version.",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
            }
        });

        Bundle extras = this.getIntent().getExtras();
        shop = extras.getParcelable(EXTRA_SHOP);

        init();

        webView = (WebView) findViewById(R.id.webView2);

        assert webView != null;
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(new myWebViewClient());
        url = shop.getUrl();
        webView.loadUrl("http://www.farmers.co.nz/beauty/perfume/women-s-perfumes/paco-rabanne-black-xs-for-her-edt-80ml-5036494003");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void setUrl(String url) {
        ShopActivity.url = url;
    }

    /**
     * The class for WebViewClient for WebView
     */
    private class myWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("mailto:") || url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(intent);
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            ShopActivity.setUrl(url);
            init();
            Snackbar.make(view, url, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            linkTxt.setText(url);
            linkEdt.setVisibility(View.GONE);
            linkEdt.setText(url);
            //link.setText();
            Parse parse = new Parse();
            String[] list = new String[8];
            list[0] = url;
            Utility.getParseParansList(list, shop);
            parse.execute(list);


        }
    }

    /**
     * AsyncTask to parse the web pages
     */
    class Parse extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            product = new Product();
            statistics = new Statistics();
            product.setMinPrice(1000);
            product.setMaxPrice(0);

            Document document;
            try {
                double valueDouble;
                String param = params[0];
                document = Jsoup.connect(param).get();
                for (int i = 1; i < params.length; i++) {
                    param = params[i];
                    String value;
                    if (param.equals("title")) {
                        value = document.title();
                        if (value.contains(" - ")) value = value.substring(0, value.indexOf(" - "));
                        product.setTitle(value);
                    } else {
                        String[] paramss = param.split("###");
                        Elements elements = document.select("." + paramss[0]);
                        for (Element element : elements) {

                            if (paramss.length > 1) {
                                for (Node childNode : element.childNodes()) {

                                    value = childNode.absUrl(paramss[1]);
                                    if (!value.isEmpty()) {
                                        product.setImgUrl(value);
                                    }
                                    value = childNode.absUrl(paramss[2]);
                                    if (!value.isEmpty()) {
                                        product.setTitle(value);
                                    }
                                }
                            } else {
                                value = element.text();
                                if (!value.isEmpty()) {
                                    if (param.equals(shop.getSpecial())) {
                                        statistics.setSpecial(value);
                                        product.setSpecial(value);
                                    }

                                    if (param.equals(shop.getStdPrice())) {
                                        valueDouble = Double.parseDouble(Utility.onlyNumbers(value));
                                        statistics.setStdPrice(valueDouble);
                                        statistics.setPrice(valueDouble);
                                        product.setStdPrice(valueDouble);
                                        product.setPrice(valueDouble);
                                        setMinMaxPrice(product, valueDouble);
                                    }

                                    if (param.equals(shop.getDiscPrice())) {
                                        valueDouble = Double.parseDouble(Utility.onlyNumbers(value));
                                        statistics.setDiscPrice(valueDouble);
                                        statistics.setPrice(valueDouble);
                                        product.setDiscPrice(valueDouble);
                                        product.setPrice(valueDouble);
                                        setMinMaxPrice(product, valueDouble);
                                    }

                                    if (param.equals(shop.getOldPrice())) {
                                        valueDouble = Double.parseDouble(Utility.onlyNumbers(value));
                                        statistics.setOldPrice(valueDouble);
                                        product.setOldPrice(valueDouble);
                                        product.setStdPrice(valueDouble);
                                        setMinMaxPrice(product, valueDouble);
                                    }

                                    if (param.equals(shop.getSavePrice())) {
                                        value = Utility.onlyNumbers(value);
                                        statistics.setSavePrice(Double.parseDouble(value));
                                    }
                                }
                            }
                        }
                        if ((param.equals(shop.getImgUrl())) && (product.getImgUrl() == null || product.getImgUrl().isEmpty())) {
                            product = null;
                            return null;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            price.setTextColor(Color.parseColor("#000000"));
            if (!(product == null)) {
                add.setEnabled(true);
                if (ProductsDAO.getProductsByUrl(ShopActivity.this, url) == 0) add.setText("+");
                else add.setText("-");

                setTitle(shop.getTitle() + " - " + product.getTitle());
                product.setUrl(url);
                product.setShopId(ShopActivity.shop.getId());
                NumberFormat format = NumberFormat.getCurrencyInstance();
                if (statistics.getStdPrice() > 0) {
                    price.setText(format.format(statistics.getStdPrice()));
                } else if (statistics.getDiscPrice() > 0) {
                    price.setText(format.format(statistics.getDiscPrice()));
                    stdPrice.setText(format.format(statistics.getOldPrice()));
                    format = NumberFormat.getPercentInstance();
                    Double disc = (statistics.getOldPrice() - statistics.getDiscPrice()) / statistics.getOldPrice();
                    discount.setText(format.format(disc));
                    price.setTextColor(Color.parseColor("#FF0000"));
                }
                product.setTrack(1);
            } else {
                add.setText("+");
                add.setEnabled(false);
            }
        }

        protected void setMinMaxPrice(Product product, double valueDouble){
            if (valueDouble < product.getMinPrice()) product.setMinPrice(valueDouble);
            if (valueDouble > product.getMaxPrice()) product.setMaxPrice(valueDouble);
        }
    }

    /**
     * Method to initialise some variables
     */
    private void init() {
        setTitle(shop.getTitle());
        add.setText("+");
        price.setText("");
        stdPrice.setText("");
        discount.setText("");
        price.setTextColor(Color.parseColor("#000000"));
    }
}
