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
public class result extends Activity {

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
        String restaurantOutput = "Name: " + randromRestaurant.getName()
                + "\nRating: " + randromRestaurant.getRating()
                + "\nPhone: " + randromRestaurant.getPhone()
                + "\nCategories: " + randromRestaurant.getCategories()
                + "\n Address: " + randromRestaurant.getAddress()
                + "\nCity: " + randromRestaurant.getCity()
                + "\nZipcode: " + randromRestaurant.getZipcode()
                + "\nLatitude: " + randromRestaurant.getLatitude()
                + "\nLongitude: " + randromRestaurant.getLongitude();
        viewResult.setText(restaurantOutput);

    }


    public void onBackToMainClick(View view) {
        onBackPressed();
    }
}
