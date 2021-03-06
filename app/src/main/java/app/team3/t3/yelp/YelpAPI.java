package app.team3.t3.yelp;

import android.os.AsyncTask;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.concurrent.ExecutionException;

/**
 * Yelp API V2 from Code sample. (Modified for android)
 * <p/>
 * See <a href="http://www.yelp.com/developers/documentation">Yelp Documentation</a> for more info.
 */
public class YelpAPI {

    private static final String API_HOST = "api.yelp.com";
    private static final int SEARCH_LIMIT = 20;
    private static final String SEARCH_PATH = "/v2/search";
    private static final String BUSINESS_PATH = "/v2/business";

    OAuthService service;
    Token accessToken;

    public YelpAPI(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service =
                new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(consumerKey)
                        .apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

    /**
     * Creates and sends a request to the Search API by term, location, category, sort, range, and coordinate.
     * <p/>
     * See <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp Search API V2</a>
     * for more info.
     *
     * @param term       <tt>String</tt> of the search term to be queried
     * @param location   <tt>String</tt> of the location
     * @param category   <tt>String</tt> of the search category to be queried
     * @param sort       <tt>int</tt> of the sort mode
     * @param coordinate <tt>String</tt> of the latitude an longitude
     * @param range      <tt>int</tt> of the range for searching
     * @return <tt>String</tt> JSON Response
     */
    public String searchForBusiness(String term, String location, String category, int sort, int range, String coordinate) {
        OAuthRequest request = createOAuthRequest(SEARCH_PATH);
        request.addQuerystringParameter("term", term);
        if(location != null) {
            request.addQuerystringParameter("location", location);
        }
        request.addQuerystringParameter("category_filter", category);
        request.addQuerystringParameter("sort", String.valueOf(sort));
        if(coordinate != null) {
            request.addQuerystringParameter("ll", coordinate);
        }
        request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
        request.addQuerystringParameter("radius_filter", String.valueOf(range));
        return sendRequestAndGetResponse(request);
    }

    /**
     * Creates and sends a request to the Business API by business ID.
     * <p/>
     * See <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp Business API V2</a>
     * for more info.
     *
     * @param businessID <tt>String</tt> business ID of the requested business
     * @return <tt>String</tt> JSON Response
     */
    public String searchByBusinessId(String businessID) {
        OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
        return sendRequestAndGetResponse(request);
    }

    /**
     * Creates and returns an {@link OAuthRequest} based on the API endpoint specified.
     *
     * @param path API endpoint to be queried
     * @return <tt>OAuthRequest</tt>
     */
    private OAuthRequest createOAuthRequest(String path) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://" + API_HOST + path);
        return request;
    }

    /**
     * Sends an {@link OAuthRequest} and returns the {@link Response} body.
     *
     * @param request {@link OAuthRequest} corresponding to the API request
     * @return <tt>String</tt> body of API response
     */
    private String sendRequestAndGetResponse(OAuthRequest request) {
        String taskResponse = "";

        try {
            taskResponse = new sendRequestAndGetRequestTask().execute(request, this.service, this.accessToken).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return taskResponse;
    }

    /**
     * Class to do Sends {@link OAuthRequest},
     * and returns the {@link Response} in the background thread
     */
    private class sendRequestAndGetRequestTask extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... params) {

            OAuthRequest request = (OAuthRequest) params[0];
            OAuthService service = (OAuthService) params[1];
            Token accessToken = (Token) params[2];

            service.signRequest(accessToken, request);
            Response response = request.send();
            return response.getBody();
        }
    }
}
