package team1.com.beaconscanner.exhibit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import team1.com.beaconscanner.R;
import team1.com.beaconscanner.overview.ListDataInterface;

public class ExhibitListFragment extends Fragment implements ListDataInterface<Exhibit> {
    static String TAG = "ExhibitListFragment";

    private FragmentListener mFragmentListener;

    ListView mListView;
    ProgressBar mProgressBar;

    private ExhibitListFragmentAdapter mFragmentAdapter;
    private ArrayList <Exhibit> mExhibits = new ArrayList<>();

    public ExhibitListFragment() {
        // Required empty public constructor
    }

    public static ExhibitListFragment newInstance() {
        ExhibitListFragment fragment = new ExhibitListFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public static ExhibitListFragment newInstance(ArrayList<Exhibit> exhibits) {
        ExhibitListFragment fragment = new ExhibitListFragment();
        fragment.mExhibits = exhibits;
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
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_exhibit_list, container, false);
//        mExhibits = mFragmentListener.getFoundExhibits();

        mListView = (ListView) view.findViewById(R.id.exhibit_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.exhibit_loader);

        mFragmentAdapter = new ExhibitListFragmentAdapter(getActivity(), R.layout.fragment_exhibit_row, mExhibits);
        mListView.setAdapter(mFragmentAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exhibit exhibit = mExhibits.get(position);
                mFragmentListener.onExhibitItemClick(exhibit);
            }
        });

        setVisibilities();

        return view;
    }

    private void setVisibilities(){
        if(mExhibits.size() == 0){
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
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    @Override
    public void onDataUpdated(ArrayList<Exhibit> exhibits) {
        Log.d(TAG, "onDataUpdated");
        mExhibits = exhibits;

        if(mFragmentAdapter == null){
            return;
        }

        mFragmentAdapter.updateData(exhibits);
        mFragmentAdapter.notifyDataSetChanged();
        setVisibilities();
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
    public interface FragmentListener {
        void onExhibitItemClick(Exhibit e);
    }


}
