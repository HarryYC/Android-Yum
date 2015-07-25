package app.team3.t3.yelp;

/**
 * Created by ivan on 7/25/15.
 */
public class RestaurantSearchException extends Exception {

    private String message = null;

    public RestaurantSearchException() {

    }

    public RestaurantSearchException(String message) {
        super(message);
        this.message = message;
    }

    public RestaurantSearchException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
