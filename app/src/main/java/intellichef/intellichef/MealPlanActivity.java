package intellichef.intellichef;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;

public class MealPlanActivity extends AppCompatActivity {
    private TextView date;
    private Button prevButton;
    private Button nextButton;
    private TextView breakfastName;
    private TextView lunchName;
    private TextView dinnerName;
    private ImageView breakfastPic;
    private ImageView lunchPic;
    private ImageView dinnerPic;
    private TextView breakfastRating;
    private TextView lunchRating;
    private TextView dinnerRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");
        setContentView(R.layout.activity_meal_plan);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_meal_plan);
        date = (TextView) findViewById(R.id.dateText);
        breakfastName = (TextView) findViewById(R.id.breakfast_name);
        lunchName = (TextView) findViewById(R.id.lunch_name);
        dinnerName = (TextView) findViewById(R.id.dinner_name);

        breakfastPic = (ImageView) findViewById(R.id.breakfast_pic);
        lunchPic = (ImageView) findViewById(R.id.lunch_pic);
        dinnerPic = (ImageView) findViewById(R.id.dinner_pic);

        breakfastRating = (TextView) findViewById(R.id.breakfast_rating);
        lunchRating = (TextView) findViewById(R.id.lunch_rating);
        dinnerRating = (TextView) findViewById(R.id.dinner_rating);

        breakfastRating.setVisibility(View.GONE);
        lunchRating.setVisibility(View.GONE);
        dinnerRating.setVisibility(View.GONE);

        // Dynamic date display
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd yyyy");
//        date.setText(dateFormat.format(c.getTime()));
//        dateFormat = new SimpleDateFormat("M-d-yyyy");
//        getMealPlans(dateFormat.foramt(c.getTime()));

        // Date Display from March 1 to 7
        String s1 = "Mar 01, 2017";
        String s2 = "Mar 07, 2017";
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        final Calendar calendarFrom = Calendar.getInstance();
        final Calendar calendarUntil = Calendar.getInstance();

        try {
            calendarFrom.setTime(dateFormat.parse(s1));
            calendarUntil.setTime(dateFormat.parse(s2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date.setText(dateFormat.format(calendarFrom.getTime()));

        // Show meal plan for Mar 1
        try {
            showMealPlans("3-1-2017");
        } catch (Exception e) {
            System.out.println("Failed to show recipes");
        }

        // Change meal plans displayed on the screen when buttons are pressed
        prevButton = (Button) findViewById(R.id.prevRecipe);
        prevButton.setVisibility(View.INVISIBLE);
        prevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Show next button
                nextButton.setVisibility(View.VISIBLE);
                calendarFrom.add(Calendar.DATE, -1);
                try {
                    showMealPlans("3-" + calendarFrom.get(Calendar.DATE) + "-2017");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                date.setText(dateFormat.format(calendarFrom.getTime()));
                if (calendarFrom.get(Calendar.DATE) == 1) {
                    //If on the first page (Mar 1), hide prev button so that the user can't go before Mar 1
                    prevButton.setVisibility(View.INVISIBLE);
                }

            }

        });

        nextButton = (Button) findViewById(R.id.nextRecipe);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: setOnTouchListener for pressed effects
                //show prev button
                prevButton.setVisibility(View.VISIBLE);
                calendarFrom.add(Calendar.DATE, 1);
                try {
                    showMealPlans("3-" + calendarFrom.get(Calendar.DATE) + "-2017");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                date.setText(dateFormat.format(calendarFrom.getTime()));
                if (calendarFrom.compareTo(calendarUntil) == 0) {
                    // if on the last screen (Mar 7), hide the next button
                    nextButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Tab Screen Change Logic
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // called when tab selected
                int tabIndex = tab.getPosition();
                switch (tabIndex) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        Intent intent = new Intent(MealPlanActivity.this, PreferencesActivity.class);
                        startActivity(intent);
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
    }


    private void showMealPlans(String date) throws JSONException {

        final String dateCopy = date;

        IntelliServerAPI.getRecipes(date, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                Recipe breakfastRecipe = new Recipe();
                try {
                    breakfastRecipe.fillParams(result.getJSONObject("breakfast"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                breakfastName.setText(breakfastRecipe.getName());
                ImageExtractor.loadIntoImage(getApplicationContext(), breakfastRecipe.getPhotoUrl(), breakfastPic);
                breakfastRating.setText("" + breakfastRecipe.getRating());
                breakfastPic.setTag("breakfast " + dateCopy);

                Recipe lunchRecipe = new Recipe();
                try {
                    lunchRecipe.fillParams(result.getJSONObject("lunch"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lunchName.setText(lunchRecipe.getName());
                ImageExtractor.loadIntoImage(getApplicationContext(), lunchRecipe.getPhotoUrl(), lunchPic);
                lunchRating.setText("" + lunchRecipe.getRating());
                lunchPic.setTag("lunch" + dateCopy);

                Recipe dinnerRecipe = new Recipe();
                try {
                    dinnerRecipe.fillParams(result.getJSONObject("dinner"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dinnerName.setText(dinnerRecipe.getName());
                ImageExtractor.loadIntoImage(getApplicationContext(), dinnerRecipe.getPhotoUrl(), dinnerPic);
                dinnerRating.setText("" + dinnerRecipe.getRating());
                dinnerPic.setTag("dinner" + dateCopy);
            }
        });

    }

    // TODO Is there a way to store a Recipe object created in showMealPlans function
    public void onRecipeImageClick(View view) {
        String[] mealDate = ((String) view.getTag()).split(" ");
        System.out.println(mealDate[0] + " " + mealDate[1]);
        try {
            showRecipe(mealDate[1], mealDate[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showRecipe(String date, String meal) throws JSONException {

        final String mealCopy = meal;
        IntelliServerAPI.getRecipes(date, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                int recipePK;
                try {
                    recipePK = result.getJSONObject(mealCopy).getInt("recipe");

                    Intent intent = new Intent(getBaseContext(), ViewRecipeActivity.class);
                    intent.putExtra("recipePK", recipePK);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
