package intellichef.intellichef;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by zachjustice on 3/20/17.
 */

public class Meal extends Recipe {
    private int mealPlanPK;
    private String eatOn;
    private String mealType;

    public void fillParams(JSONObject params) {
        super.fillParams(params);

        try {
            mealPlanPK = params.getInt("meal_plan_pk");
            eatOn = params.getString("eat_on");
            mealType = params.getString("meal_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int getMealPlanPK() {
        return mealPlanPK;
    }
}
