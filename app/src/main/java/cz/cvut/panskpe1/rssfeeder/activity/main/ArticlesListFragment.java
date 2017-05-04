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
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ARTICLE_LOADER, null, this);
    }

    public long getLastId() {
        return lastId;
    }



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

    @Override
    public void onResume() {
        Intent intentBind = new Intent(getActivity(), DownloadService.class);
//        getActivity().bindService(intentBind, mConnection, Context.BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallback.onArticleClick(id);
    }

//    @Override
//    public void onStop() {
//        if (mService != null) {
//            getActivity().unbindService(mConnection);
//        }
//        super.onStop();
//    }

//    ServiceConnection mConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mService = ((DownloadService.MyBinder) service).getServiceInstance();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mService = null;
//        }
//    };

    public interface ArticleListFragmentCallback {
        void onArticleClick(long entryId);

    }
}
