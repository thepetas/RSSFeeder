package cz.cvut.panskpe1.rssfeeder.data;

import static cz.cvut.panskpe1.rssfeeder.data.DbConstants.*;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by petr on 4/12/16.
 */
public class ContentProvider extends android.content.ContentProvider {

    private DatabaseHelper mDatabaseHelper;

    public static final String AUTHORITY = "cz.cvut.panskpe1.rssfeeder";

    private static final int FEED_LIST = 1;
    private static final int FEED_ID = 2;
    private static final int ARTICLE_LIST = 3;
    private static final int ARTICLE_ID = 4;

    private static final String BASE_PATH_FEED = "feeds";
    private static final String BASE_PATH_ARTICLE = "articles";

    public static final Uri CONTENT_URI_FEED = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_FEED);
    public static final Uri CONTENT_URI_ARTICLE = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_ARTICLE);
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_ARTICLE, ARTICLE_LIST);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_ARTICLE + "/#", ARTICLE_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_FEED, FEED_LIST);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_FEED + "/#", FEED_ID);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case ARTICLE_LIST:
                queryBuilder.setTables(ArticleTable.TABLE_ARTICLE);
                break;
            case ARTICLE_ID:
                queryBuilder.setTables(ArticleTable.TABLE_ARTICLE);
                queryBuilder.appendWhere(ID + "=" + uri.getLastPathSegment());
                break;
            case FEED_LIST:
                queryBuilder.setTables(FeedTable.TABLE_FEED);
                break;
            case FEED_ID:
                queryBuilder.setTables(FeedTable.TABLE_FEED);
                queryBuilder.appendWhere(ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) throws SQLiteConstraintException {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDatabaseHelper.getWritableDatabase();
        String path;
        long id = 0;
        switch (uriType) {
            case ARTICLE_LIST:
                id = sqlDB.insertOrThrow(ArticleTable.TABLE_ARTICLE, null, values);
                path = BASE_PATH_ARTICLE;
                break;
            case FEED_LIST:
                id = sqlDB.insertOrThrow(FeedTable.TABLE_FEED, null, values);
                path = BASE_PATH_FEED;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(path + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDatabaseHelper.getWritableDatabase();
        int rowsDeleted = 0;
        String id;
        switch (uriType) {
            case ARTICLE_LIST:
                rowsDeleted = sqlDB.delete(ArticleTable.TABLE_ARTICLE, selection, selectionArgs);
                break;
            case ARTICLE_ID:
                id = uri.getLastPathSegment();
                rowsDeleted = sqlDB.delete(ArticleTable.TABLE_ARTICLE, ID + "=" + id, null);
                break;
            case FEED_LIST:
                rowsDeleted = sqlDB.delete(FeedTable.TABLE_FEED, selection, selectionArgs);
                break;
            case FEED_ID:
                id = uri.getLastPathSegment();
                rowsDeleted = sqlDB.delete(FeedTable.TABLE_FEED, ID + "=" + id, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDatabaseHelper.getWritableDatabase();
        int rowsUpdated = 0;
        String id;
        switch (uriType) {
            case ARTICLE_LIST:
                rowsUpdated = sqlDB.update(ArticleTable.TABLE_ARTICLE, values, selection, selectionArgs);
                break;
            case ARTICLE_ID:
                id = uri.getLastPathSegment();
                rowsUpdated = sqlDB.update(ArticleTable.TABLE_ARTICLE, values, selection, selectionArgs);
                break;
            case FEED_LIST:
                rowsUpdated = sqlDB.update(FeedTable.TABLE_FEED, values, selection, selectionArgs);
                break;
            case FEED_ID:
                id = uri.getLastPathSegment();
                rowsUpdated = sqlDB.update(FeedTable.TABLE_FEED, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;

    }

}
