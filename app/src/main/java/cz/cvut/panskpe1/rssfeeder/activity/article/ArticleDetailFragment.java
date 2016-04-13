package cz.cvut.panskpe1.rssfeeder.activity.article;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.model.Feed;
import cz.cvut.panskpe1.rssfeeder.model.FeedEntry;
import cz.cvut.panskpe1.rssfeeder.model.MockData;

/**
 * Created by petr on 3/20/16.
 */
public class ArticleDetailFragment extends Fragment {

    public static final String ARG_FEED_ID = "feed_id";
    public static final String ARG_ENTRY_ID = "entry_id";


    private FeedEntry mFeedEntry;

    private TextView mTitle;
    private TextView mDate;
    private TextView mText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().getActionBar().setTitle(R.string.article_detail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle = (TextView) view.findViewById(R.id.article_detail_title);
        mDate = (TextView) view.findViewById(R.id.article_detail_date_author);
        mText = (TextView) view.findViewById(R.id.article_detail_text);

        initArticleEntry();

        if (mFeedEntry != null) {
            mTitle.setText(Html.fromHtml(mFeedEntry.getTitle()));
            String author = mFeedEntry.getAuthor();
            if (TextUtils.isEmpty(author)) {
                author = mFeedEntry.getFeed().getAuthor();
            }
            CharSequence relativeDate = DateUtils.getRelativeTimeSpanString(mFeedEntry.getUpdated());
            mDate.setText(getString(R.string.entry_date_author, relativeDate, author));
            mText.setText(Html.fromHtml(mFeedEntry.getContent()));
        } else {
            mTitle.setText(R.string.entry_not_found);
            mDate.setVisibility(View.INVISIBLE);
            mText.setText(R.string.entry_not_found_desc);
            mText.setTextColor(getResources().getColor(R.color.text_red));
        }
    }

    private void initArticleEntry() {
        Feed feed = MockData.getFeed(getArguments().getString(ARG_FEED_ID));
        Log.i("FRAGMENT", getArguments().getString(ARG_FEED_ID));
        mFeedEntry = null;
        if (feed != null) {
            String entryId = getArguments().getString(ARG_ENTRY_ID);
            for (FeedEntry entry : feed.getEntries()) {
                if (entry.getId().equals(entryId)) {
                    mFeedEntry = entry;
                    break;
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.article, menu);
        if (mFeedEntry == null) {
            menu.findItem(R.id.menu_article_share).setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_article_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.share_subject, mFeedEntry.getTitle()));
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.share_text, mFeedEntry.getLink()));
                Intent chooser = Intent.createChooser(shareIntent, getString(R.string.share_chooser));
                startActivity(chooser);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
