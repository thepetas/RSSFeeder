package cz.cvut.panskpe1.rssfeeder;

import android.content.res.Resources;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by petr on 3/19/16.
 */
public class Article {

    private int id;
    private String title;
    private String urlPage;
    private Date date;
    private String description;
    private static int PREVIEW_LEN = 120;

    public Article(int id, String title, String urlPage, String description) {
            this.id = id;
            date = null;
            this.title = title;
            this.description = description;
            this.urlPage = urlPage;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlPage() {
        return urlPage;
    }

    public void setUrlPage(String urlPage) {
        this.urlPage = urlPage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public final int getId() {
        return id;
    }

    public String getShareString(Resources res) {
        return String.format(res.getString(R.string.share_message_format), title, urlPage);
    }

    public String getPreDescription(){
        return description.substring(0, PREVIEW_LEN ) + "...";
    }


}
