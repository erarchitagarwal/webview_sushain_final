package com.futureproducts.webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.androidbrowserhelper.trusted.QualityEnforcer;
import com.google.androidbrowserhelper.trusted.TwaLauncher;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Dialog dialog;
    String fcm;
    private static final Uri LAUNCH_URI =
            Uri.parse("http://pallavifoods.com/sushainproduction/signin");

    private final TrustedWebActivityIntentBuilder builder = new TrustedWebActivityIntentBuilder(
            LAUNCH_URI);

    private CustomTabsCallback mCustomTabsCallback = new QualityEnforcer();

    private List<TwaLauncher> launchers = new ArrayList<>();

    private final CustomTabsServiceConnection customTabsServiceConnection = new CustomTabsServiceConnection() {
        CustomTabsSession mSession;
        private final static int SESSION_ID = 45;  // An arbitrary constant.

        @Override
        public void onCustomTabsServiceConnected(ComponentName name,
                                                 CustomTabsClient client) {
            mSession = client.newSession(null, SESSION_ID);

            if (mSession == null) {
                Toast.makeText(MainActivity.this,
                        "Couldn't get session from provider.", Toast.LENGTH_LONG).show();
            }

            Intent intent = builder.build(mSession).getIntent();
            intent.putExtra(Intent.EXTRA_REFERRER,
                    Uri.parse("android-app://com.google.androidbrowserhelper?twa=true"));
            startActivity(intent);
            finish();
            dialog.dismiss();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSession = null;
        }
    };

    private boolean serviceBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                fcm = s;
            }
        });

        View view = getLayoutInflater().inflate(R.layout.customdialog, null);

        dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(view);
//        dialog.setTitle("loading please wait");
        WebView browser = findViewById(R.id.webview);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if(url.contains("my-virtual-upcoming-appointment")){
                    Log.d("TAG", "checkurl");

                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            dialog.dismiss();
                        }
                    }, 3000);
                    browser.stopLoading();
                    Intent go = new Intent(MainActivity.this, lunchactivity.class);
                    startActivity(go);
                    finish();



                }else{
                    dialog.dismiss();
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String test="hi";  //Replace here the text from EditText

                view.loadUrl("javascript: (function(){document.getElementById('fcm_token').value ='" + fcm + "';})();");
            }
        });
//        browser.loadUrl("file:///android_asset/index.html");
        browser.loadUrl("http://pallavifoods.com/sushainproduction/signin");
//        WebViewClient webViewClient = new WebViewClient(){
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                String cookies = CookieManager.getInstance().getCookie(view.getUrl());
//                Log.d("Tag", "checkcoo"+cookies);
//                // save cookies or call new fun to handle actions
//                //  newCookies(cookies);
//            }
//        };
//        webView.setWebViewClient(webViewClient); 9039730708
    }

}