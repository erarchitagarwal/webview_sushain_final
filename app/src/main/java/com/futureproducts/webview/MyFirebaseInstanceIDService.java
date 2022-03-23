package com.futureproducts.webview;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        FirebaseMessaging.getInstance().subscribeToTopic("global")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        showNotification(
//                                remoteMessage.getNotification().getTitle(),
//                                remoteMessage.getNotification().getBody());
//                    }
//                });

        Log.d("TAG", "checkmessage"+remoteMessage.getData().size());
//        if (remoteMessage.getNotification() != null) {
            Log.d("TAG", "checkmessage");
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e("TAG", "Exception: " + e.getMessage());
            }

            if (remoteMessage.getData().size() > 0) {
                Log.e("TAG", "Data Payload: " + remoteMessage.getData().toString());



            }
//            Toast.makeText(this, "checkmessage", Toast.LENGTH_SHORT).show();
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
//            showNotification(
//                    remoteMessage.getNotification().getTitle(),
//                    remoteMessage.getNotification().getBody());
//        }
    }


        private void handleDataMessage(JSONObject json) {
            Log.e("tag", "push json: " + json.toString());
            try {
                JSONObject data = json.getJSONObject("data");
//                JSONObject payload =data.getJSONObject("Payload");
                Log.d("tag", "checkpay"+"payload");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("tag", "checkerror"+e.getMessage());
            }

            try {
                JSONObject data = json.getJSONObject("data");

                String title = data.getString("title");
                String message = data.getString("message");
//                boolean isBackground = false;//data.getBoolean("is_background");
                String imageUrl = data.getString("image");
                String link = data.getString("url");
//                Bitmap bitmap = getBitmapfromUrl(imageUrl);
                String timestamp = "";//data.getString("timestamp");
//                JSONObject payload =data.getJSONObject("payload");

                Log.d("TAG", "title: " + "title");
                Log.e("TAG", "message: " + imageUrl);
//                Log.e("TAG", "isBackground: " + isBackground);
//                Log.e("TAG", "payload: " + payload.toString());
//                Log.e("TAG", "imageUrl: " + imageUrl);
//                Log.e("TAG", "timestamp: " + timestamp);
                showNotification(title, message, imageUrl, link);

//                if(title.matches("paid")){
//                    Log.d("tag", "checknotification"+message);
//                    FirebaseMessaging.getInstance().subscribeToTopic("paid")
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//
//                                }
//                            });
//
//                }else {
//                    showNotification(title, message, imageUrl, link);
//                }

//                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//                    // app is in foreground, broadcast the push message
//                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                    pushNotification.putExtra("message", message);
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//                    // play notification sound
//                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                    notificationUtils.playNotificationSound();
//                } else {
//                    // app is in background, show the notification in notification tray
//                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
//                    resultIntent.putExtra("message", message);
//
//                    // check for image attachment
//                    if (TextUtils.isEmpty(imageUrl)) {
//                        showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                    } else {
//                        // image is present, show notification with image
//                        showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                    }
//                }

            } catch (JSONException e) {
                Log.e("TAG", "Json Exception: " + e.getMessage());
            } catch (Exception e) {
                Log.e("TAG", "Exception: " + e.getMessage());
            }
        }


    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }


    private RemoteViews getCustomDesign(String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,
                R.drawable.snichlogo);
        return remoteViews;
    }

    public void showNotification(String title,
                                 String message, String imageUrl, String link) {


        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        if(imageUrl.matches("")) {

            // Pass the intent to switch to the MainActivity
            Intent intent
                    = new Intent(this, MainActivity.class);
            // Assign channel ID
            String channel_id = "notification_channel";
            // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
            // the activities present in the activity stack,
            // on the top of the Activity that is to be launched
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Pass the intent to PendingIntent to start the
            // next Activity
            PendingIntent pendingIntent
                    = PendingIntent.getActivity(
                    this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder builder
                    = new NotificationCompat
                    .Builder(getApplicationContext(),
                    channel_id)
                    .setSmallIcon(R.drawable.snichlogo)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000, 1000, 1000,
                            1000, 1000})
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pendingIntent);

            if (Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.JELLY_BEAN) {
                builder = builder.setContent(
                        getCustomDesign(title, message));
            } // If Android Version is lower than Jelly Beans,
            // customized layout cannot be used and thus the
            // layout is set as follows
            else {
                builder = builder.setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.drawable.snichlogo);
            }
            NotificationManager notificationManager
                    = (NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE);
            // Check if the Android Version is greater than Oreo
            if (Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel
                        = new NotificationChannel(
                        channel_id, "webview",
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(
                        notificationChannel);
            }

            notificationManager.notify(0, builder.build());
        }else{

            Log.d("tag", "withimage");
            Intent resultIntent = new Intent(Intent.ACTION_VIEW);
            resultIntent.setData(Uri.parse(link));
            PendingIntent pending = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
            // Create a notificationManager object
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
            // If android version is greater than 8.0 then create notification channel
            if (android.os.Build.VERSION.SDK_INT >=     android.os.Build.VERSION_CODES.O) {

                // Create a notification channel
                NotificationChannel notificationChannel = new NotificationChannel("1",    "1",     NotificationManager.IMPORTANCE_HIGH);
                // Set properties to notification channel
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300});

                // Pass the notificationChannel object to notificationManager
                notificationManager.createNotificationChannel(notificationChannel);
            }

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
// Set the notification parameters to the notification builder object
            builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.snichlogo)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true);
// Set the image for the notification
            if (imageUrl != null) {
                Bitmap bitmap = getBitmapfromUrl(imageUrl);
                builder.setStyle(
                        new NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmap)
                                .bigLargeIcon(null)
                ).setLargeIcon(bitmap);
            }

            notificationManager.notify(1, builder.build());
        }



    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            Log.e("awesome", "Error in getting notification image: " + e.getLocalizedMessage());
            return null;
        }
    }
}
