package cz.cvut.panskpe1.rssfeeder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by petr on 3/20/16.
 */
public class MainFragment extends Fragment {
    private LinearLayout linearLayout;

    MainFragmentListener mListener;

    public MainFragment() {

    }

    public static Fragment newInstance() {
        return new MainFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayoutMainFragment);

        for (final Article art : DataStorage.getInstance().getAllArticles()) {
            final View v = View.inflate(getActivity(), R.layout.article_menu_item, null);
            ((TextView) v.findViewById(R.id.articleItemTitleTextView)).setText(art.getTitle());
            ((TextView) v.findViewById(R.id.articleItemDescTextView)).setText(art.getPreDescription());

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mListener.showArticle(art.getId());
                }
            });


            linearLayout.addView(v);
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MainFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getTitle() + " must implement " + this.getClass().toString() + " interface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface MainFragmentListener {
        public void showArticle(int id);
    }

}
