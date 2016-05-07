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
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mikhail on 29.04.2016.
 */
public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = ShopActivity.class.getSimpleName();
    public static final String EXTRA_SHOP = "SHOP";
    private String url;
    private Shop shop;
    private WebView webView;
    private TextView link, title, price;
    private Button add;
    private Product product;
    private Statistics statistics;

    public Elements elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        link = (TextView) findViewById(R.id.txtLink);
        add = (Button) findViewById(R.id.btnAdd);
        add.setOnClickListener(this);
        title = (TextView) findViewById(R.id.txtTitle);
        add.setEnabled(false);
        price = (TextView) findViewById(R.id.txtPrice);

        Bundle extras = this.getIntent().getExtras();
        shop = extras.getParcelable(EXTRA_SHOP);
        setTitle(shop.getTitle());

        webView = (WebView) findViewById(R.id.webView2);
        webView.clearCache(true);
        webView.clearHistory();
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

    @Override
    public void onClick(View v) {
//        ProductsDAO.addProduct();
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
            Snackbar.make(view, url, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            link.setText(url);
            //link.setText();
            Parse parse = new Parse();
            ArrayList<String> list = new ArrayList<>();
            list.add(url);
            list = Utility.getParseParansList(list, shop);
            parse.execute(list);
            HashMap<String, String> result = new HashMap<>();
            try {
                result = parse.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (!(product == null)) {
                price.setTextColor(Color.parseColor("#000000"));
                add.setEnabled(true);
                title.setText(product.getTitle());
                product.setUrl(url);
                if(statistics.getStdPrice() > 0) {
                    price.setText(String.valueOf(statistics.getStdPrice()));
                } else if(statistics.getDiscPrice() > 0) {
                    NumberFormat format = NumberFormat.getCurrencyInstance();
                    price.setText(String.valueOf(format.format(statistics.getDiscPrice())));
                    price.setTextColor(Color.parseColor("#FF0000"));
                }


            } else {
                add.setEnabled(false);
            }

        }
    }

    class Parse extends AsyncTask<ArrayList<String>, Void, HashMap<String, String>> {


        @Override
        protected HashMap<String, String> doInBackground(ArrayList<String>... params) {

            product = new Product();
            statistics = new Statistics();

            HashMap hm = new HashMap();
            Document document;
            try {

                String param = params[0].get(0);
                document = Jsoup.connect(param).get();
                for (int i = 1; i < params[0].size(); i++) {
                    param = params[0].get(i);
                    String value;
                    if (param.equals("title")) {
                        value = document.title();
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
                        elements = document.select("."+paramss[0]);
                        for (Element element : elements) {

                            if (paramss.length > 1) {
                                for (Node childNode : element.childNodes()) {

                                    value = childNode.absUrl(paramss[1]);
                                    if (!value.isEmpty()) {
                                        product.setImgUrl(value);
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
                                        statistics.setStdPrice(Double.parseDouble(value));                                    }

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
                        if ((param.equals("." + shop.getImgUrl())) && (hm.size() < 2)) {
                            product = null;
//                            hm.clear();
                            return hm;

                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return hm;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> stringStringHashMap) {

            super.onPostExecute(stringStringHashMap);
        }
    }
}
