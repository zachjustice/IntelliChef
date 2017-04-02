package intellichef.intellichef;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private ImageView breakfastRating;
    private ImageView lunchRating;
    private ImageView dinnerRating;
    private DateTime viewDate;
    private DateTime today;
    private Meal breakfastMeal;
    private Meal lunchMeal;
    private Meal dinnerMeal;
    private Button breakfastReplaceButton;
    private Button lunchReplaceButton;
    private Button dinnerReplaceButton;
    private Button breakfastRateButton;
    private Button lunchRateButton;
    private Button dinnerRateButton;

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

        breakfastRating = (ImageView) findViewById(R.id.breakfast_rating);
        lunchRating = (ImageView) findViewById(R.id.lunch_rating);
        dinnerRating = (ImageView) findViewById(R.id.dinner_rating);


        //for query
        final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        //for textfield
        final DateTimeFormatter displayFormatter = DateTimeFormat.forPattern("EEEE, MMMM d");

        //change today for testing
        today = new DateTime().minusDays(3);
        viewDate = new DateTime().minusDays(3);

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
        Log.wtf("HELLO", "I'm here2");

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //show prev button
                Log.wtf("HELLO", "I'm here");
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
                Intent intent;
                switch (tabIndex) {
                    case 0:
                        break;
                    case 1:
                        intent = new Intent(MealPlanActivity.this, MealHistoryActivity.class);
                        intent.putExtra("entityPk", entityPk);
                        startActivity(intent);
                        break;
                    case 2:
                        break;
                    case 3:
                        intent = new Intent(MealPlanActivity.this, PreferencesActivity.class);
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

    private void showMealPlans(final int entityPk, String date) throws JSONException {
        final String dateCopy = date;
        breakfastRating.setBackgroundResource(0);
        lunchRating.setBackgroundResource(0);
        dinnerRating.setBackgroundResource(0);
        Log.wtf("HELLO", "Showing meal plan");

        IntelliServerAPI.getMealPlan(entityPk, date, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                Log.wtf("HELLO", "I'm succeeding");
                breakfastMeal = new Meal();
                try {
                    breakfastMeal.fillParams(result.getJSONObject("breakfast"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                breakfastName.setText(breakfastMeal.getName());
                ImageExtractor.loadIntoImage(getApplicationContext(), breakfastMeal.getPhotoUrl(), breakfastPic, 250, 130);
                breakfastPic.setTag("breakfast " + dateCopy);
                try {
                    getUserRating(entityPk, breakfastMeal.getRecipePK(), breakfastRating);
                } catch (JSONException e) {
                    Log.v("JSON", e.getMessage());
                }

                lunchMeal = new Meal();
                try {
                    lunchMeal.fillParams(result.getJSONObject("lunch"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lunchName.setText(lunchMeal.getName());
                ImageExtractor.loadIntoImage(getApplicationContext(), lunchMeal.getPhotoUrl(), lunchPic);
                lunchPic.setTag("lunch " + dateCopy);
                try {
                    getUserRating(entityPk, lunchMeal.getRecipePK(), lunchRating);
                } catch (JSONException e) {
                    Log.v("JSON", e.getMessage());
                }

                dinnerMeal = new Meal();
                try {
                    dinnerMeal.fillParams(result.getJSONObject("dinner"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dinnerName.setText(dinnerMeal.getName());
                ImageExtractor.loadIntoImage(getApplicationContext(), dinnerMeal.getPhotoUrl(), dinnerPic, 250, 130);
                dinnerPic.setTag("dinner " + dateCopy);
                try {
                    getUserRating(entityPk, dinnerMeal.getRecipePK(), dinnerRating);
                } catch (JSONException e) {
                    Log.v("JSON", e.getMessage());
                }

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


                breakfastRateButton = (Button) findViewById(R.id.rateBreakfast);
                breakfastRateButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        promptRating(breakfastMeal.getName(), breakfastMeal.getPhotoUrl(), breakfastMeal.getRecipePK(), breakfastRating);
                    }
                });
                lunchRateButton = (Button) findViewById(R.id.rateLunch);
                lunchRateButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        promptRating(lunchMeal.getName(), lunchMeal.getPhotoUrl(), lunchMeal.getRecipePK(), lunchRating);

                    }
                });
                dinnerRateButton = (Button) findViewById(R.id.rateDinner);
                dinnerRateButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        promptRating(dinnerMeal.getName(), dinnerMeal.getPhotoUrl(), dinnerMeal.getRecipePK(), dinnerRating);

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

    public void promptRating(String recipeName, String imageUrl, Integer recipePK, final ImageView sourceView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MealPlanActivity.this);

        final int entityPk = LoginActivity.getCurrentUser().getEntityPk();
        final int recipePk = recipePK;


        ImageView view = new ImageView(MealPlanActivity.this);
        ImageExtractor.loadIntoImage(MealPlanActivity.this, imageUrl, view, 250, 130);
        Log.wtf("RATING", "ENTITY" + entityPk);
        Log.wtf("RATING", "RECIPE" + recipePk);

        builder.setTitle("Rate the Recipe!").setMessage("What did you think of the " + recipeName + "?\n").setView(view)
                .setPositiveButton("I liked this!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            if (sourceView.getBackground() == null) {
                                insertUserRating(entityPk, recipePk, 1, MealPlanActivity.this, sourceView);
                            } else {
                                updateUserRating(entityPk, recipePk, 1, MealPlanActivity.this, sourceView);
                            }
                        } catch (JSONException e) {
                            Log.v("JSON", e.getMessage());
                        }
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing
                    }
                })
                .setNegativeButton("I didn't like this.",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // code to saving rating in db
                        try {
                            if (sourceView.getBackground() == null) {
                                insertUserRating(entityPk, recipePk, 0, MealPlanActivity.this, sourceView);
                            } else {
                                updateUserRating(entityPk, recipePk, 0, MealPlanActivity.this, sourceView);
                            }
                        } catch (JSONException e) {
                            Log.v("JSON", e.getMessage());

                        }
                    }
                });
        final AlertDialog a = builder.create();
        a.show();
    }

    public static void insertUserRating(int entityPk, int recipePk, final int rating, Context context, final ImageView ratingImage) throws JSONException {
        IntelliServerAPI.insertUserRating(context, entityPk, recipePk, rating, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                try {
                    Log.wtf("RATING", "INSERTING RATING");
                    if (rating == 0) {
                        ratingImage.setBackgroundResource(R.drawable.broken_heart);
                    } else {
                        ratingImage.setBackgroundResource(R.drawable.heart);
                    }
                } catch (Exception e) {
                    Log.wtf("JSONObject", "" + e.getMessage());
                }
            }
        });

    }
    public static void getUserRating(int entityPk, int recipePk, final ImageView ratingImage) throws JSONException {
        IntelliServerAPI.getUserRating(entityPk, recipePk, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                try {
                    Log.wtf("RATING", "GETTING RATING");
                    if (result.get("rating") != null) {
                        int rating = Integer.parseInt("" + result.get("rating"));
                        if (rating == 0) {
                            ratingImage.setBackgroundResource(R.drawable.broken_heart);
                        } else {
                            ratingImage.setBackgroundResource(R.drawable.heart);
                        }
                    } else {
                        Log.wtf("RATING", "NO RATING FOR RECIPE");
                        ratingImage.setBackgroundResource(0);
                    }
                } catch (Exception e) {
                    Log.wtf("JSONObject", "" + e.getMessage());
                }
            }
        });

    }
    public static void updateUserRating(int entityPk, int recipePk, final int rating, Context context, final ImageView ratingImage) throws JSONException {
        IntelliServerAPI.updateUserRating(context, entityPk, recipePk, rating, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                try {
                    Log.wtf("RATING", "UPDATING RATING");
                    if (rating == 0) {
                        ratingImage.setBackgroundResource(R.drawable.broken_heart);
                    } else {
                        ratingImage.setBackgroundResource(R.drawable.heart);
                    }
                } catch (Exception e) {
                    Log.wtf("JSONObject", "" + e.getMessage());
                }
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

}
