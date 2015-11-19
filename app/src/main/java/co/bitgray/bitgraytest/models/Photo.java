package co.bitgray.bitgraytest.models;

import android.content.Context;
import android.database.Cursor;
import java.util.List;
import co.bitgray.bitgraytest.db.DBPhoto;

/**
 * Created by andrescamacho on 18/11/15.
 */
public class Photo {

    private long id;

    private String title;

    private String resource;

    private float latitude;

    private float longitude;

    private long idAlbum;

    private long date;

    private Context context;

    public Photo(Context context) {
        this.context = context;
    }

    public Photo(Context context, Cursor cursor) {
        this.id = cursor.getLong(DBPhoto.getcIdIndex());
        this.title = cursor.getString(DBPhoto.getcTitleIndex());
        this.resource = cursor.getString(DBPhoto.getcResourceIndex());
        this.latitude = cursor.getFloat(DBPhoto.getcLatitudeIndex());
        this.longitude = cursor.getFloat(DBPhoto.getcLongitudeIndex());
        this.idAlbum = cursor.getLong(DBPhoto.getcIdAlbumIndex());
        this.context = context;
    }

    public List<Photo> getPhotosByAlbum(long idAlbum) {
        DBPhoto dbPhoto = new DBPhoto(context);
        return dbPhoto.getPhotosByAlbum(context, idAlbum);
    }

    public void save() {
        Album album = new Album(context);
        album = album.getAlbumByDate(this.date);

        if (album == null) {
            album = new Album(context);
            album.setDate(this.getDate());
            album.save();
        }
        this.setIdAlbum(album.getId());

        DBPhoto dbPhoto = new DBPhoto(context);
        this.id = dbPhoto.insert(this);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public long getIdAlbum() {
        return idAlbum;
    }

    public String getResource() {
        return resource;
    }

    public long getDate() {
        return date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setIdAlbum(long idAlbum) {
        this.idAlbum = idAlbum;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
