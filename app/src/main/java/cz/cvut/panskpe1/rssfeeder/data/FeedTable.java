package cz.cvut.panskpe1.rssfeeder.data;

import android.database.sqlite.SQLiteDatabase;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.*;

/**
 * Created by petr on 4/12/16.
 */
public class FeedTable {

    public static final String TABLE_FEED = "feedTable";

    private static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_FEED
            + "("
            + ID + " integer primary key autoincrement, "
//            + TITLE + " text not null, "
            + TITLE + " text null, "
            + SUBTITLE + " text null, "
            + LINK + " text not null unique, "
            + UPDATED + " text null, "
            + AUTHOR + " text null, "
            + AUTHOR_EMAIL + " text null "
            + ");";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAndCreateTable(db);
    }

    public static void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAndCreateTable(db);
    }

    public static void dropAndCreateTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEED);
        onCreate(db);
    }
}
