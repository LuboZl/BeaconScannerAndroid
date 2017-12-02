package team1.com.beaconscanner.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.BottomNavigationView;

import java.util.ArrayList;

import team1.com.beaconscanner.R;
import team1.com.beaconscanner.device.BluetoothDevicesListFragment;
import team1.com.beaconscanner.device.MBluetoothDevice;
import team1.com.beaconscanner.exhibit.Exhibit;
import team1.com.beaconscanner.exhibit.ExhibitListFragment;

public class ExhibitOverviewFragment extends Fragment {
    BottomNavigationView mBottomNavigationView;
    String BLUETOOTH_DEVICES_FRAGMENT = "BLUETOOTH_DEVICES_FRAGMENT";
    String FOUND_EXHIBIT_FRAGMENT = "FOUND_EXHIBIT_FRAGMENT";
    String ALL_EXHIBITS_FRAGMENT = "ALL_EXHIBITS_FRAGMENT";

    private ArrayList<Exhibit> mFoundExhibits = new ArrayList<>();
    private ArrayList<Exhibit> mAllExhibits = new ArrayList<>();
    private ArrayList<MBluetoothDevice> mMBluetoothDevices = new ArrayList<>();
    private Fragment mCurrentFragment;
    private FragmentTransaction ft;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    public ExhibitOverviewFragment() {
        // Required empty public constructor
    }

    public static ExhibitOverviewFragment newInstance(ArrayList<Exhibit> foundExhibits, ArrayList<MBluetoothDevice> mMBluetoothDevices, ArrayList<Exhibit> allExhibits) {
        ExhibitOverviewFragment fragment = new ExhibitOverviewFragment();
        fragment.mFoundExhibits = foundExhibits;
        fragment.mMBluetoothDevices = mMBluetoothDevices;
        fragment.mAllExhibits = allExhibits;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        mBottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);

        mCurrentFragment = ExhibitListFragment.newInstance(mFoundExhibits);
        setNewFragment(FOUND_EXHIBIT_FRAGMENT);

        mOnNavigationItemSelectedListener = getOnNavigationItemSelectedListener();
        mBottomNavigationView.setSelectedItemId(R.id.menu_found_exhibits);

        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        return view;
    }

    public BottomNavigationView.OnNavigationItemSelectedListener getOnNavigationItemSelectedListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (mBottomNavigationView.getSelectedItemId() == item.getItemId()) {
                    return true;
                }

                switch (item.getItemId()) {
                    case R.id.menu_found_exhibits:
                        mCurrentFragment = ExhibitListFragment.newInstance(mFoundExhibits);
                        setNewFragment(FOUND_EXHIBIT_FRAGMENT);
                        return true;

                    case R.id.menu_bluetooth:
                        mCurrentFragment = BluetoothDevicesListFragment.newInstance();
                        setNewFragment(BLUETOOTH_DEVICES_FRAGMENT);
                        return true;

                    case R.id.menu_all_exhibits:
                        mCurrentFragment = ExhibitListFragment.newInstance(mAllExhibits);
                        setNewFragment(ALL_EXHIBITS_FRAGMENT);
                        return true;

                }
                return true;
            }

        };
    }

    private void setNewFragment(String TAG) {
        ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_overview_content, mCurrentFragment, TAG);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void onDataUpdated(ArrayList<Exhibit> foundExhibits, ArrayList<MBluetoothDevice> bluetoothDevices, ArrayList<Exhibit> allExhibits) {
        mFoundExhibits = new ArrayList<>(foundExhibits);
        mMBluetoothDevices = new ArrayList<>(bluetoothDevices);
        mAllExhibits = new ArrayList<>(allExhibits);

        updateFragments();
    }

    private void updateFragments() {
        ExhibitListFragment foundExhibitListFragment = (ExhibitListFragment) getChildFragmentManager().findFragmentByTag(FOUND_EXHIBIT_FRAGMENT);
        if (foundExhibitListFragment != null) {
            foundExhibitListFragment.onDataUpdated(mFoundExhibits);
        }

        BluetoothDevicesListFragment bluetoothDevicesListFragment = (BluetoothDevicesListFragment) getChildFragmentManager().findFragmentByTag(BLUETOOTH_DEVICES_FRAGMENT);
        if (bluetoothDevicesListFragment != null) {
            bluetoothDevicesListFragment.onDataUpdated(mMBluetoothDevices);
        }

        ExhibitListFragment allExhibitListFragment = (ExhibitListFragment) getChildFragmentManager().findFragmentByTag(ALL_EXHIBITS_FRAGMENT);
        if (allExhibitListFragment != null) {
            allExhibitListFragment.onDataUpdated(mAllExhibits);
        }
    }
}
