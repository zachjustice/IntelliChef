package intellichef.intellichef;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by b on 1/22/17.
 */

public class RegistrationActivity extends AppCompatActivity {

    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mEmailView;
    private EditText mUsernameView;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mFirstNameView = (EditText) findViewById(R.id.firstName);
        mLastNameView = (EditText) findViewById(R.id.lastName);
        mEmailView = (EditText) findViewById(R.id.email);
        mUsernameView = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPassword);

        mFirstNameView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        mLastNameView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        create = (Button) findViewById(R.id.createAccount);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    attemptRegistration();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    public void attemptRegistration() throws JSONException {
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mEmailView.setError(null);
        mUsernameView.setError(null);
        mPassword.setError(null);
        mConfirmPassword.setError(null);

        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String username = mUsernameView.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            mFirstNameView.requestFocus();
            return;
        } else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            return;
        } else if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            mUsernameView.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_field_required));
            mPassword.requestFocus();
            return;
        } else if (TextUtils.isEmpty(confirmPassword)) {
            mConfirmPassword.setError(getString(R.string.error_field_required));
            mConfirmPassword.requestFocus();
            return;
        } else if (!isValidPassword()) {
            mPassword.setError(getString(R.string.error_invalid_password));
            mPassword.requestFocus();
            return;
        } else if (!password.equals(confirmPassword)) {
            mConfirmPassword.setError("Passwords don't match.");
            mConfirmPassword.requestFocus();
            return;
        }

        RegistrationInfo registrationInfo = new RegistrationInfo(firstName, lastName, email, username, password);

        IntelliServerAPI.register(registrationInfo, this.getApplicationContext(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                /* my func */
                boolean registrationSuccessful = false;
                try {
                    registrationSuccessful = result.getBoolean("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!registrationSuccessful) {
                    mEmailView.setError("Email address or username already exists.");
                    mEmailView.requestFocus();
                } else {
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean isValidPassword() {
        return mPassword.getText().length() >= 6;
    }
}
