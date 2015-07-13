package app.team3.t3.tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.team3.t3.History;
import app.team3.t3.ImageDownloader;
import app.team3.t3.R;
import app.team3.t3.ResDatabaseHelper;
import app.team3.t3.Restaurant;

/**
 * Created by nanden on 7/5/15.
 */
public class ResultFragment extends Fragment {

    private String name;
    protected String restaurantImg;
    protected String ratingImg;
    private String reviewCount;
    private Bundle bundle;
    private Context context;
    private int width;
    private int height;
    protected ResDatabaseHelper resDatabaseHelper;
    protected Restaurant restaurant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        context = this.getActivity().getApplicationContext();
        resDatabaseHelper = new ResDatabaseHelper(context);

        restaurant = getArguments().getParcelable("result");

        name = restaurant.getName();
        reviewCount = "(" + restaurant.getReviewCount() + ")";

        restaurantImg = restaurant.getBusinessImgURL();
        ratingImg = restaurant.getRatingImgURL();

        width = bundle.getInt("screen_width");
        height = bundle.getInt("screen_height");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        final ImageView businessIV = (ImageView) view.findViewById(R.id.businessIV);
        final ImageView ratingIV = (ImageView) view.findViewById(R.id.ratingIV);

        final TextView nameTV = (TextView) view.findViewById(R.id.nameTV);
        final TextView countTV = (TextView) view.findViewById(R.id.countTV);

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
                resDatabaseHelper.saveRestaurant(restaurant);
                Intent intent = new Intent(getActivity(), History.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        if (bundle.toString() != null) {
            Log.e("restaurant", bundle.toString());

            new ImageDownloader(context, businessIV, width, height).execute(restaurantImg);
            new ImageDownloader(context, ratingIV).execute(ratingImg);

            nameTV.setText(name);
            countTV.setText(reviewCount);
        } else {
            Log.e("##result##", getArguments().toString());
        }

        return view;

    }
}
