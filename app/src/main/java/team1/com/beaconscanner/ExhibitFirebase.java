package team1.com.beaconscanner;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//TODO: READ LIST by IDS

public class ExhibitFirebase {
    private Context mContext;
    private DatabaseReference mPreviewsRef;

    public ExhibitFirebase(Context mContext) {
        this.mContext = mContext;
        mPreviewsRef = FirebaseDatabase.getInstance().getReference("dev_previews");
    }

    public void add(Exhibit exhibit){
        mPreviewsRef.child(exhibit.getId()).setValue(exhibit);
    }

    public void remove(Exhibit exhibit){
        mPreviewsRef.child(exhibit.getId()).removeValue();
    }

    public void edit(Exhibit exhibit){
        mPreviewsRef.child(exhibit.getId()).setValue(exhibit);
    }
}
