package intellichef.intellichef;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by Sally on 3/3/17.
 */

public class ImageTextAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data;
    private int selectedIndex = -1;
//    private TreeSet imageSet = new TreeSet();

    public ImageTextAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
//        imageSet = new TreeSet();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch(type) {

            }
        } else {
            holder = (ImageTextAdapter.ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private static class ViewHolder {
//        public TextView textView;
//        public ImageView imageView;
        public View view;
    }
}
