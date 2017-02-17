package intellichef.intellichef;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CalibrationActivity extends AppCompatActivity {

    Button submit;
    ArrayList<CalibrationItem> calibrationItems;
    ListView listview;
    CalibrationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        CalligraphyConfig.initDefault("fonts/Montserrat-Light.ttf");

        listview = (ListView) findViewById(R.id.listView);
        calibrationItems = new ArrayList<>();
        submit = (Button) findViewById(R.id.submit);

        calibrationItems.add(new CalibrationItem("https://lh4.ggpht.com/mJDgTDUOtIyHcrb69WM0cpaxFwCNW6f0VQ2ExA7dMKpMDrZ0A6ta64OCX3H-NMdRd20=w300", "hi"));
        adapter = new CalibrationAdapter(this, R.layout.calibration_view, calibrationItems);
        listview.setAdapter(adapter);


        try {
            populateCalibratedMeals();
        } catch (JSONException e) {
            Log.v("JSONObject", "" + e.getMessage());
        }

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                try {
//                    post calibration picks
//                    switch screens
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    public void populateCalibratedMeals() throws JSONException {

        IntelliServerAPI.getCalibratedMeals(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray result) {
                try {
                    Log.v("JSONObject", result.toString());
                    //for (JSONObject
                    //calibrationItems.add(new CalibrationItem(, "url"));
                } catch (Exception e) {
                    Log.v("JSONObject", "" + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

}
