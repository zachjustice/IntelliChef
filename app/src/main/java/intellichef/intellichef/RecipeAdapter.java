package intellichef.intellichef;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jatin1 on 2/16/17.
 */
public class RecipeAdapter extends ArrayAdapter<RecipeItem> {

    Context context;
    private int layoutResourceId;
    private ArrayList<RecipeItem> data;

    public RecipeAdapter(Context context, int layoutResourceId, ArrayList<RecipeItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        RecipeHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecipeHolder();
            holder.recipeImage = (ImageView) row.findViewById(R.id.recipeImage);
            holder.recipeName = (TextView) row.findViewById(R.id.recipeName);

            row.setTag(holder);
        } else {
            holder = (RecipeHolder) row.getTag();
        }

        RecipeItem item = data.get(position);
        holder.recipeName.setText(item.getRecipeName());
        ImageExtractor.loadIntoImage(getContext(), item.getImageUrl(), holder.recipeImage, 150, 150);

        return row;

    }

    private static class RecipeHolder {
        ImageView recipeImage;
        TextView recipeName;
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
