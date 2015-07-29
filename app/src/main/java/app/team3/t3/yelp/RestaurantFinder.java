package app.team3.t3.yelp;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

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
    //Log
    private static final String LOG_TAG = RestaurantFinder.class.getSimpleName();

    private static final int MAX_RESTAURANT = 20;
    private static final String DEFAULT_TERM = "restaurants";
    private static final String DEFAULT_LOCATION = null;
    private static final int DEFAULT_RANGE = 2000;
    private static final int DEFAULT_SORT = 1;
    private static final String DEFAULT_CATEGORY = "restaurants";
    private static final String DEFAULT_COORDINATE = null;
//    private static final String TAG = "yelp_interface";
    /**
     * big six params *
     */
    private static final int SORT = 0;
    private static String searchResponseJSON;
    boolean addressFlag = false;
    private YelpAPI yelpApi = null;
    private Restaurant[] restaurant;
    private String term;
    private String location;
    private String category;
    private int range;
    private Double latitude;
    private Double longitude;

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

        /** set default to big six params **/
        term = "restaurants";
        location = null;
        category = "restaurants";
        range = 2000;
        latitude = 0.0;
        longitude = 0.0;

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
//        Log.v(TAG, searchResponseJSON);
        Log.e(LOG_TAG, "####Yelp "+ searchResponseJSON);
    }

    /**
     * Queries the Search API based on the search terms and store the result to be process
     *
     * @param allRestaurantJSON <tt>String</tt> for json from Yelp API
     * @return restaurant <tt>Restaurant[]</tt> for deserialized restautrant java object
     */
    public Restaurant toRestaurant(String allRestaurantJSON) throws RestaurantSearchException {
        String restaurantId = "";
        String restaurantName = "";
        float restaurantRating = 0.0f;
        int restaurantReviewCount = 0;
        String restaurantPhone = "";
        String restaurantCategory = "";
        String restaurantAddress = "";
        String restaurantYelpUrl = "";
        String restaurantImgUrl = "";
        String restaurantRatingImgUrl = "";
        Double restaurantLatitude = 0.0;
        Double restaurantLongitude = 0.0;
        int currentRandom = 0;
        Random random = new Random();


        try {
            JSONObject response = new JSONObject(allRestaurantJSON);
            int totalResults = Integer.parseInt(response.getString("total"));
            if (totalResults < 1) {
                throw new RestaurantSearchException("No restaurant found");
            }
            if (totalResults > MAX_RESTAURANT) {
                totalResults = MAX_RESTAURANT;
            }
            currentRandom = Math.abs(random.nextInt()) % totalResults;

            JSONArray businesses = response.getJSONArray("businesses");
            JSONObject businessData = businesses.getJSONObject(currentRandom);

            restaurantId = businessData.getString("id");
            restaurantName = businessData.getString("name");
            restaurantRating = Float.parseFloat(businessData.getString("rating"));
            restaurantReviewCount = Integer.parseInt(businessData.getString("review_count"));

            if (businessData.has("display_phone")) {
                restaurantPhone = businessData.getString("display_phone");
            } else {
                restaurantPhone = "none";
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
            restaurantCategory = categoryString;

            restaurantYelpUrl = businessData.getString("mobile_url");

            restaurantImgUrl = businessData.getString("image_url")
                    .replace("ms.jpg", "o.jpg");

            restaurantRatingImgUrl = businessData.getString("rating_img_url_large");

            JSONObject location = businessData.getJSONObject("location");
            JSONArray address = location.getJSONArray("display_address");
            String businessAddress = "";
            for (int i = 0; i < address.length(); i++) {
                businessAddress += address.get(i).toString();
                if (i < address.length() - 1) {
                    businessAddress += ", ";
                }
            }
            restaurantAddress = businessAddress;

            JSONObject coordinate = location.getJSONObject("coordinate");

            restaurantLatitude = Double.parseDouble(coordinate.getString("latitude"));
            restaurantLongitude = Double.parseDouble(coordinate.getString("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Restaurant(restaurantId, restaurantName, restaurantRating, restaurantReviewCount, restaurantPhone,
                restaurantCategory, restaurantAddress, restaurantLatitude, restaurantLongitude, restaurantYelpUrl,
                restaurantRatingImgUrl, restaurantImgUrl);
    }

    /**
     * @param term      <tt>String</tt> for search term input
     * @param location  <tt>String</tt> for addresss of center of search input
     * @param category  <tt>String</tt> for restaurant categories input
     * @param range     <tt>int</tt> for radius of search input
     * @param sort      <tt>int</tt> for sort mode input
     * @param latitude  <tt>float</tt> for latitude input
     * @param longitude <tt>float</tt> for lontitude input
     * @return <tt>Restaurant</tt> one random restaurant data
     */
    public Restaurant filteredSearch(String term, String location, String category, int range, int sort, Double latitude, Double longitude) throws RestaurantSearchException {

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
            throw new RestaurantSearchException("Unspecified location");
        } else {
            queryAPI(yelpApi, qterm, qlocation, qcategory, qsort, qrange, qcoordinate);
            return toRestaurant(searchResponseJSON);
        }
    }

    /**
     *
     * @return Restaurant
     * @throws RestaurantSearchException
     */
    public Restaurant filteredSearch() throws RestaurantSearchException {
        Log.d(LOG_TAG, "filteredSearch called.");
        String coordinate = null;
        if (latitude != 0.0 && longitude != 0.0) {
            if (!addressFlag) {
                coordinate = String.valueOf(latitude) + "," + String.valueOf(longitude);
            }
        }
        if (location == null && coordinate == null) {
            throw new RestaurantSearchException("Unspecified location");
        } else {
            Log.e(LOG_TAG, "####Term: " + getTerm());   // restaurant -> default value
            Log.e(LOG_TAG, "####Location: " + getLocation());
//            if (addressFlag) {Log.e("####Location:",getLocation());}
            Log.e(LOG_TAG, "####Category: " + getCategory());   //restaurant -> default value
            Log.e(LOG_TAG, "####Sort: " + String.valueOf(SORT));    // 0 -> default value
            Log.e(LOG_TAG, "####Range " + String.valueOf(range));   // 2000 -> default value
            Log.e(LOG_TAG, "####coor:" + coordinate);
            // queryAPI(yelpApi, "restaurants", "San Francisco,CA", "restaurants", 1, 2000, null);
            queryAPI(yelpApi, term, location, category, SORT, range, coordinate);
            return toRestaurant(searchResponseJSON);
        }
    }

    public void useAddress(boolean addressFlag) {
        this.addressFlag = addressFlag;
    }

    /**
     * getters and setters *
     */

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}


