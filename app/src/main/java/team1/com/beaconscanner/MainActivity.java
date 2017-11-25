package team1.com.beaconscanner;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BluetoothScanner.BluetoothScannerListener {
    private ExhibitFirebase mExhibitFirebase;
    private BluetoothScanner mBluetoothScanner;
    private String TAG = "MainActivity";
    private ListView exhibitionsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exhibitionsListView = (ListView) findViewById(R.id.exhibitions);
        mExhibitFirebase = new ExhibitFirebase(this);
        mBluetoothScanner = new BluetoothScanner(this);

        final ArrayList<Exhibit> exhibitions = new ArrayList<>();
        exhibitions.add(new Exhibit("5", "Test", "Nejaky popis", "URL obrazku"));
        exhibitions.add(new Exhibit("6", "Dalsi nadpis", "Iny popis", "URL obrazku"));

        exhibitionsListView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, exhibitions) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(exhibitions.get(position).getTitle());
                text2.setText("Vzdialenosť od exponátu "+ exhibitions.get(position).getDistance() + " m");
                return view;
            }
        });
        exhibitionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = exhibitionsListView.getItemAtPosition(position);

                Intent intent = new Intent(getBaseContext(), PreviewExhibit.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddExhibit.class);
                intent.putExtra("exhibit_edit",false);
                startActivity(intent);
            }
        });
    }

    void addExhibitToFirebase(BluetoothDevice device){
        mExhibitFirebase.add(new Exhibit(device.getAddress(), "Title", "About", null));
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
        addExhibitToFirebase(device);
    }
}
