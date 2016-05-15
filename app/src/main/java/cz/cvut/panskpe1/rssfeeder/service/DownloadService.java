package cz.cvut.panskpe1.rssfeeder.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import cz.cvut.panskpe1.rssfeeder.data.UpdateManager;

/**
 * Created by petr on 5/11/16.
 */
public class DownloadService extends IntentService {

    //    public static long DOWNLOAD_INTERVAL = AlarmManager.INTERVAL_HOUR * 5;
    public static long DOWNLOAD_INTERVAL = 1000 * 15;
    private static String TAG = "DownloadService";

    IBinder mBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        updateAll();
    }

    public void updateAll() {
        Log.i(TAG, "Starting update");

        UpdateManager updateManager = new UpdateManager(getContentResolver(), getResources());
        updateManager.updateAll();
        for (int i = 0; i < 6; i++) {
            Log.i(TAG, "Time: " + (i + 1) + "/6");
            SystemClock.sleep(1000);
        }
        Log.i(TAG, "Ending update");
    }

    public class MyBinder extends Binder {
        public DownloadService getServiceInstance() {
            return DownloadService.this;
        }
    }
    

    public interface DownloadCallbacks {
        public void updateStart();
        public void updateStop();
    }
}
