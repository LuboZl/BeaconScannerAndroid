package team1.com.beaconscanner;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import team1.com.beaconscanner.exhibitlist.ExhibitListFragment;

public class MainActivity extends AppCompatActivity implements ExhibitListFragment.OnExhibitListFragmentListener{
    private ExhibitFirebase mExhibitFirebase;
    private BluetoothScanner mBluetoothScanner;
    private String TAG = "MainActivity";
    private BluetoothScanner.BluetoothScannerListener mBluetoothScannerListener;
    private ExhibitFirebase.ExhibitFirebaseListener mExhibitFirebaseListener;
    private FragmentManager.OnBackStackChangedListener mOnBackStackChangedListener;

    public View mFragmentHolder;

    public ArrayList<Exhibit> mExhibits = new ArrayList<>();

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
            }

            @Override
            public void onDeviceFound(BluetoothDevice device) {
                Log.d(TAG, "onDeviceFound");
                mExhibitFirebase.add(new Exhibit(null, "Dell Xps 9560", "Noteboocik", null, device.getAddress()));
            }
        };
    }

    private ExhibitFirebase.ExhibitFirebaseListener getExhibitFirebaseListener() {
        return new ExhibitFirebase.ExhibitFirebaseListener() {
            @Override
            public void onDataChange(ArrayList<Exhibit> exhibits) {
                mExhibits = exhibits;
                ExhibitListFragment exhibitListFragment = (ExhibitListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_holder);

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
                .replace(R.id.fragment_holder, exhibitListFragment, "exhibit_list_fragment")
                .addToBackStack("exhibit_list_fragment")
                .commit();
    }


    public void onBluetoothNotRunning(){
        Log.d(TAG, "onBluetoothNotRunning");
    }
}
