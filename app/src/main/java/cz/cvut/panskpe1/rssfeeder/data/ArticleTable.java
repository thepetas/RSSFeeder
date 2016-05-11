package cz.cvut.panskpe1.rssfeeder.data;

import android.database.sqlite.SQLiteDatabase;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.*;

/**
 * Created by petr on 4/12/16.
 */
public class ArticleTable {

    public static final String TABLE_ARTICLE = "articleTable";

    private static final String TABLE_CREATE = "create table "
            + TABLE_ARTICLE
            + " ("
            + ID + " integer primary key autoincrement, "
            + TITLE + " text not null, "
            + SUMMARY + " text null, "
            + CONTENT + " text not null, "
            + LINK + " text not null unique, "
            + UPDATED + " integer not null, "
            + AUTHOR + " text null, "
            + FEED_ID + " integer"
//            + "FOREIGN KEY ( " + FEED_ID + " ) REFERENCES " + FeedTable.TABLE_FEED + "( " + FEED_ID + ") "
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
        onCreate(db);
    }
}
