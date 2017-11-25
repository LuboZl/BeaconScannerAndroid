package team1.com.beaconscanner;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements BluetoothScanner.BluetoothScannerListener {

    private DatabaseReference mDatabase;
    private BluetoothScanner mBluetoothScanner;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mBluetoothScanner = new BluetoothScanner(this);

        // firebase test
        mDatabase = FirebaseDatabase.getInstance().getReference("previews");
        Exhibit exhibit = new Exhibit(mDatabase.push().getKey(), "Tilgnerova 11", "Testovaci", null, null);
        mDatabase.child(exhibit.getId()).setValue(exhibit);


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
    }
}
