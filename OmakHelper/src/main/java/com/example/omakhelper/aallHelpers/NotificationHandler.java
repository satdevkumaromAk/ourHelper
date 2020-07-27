package com.example.omakhelper.aallHelpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import com.example.omakhelper.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmResults;

public class NotificationHandler extends FirebaseMessagingService {
    public static Bitmap largImage, bigImage, smallIconImage;
    public static String btnTitle = "";
    public static String type = "";
    public static String postId = "";
    public static String project_id = "";
    public static String EditEmail = "";
    public static String EditPhone = "";
    public static String EditFirstName = "";
    public static String EditLastName = "";
    public static String EditAddress = "";
    public static String lead_id = "";
    public static String last_mod_str_date = "";
    public static String last_calculated = "";
    RealmHelpers realmHelpers;
    Realm realm;
    int nextNotificatoinId;
    HashMap<String, String> notiData;
    String longImageUrl = "", smallImageUrl = "", smallIcon = "", notiTitle = "", notiMessage = "";
    PreferenceFile preferenceFile;
    private Context context;
    private NotificationChannelHelpers mNotificationUtils;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        preferenceFile = new PreferenceFile();
        type = getDataKey(remoteMessage, "type");
        notiTitle = getDataKey(remoteMessage, "title");
        notiMessage = getDataKey(remoteMessage, "message");
        btnTitle = getDataKey(remoteMessage, "btn_title");
        longImageUrl = getDataKey(remoteMessage, "img_big");
        smallImageUrl = getDataKey(remoteMessage, "img_icon");
        smallIcon = getDataKey(remoteMessage, "small_icon");
        project_id = getDataKey(remoteMessage, "project_id");
        lead_id = getDataKey(remoteMessage, "lead_id");
        postId = getDataKey(remoteMessage, "post_id");
        EditPhone = getDataKey(remoteMessage, "userphone");
        EditEmail = getDataKey(remoteMessage, "useremail");
        EditFirstName = getDataKey(remoteMessage, "first_name");
        EditLastName = getDataKey(remoteMessage, "last_name");
        EditAddress = getDataKey(remoteMessage, "address");
        last_calculated = getDataKey(remoteMessage, "last_calculated");
        last_mod_str_date = getDataKey(remoteMessage, "last_mod_str_date");

        // store notification into realm
        notiData = new HashMap<>();
        notiData.put("data", new Gson().toJson(remoteMessage.getData()));
        notiData.put("type", getDataKey(remoteMessage, "type"));
        notiData.put("title", getDataKey(remoteMessage, "title"));
        notiData.put("message", getDataKey(remoteMessage, "message"));
        notiData.put("btn_title", getDataKey(remoteMessage, "btn_title"));
        notiData.put("project_id", getDataKey(remoteMessage, "project_id"));
        notiData.put("longImageUrl", getDataKey(remoteMessage, "img_big"));
        notiData.put("smallImageUrl", getDataKey(remoteMessage, "img_icon"));
        notiData.put("smallIcon", getDataKey(remoteMessage, "small_icon"));
        notiData.put("show_notification", "");
        notiData.put("channel_id", "channel_id_general");

        if (!getDataKey(remoteMessage, "remoteShowNotification").equals("")) {
            notiData.put("show_notification", getDataKey(remoteMessage, "remoteShowNotification"));
        }
        if (!getDataKey(remoteMessage, "channel_id").equals("")) {
            notiData.put("channel_id", getDataKey(remoteMessage, "channel_id"));
        }


        showGeneralNotification(remoteMessage);
    }

    private String getDataKey(RemoteMessage remoteMessage, String key) {
        return (remoteMessage.getData().get(key) != null) ? remoteMessage.getData().get(key) : "";
    }

    private void showGeneralNotification(RemoteMessage remoteMessage) {
        realmHelpers = RealmHelpers.getInstance(this);
        realm = HelperFunctions.getRealm("messages", this);


        Intent intent = new Intent();
        AtomicReference<Boolean> isRealmUpdated = new AtomicReference<>(false);
        try {
            URL largeImage = new URL(longImageUrl);
            URL bigImages = new URL(smallImageUrl);
            URL smallUrlIcon = new URL(smallIcon);
            largImage = BitmapFactory.decodeStream(largeImage.openConnection().getInputStream());
            bigImage = BitmapFactory.decodeStream(bigImages.openConnection().getInputStream());
            smallIconImage = BitmapFactory.decodeStream(smallUrlIcon.openConnection().getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }

        String channelId = "normal";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationCompat =
                new NotificationCompat.Builder(this, channelId)
                        .setLargeIcon(largImage)
                        .setContentText(notiMessage)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setColor(Color.GREEN)
                        .setContentTitle( notiTitle)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notiMessage))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationCompat.setColor(getResources().getColor(R.color.white));
        } else {
        }
        switch (type) {
            case "new-project":
           break;
        }

        String remoteShowNotification = getDataKey(remoteMessage, "show_notification");

        AtomicReference<Boolean> showNotification = new AtomicReference<Boolean>(true);

        if (!remoteShowNotification.equals("") && remoteShowNotification.equals("false")) {
            showNotification.set(false);
        }

        if (isRealmUpdated.get()) {
        }

        if (intent != null) {
            intent.putExtra("firebase", true);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationCompat
                    .setContentIntent(pendingIntent);        }

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationUtils = new NotificationChannelHelpers(getApplicationContext());
            notificationCompat.setChannelId("channel_id_general");
            if (!getDataKey(remoteMessage, "channel_id").equals("")) {
                notificationCompat.setChannelId(getDataKey(remoteMessage, "channel_id"));
            }
            mNotificationUtils.getManager().notify(nextNotificatoinId, notificationCompat.build());
        } else {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(nextNotificatoinId, notificationCompat.build());
        }
    }
}
