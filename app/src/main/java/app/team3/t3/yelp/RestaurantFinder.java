package app.team3.t3.yelp;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.team3.t3.R;

/**
 * Created by ivan on 6/30/15.
 */

/**
 * Interface for to Yelp API V2
 * <p/>
 * See <a href="http://www.yelp.com/developers/documentation">Yelp Documentation</a> for more info.
 */
public class RestaurantFinder {
    private static final int MAX_RESTAURANT = 20;
    private static final String DEFAULT_TERM = "restaurants";
    private static final String DEFAULT_LOCATION = null;
    private static final int DEFAULT_RANGE = 2000;
    private static final int DEFAULT_SORT = 1;
    private static final String DEFAULT_CATEGORY = "restaurants";
    private static final String DEFAULT_COORDINATE = null;
    private static final String TAG = "yelp_interface";
    private static String searchResponseJSON;
    boolean addressFlag = false;
    private YelpAPI yelpApi = null;
    private Restaurant[] restaurant;

    /**
     * Restaurant constructor initialize YelpAPI with OAuth credential from string.xml
     *
     * @param context <tt>Context</tt> for Activity context
     */
    public RestaurantFinder(Context context) {
        yelpApi = new YelpAPI(context.getString(R.string.yelp_consumer_key),
                context.getString(R.string.yelp_consumer_secret),
                context.getString(R.string.yelp_token),
                context.getString(R.string.yelp_token_secret));
    }

    /**
     * Queries the Search API based on the command line arguments and takes the first result to query
     * the Business API.
     *
     * @param yelpApi    <tt>YelpAPI</tt> service instance
     * @param term       <tt>String</tt> for keywords to search query
     * @param location   <tt>String</tt> for specifed address to search query
     * @param category   <tt>String</tt> for restaurant category to search query
     * @param sort       <tt>int</tt> for sort mode to search query
     * @param range      <tt>int</tt> for search range to search query
     * @param coordinate <tt>String</tt> for current location to search query
     */
    private static void queryAPI(YelpAPI yelpApi, String term, String location, String category, int sort,
                                 int range, String coordinate) {
        searchResponseJSON =
                yelpApi.searchForBusiness(term, location, category, sort, range, coordinate);
    }

    /**
     * Queries the Search API based on the search terms and store the result to be process
     * @param allRestaurantJSON <tt>String</tt> for json from Yelp API
     * @return restaurant <tt>Restaurant[]</tt> for deserialized restautrant java object
     */
    public Restaurant[] toRestaurant(String allRestaurantJSON) {
        Log.v(TAG, allRestaurantJSON);
        try {
            JSONObject response = new JSONObject(allRestaurantJSON);
            int totalResults = Integer.parseInt(response.getString("total"));
            if (totalResults < MAX_RESTAURANT) {
                restaurant = new Restaurant[totalResults];
            } else {
                restaurant = new Restaurant[MAX_RESTAURANT];
            }


            JSONArray businesses = response.getJSONArray("businesses");
            for (int index = 0; index < restaurant.length; index++) {
                restaurant[index] = new Restaurant();

                JSONObject businessData = businesses.getJSONObject(index);
                restaurant[index].setRestaurantID(businessData.getString("id"));
                restaurant[index].setName(businessData.getString("name"));
                restaurant[index].setRating(Float.parseFloat(businessData.getString("rating")));
                restaurant[index].setReviewCount(Integer.parseInt(businessData.getString("review_count")));

                if(businessData.has("display_phone")) {
                    restaurant[index].setPhone(businessData.getString("display_phone"));
                } else {
                    restaurant[index].setPhone("none");
                }

                JSONArray category = businessData.getJSONArray("categories");
                String categoryString = "";
                for (int i = 0; i < category.length(); i++) {
                    JSONArray categoryItems = category.getJSONArray(i);
                    for (int j = 0; j < categoryItems.length(); j++) {
                        categoryString += categoryItems.getString(j);
                        if (i < categoryItems.length() - 1) {
                            categoryString += ", ";
                        }
                    }
                    if (i < category.length() - 1) {
                        categoryString += ", ";
                    }
                }
                restaurant[index].setCategories(categoryString);

                restaurant[index].setRestaurantPage(businessData.getString("mobile_url"));

                restaurant[index].setRestaurantImgURL(businessData.getString("image_url")
                        .replace("ms.jpg", "o.jpg"));

                restaurant[index].setRatingImgURL(businessData.getString("rating_img_url_large"));

                JSONObject location = businessData.getJSONObject("location");
                JSONArray address = location.getJSONArray("display_address");
                String businessAddress = "";
                for (int i = 0; i < address.length(); i++) {
                    businessAddress += address.get(i).toString();
                    if (i < address.length() - 1) {
                        businessAddress += ", ";
                    }
                }
                restaurant[index].setAddress(businessAddress);

                JSONObject coordinate = location.getJSONObject("coordinate");

                restaurant[index].setLatitude(Double.parseDouble(coordinate.getString("latitude")));
                restaurant[index].setLongitude(Double.parseDouble(coordinate.getString("longitude")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurant;
    }

    /**
     *
     * @param term <tt>String</tt> for search term input
     * @param location <tt>String</tt> for addresss of center of search input
     * @param category <tt>String</tt> for restaurant categories input
     * @param range <tt>int</tt> for radius of search input
     * @param sort <tt>int</tt> for sort mode input
     * @param latitude <tt>float</tt> for latitude input
     * @param longitude <tt>float</tt> for lontitude input
     * @return <tt>Restaurant</tt> array of restaurant data
     */
    public Restaurant[] filteredSearch(String term, String location, String category, int range, int sort, Double latitude, Double longitude) {

        String qterm = DEFAULT_TERM;
        String qlocation = DEFAULT_LOCATION;
        String qcategory = DEFAULT_CATEGORY;
        int qsort = DEFAULT_SORT;
        int qrange = DEFAULT_RANGE;
        String qcoordinate = DEFAULT_COORDINATE;
        String errMsg = null;

        if (term != null) {
            qterm = term;
        }
        if (location != null) {
            if (addressFlag) {
                qlocation = location;
            }
        }
        if (category != null) {
            qcategory = "restaurants," + category;
        }
        if (range > 1 && range <= 25) {
            qrange = range * 1609;
        }
        if (sort >= 0 && sort < 3) {
            qsort = sort;
        }
        if (latitude != 0 && longitude != 0) {
            if (!addressFlag) {
                qcoordinate = String.valueOf(latitude) + "," + String.valueOf(longitude);
            }
        }
        if (location == null && qcoordinate == null) {
            Log.e(TAG, "Sorry, there is no location parameter for query");
            return null;
        } else {
            queryAPI(yelpApi, qterm, qlocation, qcategory, qsort, qrange, qcoordinate);
            return toRestaurant(searchResponseJSON);
        }
    }

    public void useAddress(boolean addressFlag) {
        this.addressFlag = addressFlag;
    }
}
