package app.team3.t3;

import android.app.AlertDialog;
import android.content.Context;
import android.hardware.SensorListener;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;


public class MainActivity extends ActionBarActivity {
    ResDatabaseHelper resDB;
    private ShakeListener mShaker;
    private Button b1;
    private Button b2;
    private TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();

        final YelpSearch mySearch = new YelpSearch(context);
        mySearch.defaultSearch();
        final Restaurant[] allRestaurant = mySearch.getRestaurant();

        resDB = new ResDatabaseHelper(context);

        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        t1 = (TextView) findViewById(R.id.textView2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setText("inserting data ...");

                resDB.insertRestaurants(allRestaurant);
                t1.setText("insert success");

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rn = new Random();
                int Random_Number = rn.nextInt(allRestaurant.length) + 1;
                Restaurant randromRestaurant = resDB.getRestaurant(Random_Number);
                String restaurantOutput = "Name: " + randromRestaurant.getName()
                        + "\nRating: " + randromRestaurant.getRating()
                        + "\nPhone: " + randromRestaurant.getPhone()
                        + "\nCategories: " + randromRestaurant.getCategories()
                        + "\n Address: " + randromRestaurant.getAddress()
                        + "\nCity: " + randromRestaurant.getCity()
                        + "\nZipcode: " + randromRestaurant.getZipcode()
                        + "\nLatitude: " + randromRestaurant.getLatitude()
                        + "\nLongitude: " + randromRestaurant.getLongitude();


                t1.setText(restaurantOutput);
            }
        });

        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                vibe.vibrate(300);
                Random rn = new Random();
                int Random_Number = rn.nextInt(allRestaurant.length) + 1;
                Restaurant randromRestaurant = resDB.getRestaurant(Random_Number);
                String restaurantOutput = "Name: " + randromRestaurant.getName()
                        + "\nRating: " + randromRestaurant.getRating()
                        + "\nPhone: " + randromRestaurant.getPhone()
                        + "\nCategories: " + randromRestaurant.getCategories()
                        + "\n Address: " + randromRestaurant.getAddress()
                        + "\nCity: " + randromRestaurant.getCity()
                        + "\nZipcode: " + randromRestaurant.getZipcode()
                        + "\nLatitude: " + randromRestaurant.getLatitude()
                        + "\nLongitude: " + randromRestaurant.getLongitude();
                t1.setText(restaurantOutput);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
