package cz.cvut.panskpe1.rssfeeder.activity.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.activity.article.ArticleDetailActivity;
import cz.cvut.panskpe1.rssfeeder.activity.article.ArticleDetailFragment;
import cz.cvut.panskpe1.rssfeeder.activity.feed.FeedActivity;
import cz.cvut.panskpe1.rssfeeder.service.AlarmBroadcastReceiver;
import cz.cvut.panskpe1.rssfeeder.service.DownloadService;

/**
 * Created by petr on 3/19/16.
 */
public class MainActivity extends Activity implements ArticlesListFragment.ArticleListFragmentCallback, DownloadService.DownloadServiceCallback {

    private static final String TAG = "MAIN_ACTIVITY";
    private boolean isXlarge;
    private DownloadService mService;
    private MenuItem mRefreshMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
            addMainFragment();

        if (findViewById(R.id.detailFragment) != null) {
            isXlarge = true;
            addArticleDetailFragment();
        } else
            isXlarge = false;
    }

    private void addArticleDetailFragment() {
        FragmentManager manager = getFragmentManager();
        if (manager.findFragmentById(R.id.detailFragment) == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.detailFragment, createArticleDetailFragment());
            transaction.commit();
        }
    }

    private Fragment createArticleDetailFragment() {
        return Fragment.instantiate(this, ArticleDetailFragment.class.getName());
    }

    private void addMainFragment() {
        getFragmentManager().beginTransaction()
                .add(R.id.containerMainActivity, new ArticlesListFragment())
                .commit();
    }

    @Override
    protected void onStop() {
        if (mService != null) {
            unbindService(mConnection);
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mRefreshMenuItem = menu.findItem(R.id.update_item);
        return true;
    }

    public void refreshingStart() {
        mRefreshMenuItem.setActionView(R.layout.action_progressbar);
        mRefreshMenuItem.expandActionView();
    }

    public void refreshingStop() {
        if (mRefreshMenuItem.getActionView() != null) {
            mRefreshMenuItem.collapseActionView();
            mRefreshMenuItem.setActionView(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feeds_item:
                Intent intent = new Intent(this, FeedActivity.class);
                startActivity(intent);
                return true;
            case R.id.update_item:
                AlarmBroadcastReceiver.startService(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intentBind = new Intent(this, DownloadService.class);
        bindService(intentBind, mConnection, Context.BIND_AUTO_CREATE);
        initArticleByLastId();
//        Toast.makeText(this, getSizeName(this), Toast.LENGTH_LONG).show();
    }

    private String getSizeName() {
        int screenLayout = this.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return "small";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return "normal";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return "large";
            case 4:
                return "xlarge";
            default:
                return "undefined";
        }
    }


    @Override
    public void onArticleClick(long id) {
        Log.i(TAG, "Open Article with ID: " + id);
        if (isXlarge) {
            ArticleDetailFragment fragment = (ArticleDetailFragment) getFragmentManager().findFragmentById(R.id.detailFragment);
            fragment.fillData(id);
        } else {
            Intent intent = new Intent(this, ArticleDetailActivity.class);
            intent.putExtra(ArticleDetailActivity.ENTRY_ID, id);
            startActivity(intent);
        }
    }

    @Override
    public void initArticleByLastId() {
        if (isXlarge) {
            ArticlesListFragment listFragment = (ArticlesListFragment) getFragmentManager().findFragmentById(R.id.containerMainActivity);
            ArticleDetailFragment fragment = (ArticleDetailFragment) getFragmentManager().findFragmentById(R.id.detailFragment);
            fragment.fillData(listFragment.getLastId());
        }
    }

    @Override
    public void startRefresh() {
        Log.d("TAG", "START REFRESH!!!!!!!!!!!!!!!!!");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshingStart();
            }
        });
    }

    @Override
    public void stopRefresh() {
        Log.d("TAG", "STOP REFRESH!!!!!!!!!!!!!!!!!");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshingStop();
            }
        });
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((DownloadService.MyBinder) service).getServiceInstance();
            mService.registerClient(MainActivity.this);
            if (mRefreshMenuItem != null && mService.isDownloading()) {
                refreshingStart();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };
}

