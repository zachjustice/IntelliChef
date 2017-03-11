package intellichef.intellichef;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ListActivity;
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
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by jatin1 on 1/27/17.
 */
public class PreferencesActivity extends AppCompatActivity {

    private Button logout;
    private Button deleteAccount;
    private Button saveAllChanges;
    private Button saveBasic;
    private Button saveDietary;
    private Button addAllergy;
    private Button saveAllergies;
    private Button editAllergies;
    private ListView allergyList;
    private ImageButton changePicture;
    private Button editBasic;
    private Button editDietary;
    private EditText first;
    private EditText last;
    private EditText email;
    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private LinearLayout basicInfoLayout;
    private LinearLayout dietaryConcernsLayout;
    private LinearLayout allergiesLayout;
    private AutoCompleteTextView enterAllergy;
    private User currentUser;
    private static int GET_FROM_GALLERY = 1;
    private List<String> dietaryRestrictions;
    private ArrayAdapter<String> allergyListAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");
        setContentView(R.layout.activity_preferences);
        currentUser = LoginActivity.getCurrentUser();


        basicInfoLayout = (LinearLayout) findViewById(R.id.basicInfoMain);
        for (int i = 0; i < basicInfoLayout.getChildCount();  i++ ){
            View view = basicInfoLayout.getChildAt(i);
            view.setEnabled(false);
        }

        dietaryConcernsLayout = (LinearLayout) findViewById(R.id.dietaryConcerns);
        for (int i = 0; i < dietaryConcernsLayout.getChildCount();  i++ ){
            View view = dietaryConcernsLayout.getChildAt(i);
            view.setEnabled(false);
        }

        allergiesLayout = (LinearLayout) findViewById(R.id.allergies);

        saveBasic = (Button) findViewById(R.id.saveBasicInfo);
        logout = (Button) findViewById(R.id.logout);
        deleteAccount = (Button) findViewById(R.id.deleteAccount);
        changePicture = (ImageButton) findViewById(R.id.profilePic);
        editBasic = (Button) findViewById(R.id.editBasicInfo);
        saveDietary = (Button) findViewById(R.id.saveDietaryConcerns);
        editDietary = (Button) findViewById(R.id.editDietaryConcerns);
        addAllergy = (Button) findViewById(R.id.addAllergy);
        allergyList = (ListView) findViewById(R.id.allergyList);
        editAllergies = (Button) findViewById(R.id.editAllergies);
        enterAllergy = (AutoCompleteTextView) findViewById((R.id.enterAllergy));
        saveAllChanges = (Button) findViewById(R.id.saveAll);
        saveAllergies = (Button) findViewById(R.id.saveAllergies);

        first = (EditText) findViewById(R.id.fn);
        last = (EditText) findViewById(R.id.ln);
        email = (EditText) findViewById(R.id.em);
        username = (EditText) findViewById(R.id.un);
        password = (EditText) findViewById(R.id.pw);
        confirmPassword = (EditText) findViewById(R.id.cpw);

        // Hide the password and confirmPassword field from the user
        password.setVisibility(View.GONE);
        confirmPassword.setVisibility(View.GONE);

        currentUser = LoginActivity.getCurrentUser();


