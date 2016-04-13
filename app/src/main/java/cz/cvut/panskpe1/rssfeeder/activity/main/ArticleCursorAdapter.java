package cz.cvut.panskpe1.rssfeeder.activity.main;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.*;

import cz.cvut.panskpe1.rssfeeder.R;

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
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.title.setText(cursor.getString(cursor.getColumnIndex(TITLE)));
        holder.summary.setText(cursor.getString(cursor.getColumnIndex(SUMMARY)));
    }

}
