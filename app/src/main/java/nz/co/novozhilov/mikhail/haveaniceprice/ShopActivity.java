package nz.co.novozhilov.mikhail.haveaniceprice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mikhail on 29.04.2016.
 */
public class ShopActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShopActivity.class.getSimpleName();

    public static final String EXTRA_SHOP = "SHOP";
    private Shop shop;
    private WebView webView;
    private TextView link, title;
    private Button add;

    public Elements elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        link = (TextView) findViewById(R.id.txtLink);
        add = (Button) findViewById(R.id.btnAdd);
        title = (TextView) findViewById(R.id.txtTitle);
        add.setEnabled(false);
        Bundle extras = this.getIntent().getExtras();
        shop = extras.getParcelable(EXTRA_SHOP);
        setTitle(shop.getTitle());


//        webView.setWebChromeClient(new MyCustomChromeClient(this));
//        webView.setWebViewClient(new MyCustomWebViewClient(this));
        webView = (WebView) findViewById(R.id.webView2);
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportZoom(true);
        //webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new myWebViewClient());
        webView.loadUrl("http://www.farmers.co.nz/beauty/perfume/women-s-perfumes");


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class myWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
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
            Parse parse = new Parse();
            ArrayList<String> list = new ArrayList<>();
            list.add(url);
            list.add(".cms-productInfo");
            list.add("title");
            list.add(".std-price");
            list.add(".new-price");
            list.add(".old-price");
            list.add(".save-price");
            parse.execute(list);
            ArrayList<String> result = null;
            //ArrayList<String> nextResult = null;
            try {
                result = parse.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            };
            if (!result.get(0).equals("0")) {
                String res = "";
                for(int i=0; i<result.size(); i++)
                    res += result.get(i);
                if (res.contains(".cms-productInfo")) {
                    add.setEnabled(true);
                }
                else add.setEnabled(false);
                title.setText(res);

            } else {
                add.setEnabled(false);
            }

        }
    }

    class Parse extends AsyncTask<ArrayList<String>, Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(ArrayList<String>... params) {

            ArrayList<String> list = new ArrayList<>();
            Document document;
            try {
                String param = params[0].get(0);
                document = Jsoup.connect(param).get();
                for(int i=1; i < params[0].size(); i++){
                    param = params[0].get(i);
                    if (param.equals("title")) {
                        list.add("title#" + document.title());
                    } else {
                        elements = document.select(param);
                        String val = "";
                        for(Element element : elements) {
                            val = element.text();
                            Log.v(LOG_TAG, val);
                        }
                        if (!val.isEmpty()) {
                            list.add(param + "#");
                            if (!param.equals(".cms-productInfo")){
                                list.add(val);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (list.size()==0){
                list.add("0");
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }
}
