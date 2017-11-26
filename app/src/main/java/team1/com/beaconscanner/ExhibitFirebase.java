package team1.com.beaconscanner;

import android.content.Context;
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
    private Context mContext;
    private DatabaseReference mPreviewsRef;
    private ExhibitFirebaseListener mExhibitFirebaseListener;
    private List <Exhibit> exhibits = new ArrayList<>();

    public ExhibitFirebase(Context mContext, final ExhibitFirebaseListener exhibitFirebaseListener) {
        this.mContext = mContext;
        this.mExhibitFirebaseListener = exhibitFirebaseListener;

        mPreviewsRef = FirebaseDatabase.getInstance().getReference("dev_previews");
        mPreviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange");
                ArrayList<Exhibit> exhibits = new ArrayList<>();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    exhibits.add(snapshot.getValue(Exhibit.class));
                }

                mExhibitFirebaseListener.onDataChange(exhibits);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
                mExhibitFirebaseListener.onCancelled();
            }
        });
    }

    public void add(Exhibit exhibit){
        if(exhibit.getId() == null){
            exhibit.setId(mPreviewsRef.push().getKey());
        }
        mPreviewsRef.child(exhibit.getId()).setValue(exhibit);
    }

    public void remove(Exhibit exhibit){
        mPreviewsRef.child(exhibit.getId()).removeValue();
    }

    public void edit(Exhibit exhibit){
        mPreviewsRef.child(exhibit.getId()).setValue(exhibit);
    }



        interface ExhibitFirebaseListener{
        void onDataChange(ArrayList<Exhibit> exhibits);
        void onCancelled();
    }
}
