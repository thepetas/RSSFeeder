package cz.cvut.panskpe1.rssfeeder.activity.feed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.data.UpdateManager;

/**
 * Created by petr on 4/13/16.
 */
public class AddFeedDialog extends DialogFragment {

    private EditText mText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.add_feed_dialog, null);
        mText = (EditText) view.findViewById(R.id.add_feed_url);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_new_feed);
        builder.setView(view);

        builder.setPositiveButton(R.string.add_feed, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String feedUrl = mText.getText().toString();
                if (!feedUrl.isEmpty()) {
                    if (new UpdateManager(getActivity().getContentResolver(), getResources()).saveInitFeed(feedUrl)) {
                        Toast.makeText(getActivity(), R.string.feed_added_mess, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.feed_exists, Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getActivity(), R.string.feed_added_mess, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.empty_url, Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, null);
        builder.setCancelable(true);
        return builder.create();
    }
}
