package cz.cvut.panskpe1.rssfeeder.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by petr on 5/11/16.
 */
public class ScheduleBroadcastReceiver extends BroadcastReceiver {

    public static final String SCHEDULE = "cz.cvut.panskpe1.rssfeeder.service.SCHEDULE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MyReceiver", "Setting repeating alarm...");
        scheduleAlarms(context);
    }

    private static void scheduleAlarms(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent launchIntent = new Intent(AlarmBroadcastReceiver.RUN);
        PendingIntent pi = PendingIntent.getBroadcast(context,
                0, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, DownloadService.DOWNLOAD_INTERVAL,
                pi);
    }
}
