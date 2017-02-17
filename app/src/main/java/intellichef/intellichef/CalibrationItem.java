package intellichef.intellichef;

/**
 * Created by jatin1 on 2/16/17.
 */
public class CalibrationItem {

    private String mImageUrl;
    private String mTitle;

    public CalibrationItem(String url, String title) {
        this.mImageUrl = url;
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
}
