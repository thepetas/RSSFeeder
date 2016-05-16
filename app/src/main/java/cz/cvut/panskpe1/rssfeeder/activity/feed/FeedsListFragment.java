package cz.cvut.panskpe1.rssfeeder.activity.feed;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.data.MyContentProvider;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.ID;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.LINK;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.TITLE;

/**
 * Created by petr on 3/20/16.
 */
public class FeedsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ADD_FEED_DIALOG_FRAGMENT = "AddFeedDialog";
    private static final int FEED_LOADER = 1;
    private FeedCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(FEED_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (FEED_LOADER == id) {
            return new CursorLoader(getActivity(), MyContentProvider.CONTENT_URI_FEED,
                    new String[]{ID, TITLE, LINK}, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!isAdded()) {
            return;
        }
        if (loader.getId() == FEED_LOADER) {
            if (mAdapter == null) {
                mAdapter = new FeedCursorAdapter(getActivity(), data);
                setListAdapter(mAdapter);
            } else
                mAdapter.changeCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == FEED_LOADER) {
            mAdapter.changeCursor(null);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        DeleteFeedDialog.show(getFragmentManager(), id);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.feed, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.add_feed_item:
                new AddFeedDialog().show(getFragmentManager(), ADD_FEED_DIALOG_FRAGMENT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
