package intellichef.intellichef;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CalibrationActivity extends AppCompatActivity {

    Button submit;
    ArrayList<RecipeItem> recipeItems;
    ListView listview;
    RecipeAdapter adapter;
    ArrayList<Integer> calibrationPks = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");

        listview = (ListView) findViewById(R.id.listView);
        recipeItems = new ArrayList<>();
        submit = (Button) findViewById(R.id.submit);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            ColorStateList oldColors =  null;
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RecipeItem selected = recipeItems.get(position);
                selected.toggleSelected();
                if (selected.isSelected()) {
                    view.setBackgroundResource(R.drawable.layout);
                    view.setBackgroundColor(Color.rgb(245,141,116));
                    TextView tv = (TextView) view.findViewById(R.id.recipeName);
                    oldColors = tv.getTextColors();
                    tv.setTextColor(Color.WHITE);

                } else {
                    view.setBackgroundDrawable(null);
                    TextView tv = (TextView) view.findViewById(R.id.recipeName);
                    tv.setTextColor(oldColors);
                }
            }
        });


        try {
            populateCalibratedMeals();

        } catch (JSONException e) {
            Log.v("JSONObject", "" + e.getMessage());
        }

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calibrationPks = new ArrayList<>();
                for (RecipeItem c: recipeItems) {
                    if (c.isSelected()) {
                        calibrationPks.add(c.getRecipePk());
                    } else {
                        calibrationPks.remove(c.getRecipePk());
                    }
                }
                Log.v("Calibrated", "" + calibrationPks.size());
                if (calibrationPks.size() < 5) {
                    Toast.makeText(CalibrationActivity.this, "Please select at least 5 recipes!",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        User currentUser = LoginActivity.getCurrentUser();
                        updateUserCalibrationPicks(calibrationPks, currentUser.getEntityPk(), CalibrationActivity.this);
                        generateMealPlan(CalibrationActivity.this, currentUser.getEntityPk());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(getBaseContext(), MealPlanActivity.class);
                                startActivity(intent);
                            }
                        }, 10000);

                    } catch (JSONException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
    }

    public void populateCalibratedMeals() throws JSONException {

        IntelliServerAPI.getCalibratedMeals(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray result) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject calibratedRecipe = (JSONObject) result.get(i);
                        recipeItems.add(new RecipeItem("" + calibratedRecipe.get("image_url"), "" + calibratedRecipe.get("name"), (Integer) calibratedRecipe.get("recipe_pk")));
                    }
                    //randomize
                    Collections.shuffle(recipeItems);
                    adapter = new RecipeAdapter(CalibrationActivity.this, R.layout.recipe_view, recipeItems);
                    adapter.notifyDataSetChanged();
                    listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Log.v("JSONObject", "" + e.getMessage());
                }
            }
        });
    }

    public void updateUserCalibrationPicks(ArrayList<Integer> calibrationPks, int entityPk, Context context) throws JSONException {
        for (Integer calibrationPk: calibrationPks) {
            IntelliServerAPI.insertUserCalibrationPick(context, entityPk, calibrationPk, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                    try {
                        Log.v("calibration", "INSERTING CALIBRATION RECIPE");
                    } catch (Exception e) {
                        Log.v("JSONObject", "" + e.getMessage());
                    }
                }
            });
        }
    }

    public void generateMealPlan(Context context, int entityPk) throws JSONException {

        IntelliServerAPI.generateMealPlan(context, entityPk, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                    Log.v("MealPlan", "generated succesfully");
                    Intent intent = new Intent(CalibrationActivity.this, MealPlanActivity.class);
                    startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

}
