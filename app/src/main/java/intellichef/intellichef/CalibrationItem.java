package intellichef.intellichef;

/**
 * Created by jatin1 on 2/16/17.
 */
public class CalibrationItem {

    private String mImageUrl;
    private String mTitle;
    private boolean isSelected;
    private Integer mCalibrationPk;

    public CalibrationItem(String url, String title, Integer calibrationPk) {
        this.mImageUrl = url;
        this.mCalibrationPk = calibrationPk;
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

    public Integer getCalibrationPk() {
        return mCalibrationPk;
    }
}
