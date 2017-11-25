package team1.com.beaconscanner;

import android.*;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class BluetoothScanner {

    private Context mContext;
    private BroadcastReceiver mReceiver;
    private BluetoothAdapter mBluetoothAdapter;
    private static String TAG = "BluetoothScanner";
    public static int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    public static int REQUEST_ENABLE_BLUETOOTH = 100;



    public BluetoothScanner(Context context) {
        mContext = context;

        initBroadcastReceiver();
        requestPermissions();
        initBluetoothScanner();
    }

    public void initBluetoothScanner(){
        Log.d(TAG, "initBluetoothScanner");

        getBluetoothAdapter();
        registerReceiver();
        startDiscovery();
    }

    public void initBroadcastReceiver(){
        Log.d(TAG, "initBroadcastReceiver");

        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                BluetoothScannerListener bluetoothScannerListener = (BluetoothScannerListener) mContext;
                String action = intent.getAction();

                if (action == null) {
                    return;
                }

                switch (action) {
                    case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                        ((BluetoothScannerListener) mContext).onDiscoveryStarted();
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        ((BluetoothScannerListener) mContext).onDiscoveryFinished();
                        startDiscovery();
                        break;
                    case BluetoothDevice.ACTION_FOUND:
                        BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if (device != null) {
                            ((BluetoothScannerListener) mContext).onDeviceFound(device);
                        }
                }
            }
        };
    }

    public void requestPermissions(){
        Log.d(TAG, "requestPermissions");

        ActivityCompat.requestPermissions( (AppCompatActivity) mContext,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
    }
    
    public void getBluetoothAdapter(){
        Log.d(TAG, "getBluetoothAdapter");

        if (!isEmulator()) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                ((BluetoothScannerListener) mContext).onDeviceNotSupported();
            }

            if(!mBluetoothAdapter.isEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((AppCompatActivity)mContext).startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
            }
        }
    }

    public void startDiscovery(){
        Log.d(TAG, "startDiscovery");

        mBluetoothAdapter.startDiscovery();
    }

    public void registerReceiver(){
        Log.d(TAG, "registerReceiver");

        if (!isEmulator()) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            mContext.registerReceiver(mReceiver, filter);
        }
    }

    public void unregisterReceiver(){
        Log.d(TAG, "unregisterReceiver");

        if( !isEmulator() ) {
            mContext.unregisterReceiver(mReceiver);
        }
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }


    public interface BluetoothScannerListener{
        void onDeviceNotSupported();
        void onDiscoveryStarted();
        void onDiscoveryFinished();
        void onDeviceFound(BluetoothDevice device);
    }
}
