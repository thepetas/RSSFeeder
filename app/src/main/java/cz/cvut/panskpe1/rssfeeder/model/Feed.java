package cz.cvut.panskpe1.rssfeeder.model;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 4/12/16.
 */
public class Feed {
    private String mId;
    private String mLink;
    private String mTitle;
    private String mSubtitle;
    private long mUpdated;
    private String mAuthor;
    private String mAuthorEmail;

    private List<FeedEntry> mEntries;

    public Feed(String id, String link, String title) {
        mId = id;
        mLink = link;
        mTitle = title;
        mEntries = new ArrayList<FeedEntry>();
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String mLink) {
        this.mLink = mLink;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
    }

    public long getUpdated() {
        return mUpdated;
    }

    public void setUpdated(long mUpdated) {
        this.mUpdated = mUpdated;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getAuthorEmail() {
        return mAuthorEmail;
    }

    public void setAuthorEmail(String mAuthorEmail) {
        this.mAuthorEmail = mAuthorEmail;
    }

    public List<FeedEntry> getEntries() {
        return mEntries;
    }

    public void setEntries(List<FeedEntry> mEntries) {
        this.mEntries = mEntries;
    }

}
