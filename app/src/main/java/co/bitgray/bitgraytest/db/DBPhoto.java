package co.bitgray.bitgraytest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import co.bitgray.bitgraytest.models.Photo;

/**
 * Created by andrescamacho on 18/11/15.
 */
public class DBPhoto {
    private static final String TAG = DBPhoto.class.getName();
    public static final String TABLE = "photo";

    public static final String C_ID = "id";
    public static final String C_TITLE = "title";
    public static final String C_RESOURCE = "resource";
    public static final String C_LATITUDE = "latitude";
    public static final String C_LONGITUDE = "longitude";
    public static final String C_RESOURCE_DATA = "resourceData";
    public static final String C_ID_ALBUM = "idAlbum";

    public static final int C_ID_INDEX = 0;
    public static final int C_TITLE_INDEX = 1;
    public static final int C_RESOURCE_INDEX = 2;
    public static final int C_LATITUDE_INDEX = 3;
    public static final int C_LONGITUDE_INDEX = 4;
    public static final int C_RESOURCE_DATA_INDEX = 5;
    public static final int C_ID_ALBUM_INDEX = 6;

    private DBHelper dbHelper;
    private SQLiteDatabase dataBase;

    public DBPhoto(Context context) {
        dbHelper = new DBHelper(context);
    }

    public static String createTable() {
        String result = "CREATE TABLE IF NOT EXISTS " + TABLE + " ( "
                + C_ID + " INTEGER PRIMARY KEY, "
                + C_TITLE + " TEXT ,"
                + C_RESOURCE + " TEXT ,"
                + C_LATITUDE + " REAL ,"
                + C_LONGITUDE + " REAL ,"
                + C_RESOURCE_DATA + " BLOB ,"
                + C_ID_ALBUM + " INTEGER )";
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

    public long insert(Photo item) {
        long insertedId = 0;
        dataBase = dbHelper.getWritableDatabase();
        dataBase.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.clear();
            values.put(C_TITLE, item.getTitle());
            values.put(C_RESOURCE, item.getResource());
            values.put(C_LATITUDE, item.getLatitude());
            values.put(C_LONGITUDE, item.getLongitude());
            values.put(C_RESOURCE_DATA, item.getResourceData());
            values.put(C_ID_ALBUM, item.getIdAlbum());
            insertedId= dataBase.insert(TABLE, null, values);

            dataBase.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e(TAG, "Error insertOrReplace: " + e.getMessage());
        } finally {
            dataBase.endTransaction();
            dataBase.close();
        }
        return insertedId;
    }

    public List<Photo> getPhotosByAlbum(Context context, long idAlbum) {

        List<Photo> items = new ArrayList<>();
        dataBase = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = dataBase.query(TABLE, null, C_ID_ALBUM + "= ? ", new String[]{String.valueOf(idAlbum)}, null, null, C_ID + " DESC");

            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    Photo photo = new Photo(context, cursor);
                    items.add(photo);
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

    public static int getcTitleIndex() {
        return C_TITLE_INDEX;
    }

    public static int getcLatitudeIndex() {
        return C_LATITUDE_INDEX;
    }

    public static int getcLongitudeIndex() {
        return C_LONGITUDE_INDEX;
    }

    public static int getcIdAlbumIndex() {
        return C_ID_ALBUM_INDEX;
    }

    public static int getcResourceIndex() {
        return C_RESOURCE_INDEX;
    }

    public static int getcResourceDataIndex() {
        return C_RESOURCE_DATA_INDEX;
    }
}
