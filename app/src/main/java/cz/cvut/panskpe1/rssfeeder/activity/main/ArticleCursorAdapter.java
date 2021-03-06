package cz.cvut.panskpe1.rssfeeder.activity.main;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.activity.article.ArticleDetailActivity;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.FEED_ID;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.ID;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.SUMMARY;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.TITLE;

/**
 * Created by petr on 4/13/16.
 */
public class ArticleCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private static final String TAG = "ARTTICLE_CURSOR_ADAPTER";
    private int mTitleColumn;
    private int mSummaryColumn;
    private int mIdColumn;

    public ArticleCursorAdapter(Context context, Cursor c) {
        super(context, c, false);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        setColumns(c);
    }

    private void setColumns(Cursor cursor) {
        if (cursor != null) {
            mTitleColumn = cursor.getColumnIndex(TITLE);
            mSummaryColumn = cursor.getColumnIndex(SUMMARY);
            mIdColumn = cursor.getColumnIndex(ID);
        }
    }

    private class ViewHolder {
        TextView title;
        TextView summary;

        ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.item_entry_title);
            summary = (TextView) view.findViewById(R.id.item_entry_summary);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_entry, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        view.setId((int) cursor.getLong(mIdColumn));
        return view;
    }

    @Override
    public void bindView(final View view, Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.title.setText(cursor.getString(cursor.getColumnIndex(TITLE)));
        String sum = cursor.getString(cursor.getColumnIndex(SUMMARY));
        sum = sum.substring(0, 100).trim();
        holder.summary.setText(sum);
    }
}
