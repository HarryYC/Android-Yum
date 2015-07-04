package app.team3.t3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sssbug on 7/3/15.
 */
public class Result extends Activity {
    ResDatabaseHelper resDB;
    Restaurant resultRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        Intent getRandomNumber = getIntent();
        int rdNumber = getRandomNumber.getIntExtra("Random_Number", 0);

        TextView viewResult = (TextView) findViewById(R.id.result_text_view);
        final Context context = getApplicationContext();
        resDB = new ResDatabaseHelper(context);
        Restaurant randomRestaurant = resDB.getRestaurant(rdNumber);
        resultRestaurant = randomRestaurant;
        String restaurantOutput = "BID: " + randomRestaurant.getBusinessID() +
                "\n\nRestaurant Name: " + randomRestaurant.getName() +
                "\n\nRating: " + randomRestaurant.getRating() +
                "\n\nPhone: " + randomRestaurant.getPhone() +
                "\n\nCategories: " + randomRestaurant.getCategories() +
                "\n\nAddress: " + randomRestaurant.getAddress() +
                "\n\nCity: " + randomRestaurant.getCity() +
                "\n\nZipCode: " + randomRestaurant.getZipCode() +
                "\n\nLatitude: " + randomRestaurant.getLatitude() +
                "\n\nLongitude: " + randomRestaurant.getLongitude() +
                "\n\nRatingImgURL: " + randomRestaurant.getRatingImgURL() +
                "\n\nBusinessImgURL: " + randomRestaurant.getRatingImgURL();

        viewResult.setText(restaurantOutput);

    }


    public void onBackToMainClick(View view) {
        onBackPressed();
    }

    public void onSaveToHistory(View view) {
        resDB.saveRestaurant(resultRestaurant);
        Log.e("#save#", "saved");
    }
}
