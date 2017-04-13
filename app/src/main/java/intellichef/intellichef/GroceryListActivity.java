package intellichef.intellichef;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class GroceryListActivity extends AppCompatActivity {
    private ListView listView;

    private GroceryListAdapter adapter;
    private ArrayList<GroceryListItem> groceryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");
        int entityPk = LoginActivity.getCurrentUser().getEntityPk();

        groceryList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);

        DateTime currDate = new DateTime();

        // find end of the week
        // find beginning of the week

        try {
            showResults(currDate, currDate.plusDays(1), entityPk);
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

    private void showResults(final DateTime startDate, final DateTime endDate, int entityPk) throws JSONException {
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
}
