package co.bitgray.bitgraytest.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.bitgray.bitgraytest.R;
import co.bitgray.bitgraytest.models.Photo;

/**
 * Created by andrescamacho on 18/11/15.
 */
public class GridAlbumAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<Photo> data = new ArrayList();

    public GridAlbumAdapter(Context context, int layoutResourceId, List<Photo> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Photo item = data.get(position);
        holder.imageTitle.setText(item.getTitle());
        //holder.image.setImageBitmap(imageBitmap);
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}