package hr.sirotic.productivityapp;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;


public class TaskNotificationsManager extends ContextWrapper {

    private static final String CHANNEL_NAME = "channelName";
    private static final String CHANNEL_ID = "channelID";
    private NotificationManager manager;

    public TaskNotificationsManager(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getChannelNotification(PendingIntent contentIntent, String notificationTitle, String notificationContent) {
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setContentTitle("TODO: " + notificationTitle)
                .setContentText(notificationContent)
                .setSmallIcon(R.drawable.ic_access_time_black_24dp)
                .setAutoCancel(true);
    }
}

