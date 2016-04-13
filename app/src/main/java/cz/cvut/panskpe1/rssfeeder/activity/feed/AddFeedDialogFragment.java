package cz.cvut.panskpe1.rssfeeder.activity.feed;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cz.cvut.panskpe1.rssfeeder.R;

/**
 * Created by petr on 4/13/16.
 */
public class AddFeedDialogFragment extends DialogFragment {

    private AlertDialog mDialog;
    private EditText mText;
    private AddFeedDialogFragmentListener mListener;
    private Button mSaveButton;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (AddFeedDialogFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getTitle() + " must implement CustomLayoutDialogFragmentListener interface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.add_new_feed);
            builder.setMessage(R.string.invalid);
            mText = new EditText(getActivity());
            mText.setHint(R.string.type_feed_url);
            builder.setView(mText);
            builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mListener.addFeed(mText.getText().toString());
                    Toast toast = Toast.makeText(getActivity(), R.string.feed_added_mess, Toast.LENGTH_LONG);
                    toast.show();
                }
            });

            builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            mDialog = builder.create();

            mText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkAndSetStatus(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        return mDialog;
    }


    @Override
    public void onStart() {
        super.onStart();
        mSaveButton = mDialog.getButton(Dialog.BUTTON_POSITIVE);
        checkAndSetStatus(mText.getText().toString());

    }

    private void checkAndSetStatus(String s) {
        if (Patterns.WEB_URL.matcher(s).matches()) {
            mDialog.setMessage(getActivity().getResources().getString(R.string.valid));
            mSaveButton.setEnabled(true);
        } else {
            mDialog.setMessage(getActivity().getResources().getString(R.string.invalid));
            mSaveButton.setEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    public AddFeedDialogFragment getInstance() {
        return new AddFeedDialogFragment();
    }

    public interface AddFeedDialogFragmentListener {
        public void addFeed(String url);
    }


}
