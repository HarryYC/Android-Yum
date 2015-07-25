package app.team3.t3.tabs;

import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import app.team3.t3.ImageDownloader;
import app.team3.t3.R;
import app.team3.t3.ResDatabaseHelper;
import app.team3.t3.yelp.Restaurant;

// import twitter library

/**
 * Created by nanden on 7/5/15.
 */
public class ResultFragment extends Fragment {

    private static final String TAG = "result_fragment";
    private Restaurant restaurant;
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
        distance = intent.getFloat("distance");
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_result, container, false);

        final ProgressBar resultProgressBar = (ProgressBar) view.findViewById(R.id.resultProgressBar);

        final ImageView businessIV = (ImageView) view.findViewById(R.id.restaurantIV);
        final ImageView ratingIV = (ImageView) view.findViewById(R.id.ratingIV);

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

            try {
                ImageDownloader businessID = new ImageDownloader(businessIV, width, height * 2 / 6);
                businessID.execute(restaurant.getRestaurantImgURL());
                ImageDownloader ratingID = new ImageDownloader(ratingIV);
                ratingID.execute(restaurant.getRatingImgURL());
                nameTV.setText(Html.fromHtml("<b><font size=\"6\">" + restaurant.getName() + "</font></b><br>(" + String.valueOf(distance) + " miles)"));
                countTV.setText("(" + String.valueOf(restaurant.getReviewCount()) + ")");
                moreInfoTV.setText(Html.fromHtml("<b>Phone:</b> " + restaurant.getPhone() +
                        "<br><br><b>Address:</b> " + restaurant.getAddress()));
                while (businessID.get() != true && ratingID.get() != true) ;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "Sorry, there is no result");
        }

        view.findViewById(R.id.restaurantInfo).setVisibility(View.VISIBLE);
        view.findViewById(R.id.buttonContainer).setVisibility(View.VISIBLE);
        resultProgressBar.setVisibility(View.GONE);

        Log.v("res_name", restaurant.getName());

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

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
