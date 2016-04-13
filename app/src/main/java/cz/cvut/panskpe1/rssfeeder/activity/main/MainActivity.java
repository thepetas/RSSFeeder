package cz.cvut.panskpe1.rssfeeder.activity.main;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.*;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.activity.feed.FeedActivity;
import cz.cvut.panskpe1.rssfeeder.data.RssFeederContentProvider;
import cz.cvut.panskpe1.rssfeeder.model.Feed;
import cz.cvut.panskpe1.rssfeeder.model.FeedEntry;

/**
 * Created by petr on 3/19/16.
 */
public class MainActivity extends Activity implements TaskFragment.TaskCallbacks {

    private static final String TAG = "MAIN_ACTIVITY";
    private boolean isRun = false;
    private static final String TASK_FRAGMENT = "TaskFragment";

    private MenuItem mRefreshMenuItem;
    private TaskFragment mTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        mTaskFragment = (TaskFragment) fm.findFragmentByTag(TASK_FRAGMENT);

        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            fm.beginTransaction().add(mTaskFragment, "task").commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feeds_item:
                Intent intent = new Intent(this, FeedActivity.class);
                startActivity(intent);
                return true;
            case R.id.update_item:
                Log.i(TAG, "Menu - click update");
                if (mRefreshMenuItem == null)
                    mRefreshMenuItem = item;

                mTaskFragment.executeTask();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPreExecute() {
        Log.i("MAIN", "Start!!!");
        mRefreshMenuItem.setActionView(R.layout.action_progressbar);
        mRefreshMenuItem.expandActionView();
    }

    @Override
    public void onPostExecute() {
        Log.i("MAIN", "Konec!!!");
        mRefreshMenuItem.collapseActionView();
        mRefreshMenuItem.setActionView(null);
    }

    @Override
    public void updateProgress(int progress) {

    }
}
