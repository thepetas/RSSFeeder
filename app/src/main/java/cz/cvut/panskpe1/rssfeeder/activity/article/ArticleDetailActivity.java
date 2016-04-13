package cz.cvut.panskpe1.rssfeeder.activity.article;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.activity.main.MainActivity;

/**
 * Created by petr on 3/19/16.
 */
public class ArticleDetailActivity extends Activity {

    public static final String FEED_ID = "feed_id";
    public static final String ENTRY_ID = "entry_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        addArticleDetailFragment();
    }

    private void addArticleDetailFragment() {
        FragmentManager manager = getFragmentManager();
        if (manager.findFragmentById(R.id.containerArticleDetailActivity) == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.containerArticleDetailActivity, createArticleDetailFragment());
            transaction.commit();
        }
    }

    private Fragment createArticleDetailFragment() {
        Bundle args = new Bundle();
        args.putString(ArticleDetailFragment.ARG_FEED_ID,
                getIntent().getStringExtra(FEED_ID));
        args.putString(ArticleDetailFragment.ARG_ENTRY_ID,
                getIntent().getStringExtra(ENTRY_ID));
        return Fragment.instantiate(this, ArticleDetailFragment.class.getName(), args);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT < 16) {
                    finish();
                    return true;
                }
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
