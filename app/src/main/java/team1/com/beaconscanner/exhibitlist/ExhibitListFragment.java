package team1.com.beaconscanner.exhibitlist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import team1.com.beaconscanner.Exhibit;
import team1.com.beaconscanner.FragmentListDataInterface;
import team1.com.beaconscanner.R;


public class ExhibitListFragment extends Fragment implements FragmentListDataInterface<Exhibit>{
    static String TAG = "ExhibitListFragment";

    private OnExhibitListFragmentListener mOnExhibitListFragmentListener;
    private ExhibitListFragmentAdapter mExhibitListFragmentAdapter;
    private ArrayList <Exhibit> mExhibits = new ArrayList<>();

    public ExhibitListFragment() {
        // Required empty public constructor
    }

    public static ExhibitListFragment newInstance(ArrayList<Exhibit> exhibits) {
        ExhibitListFragment fragment = new ExhibitListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnExhibitListFragmentListener = (OnExhibitListFragmentListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_exhibit_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.fragment_exhibit_list);

        mExhibitListFragmentAdapter = new ExhibitListFragmentAdapter(getActivity(), R.layout.fragment_exhibit_row, mExhibits);
        listView.setAdapter(mExhibitListFragmentAdapter);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExhibitListFragmentListener) {
            mOnExhibitListFragmentListener = (OnExhibitListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnExhibitListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnExhibitListFragmentListener = null;
    }

    @Override
    public void onDataUpdated(ArrayList<Exhibit> list) {
        Log.d(TAG, "onDataUpdated");

        mExhibits = list;
        mExhibitListFragmentAdapter.updateData(mExhibits);
        mExhibitListFragmentAdapter.notifyDataSetChanged();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnExhibitListFragmentListener {
    }
}
