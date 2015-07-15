package app.team3.t3.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import app.team3.t3.ImageDownloader;
import app.team3.t3.R;
import app.team3.t3.ResDatabaseHelper;
import app.team3.t3.Restaurant;

/**
 * Created by nanden on 7/5/15.
 */
public class ResultFragment extends Fragment {

    protected ResDatabaseHelper resDatabaseHelper;
    protected Restaurant restaurant;
    private Context context;
    private int width;
    private int height;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity().getApplicationContext();
        Bundle intent = getActivity().getIntent().getExtras();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        resDatabaseHelper = new ResDatabaseHelper(context);
        restaurant = intent.getParcelable("restaurant_picked");
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        final ProgressBar loadingPB = (ProgressBar) view.findViewById(R.id.loadingPB);

        final ImageView businessIV = (ImageView) view.findViewById(R.id.businessIV);
        final ImageView ratingIV = (ImageView) view.findViewById(R.id.ratingIV);

        final TextView nameTV = (TextView) view.findViewById(R.id.nameTV);
        final TextView countTV = (TextView) view.findViewById(R.id.countTV);

        final Button tryAgainBtn = (Button) view.findViewById(R.id.tryAgainIBtn);
        final Button goBtn = (Button) view.findViewById(R.id.goBtn);

        final ImageButton tweetBtn = (ImageButton) view.findViewById(R.id.tweetBtn);

        final ViewPager mViewPager = (ViewPager) container.findViewById(R.id.view_pager);


        final TweetComposer.Builder builder = new TweetComposer.Builder(getActivity());
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resDatabaseHelper.saveRestaurant(restaurant);
                mViewPager.setCurrentItem(1);
            }
        });

        if (restaurant != null) {
            final ImageDownloader restaurantID = new ImageDownloader(loadingPB, businessIV, width, height);
            final ImageDownloader ratingID = new ImageDownloader(loadingPB, ratingIV);

            builder.text(restaurant.getName());

            restaurantID.execute(restaurant.getBusinessImgURL());
            ratingID.execute(restaurant.getRatingImgURL());
            nameTV.setText(restaurant.getName());
            countTV.setText("(" + String.valueOf(restaurant.getReviewCount()) + ")");
            tweetBtn.setVisibility(View.VISIBLE);

        } else {
            Log.e("##result##", "get result failed");
        }

        tweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });

        return view;

    }
}
