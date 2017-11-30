package team1.com.beaconscanner.overview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import team1.com.beaconscanner.device.BluetoothDevicesListFragment;
import team1.com.beaconscanner.exhibit.ExhibitListFragment;

public class ExhibitOverviewFragmentPageAdapter extends FragmentPagerAdapter {
    private static final String TAG = ExhibitOverviewFragmentPageAdapter.class.getSimpleName();
    private static final int FRAGMENT_COUNT = 2;
    private FragmentManager mFragmentManager;

    public ExhibitOverviewFragmentPageAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new ExhibitListFragment();
            case 1:
                return new BluetoothDevicesListFragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Exponáty v okolí";
            case 1:
                return "Bluetooth zariadenia v okolí";
        }
        return null;
    }
}