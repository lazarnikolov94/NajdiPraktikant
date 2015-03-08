package najdipraktikant.teamvoid.com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import najdipraktikant.teamvoid.com.API.ApiService;
import najdipraktikant.teamvoid.com.Models.LoginModel;
import najdipraktikant.teamvoid.com.Models.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lazarnikolov on 3/6/15.
 */
public class LoginActivity extends ActionBarActivity {

    public static final String ENDPOINT = "http://192.168.1.68:1337";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        boolean loggedIn = preferences.getBoolean("loggedIn", false);
        int userTip = preferences.getInt("userTip", 0);
        if(loggedIn) {
            if(userTip == 0) {
                // Student e
                Intent intent = new Intent("android.intent.action.APP");
                startActivity(intent);
            } else if (userTip == 1) {
                // Kompanija e
                Intent intent = new Intent("android.intent.action.DASHBOARD");
                startActivity(intent);
            }
        }

        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setElevation(0);

        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        Button bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("android.intent.action.APP");
//                startActivity(intent);
                RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(ENDPOINT)
                        .build();
                ApiService api = adapter.create(ApiService.class);
                api.Login(etEmail.getText().toString(), etPassword.getText().toString(), new Callback<LoginModel>() {
                    @Override
                    public void success(LoginModel usr, Response response) {
                        editor.putString("userName", usr.ime);
                        editor.putInt("userID", usr.id);
                        editor.putBoolean("loggedIn", true);
                        editor.putInt("userTip", usr.tip);
                        editor.commit();

                        if(usr.tip == 0) {
                            // Student e, nosi go da bara oglasi
                            Intent intent = new Intent("android.intent.action.APP");
                            startActivity(intent);
                        } else if (usr.tip == 1) {
                            // Kompanija e, nosi go da si gi gleda oglasite
                            Intent intent = new Intent("android.intent.action.DASHBOARD");
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("ERROR", error.toString());
                        Toast.makeText(getApplicationContext(), "Имаше проблем при најавувањето, обидете се повторно", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Button bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.REGISTER");
                startActivity(intent);
            }
        });
    }
}
