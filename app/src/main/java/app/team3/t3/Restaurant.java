package app.team3.t3;

/**
 * Created by sssbug on 6/30/15.
 */
public class Restaurant {
    private int Id;
    private String Name;
    private float Rating;
    private String Phone;
    private String Categories;
    private String Address;
    private String City;
    private int Zipcode;
    private float Latitude;
    private float Longitude;
    private String ratingImgURL;
    private String businessImgURL;

    public Restaurant() {
        Name = "";
        Rating = 0;
        Phone = "";
        Categories = "";
        Address = "";
        City = "";
        Zipcode = 0;
        Latitude = 0;
        Longitude = 0;
        ratingImgURL = "";
    }

    public Restaurant(String name, float rating, String phone, String categories, String address, String city, int zipcode, float latitude, float longitude) {
        Name = name;
        Rating = rating;
        Phone = phone;
        Categories = categories;
        Address = address;
        City = city;
        Zipcode = zipcode;
        Latitude = latitude;
        Longitude = longitude;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCategories() {
        return Categories;
    }

    public void setCategories(String categories) {
        Categories = categories;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public int getZipcode() {
        return Zipcode;
    }

    public void setZipcode(int zipcode) {
        Zipcode = zipcode;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }


}
