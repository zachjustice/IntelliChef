package intellichef.intellichef;

import android.widget.TextView;

/**
 * Created by jatin1 on 2/16/17.
 */
public class RecipeItem {

    private String mImageUrl;
    private String mTitle;
    private boolean isSelected;
    private Integer mRecipePk;
    private TextView text;

    public RecipeItem(String url, String title, Integer recipePk) {
        this.mImageUrl = url;
        this.mRecipePk = recipePk;
        this.mTitle = title;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getRecipeName() {
        return mTitle;
    }

    public void setRecipeName(String mTitle) {
        this.mTitle = mTitle;
    }

    public void toggleSelected() {
        isSelected = !isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Integer getRecipePk() {
        return mRecipePk;
    }

    public void setText(TextView in) {
        text = in;
    }

    public TextView getText() {
        return text;
    }
}
