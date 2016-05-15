package cz.cvut.panskpe1.rssfeeder.activity.main;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.activity.feed.FeedActivity;
import cz.cvut.panskpe1.rssfeeder.service.DownloadService;
import cz.cvut.panskpe1.rssfeeder.service.ScheduleBroadcastReceiver;

/**
 * Created by petr on 3/19/16.
 */
public class MainActivity extends Activity /*implements TaskFragment.TaskCallbacks*/ {

    private static final String TAG = "MAIN_ACTIVITY";
    private static final String TASK_FRAGMENT = "TaskFragment";

    //    private MenuItem mRefreshMenuItem;
    private TaskFragment mTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        mTaskFragment = (TaskFragment) fm.findFragmentByTag(TASK_FRAGMENT);

        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            fm.beginTransaction().add(mTaskFragment, TASK_FRAGMENT).commit();
        }
    }

    @Override
    protected void onStart() {
        Intent intent = new Intent(ScheduleBroadcastReceiver.SCHEDULE);
        sendBroadcast(intent);
        super.onStart();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        if (mRefreshMenuItem == null)
            mRefreshMenuItem = menu.findItem(R.id.update_item);

        if (mTaskFragment.isRunning()) {
            setRefreshing();
        }
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feeds_item:
                Intent intent = new Intent(this, FeedActivity.class);
                startActivity(intent);
                return true;
            case R.id.update_item:
                mTaskFragment.executeTask();
                Intent i = new Intent(this, DownloadService.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setRefreshing() {
        mRefreshMenuItem.setActionView(R.layout.action_progressbar);
        mRefreshMenuItem.expandActionView();
    }

    private void setStop() {
        if (mRefreshMenuItem.getActionView() != null) {
            mRefreshMenuItem.collapseActionView();
            mRefreshMenuItem.setActionView(null);
        }
    }

    @Override
    public void onPreExecute() {
        setRefreshing();
    }

    @Override
    public void onPostExecute(boolean number) {
        if (!number) {
            Toast toast = Toast.makeText(this, R.string.update_failed, Toast.LENGTH_LONG);
            toast.show();
        } else {
            Toast toast = Toast.makeText(this, R.string.successfully_updated, Toast.LENGTH_LONG);
            toast.show();

        }
        setStop();
    }

    @Override
    public void updateProgress() {
        setRefreshing();
    }*/

}
