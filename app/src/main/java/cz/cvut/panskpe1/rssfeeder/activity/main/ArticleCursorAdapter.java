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

    public ArticleCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = LayoutInflater.from(context);
        mContext = context;
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
        return view;
    }

    @Override
    public void bindView(final View view, Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.title.setText(cursor.getString(cursor.getColumnIndex(TITLE)));
        String sum = cursor.getString(cursor.getColumnIndex(SUMMARY));
        sum = sum.substring(0, 100).trim();
        holder.summary.setText(sum);

        final int id = cursor.getInt(cursor.getColumnIndex(ID));
        final int feedId = cursor.getInt(cursor.getColumnIndex(FEED_ID));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                intent.putExtra(ArticleDetailActivity.ENTRY_ID, id);
                intent.putExtra(ArticleDetailActivity.FEED_ID, feedId);
                mContext.startActivity(intent);
            }
        });
    }

}
