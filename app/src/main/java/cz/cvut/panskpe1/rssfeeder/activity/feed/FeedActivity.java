package cz.cvut.panskpe1.rssfeeder.activity.feed;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.data.RssFeederContentProvider;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.AUTHOR;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.LINK;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.TITLE;

/**
 * Created by petr on 3/19/16.
 */
public class FeedActivity extends Activity implements AddFeedDialogFragment.AddFeedDialogFragmentListener {

    private static final String ADD_FEED_DIALOG_FRAGMENT = "AddFeedDialogFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.add_feed_item:
                AddFeedDialogFragment addFeedDialogFragment = new AddFeedDialogFragment().getInstance();
                addFeedDialogFragment.show(getFragmentManager(), ADD_FEED_DIALOG_FRAGMENT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void addFeed(String url) throws SQLiteConstraintException {
        ContentValues cv = new ContentValues();
        cv.put(LINK, url);
        cv.put(TITLE, getResources().getString(R.string.unknown_title));
        cv.put(AUTHOR, getResources().getString(R.string.unknown_author));
        getContentResolver().insert(RssFeederContentProvider.CONTENT_URI_FEED, cv);
    }
}