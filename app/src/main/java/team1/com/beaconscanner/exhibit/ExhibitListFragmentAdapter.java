package team1.com.beaconscanner.exhibit;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import team1.com.beaconscanner.R;

public class ExhibitListFragmentAdapter extends ArrayAdapter<Exhibit> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Exhibit> mExhibits;

    public ExhibitListFragmentAdapter(Context context, int resource, ArrayList<Exhibit> exhibits) {
        super(context, resource, exhibits);
        this.layoutResourceId = resource;
        this.context = context;
        this.mExhibits = exhibits;
    }

    public void updateData(ArrayList<Exhibit> exhibits) {
        mExhibits.clear();
        mExhibits.addAll(exhibits);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ExhibitHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ExhibitHolder();
            holder.exhibit_title = (TextView) row.findViewById(R.id.fragment_exhibit_row_title);
            holder.exhibit_about = (TextView) row.findViewById(R.id.fragment_exhibit_row_about);
            holder.exhibit_image = (ImageView) row.findViewById(R.id.fragment_exhibit_image);
            holder.exhibit_distance = (TextView) row.findViewById(R.id.fragment_exhibit_row_distance);

            row.setTag(holder);
        } else {
            holder = (ExhibitHolder) row.getTag();
        }

        Exhibit exhibit = mExhibits.get(position);

        holder.exhibit_title.setText(exhibit.getTitle());
        holder.exhibit_about.setText(exhibit.getAbout());

        Picasso.with(context)
                .load(Uri.parse(exhibit.getImagePath()))
                .resize(200, 200)
                .centerCrop()
                .into(holder.exhibit_image);

        holder.exhibit_distance.setText(exhibit.getDistanceInfo(context));

        return row;
    }

    private class ExhibitHolder {
        public TextView exhibit_title;
        public TextView exhibit_about;
        public ImageView exhibit_image;
        public TextView exhibit_distance;
    }
}
