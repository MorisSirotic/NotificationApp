package hr.sirotic.productivityapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import hr.sirotic.productivityapp.repository.TaskRepository;

import static hr.sirotic.productivityapp.constants.TaskConstants.NOTIFICATION_ID;

public class AlarmReceiver extends BroadcastReceiver {

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;


    @Override
    public void onReceive(Context context, Intent intent) {
        TaskRepository taskRepository = new TaskRepository(context.getApplicationContext());
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(context, MainActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);


        TaskNotificationsManager taskNotificationsManager = new TaskNotificationsManager(context);

        int alarmId = intent.getIntExtra(NOTIFICATION_ID, 0);
        String taskName = intent.getStringExtra("n");
        String content = intent.getStringExtra("c");

        NotificationCompat.Builder nb = taskNotificationsManager.getChannelNotification(pendingIntent, taskName, content);

        taskNotificationsManager.getManager().notify(alarmId, nb.build());


    }

    public void setAlarm(Context context, Calendar calendar, int taskID) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        calendar = Calendar.getInstance();
        pendingIntent = PendingIntent.getBroadcast(context, taskID, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        // Calculate notification time
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        // Start alarm using notification time
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime() + diffTime,
                pendingIntent);


    }

    public void setRepeatingAlarm(Context context, Calendar ic, int taskID, String title, String content) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(NOTIFICATION_ID, taskID);
        intent.putExtra("n", title);
        intent.putExtra("c", content);


        pendingIntent = PendingIntent.getBroadcast(context, taskID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                ic.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                pendingIntent);


    }

    public void cancelAlarm(Context context, int taskID) {

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(context, taskID, new Intent(context, AlarmReceiver.class), 0);

        alarmManager.cancel(pendingIntent);
    }

}
