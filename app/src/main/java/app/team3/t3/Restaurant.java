package app.team3.t3;

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
    private String businessID;
    private String name;
    private float rating;
    private int reviewCount;
    private String phone;
    private String categories;
    private String address;
    private String city;
    private int zipCode;
    private double latitude;
    private double longitude;
    private String ratingImgURL;
    private String businessImgURL;

    // Default constructor
    public Restaurant() {
        this.businessID = "";
        this.name = "";
        this.rating = 0f;
        this.reviewCount = 0;
        this.phone = "";
        this.categories = "";
        this.address = "";
        this.city = "";
        this.zipCode = 0;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.ratingImgURL = "";
        this.businessImgURL = "";
    }

    // Parcelling
    public Restaurant(Parcel source) {
        this.businessID = source.readString();
        this.name = source.readString();
        this.rating = source.readFloat();
        this.reviewCount = source.readInt();
        this.phone = source.readString();
        this.categories = source.readString();
        this.address = source.readString();
        this.city = source.readString();
        this.zipCode = source.readInt();
        this.latitude = source.readDouble();
        this.longitude = source.readDouble();
        this.ratingImgURL = source.readString();
        this.businessImgURL = source.readString();
    }


    // Parameterized Constructor
    public Restaurant(String businessID, String name, float rating, int reviewCount, String phone, String categories, String Address, String City, int zipCode, double latitude, double longitude, String ratingImgURL, String businessImgURL) {
        this.businessID = businessID;
        this.name = name;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.phone = phone;
        this.categories = categories;
        this.address = Address;
        this.city = City;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ratingImgURL = ratingImgURL;
        this.businessImgURL = businessImgURL;
    }

    // Getters and Setters
    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.businessID);
        dest.writeString(this.name);
        dest.writeFloat(this.rating);
        dest.writeInt(this.reviewCount);
        dest.writeString(this.phone);
        dest.writeString(this.categories);
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeInt(this.zipCode);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.ratingImgURL);
        dest.writeString(this.businessImgURL);
    }
}