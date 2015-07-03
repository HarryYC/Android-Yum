package app.team3.t3;

/**
 * Created by ivan on 6/30/15.
 */
public class YelpInfo {
    private String businessName = "";
    private String businessAddress = "";
    private float overallRating = 0.0f;
    private String businessCoordinate = "";
    private String businessImgURL = "";
    private String ratingImgURL = "";

    public String getName() {
        return businessName;
    }

    public void setName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddress() {
        return businessAddress;
    }

    public void setAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public float getRating() {
        return overallRating;
    }

    public void setRating(float overallRating) {
        this.overallRating = overallRating;
    }

    public String getCoordinate() {
        return businessCoordinate;
    }

    public void setCoordinate(String businessCoordinate) {
        this.businessCoordinate = businessCoordinate;
    }

    public String getBusinessImgURL() {
        return businessImgURL;
    }

    public void setBusinessImgURL(String businessImgURL) {
        this.businessImgURL = businessImgURL;
    }

    public String getRatingImgURL() {
        return ratingImgURL;
    }

    public void setRatingImgURL(String ratingImgURL) {
        this.ratingImgURL = ratingImgURL;
    }
}
