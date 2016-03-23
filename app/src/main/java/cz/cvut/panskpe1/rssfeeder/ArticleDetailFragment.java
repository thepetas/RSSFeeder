package cz.cvut.panskpe1.rssfeeder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by petr on 3/20/16.
 */
public class ArticleDetailFragment extends Fragment {
    private LinearLayout linearLayout;

    private ArticleDetailFragmentListener mListener;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageButton shareButton;
    private Article article;

    public ArticleDetailFragment() {

    }

    public static Fragment newInstance() {
        return new ArticleDetailFragment();
    }

    private void updateInformation() {
        if (article != null) {
            titleTextView.setText(article.getTitle());
            descriptionTextView.setText(article.getDescription());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        article = DataStorage.getInstance().getArticle(mListener.getArticleId());

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_article_detail, container, false);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayoutArticleDetailFragment);
        titleTextView = (TextView) view.findViewById(R.id.articleTitleTextViewFragmentArticle);
        descriptionTextView = (TextView) view.findViewById(R.id.articleDescTextViewFragmentArticle);
        updateInformation();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shareBar:
                mListener.shareArticle(getShareIntent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private Intent getShareIntent() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, DataStorage.getInstance().getArticle(article.getId()).getShareString(getResources()));
        sendIntent.setType("text/plain");
        return sendIntent;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ArticleDetailFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getTitle() + " must implement " + this.getClass().toString() + " interface.");
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }

    public interface ArticleDetailFragmentListener {
        public int getArticleId();

        public void shareArticle(Intent intent);
    }

}
