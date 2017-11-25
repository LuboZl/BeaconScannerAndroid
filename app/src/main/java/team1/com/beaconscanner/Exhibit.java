package team1.com.beaconscanner;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Exhibit {
    String id;
    String title;
    String about;
    String image;

    public Exhibit() {
    }

    public Exhibit(String id, String title, String about, String image) {
        this.id = id;
        this.title = title;
        this.about = about;
        this.image = image;
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


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
