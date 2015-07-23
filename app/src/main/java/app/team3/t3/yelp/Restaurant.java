package app.team3.t3.yelp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sssbug on 6/30/15.
 */
public class Restaurant implements Parcelable {
    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {

        @Override
        public Restaurant createFromParcel(Parcel source) {
            return new Restaurant(source);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    private String restaurantID;
    private String name;
    private float rating;
    private int reviewCount;
    private String phone;
    private String categories;
    private String address;
    private double latitude;
    private double longitude;
    private String restaurantPage;
    private String ratingImgURL;
    private String restaurantImgURL;

    // Default constructor
    public Restaurant() {
        this.restaurantID = "";
        this.name = "";
        this.rating = 0f;
        this.reviewCount = 0;
        this.phone = "";
        this.categories = "";
        this.address = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.restaurantPage = "";
        this.ratingImgURL = "";
        this.restaurantImgURL = "";
    }

    // Parcelling
    public Restaurant(Parcel source) {
        this.restaurantID = source.readString();
        this.name = source.readString();
        this.rating = source.readFloat();
        this.reviewCount = source.readInt();
        this.phone = source.readString();
        this.categories = source.readString();
        this.address = source.readString();
        this.latitude = source.readDouble();
        this.longitude = source.readDouble();
        this.restaurantPage = source.readString();
        this.ratingImgURL = source.readString();
        this.restaurantImgURL = source.readString();
    }


    // Parameterized Constructor
    public Restaurant(String restaurantID, String name, float rating, int reviewCount, String phone, String categories, String Address, double latitude, double longitude, String restaurantPage, String ratingImgURL, String restaurantImgURL) {
        this.restaurantID = restaurantID;
        this.name = name;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.phone = phone;
        this.categories = categories;
        this.address = Address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.restaurantPage = restaurantPage;
        this.ratingImgURL = ratingImgURL;
        this.restaurantImgURL = restaurantImgURL;
    }

    // Getters and Setters
    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRestaurantPage() {
        return restaurantPage;
    }

    public void setRestaurantPage(String restaurantPage) {
        this.restaurantPage = restaurantPage;
    }

    public String getRestaurantImgURL() {
        return restaurantImgURL;
    }

    public void setRestaurantImgURL(String restaurantImgURL) {
        this.restaurantImgURL = restaurantImgURL;
    }

    public String getRatingImgURL() {
        return ratingImgURL;
    }

    public void setRatingImgURL(String ratingImgURL) {
        this.ratingImgURL = ratingImgURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.restaurantID);
        dest.writeString(this.name);
        dest.writeFloat(this.rating);
        dest.writeInt(this.reviewCount);
        dest.writeString(this.phone);
        dest.writeString(this.categories);
        dest.writeString(this.address);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.restaurantPage);
        dest.writeString(this.ratingImgURL);
        dest.writeString(this.restaurantImgURL);
    }
}