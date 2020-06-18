package com.example.polyremote;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MusicService extends Service {

    private static final String CHANNEL_ID = "POLY_MUSIC";
    public static final int NOTIFICATION_ID = 1;

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_NEXT = "ACTION_NEXT";
    public static final String ACTION_PREV = "ACTION_PREV";
    public static final String ACTION_MUTE = "ACTION_MUTE";
    public static final String ACTION_CLOSE = "ACTION_CLOSE";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_START:
                    createNotification();
                    break;
                case ACTION_PLAY:
                    WebRequests.getInstance().sendAction(WebRequests.REMOTE_ACTION.PLAY);
                    break;
                case ACTION_NEXT:
                    WebRequests.getInstance().sendAction(WebRequests.REMOTE_ACTION.NEXT);
                    break;
                case ACTION_PREV:
                    WebRequests.getInstance().sendAction(WebRequests.REMOTE_ACTION.PREVIOUS);
                    break;
                case ACTION_MUTE:
                    WebRequests.getInstance().sendAction(WebRequests.REMOTE_ACTION.MUTE);
                    break;
                case ACTION_CLOSE:
                    stopSelf();
                    break;
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotification() {

        createNotificationChannel();

        // launch application intent
        Intent intentApp = new Intent(this, MainActivity.class);
        PendingIntent pIntentApp = PendingIntent.getActivity(this, 0, intentApp, 0);

        // play/pause music
        Intent intentPlay = new Intent(this, MusicService.class);
        intentPlay.setAction(ACTION_PLAY);
        PendingIntent pIntentPlay = PendingIntent.getService(this, 0, intentPlay, 0);

        // next
        Intent intentNext = new Intent(this, MusicService.class);
        intentNext.setAction(ACTION_NEXT);
        PendingIntent pIntentNext = PendingIntent.getService(this, 0, intentNext, 0);

        // prev
        Intent intentPrev = new Intent(this, MusicService.class);
        intentPrev.setAction(ACTION_PREV);
        PendingIntent pIntentPrev = PendingIntent.getService(this, 0, intentPrev, 0);

        // mute
        Intent intentMute = new Intent(this, MusicService.class);
        intentMute.setAction(ACTION_MUTE);
        PendingIntent pIntentMute = PendingIntent.getService(this, 0, intentMute, 0);

        // close
        Intent intentClose = new Intent(this, MusicService.class);
        intentClose.setAction(ACTION_CLOSE);
        PendingIntent pIntentClose = PendingIntent.getService(this, 0, intentClose, 0);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_logo)
                // Add media control buttons that invoke intents in your media service
                .addAction(R.drawable.ic_prev, "Previous", pIntentPrev)
                .addAction(R.drawable.ic_play_arrow, "Pause", pIntentPlay)
                .addAction(R.drawable.ic_next, "Next", pIntentNext)
                .addAction(R.drawable.ic_volume_up, "Mute", pIntentMute)
                .addAction(R.drawable.ic_close, "Close", pIntentClose)
                .setContentIntent(pIntentApp)
                .setColorized(true)
                .setColor(Color.BLACK)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setShowCancelButton(true))
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_extra_text))
                .setOnlyAlertOnce(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mini_logo))
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "PolyPlay";
            String description = "Control music playback";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
