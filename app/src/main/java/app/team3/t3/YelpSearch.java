package app.team3.t3;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ivan on 6/30/15.
 */


public class YelpSearch {
    private static final int MAX_RESTAURANT = 20;
    private static final String DEFAULT_TERM = "food";
    private static final String DEFAULT_LOCATION = "San Francisco, CA";
    private static final int DEFAULT_RANGE = 1609;
    private static final int DEFAULT_SORT = 0;
    private static final String DEFAULT_CATEGORY = "restaurants";
    private static final String DEFAULT_COORDINATE = null;
    private static final boolean IS_DEFAULT = true;
    private YelpAPI yelpApi = null;
    private Restaurant[] restaurant;
    private YelpAPIDII yelpApiDII = null;

    public YelpSearch(Context context) {
        yelpApi = new YelpAPI(context.getString(R.string.yelp_consumer_key),
                context.getString(R.string.yelp_consumer_secret),
                context.getString(R.string.yelp_token),
                context.getString(R.string.yelp_token_secret));
    }

    /**
     * Queries the Search API based on the search terms and store the result to be process
     *  @param yelpApi    <tt>YelpAPI</tt> service instance
     * @param yelpApiDII <tt>YelpAPICLI</tt> arguments for search
     */
    public Restaurant[] queryAPI(YelpAPI yelpApi, YelpAPIDII yelpApiDII) {
        String responseBusinessJSON =
                yelpApi.searchForBusiness(yelpApiDII.term, yelpApiDII.location, yelpApiDII.category, yelpApiDII.sort, yelpApiDII.range, yelpApiDII.coordinate);

        try {
            JSONObject response = new JSONObject(responseBusinessJSON);
            int totalResults = Integer.parseInt(response.getString("total"));
            if (totalResults < MAX_RESTAURANT) {
                restaurant = new Restaurant[totalResults];
            } else {
                restaurant = new Restaurant[MAX_RESTAURANT];
            }

            JSONArray businesses = response.getJSONArray("businesses");
            for (int index = 0; index < MAX_RESTAURANT; index++) {
                restaurant[index] = new Restaurant();

                JSONObject businessData = businesses.getJSONObject(index);
                restaurant[index].setBusinessID(businessData.getString("id"));
                restaurant[index].setName(businessData.getString("name"));
                restaurant[index].setRating(Float.parseFloat(businessData.getString("rating")));
                restaurant[index].setReviewCount(Integer.parseInt(businessData.getString("review_count")));
                restaurant[index].setPhone(businessData.getString("display_phone"));

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
        return restaurant;
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public Restaurant[] filteredSearch(String term, String location, String category, int range, int sort, float latitude, float longitude) {
        resetSearchItems();
        if (term != null) {
            yelpApiDII.term = term;
        }
        if (location != null) {
            yelpApiDII.location = location;
        }
        if (category != null) {
            yelpApiDII.category = category;
        }
        if (range > 1609 && range <= 40000) {
            yelpApiDII.range = range;
        }
        if (sort >= 0 && sort < 3) {
            yelpApiDII.sort = sort;
        }
        if (latitude != 0 && longitude != 0) {
            yelpApiDII.coordinate = String.valueOf(latitude) + "," + String.valueOf(longitude);
        }
        if (location == null && latitude == 0 && longitude == 0) {
            return null;
        } else {
            return queryAPI(yelpApi, yelpApiDII);
        }
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
    }
}
