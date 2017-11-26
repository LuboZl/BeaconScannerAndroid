package team1.com.beaconscanner.exhibit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import team1.com.beaconscanner.R;

/**
 * Created by jan on 11/25/17.
 */

public class ExhibitListFragmentAdapter extends ArrayAdapter<Exhibit> {
    Context context;
    int layoutResourceId;
    ArrayList<Exhibit> mExhibits;

    public ExhibitListFragmentAdapter(Context context, int resource, ArrayList<Exhibit> exhibits) {
        super(context, resource, exhibits);
        this.layoutResourceId = resource;
        this.context = context;
        this.mExhibits = exhibits;
    }

    public void updateData(ArrayList<Exhibit> exhibits){
        mExhibits.clear();
        mExhibits.addAll(exhibits);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ExhibitHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ExhibitHolder();
            holder.exhibit_title = (TextView) row.findViewById(R.id.fragment_exhibit_row_title);
            holder.exhibit_about = (TextView) row.findViewById(R.id.fragment_exhibit_row_about);
            holder.exhibit_distance = (TextView) row.findViewById(R.id.fragment_exhibit_row_distance);

            row.setTag(holder);
        }
        else
        {
            holder = (ExhibitHolder) row.getTag();
        }

        Exhibit exhibit = mExhibits.get(position);

        holder.exhibit_title.setText(exhibit.getTitle());
        holder.exhibit_about.setText(exhibit.getAbout());
        holder.exhibit_distance.setText("0");

        return row;
    }



    private class ExhibitHolder {
        public TextView exhibit_title;
        public TextView exhibit_about;
        public ImageView exhibit_image;
        public TextView exhibit_distance;
    }



}
