package najdipraktikant.teamvoid.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import najdipraktikant.teamvoid.com.API.ApiService;
import najdipraktikant.teamvoid.com.Models.LoginModel;
import najdipraktikant.teamvoid.com.Models.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lazarnikolov on 3/7/15.
 */
public class RegisterActivity extends ActionBarActivity {

    public static final String ENDPOINT = "http://192.168.1.68:1337";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setElevation(0);

        final TextView tvPassError = (TextView) findViewById(R.id.tvPassError);
        tvPassError.setVisibility(View.INVISIBLE);
        final RadioButton rbStudent = (RadioButton) findViewById(R.id.rbStudent);
        final RadioButton rbKompanija = (RadioButton) findViewById(R.id.rbKompanija);

        final EditText etFullName = (EditText) findViewById(R.id.etFullName);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);

        Button bRegister = (Button) findViewById(R.id.bRegister);
        Button bBack = (Button) findViewById(R.id.bBack);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass = etPassword.getText().toString();
                String confirmPass = etConfirmPassword.getText().toString();

                int type = 0;
                if( rbStudent.isChecked() ) type = 0;
                else if ( rbKompanija.isChecked() ) type = 1;

                if(pass.equals(confirmPass)) {
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint(ENDPOINT)
                            .build();
                    ApiService api = adapter.create(ApiService.class);
                    api.Register(etFullName.getText().toString(), etEmail.getText().toString(), pass, type, new Callback<LoginModel>() {
                        @Override
                        public void success(LoginModel login, Response response) {
                            Log.e("REGISTER SUCCESS", login.success + "!");
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e("REGISTER FAIL", error.toString());
                        }
                    });
                } else {
                    tvPassError.setVisibility(View.VISIBLE);
                    etPassword.setText("");
                    etConfirmPassword.setText("");
                }
            }
        });

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
