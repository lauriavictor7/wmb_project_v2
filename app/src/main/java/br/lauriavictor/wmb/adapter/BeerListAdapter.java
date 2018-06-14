package br.lauriavictor.wmb.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.model.Beer;

public class BeerListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Beer> beers;

    public BeerListAdapter(Context context, int layout, ArrayList<Beer> beers) {
        this.context = context;
        this.layout = layout;
        this.beers = beers;
    }

    @Override
    public int getCount() {
        return beers.size();
    }

    @Override
    public Object getItem(int position) {
        return beers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textViewName, textViewNote, textViewType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.textViewName = row.findViewById(R.id.textViewName);
            holder.textViewNote = row.findViewById(R.id.textViewNote);
            holder.textViewType = row.findViewById(R.id.textViewType);
            holder.imageView = row.findViewById(R.id.imgIcon);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Beer beer = beers.get(position);
        holder.textViewName.setText(beer.getName());
        holder.textViewNote.setText(beer.getNote());
        holder.textViewType.setText(beer.getType());

        byte[] beerImage = beer.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(beerImage, 0, beerImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
