package cz.cvut.panskpe1.rssfeeder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by petr on 5/15/16.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    public static final String RUN ="cz.cvut.panskpe1.rssfeeder.service.RUN";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmBroadcastReceiver", "Alarm call");
        /*Intent serviceIntent = new Intent(context, DownloadService.class);
        context.startService(serviceIntent);*/
        startService(context);
    }

    public static void startService(Context context){
        Intent serviceIntent = new Intent(context, DownloadService.class);
        context.startService(serviceIntent);
    }
}
