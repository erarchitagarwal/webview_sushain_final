package com.futureproducts.webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.androidbrowserhelper.trusted.QualityEnforcer;
import com.google.androidbrowserhelper.trusted.TwaLauncher;

import java.util.ArrayList;
import java.util.List;

public class lunchactivity extends AppCompatActivity {

    Dialog dialog;
    private static final Uri LAUNCH_URI =
            Uri.parse("https://pallavifoods.com/sushainproduction/signin");

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
                Toast.makeText(lunchactivity.this,
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
        setContentView(R.layout.activity_lunchactivity);


        View view = getLayoutInflater().inflate(R.layout.customdialog, null);

        dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(view);
        dialog.show();
        TwaLauncher launcher = new TwaLauncher(lunchactivity.this);
        launcher.launch(LAUNCH_URI);
        launchers.add(launcher);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}