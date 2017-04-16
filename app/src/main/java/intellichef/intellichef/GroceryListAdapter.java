package intellichef.intellichef;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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
        GroceryListItemHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            CheckBox ingredientCheckbox = (CheckBox) row.findViewById(R.id.ingredientCheckbox);
            ImageButton toggleButton = (ImageButton) row.findViewById(R.id.toggleIngredientUsages);
            ListView ingredientUsagesListView = (ListView) row.findViewById(R.id.ingredientUsages);

            holder = new GroceryListItemHolder(row, ingredientCheckbox, ingredientUsagesListView, toggleButton);

            row.setTag(holder);
        } else {
            holder = (GroceryListItemHolder) row.getTag();
        }

        GroceryListItem groceryListItem = groceryList.get(position);

        String ingredientName = groceryListItem.getIngredientName();
        List<String> ingredientDescriptions = groceryListItem.getIngredientDescriptions();

        holder.setIngredientName(ingredientName);
        holder.populateIngredientUsagesView(getContext(), ingredientDescriptions);

        return row;
    }

    private static class GroceryListItemHolder {

        View row;
        int originalHeight;
        CheckBox ingredientCheckbox;
        ListView ingredientUsagesListView;
        ImageButton toggleButton;

        GroceryListItemHolder(View row, CheckBox ingredientCheckbox, ListView ingredientUsagesListView, ImageButton toggleButton) {
            this.row = row;
            this.ingredientCheckbox = ingredientCheckbox;
            this.ingredientUsagesListView = ingredientUsagesListView;
            this.toggleButton = toggleButton;
            this.originalHeight = row.getLayoutParams().height;

            this.setToggleButtonListener();
        }

        void populateIngredientUsagesView(Context context, List<String> ingredientDescriptions) {
            ArrayAdapter ingredientUsagesAdapter = new ArrayAdapter(context, R.layout.custom_textview, ingredientDescriptions);
            ingredientUsagesAdapter.notifyDataSetChanged();

            ingredientUsagesListView.setAdapter(ingredientUsagesAdapter);
            ingredientUsagesAdapter.notifyDataSetChanged();
            this.ingredientUsagesListView.setVisibility(View.GONE);

        }

        void setIngredientName(String ingredientName) {
            this.ingredientCheckbox.setText(ingredientName);
        }

        private void setToggleButtonListener() {

            toggleButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (ingredientUsagesListView.getVisibility() == View.VISIBLE) {

                        ingredientUsagesListView.setVisibility(View.GONE);

                    } else {

                        ingredientUsagesListView.setVisibility(View.VISIBLE);
                        ArrayAdapter<String> arrayAdapter = (ArrayAdapter) ingredientUsagesListView.getAdapter();

                    }
                }
            });
        }
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
