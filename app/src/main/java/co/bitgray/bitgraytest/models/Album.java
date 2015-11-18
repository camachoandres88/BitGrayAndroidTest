package co.bitgray.bitgraytest.models;

import android.content.Context;
import android.database.Cursor;

import java.util.List;

import co.bitgray.bitgraytest.db.DBAlbum;

/**
 * Created by andrescamacho on 18/11/15.
 */
public class Album {

    private long id;
    private long date;
    private List<Photo> photos;
    private Context context;

    public Album(Context context) {
        this.context = context;
    }

    public Album(Context context, Cursor cursor) {
        this.id = cursor.getLong(DBAlbum.getcIdIndex());
        this.date = cursor.getLong(DBAlbum.getcDateIndex());
        this.context = context;
    }

    public List<Album> getAllAlbum() {
        DBAlbum dbAlbum = new DBAlbum(context);
        Photo photo = new Photo(context);
        List<Album> albums =  dbAlbum.getAllAlbum(context);

        for(int i = 0 ; i<albums.size();i++){
            albums.get(i).photos = photo.getPhotosByAlbum(albums.get(i).getId());
        }
        return albums;
    }

    public Album getAlbumByDate(long date) {
        DBAlbum dbAlbum = new DBAlbum(context);
        return dbAlbum.getAlbumByDate(context, date);
    }

    public void save(){
        DBAlbum dbAlbum = new DBAlbum(context);
        this.id = dbAlbum.insert(this);
    }

    public long getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
