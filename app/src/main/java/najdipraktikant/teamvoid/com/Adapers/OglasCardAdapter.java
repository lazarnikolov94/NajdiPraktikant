package najdipraktikant.teamvoid.com.Adapers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import najdipraktikant.teamvoid.com.API.ApiService;
import najdipraktikant.teamvoid.com.MainActivity;
import najdipraktikant.teamvoid.com.Models.OglasCardModel;
import najdipraktikant.teamvoid.com.Models.User;
import najdipraktikant.teamvoid.com.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lazarnikolov on 3/7/15.
 */
public class OglasCardAdapter extends ArrayAdapter<OglasCardModel> {

    public Context context;
    private final ArrayList<OglasCardModel> modelsArrayList;
    DisplayImageOptions options;
    public static final String ENDPOINT = "http://192.168.1.68:1337";
    String companyName;
    String slika;

    public OglasCardAdapter(Context context, ArrayList<OglasCardModel> modelsArrayList) {
        super(context, R.layout.oglas_card, modelsArrayList);
        this.context = context;
        this.modelsArrayList = modelsArrayList;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).build();
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.oglas_card, parent, false);
        TextView tvPozicijaTip = (TextView) rowView.findViewById(R.id.tvPozicijaTip);
        ImageView ivCompanyLogo = (ImageView) rowView.findViewById(R.id.ivCompanyLogo);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();
        final ApiService api = adapter.create(ApiService.class);
        api.zemiProfil(modelsArrayList.get(position).idKompanija, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                TextView tvCompanyName;
                tvCompanyName = (TextView) rowView.findViewById(R.id.tvCompanyName);
                companyName = user.ime;
                tvCompanyName.setText(companyName);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        tvPozicijaTip.setText(modelsArrayList.get(position).naslov);
        ImageLoader.getInstance().displayImage(ENDPOINT + "/sliki/" + modelsArrayList.get(position).idKompanija + ".jpg", ivCompanyLogo, options);

        return rowView;
    }
}
