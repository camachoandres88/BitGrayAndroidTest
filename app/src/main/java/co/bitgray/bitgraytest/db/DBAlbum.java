package co.bitgray.bitgraytest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import co.bitgray.bitgraytest.models.Album;

/**
 * Created by andrescamacho on 18/11/15.
 */
public class DBAlbum {
    private static final String TAG = DBAlbum.class.getName();
    public static final String TABLE = "album";

    public static final String C_ID = "id";
    public static final String C_DATE = "date";

    public static final int C_ID_INDEX = 0;
    public static final int C_DATE_INDEX = 1;

    private DBHelper dbHelper;
    private SQLiteDatabase dataBase;

    public DBAlbum(Context context) {
        dbHelper = new DBHelper(context);
    }

    public static String createTable() {
        String result = "CREATE TABLE IF NOT EXISTS " + TABLE + " ( "
                + C_ID + " INTEGER PRIMARY KEY, "
                + C_DATE + " INTEGER)";
        return result;
    }

    public void deleteTable() {
        dataBase = dbHelper.getReadableDatabase();
        try {
            String sql = "DELETE FROM " + TABLE;
            dataBase.execSQL(sql);
        } finally {
            dataBase.close();
        }
    }

    public long insert(Album item) {
        long insertedId = 0;
        dataBase = dbHelper.getWritableDatabase();
        dataBase.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.clear();
            values.put(C_DATE, item.getDate());
            insertedId = dataBase.insert(TABLE, null, values);

            dataBase.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e(TAG, "Error insertOrReplace: " + e.getMessage());
        } finally {
            dataBase.endTransaction();
            dataBase.close();
        }
        return insertedId;
    }


    public Album getAlbumByDate(Context context, long date) {

        List<Album> items = new ArrayList<>();
        dataBase = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = dataBase.query(TABLE, null, C_DATE + "= ? ", new String[]{String.valueOf(date)}, null, null, C_ID + " ASC");

            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    Album album = new Album(context, cursor);
                    items.add(album);
                    cursor.moveToNext();
                }
            }
        } finally {
            dataBase.close();
        }
        if (items.size() > 0) {
            return items.get(0);
        } else {
            return null;
        }

    }

    public List<Album> getAllAlbum(Context context) {

        List<Album> items = new ArrayList<>();
        dataBase = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = dataBase.query(TABLE, null, null, null, null, null, C_DATE + " DESC");

            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    Album album = new Album(context, cursor);
                    items.add(album);
                    cursor.moveToNext();
                }
            }
        } finally {
            dataBase.close();
        }
        return items;

    }

    public static int getcIdIndex() {
        return C_ID_INDEX;
    }

    public static int getcDateIndex() {
        return C_DATE_INDEX;
    }
}
