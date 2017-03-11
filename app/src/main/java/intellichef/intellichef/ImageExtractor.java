package intellichef.intellichef;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by jatin1 on 2/13/17.
 */
public class ImageExtractor {
    //simple function to take a url and put it into an imageview
    //Note: the context refers to the activity the ImageView is in, 'this', or getApplicationContext() should usually work
    public static void loadIntoImage(Context context, String url, ImageView image) {
        Picasso.with(context).load(url).into(image);
    }

    public static void loadIntoImage(Context context, String url, ImageView image, int height, int width) {
        Picasso.with(context).load(url).resize(height, width)
                .centerCrop().into(image);
    }

}
