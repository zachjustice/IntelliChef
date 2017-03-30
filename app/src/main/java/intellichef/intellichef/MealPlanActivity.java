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
                breakfastPic.setTag("breakfast " + dateCopy);

                lunchMeal = new Meal();
                try {
                    lunchMeal.fillParams(result.getJSONObject("lunch"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lunchName.setText(lunchMeal.getName());
                ImageExtractor.loadIntoImage(getApplicationContext(), lunchMeal.getPhotoUrl(), lunchPic);
                lunchPic.setTag("lunch " + dateCopy);

                dinnerMeal = new Meal();
                try {
                    dinnerMeal.fillParams(result.getJSONObject("dinner"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dinnerName.setText(dinnerMeal.getName());
                ImageExtractor.loadIntoImage(getApplicationContext(), dinnerMeal.getPhotoUrl(), dinnerPic);
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


                breakfastRateButton = (Button) findViewById(R.id.rateBreakfast);
                breakfastRateButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        promptRating(breakfastMeal.getName(), breakfastMeal.getPhotoUrl());
                    }
                });
                lunchRateButton = (Button) findViewById(R.id.rateLunch);
                lunchRateButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        promptRating(lunchMeal.getName(), lunchMeal.getPhotoUrl());

                    }
                });
                dinnerRateButton = (Button) findViewById(R.id.rateDinner);
                dinnerRateButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        promptRating(dinnerMeal.getName(), dinnerMeal.getPhotoUrl());

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

    public void promptRating(String recipeName, String imageUrl) {
        RatingDialog dialog = new RatingDialog();
        Bundle args = new Bundle();
        args.putString("name", recipeName);
        args.putString("photo", imageUrl);
        dialog.setArguments(args);
        dialog.show(MealPlanActivity.this.getFragmentManager(), "RatingDialog");
    }

    public static class RatingDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String name = getArguments().getString("name");
            String imageUrl = getArguments().getString("photo");
            ImageView view = new ImageView(getActivity());
            ImageExtractor.loadIntoImage(getActivity(), imageUrl, view);
            builder.setTitle("Rate the Recipe!").setMessage("What did you think of the " + name + "?").setView(view)
                    .setPositiveButton("Great!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // code to save rating in db
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // code to save rating in db
                        }
                    })
                    .setNegativeButton("I'd rather eat Panda Express...", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // code to saving rating in db
                        }
                    });


            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

}
