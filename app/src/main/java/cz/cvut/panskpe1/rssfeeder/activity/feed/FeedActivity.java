package cz.cvut.panskpe1.rssfeeder.activity.feed;

import android.app.ActionBar;
import android.app.Activity;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.data.UpdateManager;

/**
 * Created by petr on 3/19/16.
 */
public class FeedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
