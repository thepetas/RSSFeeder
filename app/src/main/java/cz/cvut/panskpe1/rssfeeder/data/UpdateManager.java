package cz.cvut.panskpe1.rssfeeder.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.text.Html;
import android.text.TextUtils;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import cz.cvut.panskpe1.rssfeeder.R;
import cz.cvut.panskpe1.rssfeeder.model.DateHelper;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.AUTHOR;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.CONTENT;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.FEED_ID;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.ID;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.LINK;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.SUBTITLE;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.SUMMARY;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.TITLE;
import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.UPDATED;

/**
 * Created by petr on 4/13/16.
 */
public class UpdateManager {

    public static int updateArticles(Context context) throws Exception {
        int cntUpdated = 0;
        Cursor cursor = context.getContentResolver().query(RssFeederContentProvider.CONTENT_URI_FEED,
                null, null, null, null);

        int urlIndex = cursor.getColumnIndex(LINK);
        int idIndex = cursor.getColumnIndex(ID);
        int titleIndex = cursor.getColumnIndex(TITLE);

        while (cursor.moveToNext()) {
            String url = cursor.getString(urlIndex);
            try {
                URL feedSource = new URL(url);
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(feedSource));

                if (cursor.getString(titleIndex).equals(context.getResources().getString(R.string.unknown_title))) {
                    context.getContentResolver().update(RssFeederContentProvider.CONTENT_URI_FEED,
                            getFeedWithData(feed), DbConstants.ID + "=?", new String[]{cursor.getString(idIndex)});
                }
                int idFeed = cursor.getInt(idIndex);
                List<SyndEntry> entries = feed.getEntries();

                for (SyndEntry se : entries) {
                    try {
                        context.getContentResolver().insert(RssFeederContentProvider.CONTENT_URI_ARTICLE,
                                getArticleWithData(se, idFeed));
                        cntUpdated++;
                    } catch (SQLiteConstraintException ex) {

                    }
                }
            } catch (FeedException | IOException e) {
                throw new Exception(context.getResources().getString(R.string.update_failed));
            }
        }
        cursor.close();
        return cntUpdated;
    }

    private static ContentValues getFeedWithData(SyndFeed feed) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AUTHOR, feed.getAuthor());
        contentValues.put(SUBTITLE, feed.getDescription());
        if (!TextUtils.isEmpty(feed.getAuthor())) {
            contentValues.put(TITLE, feed.getTitle());
        }
        contentValues.put(UPDATED, DateHelper.convertMiliSecondsToDate(feed.getPublishedDate().getTime()));
        return contentValues;
    }

    private static ContentValues getArticleWithData(SyndEntry article, int feedId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, article.getTitle());
        contentValues.put(AUTHOR, article.getAuthor());
        contentValues.put(FEED_ID, feedId);
        contentValues.put(LINK, article.getLink());
        contentValues.put(UPDATED, DateHelper.convertMiliSecondsToDate(article.getPublishedDate().getTime()));
        SyndContent content = article.getDescription();
        contentValues.put(CONTENT, content.getValue());
        String summary = Html.fromHtml(content.getValue()).toString();
        contentValues.put(SUMMARY, summary);
        return contentValues;
    }
}
