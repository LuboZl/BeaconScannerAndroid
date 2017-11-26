package team1.com.beaconscanner;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import team1.com.beaconscanner.exhibit.Exhibit;

public class ExhibitFirebase {
    private String TAG = "ExhibitFirebase";
    private DatabaseReference mPreviewsRef;
    private List <Exhibit> exhibits = new ArrayList<>();

    public ExhibitFirebase(final ExhibitFirebaseListener exhibitFirebaseListener) {
        mPreviewsRef = FirebaseDatabase.getInstance().getReference("dev_previews");

        if (exhibitFirebaseListener != null) {
            mPreviewsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange");
                    ArrayList<Exhibit> exhibits = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        exhibits.add(snapshot.getValue(Exhibit.class));
                    }

                    exhibitFirebaseListener.onDataChange(exhibits);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled");
                    exhibitFirebaseListener.onCancelled();
                }
            });
        }
    }

    public void add(Exhibit exhibit) {
        String key = mPreviewsRef.push().getKey();

        exhibit.setId(key);

        mPreviewsRef.child(key).setValue(exhibit);
    }

    public void remove(Exhibit exhibit) {
        mPreviewsRef.child(exhibit.getId()).removeValue();
    }

    public void edit(Exhibit exhibit) {
        mPreviewsRef.child(exhibit.getId()).setValue(exhibit);
    }

    interface ExhibitFirebaseListener {
        void onDataChange(ArrayList<Exhibit> exhibits);
        void onCancelled();
    }
}
