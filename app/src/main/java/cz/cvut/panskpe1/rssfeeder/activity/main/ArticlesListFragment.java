package cz.cvut.panskpe1.rssfeeder.activity.main;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.activity.feed.FeedActivity;
import cz.cvut.panskpe1.rssfeeder.data.ContentProvider;
import cz.cvut.panskpe1.rssfeeder.service.DownloadService;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.FEED_ID;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.ID;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.SUMMARY;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.TITLE;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.UPDATED;

/**
 * Created by petr on 3/20/16.
 */
public class ArticlesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ARTICLE_LOADER = 1;
    private ListView mListView;
    private ArticleCursorAdapter mAdapter;
    private DownloadService mService;
    private MenuItem mRefreshMenuItem;
    private boolean isRef = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(ARTICLE_LOADER, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ArticleCursorAdapter(getActivity(), null, 0);
        mListView = (ListView) getActivity().findViewById(R.id.database_content);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ARTICLE_LOADER:
                return new CursorLoader(getActivity(), ContentProvider.CONTENT_URI_ARTICLE,
                        new String[]{ID, TITLE, SUMMARY, FEED_ID}, null, null, UPDATED + " DESC");
            default:
                break;
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case ARTICLE_LOADER:
                mAdapter.changeCursor(data);
                break;
            default:
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case ARTICLE_LOADER:
                mAdapter.changeCursor(null);
                break;
            default:
                break;
        }
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("ARTICLE_LIST_FRAGMENT", "Service is connected..");
            DownloadService.MyBinder binder = (DownloadService.MyBinder) service;
            mService = binder.getServiceInstance();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ARTICLE_LIST_FRAGMENT", "Service is disconnected..");
            mService = null;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), DownloadService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mService != null) {
            getActivity().unbindService(mConnection);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feeds_item:
                Intent intent = new Intent(getActivity(), FeedActivity.class);
                startActivity(intent);
                return true;
            case R.id.update_item:
                mService.updateAll();
                if (!isRef) {
                    starRefreshing();
                } else {
                    stopRefreshing();
                }
                isRef = !isRef;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        if (mRefreshMenuItem == null)
            mRefreshMenuItem = menu.findItem(R.id.update_item);
    }

    private void starRefreshing() {
        mRefreshMenuItem.setActionView(R.layout.action_progressbar);
        mRefreshMenuItem.expandActionView();
    }

    private void stopRefreshing() {
        if (mRefreshMenuItem.getActionView() != null) {
            mRefreshMenuItem.collapseActionView();
            mRefreshMenuItem.setActionView(null);
        }
    }
}
