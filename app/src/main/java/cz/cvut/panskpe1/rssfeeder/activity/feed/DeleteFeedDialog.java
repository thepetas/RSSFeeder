package cz.cvut.panskpe1.rssfeeder.activity.feed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.os.Bundle;

import cz.cvut.panskpe1.rssfeeder.R;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.*;

import cz.cvut.panskpe1.rssfeeder.data.MyContentProvider;

/**
 * Created by petr on 5/16/16.
 */
public class DeleteFeedDialog extends DialogFragment {

    private static final String ARG_FEED_ID = "feed_id";

    private long mFeedId;

    public static void show(FragmentManager fragmentManager, long feedId) {
        String tag = DeleteFeedDialog.class.getCanonicalName();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment existingDialog = fragmentManager.findFragmentByTag(tag);
        if (existingDialog != null) {
            transaction.remove(existingDialog);
        }

        DeleteFeedDialog newDialog = new DeleteFeedDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_FEED_ID, feedId);
        newDialog.setArguments(args);
        newDialog.show(transaction, tag);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFeedId = getArguments().getLong(ARG_FEED_ID);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_feed_title);
        builder.setMessage(R.string.delete_feed_mess);

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentResolver resolver = getActivity().getContentResolver();
                resolver.delete(MyContentProvider.CONTENT_URI_ARTICLE, FEED_ID + "=?",
                        new String[]{String.valueOf(mFeedId)});
                resolver.delete(ContentUris.withAppendedId(MyContentProvider.CONTENT_URI_FEED, mFeedId), null, null);
            }
        });

        builder.setNegativeButton(R.string.cancel, null);
        builder.setCancelable(true);
        return builder.create();
    }
}
