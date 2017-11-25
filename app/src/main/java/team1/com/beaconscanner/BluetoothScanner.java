package team1.com.beaconscanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;


public class BluetoothScanner {

    private Context mContext;
    private BroadcastReceiver mReceiver;
    private BluetoothAdapter mAdapter;
    private String TAG = "BluetoothScanner";

    public BluetoothScanner(Context context) {
        mContext = context;

        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                BluetoothScannerListener bluetoothScannerListener = (BluetoothScannerListener) mContext;
                String action = intent.getAction();

                if (action == null) {
                    return;
                }

                switch (action) {
                    case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                        Log.d(TAG, "ACTION_DISCOVERY_STARTED");
                        ((BluetoothScannerListener) mContext).onDiscoveryStarted();
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        Log.d(TAG, "ACTION_DISCOVERY_FINISHED");
                        ((BluetoothScannerListener) mContext).onDiscoveryFinished();
                        break;
                    case BluetoothDevice.ACTION_FOUND:
                        BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        Log.d(TAG, "ACTION_FOUND");
                        if (device != null) {
                            ((BluetoothScannerListener) mContext).onDeviceFound(device);
                        }
                }
            }
        };
        registerReceiver();
        startDiscovery();
    }

    public void startDiscovery(){
        mAdapter.startDiscovery();
    }

    public void registerReceiver(){
        if (!isEmulator()) {
            mAdapter = BluetoothAdapter.getDefaultAdapter();
            IntentFilter filter = new IntentFilter();

            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            mContext.registerReceiver(mReceiver, filter);
        }
    }

    public void unregisterReceiver(){
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
        void onDiscoveryStarted();
        void onDiscoveryFinished();
        void onDeviceFound(BluetoothDevice device);
    }
}
