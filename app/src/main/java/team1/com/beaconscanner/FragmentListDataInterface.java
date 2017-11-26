package team1.com.beaconscanner;

import java.util.ArrayList;

public interface FragmentListDataInterface <E> {
    void onDataUpdated(ArrayList<E> list);
}
