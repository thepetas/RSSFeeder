package cz.cvut.panskpe1.rssfeeder;

import android.app.Application;
import android.content.Intent;

import cz.cvut.panskpe1.rssfeeder.service.ScheduleBroadcastReceiver;

/**
 * Created by petr on 5/4/17.
 */

public class Feeder extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(ScheduleBroadcastReceiver.SCHEDULE);
        sendBroadcast(intent);
    }
}
