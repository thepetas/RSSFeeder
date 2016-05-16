package cz.cvut.panskpe1.rssfeeder.activity.article;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.data.MyContentProvider;
import cz.cvut.panskpe1.rssfeeder.model.Feed;
import cz.cvut.panskpe1.rssfeeder.model.FeedEntry;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.AUTHOR;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.CONTENT;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.FEED_ID;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.ID;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.LINK;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.SUMMARY;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.TITLE;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.UPDATED;

/**
 * Created by petr on 3/20/16.
 */
public class ArticleDetailFragment extends Fragment {

    public static final String ARG_ENTRY_ID = "entry_id";


    private FeedEntry mFeedEntry;

    private TextView mTitle;
    private TextView mDate;
    private TextView mText;
    private TextView mLink;

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
        mLink = (TextView) view.findViewById(R.id.article_link);
        long id = getArguments().getLong(ARG_ENTRY_ID);
        fillData(id);
    }

    public void fillData(long id) {
        initArticleEntry(id);
        if (mFeedEntry != null) {
            mTitle.setText(Html.fromHtml(mFeedEntry.getTitle()));
            String author = mFeedEntry.getAuthor();
            if (TextUtils.isEmpty(author)) {
                author = mFeedEntry.getFeed().getAuthor();
            }

            CharSequence relativeDate = DateUtils.getRelativeTimeSpanString(mFeedEntry.getUpdated());

            mDate.setText(getString(R.string.time_by_author, relativeDate.toString(), author));
            String link = "<a href='" + mFeedEntry.getLink() + "'>" + getResources().getString(R.string.view_full_article)
                    + "</a>";
            Spanned textContent = Html.fromHtml(mFeedEntry.getContent());
            mText.setMovementMethod(LinkMovementMethod.getInstance());
            mText.setText(textContent);
            Spanned textLink = Html.fromHtml(link);
            mLink.setMovementMethod(LinkMovementMethod.getInstance());
            mLink.setText(textLink);

        } else {
            mTitle.setText(R.string.entry_not_found);
            mDate.setVisibility(View.INVISIBLE);
            mText.setText(R.string.entry_not_found_desc);
            mText.setTextColor(getResources().getColor(R.color.text_red));
        }
    }

    private void initArticleEntry(long id) {
        Cursor cArticle = getActivity().getContentResolver().query(
                Uri.withAppendedPath(MyContentProvider.CONTENT_URI_ARTICLE, String.valueOf(id)),
                null, null, null, null);

        if (cArticle.moveToNext()) {
            long feedId = cArticle.getLong(cArticle.getColumnIndex(FEED_ID));

            Feed feed = null;
            Cursor cFeed = getActivity().getContentResolver().query(
                    Uri.withAppendedPath(MyContentProvider.CONTENT_URI_FEED, String.valueOf(feedId)),
                    null, null, null, null);
            if (cFeed.moveToNext()) {
                feed = new Feed(String.valueOf(id), cFeed.getString(cFeed.getColumnIndex(LINK)), cFeed.getString(cFeed.getColumnIndex(TITLE)));
                feed.setAuthor(cFeed.getString(cFeed.getColumnIndex(AUTHOR)));
            }
            cFeed.close();

            mFeedEntry = new FeedEntry(feed,
                    cArticle.getString(cArticle.getColumnIndex(TITLE)),
                    cArticle.getLong(cArticle.getColumnIndex(ID)),
                    cArticle.getString(cArticle.getColumnIndex(LINK)));
            mFeedEntry.setSummary(cArticle.getString(cArticle.getColumnIndex(SUMMARY)));
            mFeedEntry.setContent(cArticle.getString(cArticle.getColumnIndex(CONTENT)));
            mFeedEntry.setUpdated(cArticle.getLong(cArticle.getColumnIndex(UPDATED)));
            mFeedEntry.setAuthor(cArticle.getString(cArticle.getColumnIndex(AUTHOR)));
        }
        mFeedEntry = null;
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
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
