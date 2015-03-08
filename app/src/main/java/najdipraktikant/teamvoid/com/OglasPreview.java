package najdipraktikant.teamvoid.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import najdipraktikant.teamvoid.com.API.ApiService;
import retrofit.RestAdapter;

/**
 * Created by lazarnikolov on 3/8/15.
 */
public class OglasPreview extends ActionBarActivity {

    public static final String ENDPOINT = "http://192.168.1.68:1337";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oglas_preview);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setElevation(0);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Преглед на оглас");

        SharedPreferences prefs = getSharedPreferences("oglasStvari", MODE_PRIVATE);
        int oglasId = prefs.getInt("kompanijaId", 0);

        TextView tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
        tvCompanyName.setText(prefs.getString("kompanijaIme", ""));

        TextView tvPozicijaTip = (TextView) findViewById(R.id.tvPozicijaTip);
        tvPozicijaTip.setText(prefs.getString("naslov", ""));

        TextView tvOglasOpis = (TextView) findViewById(R.id.tvOglasOpis);
        tvOglasOpis.setText(prefs.getString("opis", ""));

        ImageView ivCompanyLogo = (ImageView) findViewById(R.id.ivCompanyLogo);
        Button bApliciraj = (Button) findViewById(R.id.bApliciraj);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .build();
        ImageLoader.getInstance().displayImage(ENDPOINT + "/sliki/" + oglasId + ".jpg", ivCompanyLogo, options);

        bApliciraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"to@email.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Апликација за пракса");
                startActivity(Intent.createChooser(emailIntent, "Испрати порака преку..."));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
