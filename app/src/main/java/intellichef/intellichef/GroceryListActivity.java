package intellichef.intellichef;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GroceryListActivity extends AppCompatActivity {
    private ListView listView;

    private GroceryListAdapter adapter;
    private ArrayList<GroceryListItem> groceryList;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");
        int entityPk = LoginActivity.getCurrentUser().getEntityPk();

        groceryList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);
        spinner = (ProgressBar) findViewById(R.id.progress);

        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek();
        // If it's Tuesday (2nd day of week), subtract 1 day to get Monday
        LocalDate prevMonday = today.minusDays(dayOfWeek - 1);
        // If it's Tuesday (2nd day of week), add 5 days to get Sunday
        LocalDate nextSunday = today.plusDays(7 - dayOfWeek);

        Log.v("GL", prevMonday.toString() + " " + nextSunday.toString());

        try {
            showResults(prevMonday, nextSunday, entityPk);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                GroceryListItem selected = groceryList.get(position);
            }
        });

        // Tab Screen Change Logic
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        TabLayout.Tab tab = tabs.getTabAt(2);
        tab.select();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // called when tab selected
                int tabIndex = tab.getPosition();
                Intent intent;
                switch (tabIndex) {
                    case 0:
                        intent = new Intent(GroceryListActivity.this, MealPlanActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(GroceryListActivity.this, MealHistoryActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        break;
                    case 3:
                        intent = new Intent(GroceryListActivity.this, PreferencesActivity.class);
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

    private void showResults(final LocalDate startDate, final LocalDate endDate, int entityPk) throws JSONException {
        spinner.setVisibility(View.VISIBLE);
        IntelliServerAPI.getGroceryList(startDate, endDate, entityPk, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject groceryListJSON) {

                try {
                    Iterator<?> keys = groceryListJSON.keys();

                    while( keys.hasNext() ) {
                        String key = (String)keys.next();

                        if ( groceryListJSON.get(key) instanceof JSONArray) {
                            JSONArray ingredientUses = groceryListJSON.getJSONArray(key);
                            ArrayList<String> ingredientDescriptions = new ArrayList<>();

                            for(int i = 0; i < ingredientUses.length(); i++) {
                                JSONObject instanceOfUse = ingredientUses.getJSONObject(i);

                                String ingredientDescription = instanceOfUse.getString("ingredient_description");
                                String recipeName = instanceOfUse.getString("recipe_name");

                                ingredientDescriptions.add(ingredientDescription + " (" + recipeName + ")");
                            }

                            GroceryListItem groceryListItem = new GroceryListItem(key, ingredientDescriptions);
                            groceryList.add(groceryListItem);
                        }
                    }

                    adapter = new GroceryListAdapter(GroceryListActivity.this, R.layout.grocery_list_item_view, groceryList);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    spinner.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Log.e("JSONObject Exception", "" + e.getMessage());
                }
            }
        });
    }

    public void onCheckboxClicked(View view) {
        // handle onclick for grocery list checkboxes
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }
}
