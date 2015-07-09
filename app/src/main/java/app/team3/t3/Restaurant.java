package app.team3.t3;

/**
 * Created by sssbug on 6/30/15.
 */
public class Restaurant {
    private String businessID;
    private String name;
    private float rating;
    private int reviewCount;
    private String phone;
    private String categories;
    private String address;
    private String city;
    private int zipCode;
    private float latitude;
    private float longitude;
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

    // Parameterized Constructor
    public Restaurant(String name, float rating, int reviewCount, String phone, String categories, String address, String city, int zipCode, float latitude, float longitude, String ratingImgURL, String businessImgURL) {
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
    public Restaurant(String businessID, String name, float rating, int reviewCount, String phone, String categories, String Address, String City, int zipCode, float latitude, float longitude, String ratingImgURL, String businessImgURL) {
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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
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
}