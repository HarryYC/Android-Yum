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
    private String Address;
    private String City;
    private int zipCode;
    private float latitude;
    private float longitude;
    private String ratingImgURL;
    private String businessImgURL;

    // Default constructor
    public Restaurant() {
        this.businessID = "";
        this.name = "";
        this.rating = 0;
        this.reviewCount = 0;
        this.phone = "";
        this.categories = "";
        this.Address = "";
        this.City = "";
        this.zipCode = 0;
        this.latitude = 0;
        this.longitude = 0;
        this.ratingImgURL = "";
        this.businessImgURL = "";
    }

    public Restaurant(String Name, float Rating, String Phone, String Categories, String Address, String City, int Zipcode, float Latitude, float Longitude) {
        this.Name = Name;
        this.Rating = Rating;
        this.Phone = Phone;
        this.Categories = Categories;
        this.Address = Address;
        this.City = City;
        this.Zipcode = Zipcode;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }


    // Methods for data retrival
    public int getId() {
        return Id;
    }

    // Methods for data assignment
    public void setId(int Id) {
        this.Id = Id;
    }

    public String getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(String BusinessId) {
        this.BusinessId = BusinessId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float Rating) {
        this.Rating = Rating;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getCategories() {
        return Categories;
    }

    public void setCategories(String Categories) {
        this.Categories = Categories;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public int getZipcode() {
        return Zipcode;
    }

    public void setZipcode(int Zipcode) {
        this.Zipcode = Zipcode;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float Latitude) {
        this.Latitude = Latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float Longitude) {
        this.Longitude = Longitude;
    }

    public String getBusinessImgURL() {
        return businessImgURL;
    }

    public void setBusinessImgURL(String businessImgURL) {
        this.businessImgURL = businessImgURL;
    }

    public String getRatingImgURL() {
        return RatingImgURL;
    }

    public void setRatingImgURL(String RatingImgURL) {
        this.RatingImgURL = RatingImgURL;
    }
}
