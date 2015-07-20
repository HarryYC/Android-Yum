package app.team3.t3;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import app.team3.t3.yelp.Restaurant;
import app.team3.t3.yelp.RestaurantFinder;


public class LoadingActivity extends AppCompatActivity implements LocationListener {

    private String searchTerm;
    private int selectedRange;
    private String selectedAddress;
    private String selectedCategory;
    private int selectedSortMode;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private boolean useAddress = false;
    private Location currentlocation;
    private LocationManager locationManager;
    private boolean preferencesChanged = true;
    private ResDatabaseHelper resDB;
    private Context context;
    private int Random_Number;
    private RestaurantFinder mySearch;
    private ProgressBar loadProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        loadProgressBar = (ProgressBar) findViewById(R.id.loadProgressBar);
        loadProgressBar.setVisibility(View.VISIBLE);
        Bundle userPreferences = getIntent().getExtras();
        context = getApplicationContext();

        mySearch = new RestaurantFinder(context);
        resDB = new ResDatabaseHelper(context);
        searchTerm = userPreferences.getString("term", null);
        selectedRange = userPreferences.getInt("range", 1);
        selectedAddress = userPreferences.getString("address", null);
        selectedCategory = userPreferences.getString("category", null);
        selectedSortMode = userPreferences.getInt("sort", 3);
        useAddress = userPreferences.getBoolean("use_address", false);
        preferencesChanged = userPreferences.getBoolean("preferences_changed");
        Random_Number = userPreferences.getInt("random_number", 0);

        if (!useAddress) {
            // Location Service
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            String gpsProvider = LocationManager.GPS_PROVIDER;
            String networkProvider = LocationManager.NETWORK_PROVIDER;
            String serviceAvailable;
            if (locationManager.isProviderEnabled(gpsProvider)
                    || locationManager.isProviderEnabled(networkProvider)) {
                if (!locationManager.isProviderEnabled(gpsProvider)) {
                    serviceAvailable = networkProvider;
                } else {
                    serviceAvailable = gpsProvider;
                }
                locationManager.requestLocationUpdates(serviceAvailable, 5000, 0, this);
                currentlocation = locationManager.getLastKnownLocation(serviceAvailable);
                latitude = currentlocation.getLatitude();
                longitude = currentlocation.getLongitude();
            } else {
                Log.e("Location Err", "No location provider is not available. Does the device have location services enabled?");
            }
            selectedAddress = null;
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loading, menu);
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

    @Override
    public void onStart() {
        super.onStart();
        runningSearch();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!useAddress) {
            locationManager.removeUpdates(this);
        }
        loadProgressBar.setVisibility(View.GONE);
        finish();
    }

    /*
     * run yelp search and add data to database for random pick
     * if location and user preferences changed
     */
    protected void runningSearch() {
        final int WAIT_TIME = 1500;

        if (preferencesChanged) {
            mySearch.useAddress(useAddress);

            // filteredSearch(term, address, category, range, sort, latitude, longitude)
            Restaurant[] allRestaurant = mySearch.filteredSearch(searchTerm, selectedAddress, selectedCategory, selectedRange, selectedSortMode, latitude, longitude);
            resDB.insertRestaurants(allRestaurant);
        }
        preferencesChanged = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent getResultIntent = new Intent(LoadingActivity.this, ActionBarTabsPager.class);
                getResultIntent.putExtra("restaurant_picked", resDB.getRestaurant(Random_Number));
                startActivity(getResultIntent);
                finish();
            }
        }, WAIT_TIME);
    }


    // Implementing methods for LocationListener
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            currentlocation = location;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
