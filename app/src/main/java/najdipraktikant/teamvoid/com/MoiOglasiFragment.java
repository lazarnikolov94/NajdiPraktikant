package najdipraktikant.teamvoid.com;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import najdipraktikant.teamvoid.com.Adapers.MoiOglasiAdapter;
import najdipraktikant.teamvoid.com.Models.MoiOglasiModel;

/**
 * Created by lazarnikolov on 3/8/15.
 */
public class MoiOglasiFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ENDPOINT = "http://192.168.1.68:1337";
    ListView lvMoiOglasi;
    MoiOglasiAdapter adapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MoiOglasiFragment newInstance(int sectionNumber) {
        MoiOglasiFragment fragment = new MoiOglasiFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MoiOglasiFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moi_oglasi, container, false);

        lvMoiOglasi = (ListView) rootView.findViewById(R.id.lvMoiOglasi);
        adapter = new MoiOglasiAdapter(getActivity(), zemiMoiOglasi());
        lvMoiOglasi.setAdapter(adapter);
        lvMoiOglasi.setDividerHeight(0);

        return rootView;
    }

    private ArrayList<MoiOglasiModel> zemiMoiOglasi() {
        ArrayList<MoiOglasiModel> moiOglasi = new ArrayList<>();

        moiOglasi.add(new MoiOglasiModel("Android Developer"));
        moiOglasi.add(new MoiOglasiModel("WordPress Developer"));
        moiOglasi.add(new MoiOglasiModel("iOS Developer"));

        return moiOglasi;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((CompanyDashboard) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

