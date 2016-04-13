package cz.cvut.panskpe1.rssfeeder.model;

/**
 * Created by petr on 4/12/16.
 */
public class FeedEntry {

    private Feed mFeed;
    private String mTitle;
    private String mId;
    private String mLink;
    private long mUpdated;
    private String mSummary;
    private String mContent;
    private String mAuthor;

    public FeedEntry(Feed feed, String title, String id, String link) {
        mFeed = feed;
        mTitle = title;
        mId = id;
        mLink = link;
    }

    public Feed getFeed() {
        return mFeed;
    }

    public void setFeed(Feed mFeed) {
        this.mFeed = mFeed;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
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

    public long getUpdated() {
        return mUpdated;
    }

    public void setUpdated(long mUpdated) {
        this.mUpdated = mUpdated;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }
}
