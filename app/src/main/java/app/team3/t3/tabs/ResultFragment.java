package app.team3.t3.tabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import app.team3.t3.R;
import app.team3.t3.ResDatabaseHelper;
import app.team3.t3.yelp.RestaurantAdapter;

/**
 * Created by nanden on 7/5/15.
 */
public class ResultFragment extends Fragment {

    private static final String TAG = "result_fragment";
    private RestaurantAdapter restaurant;
    private int width;
    private int height;
    private float distance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle intent = getActivity().getIntent().getExtras();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        restaurant = intent.getParcelable("restaurant_picked");
        distance = intent.getFloat("distance", 0.0f);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_result, container, false);

        final ImageView businessIV = (ImageView) view.findViewById(R.id.restaurantIV);
        final ImageView ratingIV = (ImageView) view.findViewById(R.id.ratingIV);
        final ImageView yelpDisplayIV = (ImageView) view.findViewById(R.id.yelp_display);

        final TextView nameTV = (TextView) view.findViewById(R.id.nameTV);
        final TextView countTV = (TextView) view.findViewById(R.id.countTV);
        final TextView moreInfoTV = (TextView) view.findViewById(R.id.moreInfoTV);

        final Button tryAgainBtn = (Button) view.findViewById(R.id.tryAgainIBtn);
        final Button goBtn = (Button) view.findViewById(R.id.goBtn);

        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ResDatabaseHelper(getActivity().getApplicationContext()).saveRestaurant(restaurant);
                Intent directionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + restaurant.getAddress()));
                directionIntent.setPackage("com.google.android.apps.maps");
                startActivity(directionIntent);
            }
        });

        if (restaurant != null) {

            // Resize drawable for placeholder
            Drawable drawable = getResources().getDrawable(R.drawable.placeholder);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            // Scale it to width x (height * 2 / 6)
            Drawable placeholder = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, width, height * 2 / 6, true));
            // Set your new, scaled drawable "placeholder"

            /*
             * An image loading and caching library for Android focused on smooth scrolling
             * visit https://github.com/bumptech/glide
             * for more information
             */
            Glide.with(getActivity()).load(restaurant.getRestaurantImgUrl()).placeholder(placeholder).dontAnimate().override(width, height * 2 / 6).into(businessIV);
            Glide.with(getActivity()).load(restaurant.getRatingImgUrl()).override(width * 4 / 6, height / 30).into(ratingIV);


            if (distance > 0) {
                nameTV.setText(Html.fromHtml("<b><font size=\"6\">" + restaurant.getRestaurantName() + "</font></b><br>(" + String.valueOf(distance) + " miles)"));
            } else {
                nameTV.setText(Html.fromHtml("<b><font size=\"6\">" + restaurant.getRestaurantName() + "</font></b>"));
            }
            countTV.setText("(" + String.valueOf(restaurant.getReviewCount()) + ")");
            if (!restaurant.getPhoneNumber().equals("none")) {
                moreInfoTV.setText(Html.fromHtml("<b>Phone:</b> " + restaurant.getPhoneNumber() +
                        "<br><br><b>Address:</b> " + restaurant.getAddress()));
            } else {
                moreInfoTV.setText(Html.fromHtml("<b>Address:</b> " + restaurant.getAddress()));
            }

        } else {
            Log.e(TAG, "Sorry, there is no result");
        }

        Log.v("res_name", restaurant.getRestaurantName());

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", 0);
        if (sharedPreferences.getBoolean("app_setting", true)) {
            sharedPreferences.edit().putBoolean("app_setting", false).commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
