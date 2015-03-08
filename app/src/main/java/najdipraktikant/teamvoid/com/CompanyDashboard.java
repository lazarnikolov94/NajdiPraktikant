package najdipraktikant.teamvoid.com;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import najdipraktikant.teamvoid.com.API.ApiService;
import najdipraktikant.teamvoid.com.Adapers.TagoviAdapter;
import najdipraktikant.teamvoid.com.Models.LoginModel;
import najdipraktikant.teamvoid.com.Models.TagoviModel;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CompanyDashboard extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_dashboard);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, NovOglasFragment.newInstance(2))
//                .commit();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setElevation(0);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        SharedPreferences preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        switch(position) {
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, NovOglasFragment.newInstance(position + 1))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MoiOglasiFragment.newInstance(position + 1))
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileFragment.newInstance(position + 1))
                        .commit();
                break;
            case 4:
                editor.putString("userName", "");
                editor.putInt("userID", 0);
                editor.putBoolean("loggedIn", false);
                editor.putInt("userTip", 0);
                editor.commit();
                finish();
                break;
            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, NovOglasFragment.newInstance(position + 1))
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 2:
                mTitle = getString(R.string.title_company_section1);
                break;
            case 3:
                mTitle = getString(R.string.title_company_section2);
                break;
            case 4:
                mTitle = getString(R.string.title_company_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class NovOglasFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public static final String ENDPOINT = "http://192.168.1.68:1337";
        EditText etOglasNaslov;
        EditText etOglasOpis;
        ListView lvTagovi;
        TagoviAdapter tagAdapter;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static NovOglasFragment newInstance(int sectionNumber) {
            NovOglasFragment fragment = new NovOglasFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public NovOglasFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_company_dashboard, container, false);

            etOglasNaslov = (EditText) rootView.findViewById(R.id.etOglasNaslov);
            etOglasOpis = (EditText) rootView.findViewById(R.id.etOglasOpis);
            lvTagovi = (ListView) rootView.findViewById(R.id.lvTagovi);

            tagAdapter = new TagoviAdapter(getActivity(), getTags());
            lvTagovi.setAdapter(tagAdapter);

            return rootView;
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
            menu.add("Додади").setIcon(R.drawable.ic_action_navigation_check).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            dodadiOglas();
            return super.onOptionsItemSelected(item);
        }

        private void dodadiOglas() {
            SharedPreferences preferences = getActivity().getSharedPreferences("userLogin", MODE_PRIVATE);
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(ENDPOINT)
                    .build();
            ApiService api = adapter.create(ApiService.class);
            String oglasNaslov = etOglasNaslov.getText().toString();
            String oglasOpis = etOglasOpis.getText().toString();
            ArrayList<TagoviModel> modelsArrayList = tagAdapter.getModelsArrayList();
            String tagovi = "";
            int count = lvTagovi.getCount();
            for(int i = 0; i < count ; i++) {
                if(modelsArrayList.get(i).isSelected()) {
                    tagovi += i + ( i < count-1 ? "," : "");
                }
            }
            api.dodajOglas(preferences.getInt("userID", 0), oglasNaslov, oglasOpis, tagovi, new Callback<LoginModel>(){
                @Override
                public void success(LoginModel loginModel, Response response) {
                    Toast.makeText(getActivity(), "Огласот е додаден успешно", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Имаше проблем при додавање на огласот!", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", error.toString());
                }
            });
        }

        public ArrayList<TagoviModel> getTags() {
            ArrayList<TagoviModel> tags = new ArrayList<>();
            tags.add(new TagoviModel("Android Developer"));
            tags.add(new TagoviModel("iOS Developer"));
            tags.add(new TagoviModel("System Admin"));
            tags.add(new TagoviModel("Web Developer"));
            tags.add(new TagoviModel("Wordpress Developer"));
            tags.add(new TagoviModel(".NET Developer"));
            tags.add(new TagoviModel("Software Developer"));
            tags.add(new TagoviModel("Senior Hardware Engineer"));
            tags.add(new TagoviModel("Ilija Boskov"));
            tags.add(new TagoviModel("Hackaton Expert"));
            tags.add(new TagoviModel("Jesus"));
            return tags;
        }
    }

}
