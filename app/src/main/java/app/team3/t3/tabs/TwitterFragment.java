package app.team3.t3.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import app.team3.t3.R;
import app.team3.t3.yelp.Restaurant;

/**
 * Created by nanden on 7/5/15.
 */
public class TwitterFragment extends Fragment {

    Bundle bundle;
    Restaurant restaurant;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_twitter, container, false);
        final EditText editText = (EditText) view.findViewById(R.id.editText);
        Button tweetButton = (Button) view.findViewById(R.id.tweet_button);
        bundle = getActivity().getIntent().getExtras();
        restaurant = bundle.getParcelable("restaurant_picked");

        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code for tweet
                TweetComposer.Builder builder = new TweetComposer.Builder(getActivity()).text(editText.getText() + "\n\n" + restaurant.getName() + "\n" + restaurant.getRestaurantPage());
                builder.show();
            }
        });
        return view;

    }
}
