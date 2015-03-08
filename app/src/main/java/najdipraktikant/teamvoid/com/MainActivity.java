package najdipraktikant.teamvoid.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import najdipraktikant.teamvoid.com.API.ApiService;
import najdipraktikant.teamvoid.com.Adapers.OglasCardAdapter;
import najdipraktikant.teamvoid.com.Models.KompaniiModel;
import najdipraktikant.teamvoid.com.Models.OglasCardModel;
import najdipraktikant.teamvoid.com.Models.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity
        implements StudentDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private StudentDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, OglasiFragment.newInstance(2))
//                .commit();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setElevation(0);

        mNavigationDrawerFragment = (StudentDrawerFragment)
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
                        .replace(R.id.container, OglasiFragment.newInstance(position + 1))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileFragment.newInstance(position + 1))
                        .commit();
                break;
            case 3:
                editor.putString("userName", "");
                editor.putInt("userID", 0);
                editor.putBoolean("loggedIn", false);
                editor.putInt("userTip", 0);
                editor.commit();
                finish();
                break;
            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, OglasiFragment.newInstance(position + 1))
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 2:
                mTitle = getString(R.string.title_section1);
                break;
            case 3:
                mTitle = getString(R.string.title_section2);
                break;
            case 4:
                mTitle = getString(R.string.title_section3);
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
    public static class OglasiFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public static final String ENDPOINT = "http://192.168.1.68:1337";
        ImageButton ibLokacija;
        ImageButton ibProfesija;
        ImageButton ibKompanija;
        TextView tvResponse;
        int selektComp;
        ArrayList<OglasCardModel> oglasi;
        OglasCardAdapter oglasiAdapter;
        String kompanijaIme;
        SharedPreferences.Editor oglasnik;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static OglasiFragment newInstance(int sectionNumber) {
            OglasiFragment fragment = new OglasiFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public OglasiFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            final SharedPreferences preferences = getActivity().getSharedPreferences("filtriOpcii", MODE_PRIVATE);
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putString("lokacija", "");
            editor.putString("profesija", "");
            editor.putString("kompanija", "");
            editor.commit();

            ibLokacija = (ImageButton) rootView.findViewById(R.id.ibLokacija);
            ibProfesija = (ImageButton) rootView.findViewById(R.id.ibProfesija);
            ibKompanija = (ImageButton) rootView.findViewById(R.id.ibKompanija);

            ibLokacija.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // custom dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    LayoutInflater inflater = getLayoutInflater(getArguments());
                    View dialogView = inflater.inflate(R.layout.dialog_lokacija, null);

                    final Spinner sGradovi = (Spinner) dialogView.findViewById(R.id.sGradovi);

                    final ArrayAdapter<String> gradoviAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.gradovi_mk));

                    builder.setView(dialogView);
                    builder.setTitle("Избери локација");
                    builder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            editor.putString("lokacija", sGradovi.getSelectedItem().toString());
                            editor.commit();
                            ibLokacija.setImageResource(R.drawable.ic_location_on);
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            ibProfesija.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // custom dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    LayoutInflater inflater = getLayoutInflater(getArguments());
                    View dialogView = inflater.inflate(R.layout.dialog_profesija, null);

                    final Spinner sProfesii = (Spinner) dialogView.findViewById(R.id.sProfesii);

                    builder.setView(dialogView);
                    builder.setTitle("Избери професија");
                    builder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            editor.putString("profesija", sProfesii.getSelectedItem().toString());
                            editor.commit();
                            ibProfesija.setImageResource(R.drawable.ic_profesija_on);
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            ibKompanija.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // custom dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    LayoutInflater inflater = getLayoutInflater(getArguments());
                    View dialogView = inflater.inflate(R.layout.dialog_kompanija, null);

                    final Spinner sKompanii = (Spinner) dialogView.findViewById(R.id.sKompanii);
                    ArrayAdapter<String> kompaniiAdapter;
                    final ArrayList<String> kompaniiIminja = new ArrayList<String>();
                    final ArrayList<Integer> kompaniiID = new ArrayList<Integer>();

                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint(ENDPOINT)
                            .build();
                    final ApiService api = adapter.create(ApiService.class);
                    api.zemiKompanii(new Callback<ArrayList<KompaniiModel>>() {
                        @Override
                        public void success(ArrayList<KompaniiModel> kompanii, Response response) {
                            for(int i = 0; i < kompanii.size(); i++) {
                                KompaniiModel kompanija = kompanii.get(i);
                                kompaniiIminja.add(kompanija.ime);
                                kompaniiID.add(kompanija.id);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e("FAIL!!", error.toString());
                        }
                    });

                    kompaniiAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, kompaniiIminja);
                    kompaniiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sKompanii.setAdapter(kompaniiAdapter);

                    builder.setView(dialogView);
                    builder.setTitle("Избери компанија");
                    builder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences preferences = getActivity().getSharedPreferences("filtriOpcii", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            Log.e("Index", Integer.toString(sKompanii.getSelectedItemPosition()));
                            if (kompaniiID.get(sKompanii.getSelectedItemPosition()) >= 0)
                                editor.putString("kompanija", kompaniiID.get(sKompanii.getSelectedItemPosition()).toString());
                            else
                                editor.putString("kompanija", "");

                            editor.commit();
                            zemiOglasi();
                            ibKompanija.setImageResource(R.drawable.ic_action_work_on);
                            dialog.dismiss();
                            oglasiAdapter.notifyDataSetChanged();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            ListView lvListaOglasi = (ListView) rootView.findViewById(R.id.lvListaOglasi);
            oglasiAdapter = new OglasCardAdapter(getActivity(), zemiOglasi());
            lvListaOglasi.setAdapter(oglasiAdapter);
            lvListaOglasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    SharedPreferences oglasStvari = getActivity().getSharedPreferences("oglasStvari", MODE_PRIVATE);
                    oglasnik = oglasStvari.edit();

                    String opis = oglasiAdapter.getItem(position).opis;
                    String naslov = oglasiAdapter.getItem(position).naslov;
                    int companyId = oglasiAdapter.getItem(position).idKompanija;
                    oglasnik.putString("opis", opis);
                    oglasnik.putString("naslov", naslov);


                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint(ENDPOINT)
                            .build();
                    final ApiService api = adapter.create(ApiService.class);
                    api.zemiProfil(companyId, new Callback<User>() {
                        @Override
                        public void success(User user, Response response) {
                            kompanijaIme = user.ime;
                            oglasnik.putString("kompanijaIme", kompanijaIme);
                            oglasnik.putInt("kompanijaId", user.id);
                            oglasnik.commit();
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });

                    Intent intent = new Intent("android.intent.action.OGLASPREVIEW");
                    startActivity(intent);
                }
            });
            lvListaOglasi.setDividerHeight(0);

            return rootView;
        }

        private void zemiKompanii() {

        }

        private ArrayList<OglasCardModel> zemiOglasi() {
            final ArrayList<OglasCardModel> oglasi = new ArrayList<>();

//            oglasi.add(new OglasCardModel("VividCrimson", "Android Developer", "http://i.stack.imgur.com/xR5Ag.png", 1));
//            oglasi.add(new OglasCardModel("Apple", "Senior Hardware Engineer", "https://www.apple.com/euro/home/w/generic/images/og.jpg", 2));
//            oglasi.add(new OglasCardModel("VividCrimson", "Android Developer", "http://i.stack.imgur.com/xR5Ag.png",3 ));
//            oglasi.add(new OglasCardModel("Apple", "Senior Hardware Engineer", "https://www.apple.com/euro/home/w/generic/images/og.jpg", 4));
//            oglasi.add(new OglasCardModel("VividCrimson", "Android Developer", "http://i.stack.imgur.com/xR5Ag.png", 5));
//            oglasi.add(new OglasCardModel("Apple", "Senior Hardware Engineer", "https://www.apple.com/euro/home/w/generic/images/og.jpg", 6));
//            oglasi.add(new OglasCardModel("VividCrimson", "Android Developer", "http://i.stack.imgur.com/xR5Ag.png", 7));
//            oglasi.add(new OglasCardModel("Apple", "Senior Hardware Engineer", "https://www.apple.com/euro/home/w/generic/images/og.jpg", 8));

            SharedPreferences preferences = getActivity().getSharedPreferences("filtriOpcii", MODE_PRIVATE);
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(ENDPOINT)
                    .build();
            final ApiService api = adapter.create(ApiService.class);
            Log.e("KOMPANIJAAAAA", preferences.getString("kompanija", "NEMAAA"));
            if (oglasiAdapter != null) oglasiAdapter.clear();
            api.zemiOglasi(preferences.getString("lokacija", ""), preferences.getString("profesija", ""), preferences.getString("kompanija", ""), new Callback<ArrayList<OglasCardModel>>() {
                @Override
                public void success(ArrayList<OglasCardModel> oglasiLista, Response response) {
                    Log.e("SUCC", oglasiLista.size() + "");
                    for(int i = 0; i < oglasiLista.size(); i++) {
                        OglasCardModel oglas = oglasiLista.get(i);
                        oglasiAdapter.add(oglas);
                    }
                    oglasiAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("ERR", error.toString());
                }
            });

            Log.e("OGLASI GOLEMINA", oglasi.size() + "");

            return oglasi;
        }
//        Bitmap bitmapImage;
//
//        public Bitmap loadImage(String url) {
//
//            ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener(){
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    Log.e("SLIKCE", "");
//                    bitmapImage = loadedImage;
//                }
//            });
//            return bitmapImage;
//        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
