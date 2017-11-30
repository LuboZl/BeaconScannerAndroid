package team1.com.beaconscanner;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import team1.com.beaconscanner.device.BluetoothDevicesListFragment;
import team1.com.beaconscanner.exhibit.Exhibit;
import team1.com.beaconscanner.exhibit.ExhibitListFragment;
import team1.com.beaconscanner.overview.ExhibitOverviewFragment;
import team1.com.beaconscanner.device.MBluetoothDevice;

public class MainActivity extends AppCompatActivity
        implements
        ExhibitListFragment.FragmentListener,
        BluetoothDevicesListFragment.FragmentListener {



    private ExhibitFirebase mExhibitFirebase;
    private BluetoothScanner mBluetoothScanner;
    private String TAG = "MainActivity";
    private String OVERVIEW_FRAGMENT = "OVERVIEW_FRAGMENT";

    private FragmentManager.OnBackStackChangedListener mOnBackStackChangedListener;

    public View mFragmentHolder;

    public ArrayList<Exhibit> mExhibits = new ArrayList<>();
    public ArrayList<Exhibit> mFoundExhibits = new ArrayList<>();
    public ArrayList<MBluetoothDevice> mMBluetoothDevices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentHolder = findViewById(R.id.fragment_holder);

        mOnBackStackChangedListener = getOnBackStackChangedListener();

        mExhibitFirebase = new ExhibitFirebase(getExhibitFirebaseListener());
        mBluetoothScanner = new BluetoothScanner(this, getBluetoothScannerListener());

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddExhibit.class));
            }
        });

        setExhibitOverviewFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BluetoothScanner.REQUEST_ENABLE_BLUETOOTH) {
            switch (resultCode){
                case RESULT_OK:
                    mBluetoothScanner.initBluetoothScanner();
                    break;
                case RESULT_CANCELED:
                    onBluetoothNotRunning();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        mBluetoothScanner.unregisterReceiver();
        super.onDestroy();
    }


    private FragmentManager.OnBackStackChangedListener getOnBackStackChangedListener() {
        return new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

            }
        };
    }

    public void filterFoundExhibits(){
        mFoundExhibits = new ArrayList<>();

        for(MBluetoothDevice device: mMBluetoothDevices){
            for(Exhibit exhibit: mExhibits){
                if(device.getAddress().equals(exhibit.getAddress())){
                    exhibit.setRssi(device.getRssi());
                    mFoundExhibits.add(exhibit);
                }
            }
        }


    }

    private BluetoothScanner.BluetoothScannerListener getBluetoothScannerListener() {
        return new BluetoothScanner.BluetoothScannerListener() {

            @Override
            public void onDeviceNotSupported() {
                Log.d(TAG, "onDeviceNotSupported");
            }

            @Override
            public void onDiscoveryStarted() {
                Log.d(TAG, "onDiscoveryStarted");
            }

            @Override
            public void onDiscoveryFinished() {
                Log.d(TAG, "onDiscoveryFinished");

                ExhibitOverviewFragment exhibitOverviewFragment = (ExhibitOverviewFragment) getSupportFragmentManager().findFragmentByTag(OVERVIEW_FRAGMENT);

                Collections.sort(mFoundExhibits, new RssiComparator());

                if (exhibitOverviewFragment != null) {
                    mMBluetoothDevices = new ArrayList<>(mBluetoothScanner.getDevices());

                    filterFoundExhibits();
                    Collections.sort(mFoundExhibits, new RssiComparator());

                    updateFragmentOverviewData();
                }
            }

            @Override
            public void onDeviceFound(BluetoothDevice device, Intent intent) {
                Log.d(TAG, "onDeviceFound");
            }
        };
    }

    private ExhibitFirebase.ExhibitFirebaseListener getExhibitFirebaseListener() {
        return new ExhibitFirebase.ExhibitFirebaseListener() {
            @Override
            public void onDataChange(ArrayList<Exhibit> exhibits) {
                mExhibits = exhibits;
                updateFragmentOverviewData();
            }

            @Override
            public void onCancelled() {

            }
        };
    }

    private void updateFragmentOverviewData(){
        ExhibitOverviewFragment overviewFragment = (ExhibitOverviewFragment) getSupportFragmentManager().findFragmentByTag(OVERVIEW_FRAGMENT);

        if (overviewFragment != null) {
            overviewFragment.onDataUpdated(mMBluetoothDevices, mFoundExhibits);
        }
    }

    private void setExhibitOverviewFragment() {
        ExhibitOverviewFragment overviewFragment = ExhibitOverviewFragment.newInstance(mExhibits, mMBluetoothDevices);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_holder, overviewFragment, OVERVIEW_FRAGMENT)
                .commit();
    }


    public void onBluetoothNotRunning() {
        Log.d(TAG, "onBluetoothNotRunning");
    }

    @Override
    public ArrayList<Exhibit> getExhibits() {
        return mFoundExhibits;
    }

    @Override
    public void onExhibitItemClick(Exhibit exhibit) {
        Intent intent = new Intent(this, PreviewExhibit.class);
        intent.putExtra("exhibit", exhibit);

        startActivity(intent);
    }

    public Exhibit getExhibit(String adress) {
        for (Exhibit e: mExhibits) {
            if (adress.equals(e.getAddress())) {
                return e;
            }
        }

        return null;
    }

    @Override
    public ArrayList<MBluetoothDevice> getBluetoothDevices() {
        return mMBluetoothDevices;
    }

    @Override
    public void onBluetoothDeviceItemClick(MBluetoothDevice e) {

    }


    public class RssiComparator implements Comparator<Exhibit> {
        @Override
        public int compare(Exhibit obj1, Exhibit obj2) {
            return obj2.getRssi() - obj1.getRssi();

        }
    }
}
