package app.team3.t3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


import java.util.Random;


public class MainActivity extends ActionBarActivity implements SensorEventListener, LocationListener {
    protected ResDatabaseHelper resDB;
    protected LocationManager locationManager;
    protected Location location;
    protected double latitude;
    protected double longitude;
    protected String serviceAvailable = "";
    int Random_Number;
    private ImageButton shakeIB;
    private TextView rangeTV;
    private SeekBar rangeSB;
    private int currentRange = 1;
    private boolean isChanged = true;
    private YelpSearch mySearch;
    private Restaurant[] allRestaurant;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private boolean avoid_doubleShake = true; //use to avoid to get multiple searching results
    private Random rn = new Random();
    private Intent getResultIntent;

    final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 22 && avoid_doubleShake == true) {
                runningSearch();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
                Random_Number = rn.nextInt(20) + 1;
                getResultIntent.putExtra("restaurant_picked", resDB.getRestaurant(Random_Number));
                avoid_doubleShake = false;
                startActivity(getResultIntent);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

     /* Sensor Listener  */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restoreChanges();
        if (!SplashScreen.splash.isDestroyed()) {
            SplashScreen.splash.finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getResultIntent = new Intent(MainActivity.this, ActionBarTabsPager.class);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            serviceAvailable = LocationManager.NETWORK_PROVIDER;
        } else {
            serviceAvailable = LocationManager.GPS_PROVIDER;
        }
        locationManager.requestLocationUpdates(serviceAvailable, 10000, 5000, this);
        location = locationManager.getLastKnownLocation(serviceAvailable);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

         /* Yelp  */
        final Context context = getApplicationContext();
        mySearch = new YelpSearch(context);


         /* Shake Sensor  */
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

         /* Database  */
        resDB = new ResDatabaseHelper(context);

         /* Components  */
        shakeIB = (ImageButton) findViewById(R.id.shakeIB);
        rangeTV = (TextView) findViewById(R.id.rangeTV);
        rangeSB = (SeekBar) findViewById(R.id.rangeSB);

         /* Listeners  */
        shakeIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runningSearch();
                Random_Number = rn.nextInt(20) + 1;
                getResultIntent.putExtra("restaurant_picked", resDB.getRestaurant(Random_Number));
                startActivity(getResultIntent);
            }
        });

        rangeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentRange = progress + 1;
                String unit = "";
                if (currentRange > 1) {
                    unit = " Miles";
                } else {
                    unit = " Mile";
                }
                rangeTV.setText("Range: " + currentRange + unit);
                isChanged = true;
                saveChanges();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    } //end onCreat

    /*
     * run yelp search and add data to database for random pick
     * if location and user preferences changed
     */
    protected void runningSearch() {
        if (isChanged) {
            // filteredSearch(term, address, category, range, sort, latitude, longitude)
            allRestaurant = mySearch.filteredSearch(null, null, null, currentRange, 1, latitude, longitude);
            isChanged = false;
            resDB.insertRestaurants(allRestaurant);
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
        getActionBar();
        locationManager.removeUpdates(this);
        saveChanges();
    }

    protected void restoreChanges() {
        SharedPreferences sharedPref = this.getSharedPreferences("user_preferences", 0);
        currentRange = sharedPref.getInt("range", 3);
    }

    protected void saveChanges() {
        SharedPreferences sharedPref = this.getSharedPreferences("user_preferences", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("range", currentRange);
        editor.commit();
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        restoreChanges();
        avoid_doubleShake = true;
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);


        String gpsProvider = LocationManager.GPS_PROVIDER;
        String networkProvider = LocationManager.NETWORK_PROVIDER;
        if (locationManager.isProviderEnabled(gpsProvider)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (!locationManager.isProviderEnabled(gpsProvider)) {
                serviceAvailable = networkProvider;
            } else {
                serviceAvailable = gpsProvider;
            }
            locationManager.requestLocationUpdates(serviceAvailable, 10000, 5000, this);
            location = locationManager.getLastKnownLocation(serviceAvailable);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        } else {
            Log.e("Location Err", "No location provider is not available. Does the device have location services enabled?");
        }
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

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        isChanged = true;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }
}
