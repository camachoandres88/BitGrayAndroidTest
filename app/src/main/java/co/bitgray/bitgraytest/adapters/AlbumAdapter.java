package co.bitgray.bitgraytest.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.internal.Util;

import java.text.ParseException;
import java.util.List;

import co.bitgray.bitgraytest.R;
import co.bitgray.bitgraytest.models.Album;
import co.bitgray.bitgraytest.ui.ExtendedGridView;
import co.bitgray.bitgraytest.utils.GeneralUtils;

/**
 * Created by andrescamacho on 18/11/15.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private static final String TAG = AlbumAdapter.class.getName().toString();
    private static List<Album> albums;
    private int itemLayout;
    private static Context parentActivity;


    public AlbumAdapter(Context context, List<Album> albums, int itemLayout) {
        this.albums = albums;
        this.itemLayout = itemLayout;
        this.parentActivity = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    public static Long getIDFromIndex(int Index) {
        return albums.get(Index).getId();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Album album = albums.get(position);

        try {
            holder.albumNameItem.setText(GeneralUtils.dateToString(GeneralUtils.millisecondsToDate(album.getDate())));
            holder.albumGridViewItem.setAdapter(new GridAlbumAdapter(parentActivity, R.layout.item_grid_album, album.getPhotos()));
            holder.albumGridViewItem.setExpanded(true);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return albums.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView albumNameItem;
        public ExtendedGridView albumGridViewItem;

        public ViewHolder(View itemView) {
            super(itemView);
            albumNameItem = (TextView) itemView.findViewById(R.id.albumNameItem);
            albumGridViewItem = (ExtendedGridView) itemView.findViewById(R.id.albumGridViewItem);
        }
    }
}
