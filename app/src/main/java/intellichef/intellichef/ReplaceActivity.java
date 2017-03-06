package intellichef.intellichef;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

public class ReplaceActivity extends AppCompatActivity {
    private MultiAutoCompleteTextView searchRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace);

        searchRecipe = (MultiAutoCompleteTextView) findViewById(R.id.search);

        //ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.mytextview, ???);
        //searchRecipe.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        //searchRecipe.setAdapter(adapter);

    }
}
