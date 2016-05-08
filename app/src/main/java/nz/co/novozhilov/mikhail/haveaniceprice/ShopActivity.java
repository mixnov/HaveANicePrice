package nz.co.novozhilov.mikhail.haveaniceprice;

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
import java.util.ArrayList;

import nz.co.novozhilov.mikhail.haveaniceprice.db.ProductsDAO;

/**
 * Created by Mikhail on 29.04.2016.
 */
public class ShopActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShopActivity.class.getSimpleName();
    public static final String EXTRA_SHOP = "SHOP";
    private static String url;
    private static Shop shop;
    private WebView webView;
    private TextView linkTxt, title, price, stdPrice, discount;
    private EditText linkEdt;
    private Button add;
    private Product product;
    private Statistics statistics;

    public Elements elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        linkTxt = (TextView) findViewById(R.id.txtLink);
//        title = (TextView) findViewById(R.id.txtTitle);
        price = (TextView) findViewById(R.id.txtPrice);
        stdPrice = (TextView)  findViewById(R.id.txtStdPrice);
        discount = (TextView)  findViewById(R.id.txtVDiscount);
        linkEdt = (EditText) findViewById(R.id.edtLink);

        add = (Button) findViewById(R.id.btnAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long idP = ProductsDAO.addProduct(ShopActivity.this, product);
                if(idP > 0){
//                    long ids = StatisticsDAO.addStatistics(ShopActivity.this, Statistics);
                    Snackbar.make(v, "'" + product.getTitle() + "' was added to DB successfully...",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
            }
        });

        Bundle extras = this.getIntent().getExtras();
        shop = extras.getParcelable(EXTRA_SHOP);

        init();

        webView = (WebView) findViewById(R.id.webView2);
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(new myWebViewClient());
        url = shop.getUrl();
        webView.loadUrl("http://www.farmers.co.nz/beauty/perfume/women-s-perfumes/paco-rabanne-black-xs-for-her-edt-80ml-5036494003");
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(butCalculate.getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void setUrl(String url){
        ShopActivity.url = url;
    }

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
            ArrayList<String> list = new ArrayList<>();
            list.add(url);
            list = Utility.getParseParansList(list, shop);
            parse.execute(list);
//            HashMap<String, String> result = new HashMap<>();


        }
    }

    class Parse extends AsyncTask<ArrayList<String>, Void, Void> {


        @Override
        protected Void doInBackground(ArrayList<String>... params) {

            product = new Product();
            statistics = new Statistics();

//            HashMap hm = new HashMap();
            Document document;
            try {

                String param = params[0].get(0);
                document = Jsoup.connect(param).get();
                for (int i = 1; i < params[0].size(); i++) {
                    param = params[0].get(i);
                    String value;
                    if (param.equals("title")) {
                        value = document.title();
                        if(value.contains(" - ")) value = value.substring(0, value.indexOf(" - "));
//                        hm.put(param, value);
                        product.setTitle(value);
//                    } else
//                        elements = document.select(param);
//                        String val = "";
//                        for (Element element : elements) {
//                            val = element.text();
//                        }
//                        if (val.isEmpty()) break;
                    } else {
                        value = "";
                        String[] paramss = param.split("###");
                        elements = document.select("." + paramss[0]);
                        for (Element element : elements) {

                            if (paramss.length > 1) {
                                for (Node childNode : element.childNodes()) {

                                    value = childNode.absUrl(paramss[1]);
                                    if (!value.isEmpty()) {
                                        product.setImgUrl(value);
//                                        hm.put(param, value);
                                    }
                                    value = childNode.absUrl(paramss[2]);
                                    if (!value.isEmpty()) {
                                        product.setTitle(value);
//                                        hm.put(param, value);
                                    }
                                }
                            } else {
                                value = element.text();
                                if (!value.isEmpty()) {
                                    if (param.equals(shop.getSpecial())) {
                                        statistics.setSpecial(value);
                                    }

                                    if (param.equals(shop.getStdPrice())) {
                                        value = Utility.onlyNumbers(value);
                                        statistics.setStdPrice(Double.parseDouble(value));
                                    }

                                    if (param.equals(shop.getDiscPrice())) {
                                        value = Utility.onlyNumbers(value);
                                        statistics.setDiscPrice(Double.parseDouble(value));
                                    }

                                    if (param.equals(shop.getOldPrice())) {
                                        value = Utility.onlyNumbers(value);
                                        statistics.setOldPrice(Double.parseDouble(value));
                                    }

                                    if (param.equals(shop.getSavePrice())) {
                                        value = Utility.onlyNumbers(value);
                                        statistics.setSavePrice(Double.parseDouble(value));
                                    }

//                                    hm.put(param, value);

                                }
                            }

                        }
                        if ((param.equals(shop.getImgUrl())) && (product.getImgUrl() == null || product.getImgUrl().isEmpty())) {
                            product = null;
//                            hm.clear();
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
                if(ProductsDAO.getProductsByUrl(ShopActivity.this, url) == 0) {
                    add.setEnabled(true);
                }
                setTitle(shop.getTitle() + " - " + product.getTitle());
//                title.setText(product.getTitle());
//                title.setVisibility(View.GONE);
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


            } else {
                add.setEnabled(false);
            }
        }
    }

    private void init(){
        setTitle(shop.getTitle());
        add.setEnabled(false);
//        title.setText("");
        price.setText("");
        stdPrice.setText("");
        discount.setText("");
        price.setTextColor(Color.parseColor("#000000"));
    }
}
