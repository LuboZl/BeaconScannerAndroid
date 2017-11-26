package team1.com.beaconscanner;

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
    private BluetoothScannerListener mBluetoothScannerListener;

    private static String TAG = "BluetoothScanner";
    private static int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    public static int REQUEST_ENABLE_BLUETOOTH = 100;



    public BluetoothScanner(Context context, BluetoothScannerListener bluetoothScannerListener) {
        mContext = context;
        this.mBluetoothScannerListener = bluetoothScannerListener;

        initBroadcastReceiver();
        requestPermissions();
        initBluetoothScanner();
    }

    public void initBluetoothScanner() {
        Log.d(TAG, "initBluetoothScanner");

        getBluetoothAdapter();
        registerReceiver();
        startDiscovery();
    }

    private void initBroadcastReceiver() {
        Log.d(TAG, "initBroadcastReceiver");

        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (action == null) {
                    return;
                }

                switch (action) {
                    case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                        mBluetoothScannerListener.onDiscoveryStarted();
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        mBluetoothScannerListener.onDiscoveryFinished();
                        startDiscovery();

                        break;
                    case BluetoothDevice.ACTION_FOUND:
                        BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if (device != null) {
                            mBluetoothScannerListener.onDeviceFound(device, intent);
                        }
                }
            }
        };
    }

    private void requestPermissions() {
        Log.d(TAG, "requestPermissions");

        ActivityCompat.requestPermissions( (AppCompatActivity) mContext,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
    }

    private void getBluetoothAdapter() {
        Log.d(TAG, "getBluetoothAdapter");

        if (!isEmulator()) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                mBluetoothScannerListener.onDeviceNotSupported();
            }

            if(!mBluetoothAdapter.isEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((AppCompatActivity)mContext).startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
            }
        }
    }

    private void startDiscovery() {
        Log.d(TAG, "startDiscovery");

        if (!isEmulator()) mBluetoothAdapter.startDiscovery();
    }

    private void registerReceiver() {
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
        void onDeviceFound(BluetoothDevice device, Intent intent);
    }
}
