package app.team3.t3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sssbug on 7/3/15.
 */
public class Result extends Activity {
    ResDatabaseHelper resDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        Intent getRandomNumber = getIntent();
        int rdNumber = getRandomNumber.getIntExtra("Random_Number", 0);

        TextView viewResult = (TextView) findViewById(R.id.result_text_view);
        final Context context = getApplicationContext();
        resDB = new ResDatabaseHelper(context);
        Restaurant randromRestaurant = resDB.getRestaurant(rdNumber);
        String restaurantOutput = "BID: " + randromRestaurant.getBusinessID() +
                "\n\nRestaurant Name: " + randromRestaurant.getName()
                + "\n\nRating: " + randromRestaurant.getRating()
                + "\n\nPhone: " + randromRestaurant.getPhone()
                + "\n\nCategories: " + randromRestaurant.getCategories()
                + "\n\nAddress: " + randromRestaurant.getAddress()
                + "\n\nCity: " + randromRestaurant.getCity()
                + "\n\nZipcode: " + randromRestaurant.getZipCode()
                + "\n\nLatitude: " + randromRestaurant.getLatitude()
                + "\n\nLongitude: " + randromRestaurant.getLongitude()
                + "\n\nRatingImgURL: " + randromRestaurant.getRatingImgURL()
                + "\n\nBusinessImgURL: " + randromRestaurant.getRatingImgURL();

        viewResult.setText(restaurantOutput);

    }


    public void onBackToMainClick(View view) {
        onBackPressed();
    }
}
