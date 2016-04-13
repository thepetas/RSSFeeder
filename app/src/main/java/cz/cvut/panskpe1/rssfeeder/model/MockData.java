package cz.cvut.panskpe1.rssfeeder.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MockData {
    private static List<Feed> sFeeds = new ArrayList<Feed>();
    private static Map<String, Feed> sIdToFeedMap = new HashMap<String, Feed>();

    static {
        Feed feed = new Feed("tag:blogger.com,1999:blog-6755709643044947179",
                "http://android-developers.blogspot.com/feeds/posts/default", "Android Developers Blog");
        feed.setSubtitle("An Open Handset Alliance Project.");
        feed.setUpdated(System.currentTimeMillis());
        feed.setAuthor("Emily Wood");
        feed.setAuthorEmail("noreply@blogger.com");
        sFeeds.add(feed);
        sIdToFeedMap.put(feed.getId(), feed);

        FeedEntry entry = new FeedEntry(feed, "Lorem ipsum dolor", "id 1",
                "http://android-developers.blogspot.com/some-article");
        entry.setSummary("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        entry.setContent(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam mollis imperdiet " +
                        "placerat. Morbi molestie lorem vitae nisi interdum, in lobortis tortor commodo. " +
                        "Maecenas et odio id sem vulputate adipiscing. " +
                        "<br/><br/>" +
                        "Nulla sed tempor magna. Aenean ut " +
                        "tortor a lorem elementum varius. Nam cursus odio vitae imperdiet semper. Vestibulum " +
                        "vitae quam dapibus mauris dignissim lobortis. Nunc vulputate neque eget libero " +
                        "elementum ultricies.");
        entry.setUpdated(System.currentTimeMillis());
        feed.getEntries().add(entry);

        entry = new FeedEntry(feed, "Praesent at eros ut est accumsan venenatis auctor pharetra", "id 2",
                "http://android-developers.blogspot.com/some-article2");
        entry.setContent(
                "Praesent at eros ut est accumsan venenatis. Mauris auctor pharetra magna, " +
                        "ac porta odio rutrum non. Maecenas ullamcorper adipiscing pulvinar. Morbi nec " +
                        "suscipit libero, sed vulputate lorem. Nullam sed diam id enim dignissim sollicitudin" +
                        " vel quis lorem. Sed a augue pharetra, mattis leo vel, consequat orci. Ut eu luctus " +
                        "quam, vitae tincidunt sapien. Vestibulum et scelerisque urna. " +
                        "<br/><br/>" +
                        "Proin bibendum, " +
                        "augue ut porta iaculis, nunc urna ultricies tortor, ut dapibus mauris lacus sed arcu" +
                        ". Quisque in nibh ut tortor facilisis malesuada. Nullam posuere in urna nec bibendum" +
                        ". Pellentesque iaculis nunc tellus, luctus mattis odio adipiscing nec. Quisque quis " +
                        "scelerisque purus. Suspendisse ac enim faucibus orci pellentesque euismod quis nec " +
                        "sapien.");
        entry.setUpdated(System.currentTimeMillis());
        feed.getEntries().add(entry);
    }

    /**
     * Gets the list of Feeds.
     *
     * @return The list of Feeds.
     */
    public static List<Feed> getFeeds() {
        return Collections.unmodifiableList(sFeeds);
    }

    /**
     * Gets the Feed.
     *
     * @param id The Feed's id.
     * @return The Feed with the given id or null if not found.
     */
    public static Feed getFeed(String id) {
        return sIdToFeedMap.get(id);
    }

    /**
     * Gets the list of all entries, order by the 'updated' time.
     *
     * @return The list of all entries.
     */
    public static List<FeedEntry> getAllEntries() {
        List<FeedEntry> entries = new ArrayList<FeedEntry>();
        for (Feed feed : sFeeds) {
            entries.addAll(feed.getEntries());
        }

        Collections.sort(entries, new Comparator<FeedEntry>() {
            @Override
            public int compare(FeedEntry feedEntry, FeedEntry feedEntry2) {
                return (int) ((feedEntry.getUpdated() - feedEntry2.getUpdated()) / 1000);
            }
        });

        return entries;
    }
}
