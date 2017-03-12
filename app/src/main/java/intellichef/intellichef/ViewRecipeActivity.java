package intellichef.intellichef;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

import cz.msebera.android.httpclient.Header;

public class ViewRecipeActivity extends AppCompatActivity {

    private int recipePK;
    private TextView recipeName;
    private ImageView recipeImage;
    private TextView description;
//    private ListView notesList;
    private ArrayAdapter<String> notesAdapter;
    private EditText addNotes;
    private ArrayAdapter<String> recipeViewAdapter;
    private ListView recipeViewList;
    private Button addNoteButton;
    ArrayList<String> instructionArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        recipePK = getIntent().getIntExtra("recipePk", -1);
        recipeName = (TextView) findViewById(R.id.recipeName);
        recipeImage = (ImageView) findViewById(R.id.recipeImage);
        description = (TextView) findViewById(R.id.description);
//        notesList = (ListView) findViewById(R.id.notesList);
        addNotes = (EditText) findViewById(R.id.addNotesField);
        addNoteButton = (Button) findViewById(R.id.noteButton);

        instructionArray = new ArrayList<>();
        recipeViewList = (ListView) findViewById(R.id.recipeView);
        recipeViewAdapter = new ArrayAdapter(ViewRecipeActivity.this, R.layout.mytextview, instructionArray);
        recipeViewList.setAdapter(recipeViewAdapter);

        try {
            showRecipe(recipePK);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        instructionArray.add("Hello");
        instructionArray.add("Hello");
        instructionArray.add("Hello");
        instructionArray.add("Hello");
        instructionArray.add("Hello");
        instructionArray.add("Hello");
        instructionArray.add("Hello");
        instructionArray.add("Hello");
        instructionArray.add("Hello");
        instructionArray.add("Hello");
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

    }

    public void showRecipe(int recipePK) throws JSONException {

        IntelliServerAPI.getRecipe(recipePK, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result) {

                Recipe recipe = new Recipe();
                recipe.fillParams(result);
                String[] instructions = recipe.getInstructions();
                for (String instruction: instructions) {
                    instructionArray.add(instruction);
                }
                recipeViewAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(recipeViewList);
                System.out.println("hello");

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



}
