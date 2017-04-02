package intellichef.intellichef;

import android.widget.ListView;

/**
 * Created by Sally on 4/1/17.
 */

class MealPlanItem {
    private String mDate;
    private ListView mMeals;

    public MealPlanItem(String date, ListView meals) {
        this.mDate = date;
        this.mMeals = meals;
    }

    public String getDate() {
        return mDate;
    }

    public ListView getMeals() {
        return mMeals;
    }
}
