package intellichef.intellichef;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewRecipeActivity extends AppCompatActivity {

    private int recipePK;
    private ArrayAdapter<String> adapter;
    private ListView recipeViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        recipePK = getIntent().getIntExtra("recipePK", -1);
        System.out.println(recipePK);
//        ArrayList<String> test = new ArrayList<>();
//        adapter = new ArrayAdapter(ViewRecipeActivity.this, R.layout.calibration_view, test);
//        recipeViewList = (ListView) findViewById(R.id.recipeView);
//        recipeViewList.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }


}
