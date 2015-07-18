package app.team3.t3.tabs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.concurrent.ExecutionException;

import app.team3.t3.ImageDownloader;
import app.team3.t3.R;
import app.team3.t3.ResDatabaseHelper;
import app.team3.t3.yelp.Restaurant;

/**
 * Created by nanden on 7/5/15.
 */
public class ResultFragment extends Fragment {

    private static final String TAG = "result_fragment";
    private ResDatabaseHelper resDatabaseHelper;
    private Restaurant restaurant;
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
        // restaurant = resDatabaseHelper.getRestaurant(intent.getInt("restaurant_picked"));
        restaurant = intent.getParcelable("restaurant_picked");
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_result, container, false);

        final ImageView businessIV = (ImageView) view.findViewById(R.id.restaurantIV);
        final ImageView ratingIV = (ImageView) view.findViewById(R.id.ratingIV);

        final TextView nameTV = (TextView) view.findViewById(R.id.nameTV);
        final TextView countTV = (TextView) view.findViewById(R.id.countTV);

        final Button tryAgainBtn = (Button) view.findViewById(R.id.tryAgainIBtn);
        final Button goBtn = (Button) view.findViewById(R.id.goBtn);

        final ImageButton tweetBtn = (ImageButton) view.findViewById(R.id.tweetBtn);
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_result);
        final TweetComposer.Builder builder = new TweetComposer.Builder(getActivity()).text(restaurant.getName() + "\n" + restaurant.getRestaurantPage());

        tweetBtn.bringToFront();
        tweetBtn.setOnTouchListener(new View.OnTouchListener() {
            float m_lastTouchX;
            float m_lastTouchY;
            float m_dx;
            float m_dy;
            float m_prevX = 1.0f;
            float m_prevY = 1.0f;
            float m_posX;
            float m_posY;
            private long lastActionDownTime = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        m_lastTouchX = event.getX();
                        m_lastTouchY = event.getY();
                        lastActionDownTime = System.currentTimeMillis();
                        Log.e("x, y", String.valueOf(m_lastTouchX) + ", " + String.valueOf(m_lastTouchY));
                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        if (System.currentTimeMillis() - lastActionDownTime < 500) {
                            builder.show();
                        }
                        break;
                    }

                    case MotionEvent.ACTION_MOVE: {
                        m_dx = event.getX() - m_lastTouchX;
                        m_dy = event.getY() - m_lastTouchY;

                        m_posX = m_prevX + m_dx;
                        m_posY = m_prevY + m_dy;

                        Log.i("cx, cy", String.valueOf(m_posX) + ", " + String.valueOf(m_posY));

                        if (m_posY >= 0 && (m_posY + v.getHeight()) < relativeLayout.getHeight()) {
                            m_prevY = m_posY;
                            tweetBtn.setY(m_posY);
                        }

                        if (m_posX >= 0 && (m_posX + v.getWidth()) < relativeLayout.getWidth()) {
                            tweetBtn.setX(m_posX);
                            m_prevX = m_posX;
                        }
                        break;
                    }
                    default:
                        break;
                }
                return true;
            }
        });

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
                Intent directionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + restaurant.getAddress()));
                directionIntent.setPackage("com.google.android.apps.maps");
                startActivity(directionIntent);
            }
        });

        if (restaurant != null) {
            try {
                businessIV.setImageBitmap(new ImageDownloader(width, height * 2 / 6).execute(restaurant.getRestaurantImgURL()).get());
                // businessIV.setImageBitmap(new ImageDownloader().execute(restaurant.getRestaurantImgURL()).get());
                ratingIV.setImageBitmap(new ImageDownloader().execute(restaurant.getRatingImgURL()).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            nameTV.setText(restaurant.getName());
            countTV.setText("(" + String.valueOf(restaurant.getReviewCount()) + ")");

            tweetBtn.setVisibility(View.VISIBLE);

        } else {
            Log.e(TAG, "Sorry, there is no result");
        }

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
