package intellichef.intellichef;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by jatin1 on 1/27/17.
 */
public class PreferencesActivity extends AppCompatActivity {

    private Button logout;
    private Button deleteAccount;
    private ImageButton changePicture;
    private EditText first;
    private EditText last;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private static int GET_FROM_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");
        setContentView(R.layout.activity_preferences);
        logout = (Button) findViewById(R.id.logout);
        deleteAccount = (Button) findViewById(R.id.deleteAccount);
        changePicture = (ImageButton) findViewById(R.id.profilePic);
        first = (EditText) findViewById(R.id.fn);
        last = (EditText) findViewById(R.id.ln);
        email = (EditText) findViewById(R.id.em);
        password = (EditText) findViewById(R.id.pw);
        confirmPassword = (EditText) findViewById(R.id.cpw);
        //TODO: Zach, query the values for the database and then uncomment
        //String firstname = *INSERT FIRST NAME QUERY HERE*
        //first.setText(firstname);
        //String lastname = *INSERT LAST NAME QUERY HERE*
        //last.setText(lastname);
        //String emailaddress = *INSERT EMAIL QUERY HERE*
        //email.setText(emailaddress);

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                try {
                    logout();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                try {
                    removeAccount();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        changePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(i, GET_FROM_GALLERY);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/";
            changePicture.setImageBitmap(BitmapFactory.decodeFile(path));
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void logout() throws JSONException {
        String email = LoginActivity.getCurrentEmail();
        if (email == null) {
            email = RegistrationActivity.getCurrentEmail();
        }

        IntelliServerAPI.logout(email, this.getApplicationContext(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                boolean logoutSuccessful = false;
                try {
                    logoutSuccessful = result.getBoolean("status");
                    if (logoutSuccessful) {
                        Log.v("JSON", "Successfully logged out of account!");
                        Intent intent = new Intent(PreferencesActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Log.v("JSON", "LOGOUT FAILED");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void removeAccount() throws JSONException {
        String email = LoginActivity.getCurrentEmail();
        if (email == null) {
            email = RegistrationActivity.getCurrentEmail();
        }

        IntelliServerAPI.removeAccount(email, this.getApplicationContext(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                boolean removeSuccessful = false;
                try {
                    removeSuccessful = result.getBoolean("status");
                    if (removeSuccessful) {
                        Log.v("JSON", "Successfully removed account!");
                        Intent intent = new Intent(PreferencesActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Log.v("JSON", "Failed to remove account");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