        dietaryRestrictions = new ArrayList<>();
        allergyListAdpater = new ArrayAdapter<>(this, R.layout.mytextview, dietaryRestrictions);
        allergyList.setAdapter(allergyListAdpater);
        try {
            getUserInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        saveBasic.setVisibility(View.GONE);
        saveDietary.setVisibility(View.GONE);
        saveAllergies.setVisibility(View.GONE);


        changePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(i, GET_FROM_GALLERY);
            }
        });

        editBasic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //expand();
                for (int i = 0; i < basicInfoLayout.getChildCount();  i++ ){
                    View view = basicInfoLayout.getChildAt(i);
                    view.setEnabled(true);
                }
                // Show password fields for the user to edit
                password.setVisibility(View.VISIBLE);
                confirmPassword.setVisibility(View.VISIBLE);
                editBasic.setVisibility(View.GONE);
                saveBasic.setVisibility(View.VISIBLE);
            }
        });

        saveBasic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //collapse();
                JSONObject userInfo = new JSONObject();
                try {
                    // Create new user inforamtion json object to update the server
                    userInfo.put("first_name", first.getText().toString());
                    userInfo.put("last_name", last.getText().toString());
                    userInfo.put("email", email.getText().toString());
                    String newPassword = password.getText().toString();
                    if (!newPassword.isEmpty()) {
                        userInfo.put("password", newPassword);
                    }
                    updateUserInfo(userInfo);
                } catch(JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < basicInfoLayout.getChildCount();  i++) {
                    View view = basicInfoLayout.getChildAt(i);
                    view.setEnabled(false);
                }
                // Hide password fields again
                password.setVisibility(View.GONE);
                confirmPassword.setVisibility(View.GONE);
                editBasic.setVisibility(View.VISIBLE);
                saveBasic.setVisibility(View.GONE);
                editDietary.setEnabled(true);
            }
        });

        editDietary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (int i = 0; i < dietaryConcernsLayout.getChildCount();  i++ ){
                    View view = dietaryConcernsLayout.getChildAt(i);
                    view.setEnabled(true);
                }
                editDietary.setVisibility(View.GONE);
                saveDietary.setVisibility(View.VISIBLE);
            }
        });

        saveDietary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //collapse();
                JSONArray newDiets = new JSONArray();
                JSONObject updatedUser = new JSONObject();
                for (int i = 0; i < dietaryConcernsLayout.getChildCount();  i++) {
                    View view = dietaryConcernsLayout.getChildAt(i);
                    if (view instanceof CheckBox && ((CheckBox) view).isChecked()) {
                        newDiets.put(((CheckBox) view).getText());
                    }
                    view.setEnabled(false);
                }
                try {
                    updatedUser.put("dietary_concerns", newDiets);
                    updateUserInfo(updatedUser);
                } catch(JSONException e) {
                    e.printStackTrace();
                }
                saveDietary.setEnabled(true);
                saveDietary.setVisibility(View.GONE);
                editDietary.setVisibility(View.VISIBLE);
                editDietary.setEnabled(true);
            }
        });

        // Allergies
        enterAllergy.setVisibility(View.GONE);
        addAllergy.setVisibility(View.GONE);

        editAllergies.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enterAllergy.setVisibility(View.VISIBLE);
                addAllergy.setVisibility(View.VISIBLE);
                saveAllergies.setVisibility(View.VISIBLE);
                enterAllergy.clearListSelection();
                editAllergies.setVisibility(View.GONE);
            }
        });

        addAllergy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newAllergy = enterAllergy.getText().toString();
                if (!newAllergy.isEmpty()) {
                    dietaryRestrictions.add(newAllergy);
                    allergyListAdpater.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager) PreferencesActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    enterAllergy.setText("");
                }
            }
        });

        saveAllergies.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //collapse();
                JSONObject updatedUser = new JSONObject();
                try {
                    updatedUser.put("allergies", new JSONArray(dietaryRestrictions));
                    updateUserInfo(updatedUser);
                } catch (JSONException e) {
                    e.printStackTrace();;
                }
                addAllergy.setVisibility(View.GONE);
                enterAllergy.setVisibility(View.GONE);
                saveAllergies.setVisibility(View.GONE);
                editAllergies.setVisibility(View.VISIBLE);
                editDietary.setEnabled(true);
            }
        });

        saveAllChanges.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, MealPlanActivity.class);
                if(currentUser.isNewUser())
                {
                    currentUser.setNewUser(false);
                    intent = new Intent(PreferencesActivity.this, CalibrationActivity.class);
                }

                startActivity(intent);
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

        // Tab Screen Change Logic
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        TabLayout.Tab tab = tabs.getTabAt(3);
        tab.select();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // called when tab selected
                int tabIndex = tab.getPosition();
                Intent intent;
                switch (tabIndex) {
                    case 0:
                        intent = new Intent(PreferencesActivity.this, MealPlanActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // called when tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // called when a tab is reselected
            }
        });

        // Make the page look correctly for new users after registration
        if(currentUser.isNewUser()) {
            logout.setVisibility(View.GONE);
            deleteAccount.setVisibility(View.GONE);
            tabs.setVisibility(View.GONE);
        }
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

//    private void expand() {
//        basicInfoLayout.setVisibility(View.GONE);
//        basicInfoLayout2.setVisibility(View.VISIBLE);
//
//        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        basicInfoLayout2.measure(widthSpec, heightSpec);
//
//        ValueAnimator mAnimator = slideAnimator(0, basicInfoLayout2.getMeasuredHeight());
//        mAnimator.start();
//    }

//    private void collapse() {
//        basicInfoLayout2.setVisibility(View.GONE);
//        basicInfoLayout.setVisibility(View.VISIBLE);

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
//    }

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
        String email = LoginActivity.getCurrentUser().getRegistrationInfo().getEmail();

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

    private void getUserInfo() throws JSONException {
        int entity_pk = currentUser.getEntityPk();
        String entityEmail = currentUser.getRegistrationInfo().getEmail();
        String entityPassword = currentUser.getRegistrationInfo().getPassword();

        IntelliServerAPI.getUserInfo(entity_pk, entityEmail, entityPassword, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                try {
                    first.setText(result.getString("first_name"));
                    last.setText(result.getString("last_name"));
                    email.setText(result.getString("email"));
                    username.setText(result.getString("username"));

                    // TODO check if this works (need a user with preferences filled out
                    JSONArray dietaryConcerns = result.getJSONArray("dietary_concerns");
                    for (int i = 0; i < dietaryConcernsLayout.getChildCount() - 1; i++) {
                        CheckBox diet = (CheckBox) dietaryConcernsLayout.getChildAt(i);
                        diet.setChecked(false);
                        //TODO find better/efficient way
                        if (dietaryConcerns.toString().contains(diet.getText())) {
                            diet.setChecked(true);
                        }
                    }

                    JSONArray allergies = result.getJSONArray("allergies");
                    for (int i = 0; i < allergies.length(); i++) {
                        String allergy = allergies.getString(i);
                        dietaryRestrictions.add(allergy);
                    }

                    allergyListAdpater.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateUserInfo(JSONObject userInfo) throws JSONException {
        int entity_pk = currentUser.getEntityPk();
        IntelliServerAPI.updateUserInfo(entity_pk, userInfo, this.getApplicationContext(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                //TODO what to do...
            }
        });
    }
    //TODO: IntelliServerAPI.getUserInfo
    //v2.0/entities/<int:entity_pk> GET
    //Set textfields in onSuccess
    //PUT route with new user info

    private void removeAccount() throws JSONException {
        String email = LoginActivity.getCurrentUser().getRegistrationInfo().getEmail();
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

