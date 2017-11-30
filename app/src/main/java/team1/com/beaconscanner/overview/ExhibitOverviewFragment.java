package team1.com.beaconscanner.overview;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import team1.com.beaconscanner.R;
import team1.com.beaconscanner.device.BluetoothDevicesListFragment;
import team1.com.beaconscanner.device.MBluetoothDevice;
import team1.com.beaconscanner.exhibit.Exhibit;
import team1.com.beaconscanner.exhibit.ExhibitListFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExhibitOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExhibitOverviewFragment extends Fragment {
    private static String TAG = "ExhibitOverviewFragment";
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private ArrayList <Exhibit> mFoundExhibits = new ArrayList<>();
    private ArrayList <MBluetoothDevice> mMBluetoothDevices = new ArrayList<>();

    public ExhibitOverviewFragment() {
        // Required empty public constructor
    }
    public static ExhibitOverviewFragment newInstance(ArrayList <Exhibit> exhibits,  ArrayList <MBluetoothDevice> mMBluetoothDevices) {
        ExhibitOverviewFragment fragment = new ExhibitOverviewFragment();
        fragment.mFoundExhibits = exhibits;
        fragment.mMBluetoothDevices = mMBluetoothDevices;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        mTabLayout = (TabLayout)view.findViewById(R.id.tabs);
        mViewPager = (ViewPager)view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(new ExhibitOverviewFragmentPageAdapter(getChildFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
        return view;

    }

    public void onDataUpdated(ArrayList<MBluetoothDevice> bluetoothDevices,
                              ArrayList<Exhibit> foundExhibits) {
        Log.d(TAG, "onDataUpdated");

        mMBluetoothDevices = new ArrayList<>(bluetoothDevices);
        mFoundExhibits = new ArrayList<>(foundExhibits);

        updateFragments();
    }

    private void updateFragments(){
//      TOTO UPDATUJE IBA AKTUALNY FRAGMENT
//        switch (mViewPager.getCurrentItem()){
//            case 0:
//                ExhibitListFragment exhibitExhibitListFragment = (ExhibitListFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
//                exhibitExhibitListFragment.onDataUpdated(mFoundExhibits);
//                break;
//            case 1:
//                BluetoothDevicesListFragment bluetoothDevicesListFragment =
//                        (BluetoothDevicesListFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
//                bluetoothDevicesListFragment.onDataUpdated(mMBluetoothDevices);
//
//        }

//      TOTO UPDATUJE OBA FRAGMENTY (asi horsia volba, ale data sa naplnaju aj bez toho aby bolo viditelne)

        ExhibitListFragment exhibitExhibitListFragment = (ExhibitListFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
        exhibitExhibitListFragment.onDataUpdated(mFoundExhibits);

        BluetoothDevicesListFragment bluetoothDevicesListFragment =
                (BluetoothDevicesListFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 1);
        bluetoothDevicesListFragment.onDataUpdated(mMBluetoothDevices);
    }



}
