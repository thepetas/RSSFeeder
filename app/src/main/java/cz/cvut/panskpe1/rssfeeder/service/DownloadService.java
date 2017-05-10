package cz.cvut.panskpe1.rssfeeder.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
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
    private static String TAG = "DownloadService";
    private DownloadServiceCallback mCallback;
    private boolean isRunning = false;

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


//        TODO start
        if (mCallback != null)
            mCallback.startRefresh();
        isRunning = true;
        Log.i(TAG, "Starting update");
        boolean res = updateArticles();
        isRunning = false;
        if (mCallback != null)
            mCallback.stopRefresh();
        if (!res && mCallback != null) {
            mCallback.notifyDownloadFailed();
        }
    }

    private boolean updateArticles() {
        UpdateManager updateManager = new UpdateManager(getContentResolver(), getResources());
        Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI_FEED,
                null, null, null, null);
        int urlIndex = cursor.getColumnIndex(DbConstants.LINK);

        try {
            while (cursor.moveToNext()) {
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

    public void registerClient(Activity activity) {
        mCallback = (DownloadServiceCallback) activity;
    }

    public boolean isDownloading() {
        return isRunning;
    }

    public interface DownloadServiceCallback {
        public void startRefresh();

        public void notifyDownloadFailed();

        public void stopRefresh();
    }

}
