package cz.cvut.panskpe1.rssfeeder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by petr on 4/12/16.
 */
public class RssFeederDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rssfeeder.db";
    public static final int DATABASE_VERSION = 1;

    public RssFeederDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FeedTable.onCreate(db);
        ArticleTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        FeedTable.onUpgrade(db, oldVersion, newVersion);
        ArticleTable.onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        FeedTable.onDowngrade(db, oldVersion, newVersion);
        ArticleTable.onDowngrade(db, oldVersion, newVersion);
    }
}
