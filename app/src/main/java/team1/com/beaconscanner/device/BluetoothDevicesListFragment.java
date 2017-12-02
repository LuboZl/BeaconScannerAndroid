package team1.com.beaconscanner.device;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import team1.com.beaconscanner.R;
import team1.com.beaconscanner.overview.ListDataInterface;

public class BluetoothDevicesListFragment extends Fragment implements ListDataInterface<MBluetoothDevice> {
    private FragmentListener mFragmentListener;
    ListView mListView;
    ProgressBar mProgressBar;

    private BluetoothDevicesListFragmentAdapter mFragmentAdapter;
    private ArrayList<MBluetoothDevice> mBluetoothDevices = new ArrayList<>();

    public BluetoothDevicesListFragment() {
    }

    public static BluetoothDevicesListFragment newInstance() {
        BluetoothDevicesListFragment fragment = new BluetoothDevicesListFragment();
        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentListener = (FragmentListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devices_list, container, false);
        mBluetoothDevices = mFragmentListener.getBluetoothDevices();

        mListView = (ListView) view.findViewById(R.id.bluetooth_devics_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.devices_loader);

        mFragmentAdapter = new BluetoothDevicesListFragmentAdapter(getActivity(), R.layout.fragment_devices_row, mBluetoothDevices);
        mListView.setAdapter(mFragmentAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MBluetoothDevice device = mBluetoothDevices.get(position);
                mFragmentListener.onBluetoothDeviceItemClick(device);
            }
        });

        setVisibilities();
        return view;
    }

    private void setVisibilities() {
        if (mBluetoothDevices.size() == 0) {
            mListView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);

            return;
        }

        mListView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FragmentListener) {
            mFragmentListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    @Override
    public void onDataUpdated(ArrayList<MBluetoothDevice> devices) {
        mBluetoothDevices = devices;

        if (mFragmentAdapter == null) {
            return;
        }

        mFragmentAdapter.updateData(devices);
        mFragmentAdapter.notifyDataSetChanged();
        setVisibilities();
    }

    public interface FragmentListener {
        ArrayList<MBluetoothDevice> getBluetoothDevices();

        void onBluetoothDeviceItemClick(MBluetoothDevice e);
    }
}
