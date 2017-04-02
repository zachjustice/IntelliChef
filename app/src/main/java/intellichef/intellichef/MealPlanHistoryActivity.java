package intellichef.intellichef;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MealPlanHistoryActivity extends AppCompatActivity {

    ArrayList<MealPlanItem> mealPlanItems;
    ListView mealPlanListView;
    MealPlansAdapter mealPlansAdapter; // outer adapter that holds list(View) of meals for the given date
//    ListView meals;

    final int entityPk = LoginActivity.getCurrentUser().getEntityPk();
    RecipeAdapter recipeAdapter;
    ArrayList<RecipeItem> recipeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_history);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");

        recipeItems = new ArrayList<>();
        mealPlanListView = (ListView) findViewById(R.id.mealPlansListView);
        mealPlanItems = new ArrayList<>();
        populateMealPlanItems(7, new DateTime(), entityPk); // how many items to populate initially

    }
    public void populateMealPlanItems(int days, DateTime date, int entityPk) {
        final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd"); //query
        View view = this.getLayoutInflater().inflate(R.layout.meal_plan_view, null, false);
        mealPlansAdapter = new MealPlansAdapter(MealPlanHistoryActivity.this, R.layout.meal_plan_view, mealPlanItems);
        try {
            for (int i = 0; i < days; i++) {
                date = date.plusDays(-1);
                ListView meals = (ListView) view.findViewById(R.id.mealListView);
                MealPlanItem mealPlanItem = new MealPlanItem(formatter.print(date), meals);
                mealPlanItems.add(mealPlanItem);
                populateMealsforDay(entityPk, formatter.print(date), i);
            }
//            mealPlanItems.add(new MealPlanItem("", null));
            mealPlanListView.setAdapter(mealPlansAdapter);
            mealPlansAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.v("JSONObject", "" + e.getMessage());
        }
    }

    public void populateMealsforDay(int entityPk, String date, final int position) throws JSONException {
        final String finalDate = date;
//        final ListView finalMeal = meals;
        IntelliServerAPI.getMealPlan(entityPk, date, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
//                recipeItems = new ArrayList<>();
                Meal breakfastMeal = new Meal();
                try {
                    breakfastMeal.fillParams(result.getJSONObject("breakfast"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RecipeItem breakfastItem = new RecipeItem(breakfastMeal.getPhotoUrl(), breakfastMeal.getName(), breakfastMeal.getRecipePK());
                recipeItems.add(breakfastItem);

                Meal lunchMeal = new Meal();
                try {
                    lunchMeal.fillParams(result.getJSONObject("lunch"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RecipeItem lunchItem = new RecipeItem(lunchMeal.getPhotoUrl(), lunchMeal.getName(), lunchMeal.getRecipePK());
                recipeItems.add(lunchItem);

                Meal dinnerMeal = new Meal();
                try {
                    dinnerMeal.fillParams(result.getJSONObject("dinner"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RecipeItem dinnerItem = new RecipeItem(dinnerMeal.getPhotoUrl(), dinnerMeal.getName(), dinnerMeal.getRecipePK());
                recipeItems.add(dinnerItem);


//                View view = MealPlanHistoryActivity.this.getLayoutInflater().inflate(R.layout.meal_plan_view, null, false);

                recipeAdapter = new RecipeAdapter(MealPlanHistoryActivity.this, R.layout.recipe_view, recipeItems);
                recipeAdapter.notifyDataSetChanged();
                mealPlanItems.get(position).getMeals().setAdapter(recipeAdapter);
                recipeAdapter.notifyDataSetChanged();


                //TODO Need to add the date if the user actually has a meal plan for that day
//                MealPlanItem mealPlanItem = new MealPlanItem(finalDate, finalMeal);
//                mealPlanItems.add(mealPlanItem);
//                mealPlanListView.setAdapter(mealPlansAdapter);
//                mealPlansAdapter.notifyDataSetChanged();

            }
        });
    }

}
