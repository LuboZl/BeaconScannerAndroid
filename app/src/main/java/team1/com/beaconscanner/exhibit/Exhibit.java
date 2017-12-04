package team1.com.beaconscanner.exhibit;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;

import team1.com.beaconscanner.MainActivity;
import team1.com.beaconscanner.R;

public class Exhibit implements Parcelable {
    private String id;
    private String title;
    private String about;
    private String imagePath;
    private String address;
    private int rssi;

    public Exhibit() {
    }

    public Exhibit(String id, String title, String about, String imagePath, String address) {
        this.id = id;
        this.title = title;
        this.about = about;
        this.imagePath = imagePath;
        this.address = address;
        this.rssi = -100;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int distance) {
        this.rssi = distance;
    }

    public String getDistanceInfo(Context context) {
        if (this.rssi == 0) return context.getString(R.string.distance_unavailable);
        else if (-this.rssi <= 50) return context.getString(R.string.distance_very_close);
        else if (-this.rssi <= 65) return context.getString(R.string.distance_close);
        else if (-this.rssi <= 80) return context.getString(R.string.distance_nearby);
        else return context.getString(R.string.distance_away);
    }

    public int getDistanceColor(Context context) {
        if (this.rssi == 0) return ContextCompat.getColor(context, R.color.distance_unavailable);
        else if (-this.rssi <= 50) return ContextCompat.getColor(context, R.color.distance_very_close);
        else if (-this.rssi <= 65) return ContextCompat.getColor(context, R.color.distance_close);
        else if (-this.rssi <= 80) return ContextCompat.getColor(context, R.color.distance_nearby);
        else return ContextCompat.getColor(context, R.color.distance_away);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Exhibit(Parcel in) {
        String[] data = new String[5];
        in.readStringArray(data);

        this.setId(data[0]);
        this.setTitle(data[1]);
        this.setAbout(data[2]);
        this.setImagePath(data[3]);
        this.setAddress(data[4]);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.getId(),
                this.getTitle(),
                this.getAbout(),
                this.getImagePath(),
                this.getAddress()
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Exhibit createFromParcel(Parcel in) {
            return new Exhibit(in);
        }

        public Exhibit[] newArray(int size) {
            return new Exhibit[size];
        }
    };

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Exhibit(
                this.getId(), this.getTitle(), this.getAbout(), this.getImagePath(), this.getAddress()
        );
    }
}
