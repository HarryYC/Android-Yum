package app.team3.t3.yelp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * This class is implementation of the RestaurantManager
 */
public class RestaurantAdapter implements RestaurantManager, Parcelable {

    //Used for Log message
    private static final String TAG = RestaurantAdapter.class.getSimpleName();

    public static final Parcelable.Creator<RestaurantAdapter> CREATOR = new Parcelable.Creator<RestaurantAdapter>() {
        @Override
        public RestaurantAdapter createFromParcel(Parcel source) {
            return new RestaurantAdapter(source);
        }

        @Override
        public RestaurantAdapter[] newArray(int size) {
            return new RestaurantAdapter[size];
        }
    };

    private String mRestaurantID;
    private String mRestaurantName;
    private float mRatingCount;
    private int mReviewCount;
    private String mPhoneNumber;
    private String mCategory;
    private String mAddress;
    private double mLatitude;
    private double mLongitude;
    private String mRestaurantPage;
    private String mRatingImgURL;
    private String mRestaurantImgURL;

    // default constructor;
    public RestaurantAdapter() {
        mRestaurantID = "";
        mRestaurantName = "";
        mRatingCount = 0f;
        mReviewCount = 0;
        mPhoneNumber = "";
        mCategory = "";
        mAddress = "";
        mLatitude = 0.0;
        mLongitude = 0.0;
        mRestaurantPage = "";
        mRatingImgURL = "";
        mRestaurantImgURL = "";
    }

    // constructor
    public RestaurantAdapter(String id, String name, float ratingCount, int reviewCount, String phone, String category, String address, double latitude, double longitude, String page, String ratingImgUrl, String restaurantImgUrl) {
        mRestaurantID = id;
        mRestaurantName = name;
        mRatingCount = ratingCount;
        mReviewCount =  reviewCount;
        mPhoneNumber = phone;
        mCategory = category;
        mAddress = address;
        mLatitude = latitude;
        mLongitude = longitude;
        mRestaurantPage = page;
        mRatingImgURL = ratingImgUrl;
        mRestaurantImgURL = restaurantImgUrl;

        Log.d(TAG, "id: " + id + " name: " + name + " ratingCount: " + ratingCount + " phone: " + phone + " category: " +
                category + " address: " + category + " address: " + address + " latitude: " + latitude + " longitude: " +
                longitude + " page: " + page + " ratingImg: " + ratingImgUrl + " restaurantImgUrl: " + restaurantImgUrl);
    }

    // Parcelling
    public RestaurantAdapter(Parcel source) {
        mRestaurantID = source.readString();
        mRestaurantName = source.readString();
        mRatingCount = source.readFloat();
        mReviewCount = source.readInt();
        mPhoneNumber = source.readString();
        mCategory = source.readString();
        mAddress = source.readString();
        mLatitude = source.readDouble();
        mLongitude = source.readDouble();
        mRestaurantPage = source.readString();
        mRatingImgURL = source.readString();
        mRestaurantImgURL = source.readString();
    }

    @Override
    public String getRestaurantId() {
        return mRestaurantID;
    }

    @Override
    public String getRestaurantName() {
        return mRestaurantName;
    }

    @Override
    public float getRating() {
        return mRatingCount;
    }

    @Override
    public int getReviewCount() {
        return mReviewCount;
    }

    @Override
    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    @Override
    public String getCategory() {
        return mCategory;
    }

    @Override
    public String getAddress() {
        return mAddress;
    }

    @Override
    public double getLatitude() {
        return mLatitude;
    }

    @Override
    public double getLongitude() {
        return mLongitude;
    }

    @Override
    public String getRestaurantPage() {
        return mRestaurantPage;
    }

    @Override
    public String getRatingImgUrl() {
        return mRatingImgURL;
    }

    @Override
    public String getRestaurantImgUrl() {
        return mRestaurantImgURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(mRestaurantID);
        dest.writeString(mRestaurantName);
        dest.writeFloat(mRatingCount);
        dest.writeInt(mReviewCount);
        dest.writeString(mPhoneNumber);
        dest.writeString(mCategory);
        dest.writeString(mAddress);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeString(mRestaurantPage);
        dest.writeString(mRatingImgURL);
        dest.writeString(mRestaurantImgURL);

    }
}
