package intellichef.intellichef;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MealHistoryActivity extends AppCompatActivity {
    private int entityPk;
    private ListView listView;
    private boolean flag_loading; // true while loading the next set of recipes
    private DateTime currDate;
    private final int numDays = -7;

    private RecipeAdapter adapter;
    private ArrayList<RecipeItem> recipeItems;
    private boolean moreResults;

    private boolean isFavoriteChecked;
    private boolean isBreakfastChecked;
    private boolean isLunchChecked;
    private boolean isDinnerChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_history);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");
        entityPk = LoginActivity.getCurrentUser().getEntityPk();

        flag_loading = false;
        moreResults = true;

        recipeItems = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);

        currDate = new DateTime();

        try {
            // start at 7 days ago until the current day
            showResults(currDate.minusDays(numDays), currDate, entityPk);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RecipeItem selected = recipeItems.get(position);
                int recipePK = selected.getRecipePk();

                Intent intent = new Intent(MealHistoryActivity.this, ViewRecipeActivity.class);
                intent.putExtra("recipePk", recipePK);
                startActivity(intent);
            }
        });

        // handle paginated scrolling for recipes
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0)
                {
                    // only load more results if we've finished loading the last set
                    // AND if there are more results to get.
                    if(!flag_loading && moreResults)
                    {
                        flag_loading = true;

                        try {
                            showResults(currDate.minusDays(numDays), currDate, entityPk);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        // Tab Screen Change Logic
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        TabLayout.Tab tab = tabs.getTabAt(1);
        tab.select();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // called when tab selected
                int tabIndex = tab.getPosition();
                Intent intent;
                switch (tabIndex) {
                    case 0:
                        intent = new Intent(MealHistoryActivity.this, MealPlanActivity.class);
                        intent.putExtra("entityPk", entityPk);
                        startActivity(intent);
                        break;
                    case 1:
                        break;
                    case 2:
                        intent = new Intent(MealHistoryActivity.this, GroceryListActivity.class);
                        intent.putExtra("entityPk", entityPk);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MealHistoryActivity.this, PreferencesActivity.class);
                        intent.putExtra("entityPk", entityPk);
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

        // Filter Logic
        CheckBox favorite = (CheckBox) findViewById(R.id.favoriteBox);
        CheckBox breakfast = (CheckBox) findViewById(R.id.breakfastBox);
        CheckBox lunch = (CheckBox) findViewById(R.id.lunchBox);
        CheckBox dinner = (CheckBox) findViewById(R.id.dinnerBox);

        favorite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isFavoriteChecked = !isFavoriteChecked;
                regenerateMealHistory();
            }
        });
        breakfast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isBreakfastChecked = !isBreakfastChecked;
                regenerateMealHistory();
            }
        });
        lunch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isLunchChecked = !isLunchChecked;
                regenerateMealHistory();
            }
        });
        dinner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isDinnerChecked = !isDinnerChecked;
                regenerateMealHistory();
            }
        });
    }

    private void showResults(final DateTime startDate, final DateTime endDate, int entityPk) throws JSONException {

        IntelliServerAPI.getMealPlanHistory(startDate, endDate, entityPk, isFavoriteChecked, isBreakfastChecked, isLunchChecked, isDinnerChecked, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {

                if(result.length() <= 0)
                {
                    // Keeps track of when we've run out of results
                    moreResults = false;

                    // Return with no results to avoid scroll-to-top bug
                    return;
                }

                final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

                Log.v("RESULT", "" + result.length());

                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject meal = result.getJSONObject(formatter.print(startDate.plusDays(i)));
                        String[] mealNames = {"breakfast", "lunch", "dinner"};

                        for (String mealName : mealNames) {
                            if (meal.has(mealName)) {
                                JSONObject recipe = meal.getJSONObject(mealName);

                                String image_url = recipe.getString("image_url");
                                String name = recipe.getString("name");
                                int recipe_pk = recipe.getInt("recipe_pk");

                                RecipeItem item = new RecipeItem(image_url, name, recipe_pk);
                                recipeItems.add(item);
                            } else {
                                Log.e("MealPlanHistory", "No " + mealName + " for " + startDate.plusDays(i));
                            }
                        }
                    }

                    adapter = new RecipeAdapter(MealHistoryActivity.this, R.layout.recipe_view, recipeItems);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    flag_loading = false;

                } catch (Exception e) {
                    Log.e("JSONObject Exception", "" + e.getMessage());
                }
            }
        });
    }

    private void regenerateMealHistory() {
        currDate = new DateTime();
        recipeItems.clear();
        adapter.notifyDataSetChanged();
        try {
            showResults(currDate.plusDays(numDays), currDate, entityPk);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
