package najdipraktikant.teamvoid.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import najdipraktikant.teamvoid.com.API.ApiService;
import najdipraktikant.teamvoid.com.ContentProvider.CameraContentProvider;
import najdipraktikant.teamvoid.com.Models.LoginModel;
import najdipraktikant.teamvoid.com.Models.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lazarnikolov on 3/7/15.
 */
public class ProfileFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ENDPOINT = "http://192.168.1.68:1337";
    private static final int TAKE_PHOTO_CODE = 1;
    CircleImageView imagePhoto;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    DisplayImageOptions options;
    TextView tvUserName;
    TextView tvUserBio;
    TextView tvUserInfo;
    TextView tvUserInteres;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProfileFragment newInstance(int sectionNumber) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        preferences = getActivity().getSharedPreferences("userLogin", Context.MODE_PRIVATE);
        editor = preferences.edit();
        int userid = preferences.getInt("userID", 0);

        RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(ENDPOINT)
                    .build();
        ApiService api = adapter.create(ApiService.class);
        api.zemiProfil(userid, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Log.e("SUCCESS!!!", user.ime + ", " + user.info + ", " + user.bio);
                editor.putString("userName", user.ime);
                editor.putString("userInfo", user.info);
                editor.putString("userBio", user.bio);
                editor.putString("userInteres", user.interes);
                Gson gson = new Gson();
                String userJson = gson.toJson(user);
                editor.putString("userObject", userJson);
                editor.commit();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("FAIL!!!!!!", error.toString());
            }
        });

        View rootView = inflater.inflate(R.layout.user_profile, container, false);

        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        tvUserBio = (TextView) rootView.findViewById(R.id.tvUserBio);
        tvUserInfo = (TextView) rootView.findViewById(R.id.tvUserInfo);
        tvUserInteres = (TextView) rootView.findViewById(R.id.tvUserProfession);

        tvUserName.setText(preferences.getString("userName", "Нема вредност, измени профил."));
        tvUserBio.setText(preferences.getString("userBio", "Нема вредност, измени профил."));
        tvUserInfo.setText(preferences.getString("userInfo", "Нема вредност, измени профил."));
        tvUserInteres.setText(preferences.getString("userInteres", "Нема вредност, измени профил."));

        imagePhoto = (CircleImageView) rootView.findViewById(R.id.profile_image);
        if(preferences.getString("userPhoto", "").isEmpty()) {
            Log.e("PRED SLIKA", "-------------------------------");
            ImageLoader.getInstance().displayImage(ENDPOINT + "/sliki/" + userid + ".jpg", imagePhoto);
            Log.e("POSLE SLIKA", "-------------------------------");
        }
        imagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(getActivity())));
                startActivityForResult(takePicture, TAKE_PHOTO_CODE);
            }
        });

        return rootView;
    }

    private File getTempFile(FragmentActivity activity) {
        //it will return /sdcard/image.tmp
        final File path = new File( Environment.getExternalStorageDirectory(), getActivity().getPackageName() );
        if(!path.exists()){
            path.mkdir();
        }
        return new File(path, "image.tmp");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO_CODE:
                    final File file = getTempFile(getActivity());
                    try {
                        Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));

                        Matrix m = new Matrix();
                        m.setRectToRect(new RectF(0, 0, captureBmp.getWidth(), captureBmp.getHeight()), new RectF(0, 0, 350, 350), Matrix.ScaleToFit.CENTER);
                        captureBmp = Bitmap.createBitmap(captureBmp, 0, 0, captureBmp.getWidth(), captureBmp.getHeight(), m, true);

                        imagePhoto.setImageBitmap(captureBmp);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        captureBmp.compress(Bitmap.CompressFormat.JPEG,
                                100, baos);
                        byte[] b = baos.toByteArray();
                        String encodedBitmap = Base64
                                .encodeToString(b, Base64.DEFAULT);

                        RestAdapter adapter = new RestAdapter.Builder()
                                .setEndpoint(ENDPOINT)
                                .build();
                        ApiService api = adapter.create(ApiService.class);
                        api.smeniSlika(preferences.getInt("userID", 0), encodedBitmap, new Callback<LoginModel>() {
                            @Override
                            public void success(LoginModel loginModel, Response response) {
                                Log.e("SUCCESS", "Bravo momce!");
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e("FAIL!!!!!!", error.toString());
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                break;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Промени профил").setIcon(R.drawable.ic_action_editor_mode_edit_white).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater(getArguments());
        View dialogView = inflater.inflate(R.layout.edit_profile, null);

        preferences = getActivity().getSharedPreferences("userLogin", Context.MODE_PRIVATE);
        editor = preferences.edit();
        final EditText etUserName = (EditText) dialogView.findViewById(R.id.etUserName);
        final EditText etUserInfo = (EditText) dialogView.findViewById(R.id.etUserInfo);
        final EditText etUserBio = (EditText) dialogView.findViewById(R.id.etUserBio);
        final EditText etUserProfession = (EditText) dialogView.findViewById(R.id.etUserProfession);
        etUserName.setText(preferences.getString("userName", ""));
        etUserInfo.setText(preferences.getString("userInfo", ""));
        etUserBio.setText(preferences.getString("userBio", ""));
        etUserProfession.setText(preferences.getString("userInteres", ""));

        builder.setView(dialogView);
        builder.setTitle("Измени профил");
        builder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                editor.putString("userName", etUserName.getText().toString());
                editor.putString("userInfo", etUserInfo.getText().toString());
                editor.putString("userBio", etUserBio.getText().toString());
                editor.putString("userInteres", etUserProfession.getText().toString());
                editor.commit();

                User novUser = new User();
                novUser.bio = etUserBio.getText().toString();
                novUser.ime = etUserName.getText().toString();
                novUser.info = etUserInfo.getText().toString();
                novUser.interes = etUserProfession.getText().toString();
                novUser.id = preferences.getInt("userID", 0);

                Gson gson = new Gson();
                String json = gson.toJson(novUser);
                editor.putString("userObject", json);
                editor.commit();

                RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(ENDPOINT)
                        .build();
                ApiService api = adapter.create(ApiService.class);
                api.obnoviProfil(json, new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        Log.e("USER SEND SUCCESS", "YEAH!");
                        zemiSiProfilce();
                        dialog.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("ERROR!!!!!!!", error.toString());
                    }
                });
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        return super.onOptionsItemSelected(item);
    }

    public void zemiSiProfilce() {
        preferences = getActivity().getSharedPreferences("userLogin", Context.MODE_PRIVATE);
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();
        ApiService api = adapter.create(ApiService.class);
        api.zemiProfil(preferences.getInt("userID", 0), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Log.e("SUCCESS!!!", user.ime + ", " + user.info + ", " + user.bio);
                editor.putString("userName", user.ime);
                editor.putString("userInfo", user.info);
                editor.putString("userBio", user.bio);
                editor.putString("userInteres", user.interes);
                Gson gson = new Gson();
                String userJson = gson.toJson(user);
                editor.putString("userObject", userJson);
                editor.commit();
                tvUserName.setText(preferences.getString("userName", "Нема вредност, измени профил."));
                tvUserBio.setText(preferences.getString("userBio", "Нема вредност, измени профил."));
                tvUserInfo.setText(preferences.getString("userInfo", "Нема вредност, измени профил."));
                tvUserInteres.setText(preferences.getString("userInteres", "Нема вредност, измени профил."));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("FAIL!!!!!!", error.toString());
            }
        });
    }
}