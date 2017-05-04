package cz.cvut.panskpe1.rssfeeder.activity.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.activity.article.ArticleDetailActivity;
import cz.cvut.panskpe1.rssfeeder.activity.article.ArticleDetailFragment;
import cz.cvut.panskpe1.rssfeeder.service.ScheduleBroadcastReceiver;

/**
 * Created by petr on 3/19/16.
 */
public class MainActivity extends Activity implements ArticlesListFragment.ArticleListFragmentCallback {

    private static final String TAG = "MAIN_ACTIVITY";
    private boolean isXlarge;

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
        Bundle args = new Bundle();
        args.putLong(ArticleDetailFragment.ARG_ENTRY_ID,
                getIntent().getLongExtra(ArticleDetailActivity.ENTRY_ID, 1));
        return Fragment.instantiate(this, ArticleDetailFragment.class.getName(), args);
    }

    private void addMainFragment() {
        getFragmentManager().beginTransaction()
                .add(R.id.containerMainActivity, new ArticlesListFragment())
                .commit();
    }

    /*@Override
    protected void onStop() {
//        if (mService != null) {
//            unbindService(mConnection);
//        }
        super.onStop();
    }*/

    @Override
    protected void onStart() {
        if (isXlarge) {
            ArticlesListFragment list = (ArticlesListFragment) getFragmentManager().findFragmentById(R.id.containerMainActivity);
            ArticleDetailFragment fragment = (ArticleDetailFragment) getFragmentManager().findFragmentById(R.id.detailFragment);
            fragment.fillData(list.getLastId());
        }
//        Intent intentBind = new Intent(this, DownloadService.class);
//        bindService(intentBind, mConnection, Context.BIND_AUTO_CREATE);
        super.onStart();
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
        Log.i(TAG, "ID: " + id);
        if (isXlarge) {
            ArticleDetailFragment fragment = (ArticleDetailFragment) getFragmentManager().findFragmentById(R.id.detailFragment);
            fragment.fillData(id);
        } else {
            Intent intent = new Intent(this, ArticleDetailActivity.class);
            intent.putExtra(ArticleDetailActivity.ENTRY_ID, id);
            startActivity(intent);
        }
    }

}
