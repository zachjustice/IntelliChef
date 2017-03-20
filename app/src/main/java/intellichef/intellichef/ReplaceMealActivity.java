package intellichef.intellichef;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ReplaceMealActivity extends AppCompatActivity {
    private int mealPlanPK;
    private SearchView searchView;
    private ListView listView;
    private boolean flag_loading; // true while loading the next set of recipes
    private int page; // the current page we're on
    private int page_size; // the size of the page
    private String searchQuery; // private so that rest of the class has access

    RecipeAdapter adapter;
    ArrayList<RecipeItem> recipeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_meal);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");
        mealPlanPK = getIntent().getIntExtra("mealPlanPK", -1);

        flag_loading = false;
        page = 0;
        page_size = 20;

        recipeItems = new ArrayList<>();
        searchView = (SearchView) findViewById(R.id.search);
        listView = (ListView) findViewById(R.id.list);

        searchView.setIconified(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RecipeItem selected = recipeItems.get(position);
                int recipePK = selected.getRecipePk();

                try {
                    updateMealPlan(recipePK);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // handle paginated scrolling for recipes
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0)
                {
                    if(!flag_loading)
                    {
                        flag_loading = true;

                        try {
                            showResults(searchQuery);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        // handle clearing recipes on the screen
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                clearList();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                try {
                    showResults(query);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }

            public boolean onQueryTextChange(String change) {
                if(change.equals("")) {
                    clearList();
                }
                return false;
            }
        });
    }

    private void showResults(String query) throws JSONException {
        IntelliServerAPI.searchRecipes(query, page, page_size, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray result) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject calibratedRecipe = result.getJSONObject(i);

                        String image_url = calibratedRecipe.getString("image_url");
                        String name = calibratedRecipe.getString("name");
                        int recipe_pk = calibratedRecipe.getInt("recipe_pk");

                        RecipeItem item = new RecipeItem(image_url, name, recipe_pk);
                        recipeItems.add(item);
                    }

                    adapter = new RecipeAdapter(ReplaceMealActivity.this, R.layout.recipe_view, recipeItems);
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

    private void updateMealPlan(int recipePK) throws JSONException {
        IntelliServerAPI.updateMealPlan(mealPlanPK, recipePK, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {
                Intent intent = new Intent(ReplaceMealActivity.this, MealPlanActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clearList() {
        page = 0;
        recipeItems = new ArrayList<>();
        adapter = new RecipeAdapter(ReplaceMealActivity.this, R.layout.recipe_view, recipeItems);

        adapter.notifyDataSetChanged();
        listView.setAdapter(null);
        adapter.notifyDataSetChanged();
    }
}
