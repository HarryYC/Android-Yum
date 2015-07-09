package app.team3.t3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
        TextView viewResult = (TextView) findViewById(R.id.result_text_view);
        final Context context = getApplicationContext();
        resDB = new ResDatabaseHelper(context);

        /* get Intent from Main activity or history activity */
        Intent getIntents = getIntent();
        int rdNumber = getIntents.getIntExtra("Random_Number", 0);
        String bussinessID = getIntents.getStringExtra("Business_ID");


        /* If the caller is from main activity, the random number won't be zero */
        if (rdNumber != 0) {
            Restaurant randomRestaurant = resDB.getRestaurant(rdNumber);
            resultRestaurant = randomRestaurant;
            String restaurantOutput = "BID: " + randomRestaurant.getBusinessID() +
                    "\n\nName: " + randomRestaurant.getName() +
                    "\n\nRating: " + randomRestaurant.getRating() +
                    "\n\nPhone: " + randomRestaurant.getPhone() +
//                    "\n\nCategories: " + randomRestaurant.getCategories() +
                    "\n\nAddress: " + randomRestaurant.getAddress() +
                    "\n\nCity: " + randomRestaurant.getCity() +
                    "\n\nZipCode: " + randomRestaurant.getZipCode();
//                    "\n\nLatitude: " + randomRestaurant.getLatitude() +
//                    "\n\nLongitude: " + randomRestaurant.getLongitude() +
//                    "\n\nRatingImgURL: " + randomRestaurant.getRatingImgURL() +
//                    "\n\nBusinessImgURL: " + randomRestaurant.getRatingImgURL()

            viewResult.setText(restaurantOutput);
        }

        /* deal with the call from history activity */
        if (bussinessID != null && !bussinessID.isEmpty()) {
            Log.e("##BID## ", bussinessID);

            Restaurant randomRestaurant = resDB.getRestaurant(bussinessID);
            resultRestaurant = randomRestaurant;
            String restaurantOutput = "BID: " + randomRestaurant.getBusinessID() +
                    "\n\nName: " + randomRestaurant.getName() +
                    "\n\nRating: " + randomRestaurant.getRating() +
                    "\n\nPhone: " + randomRestaurant.getPhone() +
//                    "\n\nCategories: " + randomRestaurant.getCategories() +
                    "\n\nAddress: " + randomRestaurant.getAddress() +
                    "\n\nCity: " + randomRestaurant.getCity() +
                    "\n\nZipCode: " + randomRestaurant.getZipCode();
//                    "\n\nLatitude: " + randomRestaurant.getLatitude() +
//                    "\n\nLongitude: " + randomRestaurant.getLongitude() +
//                    "\n\nRatingImgURL: " + randomRestaurant.getRatingImgURL() +
//                    "\n\nBusinessImgURL: " + randomRestaurant.getRatingImgURL();

            viewResult.setText(restaurantOutput);

        }





    }

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or
     * {@link #onPause}, for your activity to start interacting with the user.
     * This is a good place to begin animations, open exclusive-access devices
     * (such as the camera), etc.
     * <p/>
     * <p>Keep in mind that onResume is not the best indicator that your activity
     * is visible to the user; a system window such as the keyguard may be in
     * front.  Use {@link #onWindowFocusChanged} to know for certain that your
     * activity is visible to the user (for example, to resume a game).
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestoreInstanceState
     * @see #onRestart
     * @see #onPostResume
     * @see #onPause
     */



    public void onBackToMainClick(View view) {
        onBackPressed();
    }

    public void onSaveToHistory(View view) {
        resDB.saveRestaurant(resultRestaurant);
    }

    public void onHistoryClick(View view) {
        Intent goHistoryIntent = new Intent(this, History.class);
        finish(); //avoid multiple activity
        startActivity(goHistoryIntent);
    }
}
