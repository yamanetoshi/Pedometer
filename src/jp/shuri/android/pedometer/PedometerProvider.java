package jp.shuri.android.pedometer;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
//import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class PedometerProvider extends ContentProvider {
    public static final String AUTHORITY = "jp.shuri.android.pedometer";
    private static final String DATABASE_NAME = "pedometer.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TAG = "NotesProvider";
      
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table pedometer (_id integer primary key autoincrement, "
            + "date text not null, count integer);";

    private static class DatabaseHelper extends SQLiteOpenHelper {

            public DatabaseHelper(Context context) {
                    super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            public void onCreate(SQLiteDatabase db) {
                    db.execSQL(DATABASE_CREATE);
            }

            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                                    + newVersion + ", which will destroy all old data");
                    db.execSQL("DROP TABLE IF EXISTS pedometer");
                    onCreate(db);
            }
    }

    /*
    private UriMatcher urlMatcher; 
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;
    */
      
    private SQLiteDatabase db;

    public PedometerProvider() {
    	/*
            urlMatcher = new UriMatcher(UriMatcher.NO_MATCH);
            urlMatcher.addURI(AUTHORITY, "notes", NOTES);
            urlMatcher.addURI(AUTHORITY, "notes/#", NOTES_ID);
            */
    }
      
    @Override
    public boolean onCreate() {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            db = dbHelper.getWritableDatabase();
            return db != null;
    }

    public int delete(Uri url, String where, String[] selectionArgs) {
            int count = db.delete(DBColumns.TABLE, where, selectionArgs);
        getContext().getContentResolver().notifyChange(url, null, true);
        return count;
    }

    public String getType(Uri url) {
    	/*
            switch (urlMatcher.match(url)) {
          case NOTES:
            return DBColumns.CONTENT_TYPE;
          case NOTES_ID:
            return DBColumns.CONTENT_ITEMTYPE;
          default:
            throw new IllegalArgumentException("Unknown URL " + url);
            }
            */
    	return null;
    }

    public Uri insert(Uri url, ContentValues initialValues) {
            ContentValues values;
        if (initialValues != null) {
            values = initialValues;
        } else {
            values = new ContentValues();
        }
        long rowId = db.insert(DBColumns.TABLE, null, values);
        if (rowId >= 0) {
            Uri uri = ContentUris.appendId(DBColumns.CONTENT_URI.buildUpon(), rowId).build();
            getContext().getContentResolver().notifyChange(url, null, true);
            return uri;
        }
        throw new SQLiteException("Failed to insert row into " + url);
    }

    public Cursor query(
                    Uri url, String[] projection, String selection, String[] selectionArgs,
                    String sort) {
            Cursor c = db.query(true, DBColumns.TABLE, 
                            new String[] {DBColumns._ID, DBColumns.KEY_DATE,
                            DBColumns.KEY_COUNT}, selection, null,
                            null, null, null, null);
        c.setNotificationUri(getContext().getContentResolver(), url);
        return c;
    }

    public int update(Uri url, ContentValues values, String where,
                    String[] selectionArgs) {
            int count = db.update(DBColumns.TABLE, values, where, null);
            getContext().getContentResolver().notifyChange(url, null, true);
        return count;
    }

}
