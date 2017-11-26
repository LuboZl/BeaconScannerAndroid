package team1.com.beaconscanner;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import team1.com.beaconscanner.exhibit.Exhibit;
import team1.com.beaconscanner.exhibit.ExhibitListFragment;

public class MainActivity extends AppCompatActivity implements ExhibitListFragment.OnExhibitListFragmentListener{
    private ExhibitFirebase mExhibitFirebase;
    private BluetoothScanner mBluetoothScanner;
    private String TAG = "MainActivity";
    private String EXH_LIST_FRAGMENT_TAG = "EXH_LIST_FRAGMENT_TAG";
    private BluetoothScanner.BluetoothScannerListener mBluetoothScannerListener;
    private ExhibitFirebase.ExhibitFirebaseListener mExhibitFirebaseListener;
    private FragmentManager.OnBackStackChangedListener mOnBackStackChangedListener;

    public View mFragmentHolder;

    public ArrayList<Exhibit> mExhibits = new ArrayList<>();
    public ArrayList<Exhibit> mFoundExhibits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentHolder = findViewById(R.id.fragment_holder);

        mOnBackStackChangedListener = getOnBackStackChangedListener();

        mExhibitFirebaseListener = getExhibitFirebaseListener();
        mExhibitFirebase = new ExhibitFirebase(this, mExhibitFirebaseListener);

        mBluetoothScannerListener = getBluetoothScannerListener();
        mBluetoothScanner = new BluetoothScanner(this, mBluetoothScannerListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddExhibit.class);
                intent.putExtra("exhibit_edit",false);
                startActivity(intent);
//                goToNewExhibitActivity();

            }
        });

        setExhibitListFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == BluetoothScanner.REQUEST_ENABLE_BLUETOOTH){
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


    private FragmentManager.OnBackStackChangedListener getOnBackStackChangedListener(){
        return new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

            }
        };
    }


    private BluetoothScanner.BluetoothScannerListener getBluetoothScannerListener(){
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
                Log.d(TAG,"Found exhibits: "+mFoundExhibits.size());

                // zosortujem na zaklade rssi
                Collections.sort(mFoundExhibits, new RssiComparator());

                ExhibitListFragment exhibitListFragment = (ExhibitListFragment) getSupportFragmentManager().findFragmentByTag(EXH_LIST_FRAGMENT_TAG);
                if(exhibitListFragment != null){
                    exhibitListFragment.onDataUpdated(mFoundExhibits);
                }

                // zmazem zoznam
                mFoundExhibits.clear();
            }

            @Override
            public void onDeviceFound(BluetoothDevice device, Intent intent) {

                Exhibit foundExhibit = getExhibit(device.getAddress());
                if(foundExhibit!=null) {
                    Short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    foundExhibit.setRssi(rssi);
                    mFoundExhibits.add(foundExhibit);
                }else{
                //    mExhibitFirebase.add(new Exhibit(null,device.getName(), "test", "", device.getAddress()));
                }
            }
        };
    }

    private ExhibitFirebase.ExhibitFirebaseListener getExhibitFirebaseListener() {
        return new ExhibitFirebase.ExhibitFirebaseListener() {
            @Override
            public void onDataChange(ArrayList<Exhibit> exhibits) {
                mExhibits = exhibits;
                ExhibitListFragment exhibitListFragment = (ExhibitListFragment) getSupportFragmentManager().findFragmentByTag(EXH_LIST_FRAGMENT_TAG);

                if(exhibitListFragment != null){
                    exhibitListFragment.onDataUpdated(mExhibits);
                }
            }

            @Override
            public void onCancelled() {

            }
        };
    }

    private void setExhibitListFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ExhibitListFragment exhibitListFragment = ExhibitListFragment.newInstance(mExhibits);

        Bundle bundle = new Bundle();
        exhibitListFragment.setArguments(bundle);
        fragmentTransaction
                .replace(R.id.fragment_holder, exhibitListFragment, EXH_LIST_FRAGMENT_TAG)
                .addToBackStack(EXH_LIST_FRAGMENT_TAG)
                .commit();
    }


    public void onBluetoothNotRunning(){
        Log.d(TAG, "onBluetoothNotRunning");
    }

    @Override

    public void onExhibitItemClick(Exhibit exhibit) {
    //    TODO: CEZ FRAGMENT
        Intent intent = new Intent(this, PreviewExhibit.class);
        intent.putExtra("exhibit", exhibit);
        startActivity(intent);
    }

    public Exhibit getExhibit(String adress) {

        for(Exhibit e: mExhibits){
            if(adress.equals(e.getAddress())){
                return e;
            }
        }
        return null;
    }

    public class RssiComparator implements Comparator<Exhibit> {

        @Override
        public int compare(Exhibit obj1, Exhibit obj2) {
            return obj2.getRssi() - obj1.getRssi();

        }
    }

}
