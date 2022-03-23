package com.futureproducts.webview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class splashscreen extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    DatabaseReference uref;
    private FirebaseAuth mAuth;
// Put below line in onCreate method


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        mAuth = FirebaseAuth.getInstance();

        uref = db.child("version");
        uref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    double b = Double.parseDouble(snapshot.child("name").getValue(String.class));
                    int versionCode = BuildConfig.VERSION_CODE;
                    String versionName = BuildConfig.VERSION_NAME;
                    double a = Double.parseDouble(versionName);
                    if(a < b) {
                        Log.d("TAG", "checkversion" + versionName);
                    }else{
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                FirebaseMessaging.getInstance().subscribeToTopic("global")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                Intent go = new Intent(splashscreen.this, lunchactivity.class);
                                startActivity(go);
                                finish();
                            }
                        },3000);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }
}