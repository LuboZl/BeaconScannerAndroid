package team1.com.beaconscanner;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements BluetoothScanner.BluetoothScannerListener {
    private ExhibitFirebase mExhibitFirebase;
    private BluetoothScanner mBluetoothScanner;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mExhibitFirebase = new ExhibitFirebase(this);
        mBluetoothScanner = new BluetoothScanner(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"fab clicked!");
//                Intent intent = new Intent(this, AddExhibit.class);
                Intent intent = new Intent(getBaseContext(), AddExhibit.class);
                intent.putExtra("exhibit_edit",false);
                startActivity(intent);
//                goToNewExhibitActivity();
            }
        });
        Button test_preview = (Button) findViewById(R.id.btn_preview);
        test_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"btn_preview clicked!");
                Intent intent = new Intent(getBaseContext(), PreviewExhibit.class);
                startActivity(intent);
            }
        });
    }

    void addExhibitToFirebase(BluetoothDevice device){
        Exhibit exhibit = new Exhibit(device.getAddress(), "Title", "About", null);
        mExhibitFirebase.add(exhibit);
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

    public void onBluetoothNotRunning(){
        Log.d(TAG, "onBluetoothNotRunning");
    }

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
        addExhibitToFirebase(device);
    }
}
