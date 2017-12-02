package team1.com.beaconscanner.device;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import team1.com.beaconscanner.R;

public class BluetoothDevicesListFragmentAdapter extends ArrayAdapter<MBluetoothDevice> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<MBluetoothDevice> mBluetoothDevices;

    public BluetoothDevicesListFragmentAdapter(Context context, int resource, ArrayList<MBluetoothDevice> devices) {
        super(context, resource, devices);
        this.layoutResourceId = resource;
        this.context = context;
        this.mBluetoothDevices = devices;
    }

    public void updateData(ArrayList<MBluetoothDevice> devices) {
        mBluetoothDevices.clear();
        mBluetoothDevices.addAll(devices);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DeviceHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DeviceHolder();
            holder.device_name = (TextView) row.findViewById(R.id.fragment_device_row_name);
            holder.device_address = (TextView) row.findViewById(R.id.fragment_device_row_address);
            holder.device_rssi = (TextView) row.findViewById(R.id.fragment_device_row_rssi);

            row.setTag(holder);
        } else {
            holder = (DeviceHolder) row.getTag();
        }

        MBluetoothDevice device = mBluetoothDevices.get(position);

        holder.device_name.setText(device.getName());
        holder.device_address.setText(device.getAddress());
        holder.device_rssi.setText(context.getString(R.string.devices_rssi) + device.getRssi());

        return row;
    }

    private class DeviceHolder {
        public TextView device_name;
        public TextView device_address;
        public TextView device_rssi;
    }
}
