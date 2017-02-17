package intellichef.intellichef;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Sally on 1/22/17.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private Button registerButton;
    private Button loginButton;
    private static String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");
        setContentView(R.layout.activity_login);

        IntelliServerAPI.initialize();

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            // Perform action on click
                try {
                    attemptLogin();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            // Perform action on click
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    private void attemptLogin() throws JSONException {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            mPasswordView.requestFocus();
            return;
        }
        final String currentUserEmail = email;

        IntelliServerAPI.login(currentUserEmail, password, this.getApplicationContext(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                boolean loginSuccessful = false;
                try {
                    loginSuccessful = result.getBoolean("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!loginSuccessful) {
                    mEmailView.setError("Invalid email address or password.");
                    mEmailView.requestFocus();
                } else {
                    currentUser = currentUserEmail;
                    Intent intent = new Intent(LoginActivity.this, CalibrationActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public static String getCurrentEmail() {
        return currentUser;
    }

    /* my lambda function */
}
