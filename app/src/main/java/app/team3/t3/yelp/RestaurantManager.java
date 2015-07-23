package app.team3.t3.yelp;

/**
 * This class is used for getting information about the restaurant.
 */
public interface RestaurantManager {

    /**
     * @return restaurant id
     */
    String getRestaurantId();

    /**
     * @return restaurant name;
     */
    String getRestaurantName();

    /**
     * @return rating number of the restaurant.
     */
    float getRating();

    /**
     * @return number of reviews for the restaurant.
     */
    int getReviewCount();

    /**
     * @return phone number of the restaurant.
     */
    String getPhoneNumber();

    /**
     * @return category of food.
     */
    String getCategory();

    /**
     * @return address of the restaurant.
     */
    String getAddress();

    /**
     * @return latitude of the restaurant.
     */
    double getLatitude();

    /**
     * @return longitude of the restaurant.
     */
    double getLongitude();

    /**
     * Used for twitter post
     * @return
     */
    String getRestaurantPage();

    /**
     * @return rating star img url
     */
    String getRatingImgUrl();

    /**
     * @return restaurant img url
     */
    String getRestaurantImgUrl();

}
