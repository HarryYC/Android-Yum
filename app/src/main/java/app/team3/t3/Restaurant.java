package app.team3.t3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sssbug on 6/30/15.
 */
public class Restaurant implements Parcelable {
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
        this.name = "";
        this.rating = 0;
        this.reviewCount = 0;
        this.phone = "";
        this.categories = "";
        this.address = "";
        this.city = "";
        this.zipCode = 0;
        this.latitude = 0;
        this.longitude = 0;
        this.ratingImgURL = "";
        this.businessImgURL = "";
    }

    // Parcelling
    public Restaurant(Parcel source) {
        String[] dataString = new String[8];
        int[] dataInt = new int[2];
        double[] dataDouble = new double[2];

        source.readStringArray(dataString);
        source.readIntArray(dataInt);
        source.readDoubleArray(dataDouble);

        this.businessID = dataString[0];
        this.name = dataString[1];
        this.phone = dataString[2];
        this.categories = dataString[3];
        this.address = dataString[4];
        this.city = dataString[5];
        this.ratingImgURL = dataString[6];
        this.businessImgURL = dataString[7];

        this.reviewCount = dataInt[0];
        this.zipCode = dataInt[1];

        this.latitude = dataDouble[0];
        this.longitude = dataDouble[1];

        this.rating = source.readFloat();

    }

    // Parameterized Constructor
    public Restaurant(String name, float rating, int reviewCount, String phone, String categories, String address, String city, int zipCode, double latitude, double longitude, String businessImgURL, String ratingImgURL) {
        this.name = name;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.phone = phone;
        this.categories = categories;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ratingImgURL = ratingImgURL;
        this.businessImgURL = businessImgURL;
    }

    // Parameterized Constructor
    public Restaurant(String businessID, String name, float rating, int reviewCount, String phone, String categories, String Address, String City, int zipCode, double latitude, double longitude, String businessImgURL, String ratingImgURL) {
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


    // Method to retrieve business informatino
    public String getBusinessID() {
        return businessID;
    }

    // Methods to assign business informations
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
        dest.writeStringArray(new String[]{
                this.businessID,
                this.name,
                this.phone,
                this.categories,
                this.address,
                this.city,
                this.ratingImgURL,
                this.businessImgURL
        });
        dest.writeFloat(this.rating);
        dest.writeIntArray(new int[]{
                this.reviewCount,
                this.zipCode
        });
        dest.writeDoubleArray(new double[]{
                this.latitude,
                this.longitude
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Restaurant createFromParcel(Parcel source) {
            return new Restaurant(source);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}