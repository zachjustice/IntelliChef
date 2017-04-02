package intellichef.intellichef;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewRecipeActivity extends AppCompatActivity {

    private int recipePK;
    private TextView recipeName;
    private ImageView recipeImage;
    private TextView description;
    //    private ListView notesList;
    private ArrayAdapter<String> notesAdapter;
    private EditText addNotes;
    private Button addNoteButton;

    private ArrayAdapter<String> recipeViewAdapter;
    private ArrayAdapter<String> ingredientsAdapter;
    private ArrayAdapter<String> nutitionInfoAdapter;

    private ListView recipeViewList;
    private ListView ingredientsList;
    private ListView nutritionInfoList;

    ArrayList<String> instructionArray;
    ArrayList<String> ingredientArray;
    ArrayList<String> nutitionInfoArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");
        recipePK = getIntent().getIntExtra("recipePk", -1);
        recipeName = (TextView) findViewById(R.id.recipeName);
        recipeImage = (ImageView) findViewById(R.id.recipeImage);
        description = (TextView) findViewById(R.id.description);
//        notesList = (ListView) findViewById(R.id.notesList);
        addNotes = (EditText) findViewById(R.id.addNotesField);
        addNoteButton = (Button) findViewById(R.id.noteButton);

        instructionArray = new ArrayList<>();
        recipeViewList = (ListView) findViewById(R.id.recipeView);
        recipeViewAdapter = new ArrayAdapter(ViewRecipeActivity.this, R.layout.custom_textview, instructionArray);
        recipeViewList.setAdapter(recipeViewAdapter);

        ingredientArray = new ArrayList<>();
        ingredientsList = (ListView) findViewById(R.id.ingredients);
        ingredientsAdapter = new ArrayAdapter(ViewRecipeActivity.this, R.layout.custom_textview, ingredientArray);
        ingredientsList.setAdapter(ingredientsAdapter);

        nutitionInfoArray = new ArrayList<>();
        nutritionInfoList = (ListView) findViewById(R.id.nutritionInfo);
        nutitionInfoAdapter = new ArrayAdapter(ViewRecipeActivity.this, R.layout.custom_textview, nutitionInfoArray);
        nutritionInfoList.setAdapter(nutitionInfoAdapter);


        try {
            showRecipe(recipePK);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recipeViewAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(recipeViewList);

        addNotes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                addNotes.setFocusableInTouchMode(true);

                return false;
            }
        });

        // Adding notes logic
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (addNoteButton.getText().equals("Edit")) {
                    addNotes.setEnabled(true);
                    addNoteButton.setText("Done");
                } else {
                    addNotes.setEnabled(false);
                    addNoteButton.setText("Edit");
                    //TODO API call to save the notes
                }
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
                        Intent intent = new Intent(ViewRecipeActivity.this, MealPlanActivity.class);
                        startActivity(intent);
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        intent = new Intent(ViewRecipeActivity.this, PreferencesActivity.class);
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

    public void showRecipe(int recipePK) throws JSONException {

        IntelliServerAPI.getRecipe(recipePK, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {

                Recipe recipe = new Recipe();
                recipe.fillParams(result);
                String[] instructions = recipe.getInstructions();
                String[] ingredients = recipe.getIngredients();
                String[] nutritionFacts = recipe.getNutritionFacts();

                Collections.addAll(instructionArray, instructions);
                Collections.addAll(ingredientArray, ingredients);
                Collections.addAll(nutitionInfoArray, nutritionFacts);

                recipeViewAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(recipeViewList);

                ingredientsAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(ingredientsList);

                nutitionInfoAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(nutritionInfoList);

                recipeName.setText(recipe.getName());
                ImageExtractor.loadIntoImage(getApplicationContext(), recipe.getPhotoUrl(), recipeImage);
                description.setText(recipe.getDescription());
            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ArrayAdapter<String> arrayAdapter = (ArrayAdapter) listView.getAdapter();
        if (arrayAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            View listItem = arrayAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (arrayAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }
}
