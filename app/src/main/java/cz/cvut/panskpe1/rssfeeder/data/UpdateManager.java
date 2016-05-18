package cz.cvut.panskpe1.rssfeeder.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

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

    private ContentResolver mResolver;
    private Resources mResources;

    public UpdateManager(ContentResolver contentResolver, Resources res) {
        this.mResolver = contentResolver;
        this.mResources = res;
    }

    public boolean saveInitFeed(String url) {
        String feedSelection = LINK + " = ?";
        String[] feedSelectionArgs = {url};

        Cursor savedFeed = mResolver.query(MyContentProvider.CONTENT_URI_FEED, null, feedSelection,
                feedSelectionArgs, null);

        try {
            if (savedFeed != null && savedFeed.getCount() > 0) {
                return false;

            } else {
                ContentValues cv = new ContentValues();
                cv.put(LINK, url);
                cv.put(TITLE, mResources.getString(R.string.unknown_title));
                cv.put(AUTHOR, mResources.getString(R.string.unknown_author));
                mResolver.insert(MyContentProvider.CONTENT_URI_FEED, cv);
                return true;
            }
        } finally {
            if (savedFeed != null)
                savedFeed.close();
        }
    }

    public void saveSyndFeed(String url, SyndFeed feed) {
        long id = saveFeed(url, feed);
        saveEntries(id, feed);
    }

    private long saveFeed(String url, SyndFeed feed) {
        ContentValues feedValues = makeFeedWithData(feed);

        String feedSelection = LINK + " = ?";
        String[] feedSelectionArgs = {url};
        Cursor savedFeed = mResolver.query(MyContentProvider.CONTENT_URI_FEED, null, feedSelection,
                feedSelectionArgs, null);

        try {
            if (savedFeed != null && savedFeed.moveToFirst()) {
                long feedId = savedFeed.getLong(savedFeed.getColumnIndex(ID));
                int titleIndex = savedFeed.getColumnIndex(TITLE);
                if (savedFeed.getString(titleIndex).equals(mResources.getString(R.string.unknown_title))) {
                    mResolver.update(ContentUris.withAppendedId(
                            MyContentProvider.CONTENT_URI_FEED, feedId), feedValues, null, null);
                }
                return feedId;
            } else {
                Uri feedUri = mResolver.insert(MyContentProvider.CONTENT_URI_FEED, feedValues);
                return ContentUris.parseId(feedUri);
            }
        } finally {
            if (savedFeed != null) {
                savedFeed.close();
            }
        }
    }

    private void saveEntries(long feedId, SyndFeed feed) {
        List<SyndEntry> entries = feed.getEntries();
        for (SyndEntry entry : entries) {
            ContentValues entryValues = makeArticleWithData(entry, feedId);

            String entrySelection = LINK + " = ?";
            String[] entrySelectionArgs = {entry.getLink()};
            Cursor savedEntry = mResolver.query(MyContentProvider.CONTENT_URI_ARTICLE, null, entrySelection,
                    entrySelectionArgs, null);
            try {
                if (savedEntry != null && savedEntry.getCount() > 0) {
                    mResolver.update(MyContentProvider.CONTENT_URI_ARTICLE, entryValues, entrySelection,
                            entrySelectionArgs);
                } else {
                    mResolver.insert(MyContentProvider.CONTENT_URI_ARTICLE, entryValues);
                }
            } finally {
                if (savedEntry != null) {
                    savedEntry.close();
                }
            }
        }
    }

    public boolean updateAll() {
        Cursor cursor = mResolver.query(MyContentProvider.CONTENT_URI_FEED,
                null, null, null, null);
        int urlIndex = cursor.getColumnIndex(LINK);

        try {
            while (cursor.moveToNext()) {
                String url = cursor.getString(urlIndex);
                URL feedSource = new URL(url);
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(feedSource));
                saveSyndFeed(url, feed);
                deleteOldEntries(url);
            }
        } catch (Exception e) {
            return false;
        } finally {
            cursor.close();
        }
        return true;
    }

    private ContentValues makeFeedWithData(SyndFeed feed) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AUTHOR, feed.getAuthor());
        contentValues.put(SUBTITLE, feed.getDescription());
        if (!TextUtils.isEmpty(feed.getAuthor())) {
            contentValues.put(TITLE, feed.getTitle());
        }
        contentValues.put(UPDATED, feed.getPublishedDate().getTime());
        return contentValues;
    }

    private ContentValues makeArticleWithData(SyndEntry article, long feedId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, article.getTitle());
        contentValues.put(AUTHOR, article.getAuthor());
        contentValues.put(FEED_ID, feedId);
        contentValues.put(LINK, article.getLink());
        contentValues.put(UPDATED, article.getPublishedDate().getTime());
        SyndContent content = article.getDescription();
        contentValues.put(CONTENT, content.getValue());
        String summary = Html.fromHtml(content.getValue()).toString();
        contentValues.put(SUMMARY, summary);
        return contentValues;
    }

    public void deleteOldEntries(String feedLink) {
        String feedSelection = LINK + " = ?";
        String[] feedSelectionArgs = {feedLink};
        Cursor savedFeed = mResolver.query(MyContentProvider.CONTENT_URI_FEED, new String[]{ID},
                feedSelection, feedSelectionArgs, null);
        long feedId = -1;
        if (savedFeed != null) {
            if (savedFeed.moveToFirst()) {
                feedId = savedFeed.getLong(savedFeed.getColumnIndex(ID));
            }
            savedFeed.close();
        }

        if (feedId >= 0) {
            mResolver.delete(MyContentProvider.CONTENT_URI_ARTICLE,
                    FEED_ID + " = ? AND " + UPDATED + " < ?",
                    new String[]{feedLink, String.valueOf(
                            System.currentTimeMillis() - 3600 * 24 * 30 * 1000)});
        }
    }
}
