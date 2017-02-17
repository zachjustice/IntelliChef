package intellichef.intellichef;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
    private Button saveBasic;
    private ImageButton changePicture;
    private ImageButton editBasic;
    private EditText first;
    private EditText last;
    private EditText email;
    private EditText usern;
    private EditText password;
    private EditText confirmPassword;
    private EditText fnDisplay;
    private EditText lnDisplay;
    private LinearLayout basicInfoLayout;
    private LinearLayout basicInfoLayout2;
    private static int GET_FROM_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");
        setContentView(R.layout.activity_preferences);


        basicInfoLayout = (LinearLayout) findViewById(R.id.basicInfoCollapsed);
        basicInfoLayout2 = (LinearLayout) findViewById(R.id.basicInfoMain);
        basicInfoLayout2.setVisibility(View.GONE);
        for (int i = 0; i < basicInfoLayout.getChildCount();  i++ ){
            View view = basicInfoLayout.getChildAt(i);
            view.setEnabled(false);
        }

        saveBasic = (Button) findViewById(R.id.saveBasicInfo);
        logout = (Button) findViewById(R.id.logout);
        deleteAccount = (Button) findViewById(R.id.deleteAccount);
        changePicture = (ImageButton) findViewById(R.id.profilePic);
        editBasic = (ImageButton) findViewById(R.id.editBasicInfo);

        first = (EditText) findViewById(R.id.fn);
        last = (EditText) findViewById(R.id.ln);
        email = (EditText) findViewById(R.id.em);
        usern = (EditText) findViewById(R.id.un);
        password = (EditText) findViewById(R.id.pw);
        confirmPassword = (EditText) findViewById(R.id.cpw);

//        String firstname = RegistrationActivity.getCurrentFirstName();
//        first.setText(firstname);
//        fnDisplay.setText(firstname);
//        String lastname = RegistrationActivity.getCurrentLastName();
//        last.setText(lastname);
//        lnDisplay.setText(lastname);
//        String emailaddress = RegistrationActivity.getCurrentEmail();
//        email.setText(emailaddress);
//        String username = RegistrationActivity.getCurrentUsername();
//        usern.setText(username);

        changePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(i, GET_FROM_GALLERY);
            }
        });

        editBasic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                expand();
                editBasic.setEnabled(false);
            }
        });

        saveBasic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                collapse();
                editBasic.setEnabled(true);
            }
        });

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
                changePicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void expand() {
        basicInfoLayout.setVisibility(View.GONE);
        basicInfoLayout2.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        basicInfoLayout2.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, basicInfoLayout2.getMeasuredHeight());
        mAnimator.start();
    }

    private void collapse() {
        basicInfoLayout2.setVisibility(View.GONE);
        basicInfoLayout.setVisibility(View.VISIBLE);

//        int finalHeight = basicInfoLayout.getHeight();
//
//        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
//
//        mAnimator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                //Height=0, but it set visibility to GONE
//                basicInfoLayout.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//
//        });
//        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = basicInfoLayout.getLayoutParams();
                layoutParams.height = value;
                basicInfoLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
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

