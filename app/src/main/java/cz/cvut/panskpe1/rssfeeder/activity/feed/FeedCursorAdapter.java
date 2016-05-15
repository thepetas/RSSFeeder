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
    private static final String TAG = "FEED_CURSOR_ADAPTER";

    public FeedCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = LayoutInflater.from(context);
        mContext = context;
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
        holder.title.setText(cursor.getString(cursor.getColumnIndex(TITLE)));
        holder.link.setText(cursor.getString(cursor.getColumnIndex(LINK)));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.delete_feed_title);
                builder.setMessage(R.string.delete_feed_mess)
                        .setCancelable(false)
                        .setPositiveButton(R.string.Delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteFeedWithArticles(cursor.getString(cursor.getColumnIndex(ID)));
                                Toast toast = Toast.makeText(mContext, R.string.feed_deleted_mess, Toast.LENGTH_LONG);
                                toast.show();
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void deleteFeedWithArticles(String id) {
        mContext.getContentResolver().delete(Uri.withAppendedPath(MyContentProvider.CONTENT_URI_FEED, id), null, null);
        mContext.getContentResolver().delete(MyContentProvider.CONTENT_URI_ARTICLE, FEED_ID + "= ?",
                new String[]{id});
    }


}
