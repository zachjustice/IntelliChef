package intellichef.intellichef;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;

public class MealPlanActivity extends AppCompatActivity {
    private TextView date;
    private Button prevButton;
    private Button nextButton;
    private TextView breakfastName;
    private TextView lunchName;
    private TextView dinnerName;
    private Toolbar toolbar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_meal_plan);
        date = (TextView) findViewById(R.id.dateText);
        breakfastName = (TextView) findViewById(R.id.breakfast_name);
        lunchName = (TextView) findViewById(R.id.lunch_name);
        dinnerName = (TextView) findViewById(R.id.dinner_name);

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

        prevButton = (Button) findViewById(R.id.prevRecipe);
        prevButton.setVisibility(View.GONE);
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
                    prevButton.setVisibility(View.GONE);
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
                    nextButton.setVisibility(View.GONE);
                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void showMealPlans(String date) throws JSONException {

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

                Recipe lunchRecipe = new Recipe();
                try {
                    lunchRecipe.fillParams(result.getJSONObject("lunch"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lunchName.setText(lunchRecipe.getName());

                Recipe dinnerRecipe = new Recipe();
                try {
                    dinnerRecipe.fillParams(result.getJSONObject("dinner"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dinnerName.setText(dinnerRecipe.getName());
            }
        });

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MealPlan Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
