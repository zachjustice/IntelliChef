package intellichef.intellichef;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by jatin1 on 2/13/17.
 */
public class ImageExtractor {
    //simple function to take a url and put it into an imageview
    //Note: the context refers to the activity the ImageView is in, 'this', or getApplicationContext() should usually work
    public static void loadIntoImage(final ProgressBar spinner, Context context, String url, ImageView image) {
        spinner.setVisibility(View.VISIBLE);

        try {
            Picasso.with(context).load(url).into(image, new Callback() {
                @Override
                public void onSuccess() {
                    spinner.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {
                    spinner.setVisibility(View.INVISIBLE);
                }
            });
        } catch (IllegalArgumentException ex) {
            Log.wtf("ImageExtractor Error", "Empty image path. Probably an empty result set from the API.");
            throw ex;
        }
    }

    public static void loadIntoImage(final ProgressBar spinner, Context context, String url, ImageView image, int height, int width) {
        spinner.setVisibility(View.VISIBLE);
        try {
            Picasso.with(context).load(url).resize(height, width)
                    .centerCrop().into(image, new Callback() {
                @Override
                public void onSuccess() {
                    Log.wtf("SPINNER", "ON SUCCESS BEING CALLED");
                    spinner.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {
                    spinner.setVisibility(View.INVISIBLE);
                }
            });
        } catch (IllegalArgumentException ex) {
            Log.wtf("ImageExtractor Error", "Empty image path. Probably an empty result set from the API.");
            throw ex;
        }
    }
    public static void loadIntoImage(Context context, String url, ImageView image) {
        try {
            Picasso.with(context).load(url).into(image);
        } catch (IllegalArgumentException ex) {
            Log.wtf("ImageExtractor Error", "Empty image path. Probably an empty result set from the API.");
            throw ex;
        }
    }

    public static void loadIntoImage(Context context, String url, ImageView image, int height, int width) {
        try {
            Picasso.with(context).load(url).resize(height, width)
                    .centerCrop().into(image);
        } catch (IllegalArgumentException ex) {
            Log.wtf("ImageExtractor Error", "Empty image path. Probably an empty result set from the API.");
            throw ex;
        }
    }
}
