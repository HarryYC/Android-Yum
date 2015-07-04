package app.team3.t3;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ivan on 6/30/15.
 */


public class YelpSearch {
    private static final int MAX_RESTAURANT = 20;
    private static final String DEFAULT_TERM = "dinner";
    private static final String DEFAULT_LOCATION = "San Francisco, CA";
    //    private static final String DEFAULT_LOCATION = "Santa Barbara, CA";
    //    private static final String DEFAULT_LOCATION = "";
    private static final int DEFAULT_RANGE = 10000;
    private static final int DEFAULT_SORT = 1;
    private static final String DEFAULT_CATEGORY = "Food";
    private static final String DEFAULT_COORDINATE = "37.7833,122.4167";
    //    private static final String DEFAULT_COORDINATE = "34.4258,119.7142";
    private static final boolean IS_DEFAULT = true;
    static YelpAPI yelpApi = null;
    static Restaurant[] restaurant = new Restaurant[MAX_RESTAURANT];
    static YelpAPIDII yelpApiDII = null;

    private static String responseBusinessJSON = "";
    private static String responseJSON = "";

    public YelpSearch(Context context) {
        yelpApi = new YelpAPI(context.getString(R.string.yelp_consumer_key),
                context.getString(R.string.yelp_consumer_secret),
                context.getString(R.string.yelp_token),
                context.getString(R.string.yelp_token_secret));
        for (int i = 0; i < MAX_RESTAURANT; i++) {
            restaurant[i] = new Restaurant();
        }
    }

    /**
     * Queries the Search API based on the search terms and store the result to be process
     *
     * @param yelpApi    <tt>YelpAPI</tt> service instance
     * @param yelpApiDII <tt>YelpAPICLI</tt> arguments for search
     */
    public static void queryAPI(YelpAPI yelpApi, YelpAPIDII yelpApiDII) {

        if (yelpApiDII.isDefault == true) {
            responseBusinessJSON =
                    yelpApi.searchForBusinessesByLocation(yelpApiDII.term, yelpApiDII.location);
            responseJSON = responseBusinessJSON;
        } else {
            responseBusinessJSON =
                    yelpApi.searchForBusinessesByLocation(yelpApiDII.term, yelpApiDII.coordinate);
            responseJSON = responseBusinessJSON;
        }
    }

    /**
     * Process the search result and store as object
     *
     * @param stringJSON <tt>String</tt> for the result from the search
     */
    private void processJSON(String stringJSON) {
        try {
            JSONObject response = new JSONObject(stringJSON);
            JSONArray businesses = response.getJSONArray("businesses");
            for (int index = 0; index < MAX_RESTAURANT; index++) {
                JSONObject businessData = businesses.getJSONObject(index);

                restaurant[index].setBusinessID(businessData.getString("id"));
                restaurant[index].setName(businessData.getString("name"));
                restaurant[index].setRating(Float.parseFloat(businessData.getString("rating")));
                restaurant[index].setPhone(businessData.getString("display_phone"));

                JSONArray category = businessData.getJSONArray("categories");
                String categoryString = "";
                for (int i = 0; i < category.length(); i++) {
                    categoryString += category.getString(i);
                    if (i < category.length() - 1) {
                        categoryString += ", ";
                    }
                }

                restaurant[index].setCategories(categoryString);

                String businessImg = businessData.getString("image_url").replace("ms.jpg", "o.jpg");

                restaurant[index].setBusinessImgURL(businessImg);
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
                restaurant[index].setCity(location.getString("city"));
                restaurant[index].setZipCode(Integer.parseInt(location.getString("postal_code")));

                JSONObject coordinate = location.getJSONObject("coordinate");

                restaurant[index].setLatitude(Float.parseFloat(coordinate.getString("latitude")));
                restaurant[index].setLongitude(Float.parseFloat(coordinate.getString("longitude")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * reset the argument for search
     */
    private void resetSearchItems() {
        if (yelpApiDII == null) {
            yelpApiDII = new YelpAPIDII();
        } else {
            yelpApiDII = null;
            yelpApiDII = new YelpAPIDII();
        }
    }

    /**
     * Run the search query in the background with AsyncTask class
     */
    private void doSearch() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    queryAPI(yelpApi, yelpApiDII);
                    Log.e("#######1", responseBusinessJSON);
                    processJSON(responseBusinessJSON);
                } catch (Exception e) {
                    Log.e("#######2", responseBusinessJSON);
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public void defaultSearch() {
        resetSearchItems();
        doSearch();
    }

    public Restaurant[] getRestaurant() {
        return restaurant;
    }

    public String getResponseJSONString() {
        return responseJSON;
    }

    public void filteredSearch(String term, String location, String category, int range, int sort, String coordinate) {
        resetSearchItems();
        yelpApiDII.term = term;
        yelpApiDII.location = location;
        yelpApiDII.category = category;
        yelpApiDII.range = range;
        yelpApiDII.sort = sort;
        yelpApiDII.coordinate = coordinate;
        yelpApiDII.isDefault = false;
        doSearch();
    }

    /**
     * interface for the input.
     */
    private static class YelpAPIDII {
        public String term = DEFAULT_TERM;
        public String location = DEFAULT_LOCATION;
        public int range = DEFAULT_RANGE;
        public int sort = DEFAULT_SORT;
        public String category = DEFAULT_CATEGORY;
        public String coordinate = DEFAULT_COORDINATE;
        public boolean isDefault = IS_DEFAULT;
    }
}