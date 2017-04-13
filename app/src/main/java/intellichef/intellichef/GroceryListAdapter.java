package intellichef.intellichef;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by jatin1 on 2/16/17.
 */
public class GroceryListAdapter extends ArrayAdapter<GroceryListItem> {

    Context context;
    private int layoutResourceId;
    private ArrayList<GroceryListItem> groceryList;

    public GroceryListAdapter(Context context, int layoutResourceId, ArrayList<GroceryListItem> groceryList) {
        super(context, layoutResourceId, groceryList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.groceryList = groceryList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        GroceryListItemHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new GroceryListItemHolder();
            holder.ingredientCheckbox = (CheckBox) row.findViewById(R.id.ingredientCheckbox);
            //holder.ingredientDescriptions = (ListView) row.findViewById(R.id.ingredientDescriptions);

            row.setTag(holder);
        } else {
            holder = (GroceryListItemHolder) row.getTag();
        }

        GroceryListItem item = groceryList.get(position);
        holder.ingredientCheckbox.setText(item.getIngredientName());

        return row;

    }

    private static class GroceryListItemHolder {
        CheckBox ingredientCheckbox;
        //TextView ingredientDescriptions;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

}
