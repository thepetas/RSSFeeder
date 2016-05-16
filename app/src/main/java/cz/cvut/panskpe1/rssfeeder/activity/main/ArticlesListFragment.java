package cz.cvut.panskpe1.rssfeeder.activity.main;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.activity.feed.FeedActivity;
import cz.cvut.panskpe1.rssfeeder.data.MyContentProvider;
import cz.cvut.panskpe1.rssfeeder.service.AlarmBroadcastReceiver;
import cz.cvut.panskpe1.rssfeeder.service.DownloadService;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.FEED_ID;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.ID;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.SUMMARY;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.TITLE;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.UPDATED;

/**
 * Created by petr on 3/20/16.
 */
public class ArticlesListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ARTICLE_LOADER = 1;
    private ArticleCursorAdapter mAdapter;
    private MenuItem mRefreshMenuItem;
    private DownloadService mService;
    private ArticleListFragmentCallback mCallback;
    private long lastId = -1;


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            mCallback = (ArticleListFragmentCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(mAdapter);
//        TODO blablabla
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ARTICLE_LOADER, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        if (mRefreshMenuItem == null)
            mRefreshMenuItem = menu.findItem(R.id.update_item);

//        if (mTaskFragment.isRunning()) {
//            refreshingStart();
//        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feeds_item:
                Intent intent = new Intent(getActivity(), FeedActivity.class);
                startActivity(intent);
                return true;
            case R.id.update_item:
                AlarmBroadcastReceiver.startService(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public long getLastId() {
        return lastId;
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

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("ARTICLE_LIST_FRAGMENT", "Service is connected..");
            mService = (DownloadService) ((DownloadService.MyBinder) service).getServiceInstance();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ARTICLE_LIST_FRAGMENT", "Service is disconnected..");
            mService = null;
        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ARTICLE_LOADER:
                return new CursorLoader(getActivity(), MyContentProvider.CONTENT_URI_ARTICLE,
                        new String[]{ID, TITLE, SUMMARY, FEED_ID}, null, null, UPDATED + " DESC");
            default:
                break;
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!isAdded())
            return;

        if (loader.getId() == ARTICLE_LOADER) {
            if (mAdapter == null) {
                mAdapter = new ArticleCursorAdapter(getActivity(), data);
                setListAdapter(mAdapter);
            }
            mAdapter.changeCursor(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == ARTICLE_LOADER) {
            mAdapter.changeCursor(null);
        }
    }

    private BroadcastReceiver refreshStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(DownloadService.STATE, -1);
            switch (state) {
                case DownloadService.STATE_STARTED:
                    refreshingStart();
                    break;
                case DownloadService.STATE_FINISHED_OK:
                    refreshingStop();
                    Toast.makeText(getActivity(), R.string.successfully_updated, Toast.LENGTH_LONG).show();
                    break;

                case DownloadService.STATE_FINISHED_FAIL:
                    refreshingStop();
                    Toast.makeText(getActivity(), R.string.update_failed, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        Intent intentBind = new Intent(getActivity(), DownloadService.class);
        getActivity().bindService(intentBind, mConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(refreshStateReceiver, new IntentFilter(DownloadService.BROADCAST_REFRESH));
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FR", String.valueOf(getSelectedItemPosition()));
        Log.i("FR", String.valueOf(position));
        Cursor entry = (Cursor) mAdapter.getItem(position);
        int idColumn = entry.getColumnIndex(ID);
        lastId = entry.getLong(idColumn);
        mCallback.onArticleClick(entry.getLong(idColumn));
    }

    @Override
    public void onPause() {
        if (mService != null) {
            getActivity().unbindService(mConnection);
        }
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(refreshStateReceiver);
        super.onPause();
    }

    public static interface ArticleListFragmentCallback {
        public void onArticleClick(long entryId);

    }
}
