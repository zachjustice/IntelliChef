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
    private static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");
        setContentView(R.layout.activity_login);


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
            public void onFailure(int statusCode, Header[] headers, String result, Throwable throwable) {
                if( statusCode == 401) { // 401 status code corresponds to unauthorized access
                    mEmailView.setError("Invalid email address or password.");
                    mEmailView.requestFocus();
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject entity) {
                int entityPk;
                String username;
                String first_name;
                String last_name;
                String email;
                String password;

                try {
                    username = entity.getString("username");
                    entityPk = entity.getInt("entity_pk");
                    first_name = entity.getString("first_name");
                    last_name = entity.getString("last_name");
                    email = entity.getString("email");
                    password = entity.getString("password");

                    RegistrationInfo registrationInfo = new RegistrationInfo(first_name, last_name, email, username, password);

                    currentUser = new User(registrationInfo);
                    currentUser.setEntityPk(entityPk);
                    currentUser.setNewUser(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (entity == null) {
                    mEmailView.setError("Invalid email address or password.");
                    mEmailView.requestFocus();
                } else {
                    Intent intent = new Intent(LoginActivity.this, MealPlanActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        LoginActivity.currentUser = currentUser;
    }
}
