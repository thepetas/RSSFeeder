package cz.cvut.panskpe1.rssfeeder.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

import java.net.URL;

import cz.cvut.panskpe1.rssfeeder.data.DbConstants;
import cz.cvut.panskpe1.rssfeeder.data.MyContentProvider;
import cz.cvut.panskpe1.rssfeeder.data.UpdateManager;

/**
 * Created by petr on 5/11/16.
 */
public class DownloadService extends IntentService {

    public static long DOWNLOAD_INTERVAL = AlarmManager.INTERVAL_HOUR * 5;
    //    public static long DOWNLOAD_INTERVAL = 1000 * 50;
    private static String TAG = "DownloadService";

    public static final String BROADCAST_REFRESH = "rss_reader_refresh";
    public static final int STATE_STARTED = 0;
    public static final int STATE_FINISHED_OK = 1;
    public static final int STATE_FINISHED_FAIL = 2;
    public static final String STATE = "STATUS_REFRESH";

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
        publishChange(STATE_STARTED);
        Log.i(TAG, "Starting update");

//        UpdateManager updateManager = new UpdateManager(getContentResolver(), getResources());
//        boolean res = updateManager.updateAll();
        boolean res = updateArticles();
        for (int i = 0; i < 6; i++) {
            publishChange(STATE_STARTED);
            Log.i(TAG, "Time: " + (i + 1) + "/6");
            SystemClock.sleep(1000);
        }

        Log.i(TAG, "Ending update");
        if (res)
            publishChange(STATE_FINISHED_OK);
        else
            publishChange(STATE_FINISHED_FAIL);
    }

    private boolean updateArticles() {
        UpdateManager updateManager = new UpdateManager(getContentResolver(), getResources());
        Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI_FEED,
                null, null, null, null);
        int urlIndex = cursor.getColumnIndex(DbConstants.LINK);

        try {
            while (cursor.moveToNext()) {
                publishChange(STATE_STARTED);
                String url = cursor.getString(urlIndex);
                URL feedSource = new URL(url);
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(feedSource));
                updateManager.saveSyndFeed(url, feed);
                updateManager.deleteOldEntries(url);
            }
        } catch (Exception e) {
            return false;
        } finally {
            cursor.close();
        }
        return true;
    }

    private void publishChange(int num) {
        Intent intent = new Intent(BROADCAST_REFRESH);
        intent.putExtra(STATE, num);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public class MyBinder extends Binder {
        public DownloadService getServiceInstance() {
            return DownloadService.this;
        }
    }

    @Override
    public void onDestroy() {
        mBinder = null;
        super.onDestroy();
    }

}
