package cz.cvut.panskpe1.rssfeeder.activity.feed;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.data.MyContentProvider;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.FEED_ID;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.ID;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.LINK;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.TITLE;

/**
 * Created by petr on 4/13/16.
 */
public class FeedCursorAdapter extends CursorAdapter {


    private LayoutInflater mInflater;
    private Context mContext;
    private int mTitleColumn;
    private int mLinkColumn;

    public FeedCursorAdapter(Context context, Cursor c) {
        super(context, c, false);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        initColumns(c);
    }

    private void initColumns(Cursor cursor) {
        if (cursor != null) {
            mTitleColumn = cursor.getColumnIndex(TITLE);
            mLinkColumn = cursor.getColumnIndex(LINK);
        }
    }

    private class ViewHolder {
        TextView title;
        TextView link;

        ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.item_feed_title);
            link = (TextView) view.findViewById(R.id.item_feed_link);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_feed, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.title.setText(cursor.getString(mTitleColumn));
        holder.link.setText(cursor.getString(mLinkColumn));
    }

//    private void deleteFeedWithArticles(String id) {
//        mContext.getContentResolver().delete(Uri.withAppendedPath(MyContentProvider.CONTENT_URI_FEED, id), null, null);
//        mContext.getContentResolver().delete(MyContentProvider.CONTENT_URI_ARTICLE, FEED_ID + "= ?",
//                new String[]{id});
//    }
}
