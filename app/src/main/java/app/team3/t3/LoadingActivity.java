package app.team3.t3;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import app.team3.t3.yelp.Restaurant;
import app.team3.t3.yelp.RestaurantFinder;


public class LoadingActivity extends AppCompatActivity {

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
    private LocationListener locationListener;
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
            // Session of LocationListener methods implementation
            locationListener = new LocationListener() {
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
            };

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
                locationManager.requestLocationUpdates(serviceAvailable, 30000, 0, locationListener);
                currentlocation = locationManager.getLastKnownLocation(serviceAvailable);
                latitude = currentlocation.getLatitude();
                longitude = currentlocation.getLongitude();
            } else {
                Log.e("Location Err", "No location provider is not available. Does the device have location services enabled?");
            /*
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("We notice your location services are not enabled, please go to settings and enable them.");
            builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent cont = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(cont);
                }

            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            */
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
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
        loadProgressBar.setVisibility(View.GONE);
        finish();
    }

    /*
     * run yelp search and add data to database for random pick
     * if location and user preferences changed
     */
    protected void runningSearch() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
        if (preferencesChanged) {
            // filteredSearch(term, address, category, range, sort, latitude, longitude)
            Restaurant[] allRestaurant = mySearch.filteredSearch(searchTerm, selectedAddress, selectedCategory, selectedRange, selectedSortMode, latitude, longitude);
            resDB.insertRestaurants(allRestaurant);
        }
        Intent getResultIntent = new Intent(LoadingActivity.this, ActionBarTabsPager.class);
        getResultIntent.putExtra("restaurant_picked", resDB.getRestaurant(Random_Number));
        preferencesChanged = false;
        startActivity(getResultIntent);
    }
}