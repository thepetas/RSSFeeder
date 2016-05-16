package cz.cvut.panskpe1.rssfeeder.data;

/**
 * Created by petr on 4/12/16.
 */
public class DbConstants {

    private DbConstants() {
        throw new AssertionError();
    }

    public static final String ID = "_id";
    public static final String LINK = "link";
    public static final String TITLE = "title";
    public static final String SUBTITLE = "subtitle";
    public static final String UPDATED = "updated";
    public static final String AUTHOR = "author";
    public static final String AUTHOR_EMAIL = "email";

    public static final String SUMMARY = "summary";
    public static final String CONTENT = "content";
    public static final String FEED_ID = "feed_id";
}
