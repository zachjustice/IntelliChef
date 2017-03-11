package intellichef.intellichef;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jatin1 on 2/16/17.
 */
public class CalibrationAdapter extends ArrayAdapter<CalibrationItem> {

    Context context;
    int layoutResourceId;
    ArrayList<CalibrationItem> data;
    int selectedIndex = -1;



    public CalibrationAdapter(Context context, int layoutResourceId, ArrayList<CalibrationItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CalibrationHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new CalibrationHolder();
            holder.recipeImage = (ImageView) row.findViewById(R.id.recipeImage);
            holder.recipeName = (TextView) row.findViewById(R.id.recipeName);

            row.setTag(holder);
        } else {
            holder = (CalibrationHolder) row.getTag();
        }

        CalibrationItem item = data.get(position);
        holder.recipeName.setText(item.getRecipeName());
        ImageExtractor.loadIntoImage(getContext(), item.getImageUrl(), holder.recipeImage, 150, 150);

        return row;

    }

    static class CalibrationHolder {
        ImageView recipeImage;
        TextView recipeName;
    }

}
