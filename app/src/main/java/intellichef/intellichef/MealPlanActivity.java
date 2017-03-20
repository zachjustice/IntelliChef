package intellichef.intellichef;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


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
    private DateTime viewDate;
    private DateTime today;
    private Meal breakfastMeal;
    private Meal lunchMeal;
    private Meal dinnerMeal;
    private Button breakfastReplaceButton;
    private Button lunchReplaceButton;
    private Button dinnerReplaceButton;

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

        //for query
        final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        //for textfield
        final DateTimeFormatter displayFormatter = DateTimeFormat.forPattern("EEEE, MMMM d");

        //change today for testing
        today = new DateTime();//.plusDays(1);
        viewDate = new DateTime();

        final int weekDay = today.getDayOfWeek();
        final int entityPk = LoginActivity.getCurrentUser().getEntityPk();

        date.setText(displayFormatter.print(viewDate));

        try {
            showMealPlans(entityPk, formatter.print(viewDate));
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
                try {
                    viewDate = viewDate.plusDays(-1);
                    showMealPlans(entityPk, formatter.print(viewDate));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                date.setText(displayFormatter.print(viewDate));
                if (viewDate.getDayOfWeek() == today.getDayOfWeek()) {
                    prevButton.setVisibility(View.INVISIBLE);
                }

            }

        });


        nextButton = (Button) findViewById(R.id.nextRecipe);
        if (weekDay == 7) {
            nextButton.setVisibility(View.INVISIBLE);
        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //show prev button
                prevButton.setVisibility(View.VISIBLE);
                try {
                    viewDate = viewDate.plusDays(1);
                    showMealPlans(entityPk, formatter.print(viewDate));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                date.setText(displayFormatter.print(viewDate));
                if (viewDate.getDayOfWeek() == 7) {
                    nextButton.setVisibility(View.INVISIBLE);
                }
            }
        });



        // Show recipe clicked
        breakfastPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openRecipeScreen(breakfastMeal);
            }
        });
        lunchPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openRecipeScreen(lunchMeal);
            }
        });
        dinnerPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openRecipeScreen(dinnerMeal);
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

    private void showMealPlans(int entityPk, String date) throws JSONException {
        final String dateCopy = date;

        IntelliServerAPI.getMealPlan(entityPk, date, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                breakfastMeal = new Meal();
                try {
                    breakfastMeal.fillParams(result.getJSONObject("breakfast"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                breakfastName.setText(breakfastMeal.getName());
                ImageExtractor.loadIntoImage(getApplicationContext(), breakfastMeal.getPhotoUrl(), breakfastPic);
                //breakfastRating.setText("" + breakfastMeal.getRating());
                breakfastPic.setTag("breakfast " + dateCopy);

                lunchMeal = new Meal();
                try {
                    lunchMeal.fillParams(result.getJSONObject("lunch"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lunchName.setText(lunchMeal.getName());
                ImageExtractor.loadIntoImage(getApplicationContext(), lunchMeal.getPhotoUrl(), lunchPic);
                //lunchRating.setText("" + lunchMeal.getRating());
                lunchPic.setTag("lunch " + dateCopy);

                dinnerMeal = new Meal();
                try {
                    dinnerMeal.fillParams(result.getJSONObject("dinner"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dinnerName.setText(dinnerMeal.getName());
                ImageExtractor.loadIntoImage(getApplicationContext(), dinnerMeal.getPhotoUrl(), dinnerPic);
                //dinnerRating.setText("" + dinnerMeal.getRating());
                dinnerPic.setTag("dinner " + dateCopy);

                // need to get meal plan pk's before setting up replace buttons
                breakfastReplaceButton = (Button) findViewById(R.id.replaceBreakfast);
                breakfastReplaceButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MealPlanActivity.this, ReplaceMealActivity.class);
                        Log.v("replace", "bfast meal pk " + breakfastMeal.getMealPlanPK());
                        intent.putExtra("mealPlanPK", breakfastMeal.getMealPlanPK());
                        startActivity(intent);
                    }
                });

                lunchReplaceButton = (Button) findViewById(R.id.replaceLunch);
                lunchReplaceButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MealPlanActivity.this, ReplaceMealActivity.class);
                        intent.putExtra("mealPlanPK", lunchMeal.getMealPlanPK());
                        startActivity(intent);
                    }
                });

                dinnerReplaceButton = (Button) findViewById(R.id.replaceDinner);
                dinnerReplaceButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MealPlanActivity.this, ReplaceMealActivity.class);
                        intent.putExtra("mealPlanPK", dinnerMeal.getMealPlanPK());
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public void openRecipeScreen(Recipe recipe) {
        if (recipe == null) {
            System.out.println("Recipe is null");
            return;
        }
        Intent intent = new Intent(getBaseContext(), ViewRecipeActivity.class);
        intent.putExtra("recipePk", recipe.getRecipePK());
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

}
