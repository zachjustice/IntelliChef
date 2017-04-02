package intellichef.intellichef;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sally on 4/1/17.
 */

public class MealPlansAdapter extends ArrayAdapter<MealPlanItem> {

        Context context;
        private int layoutResourceId;
        private ArrayList<MealPlanItem> data;

        public MealPlansAdapter(Context context, int layoutResourceId, ArrayList<MealPlanItem> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }



        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            MealPlanHolder holder = null;

            if (row == null) {
                    LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                    row = inflater.inflate(layoutResourceId, parent, false);

                    holder = new MealPlanHolder();
//                    holder.meals = (ListView) row.findViewById(R.id.mealListView);
                    holder.date = (TextView) row.findViewById(R.id.dateOfMeal);

                    row.setTag(holder);
            } else {
                holder = (MealPlanHolder) row.getTag();
            }

                MealPlanItem item = data.get(position);
                holder.date.setText(item.getDate());
                holder.meals = item.getMeals();
                Log.v("JSONObject", "helllllooooo?");

                return row;

        }

        private static class MealPlanHolder {
            ListView meals;
            TextView date;
        }

        @Override
        public int getViewTypeCount() {
            Log.v("JSONObject", "COUNT" + getCount());
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

}
