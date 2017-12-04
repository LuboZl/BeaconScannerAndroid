package team1.com.beaconscanner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1.com.beaconscanner.exhibit.Exhibit;

public class ExhibitFirebase {
    private DatabaseReference mPreviewsRef;
    public ExhibitFirebase(final ExhibitFirebaseListener exhibitFirebaseListener) {
        mPreviewsRef = FirebaseDatabase.getInstance().getReference("dev_previews");

        if (exhibitFirebaseListener != null) {
            mPreviewsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Exhibit> exhibits = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        exhibits.add(snapshot.getValue(Exhibit.class));
                    }

                    exhibitFirebaseListener.onDataChange(exhibits);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    exhibitFirebaseListener.onCancelled();
                }
            });
        }
    }

    public void add(Exhibit exhibit) {
        String key = mPreviewsRef.push().getKey();
        exhibit.setRssi(0);
        exhibit.setId(key);

        mPreviewsRef.child(key).setValue(exhibit);
    }

    public void edit(Exhibit exhibit) {
        String key = mPreviewsRef.push().getKey();
        mPreviewsRef.child(exhibit.getId()).setValue(exhibit);
    }

    public void remove(String id) {
        mPreviewsRef.child(id).removeValue();
    }

    interface ExhibitFirebaseListener {
        void onDataChange(ArrayList<Exhibit> exhibits);
        void onCancelled();
    }
}
