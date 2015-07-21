package app.team3.t3;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.Random;

import app.team3.t3.yelp.Restaurant;
import app.team3.t3.yelp.RestaurantFinder;


public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    SoundPool mySound;
    int touchId, boomId;
    private ImageButton mShakeImageButton;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private boolean avoid_doubleShake = true; //use to avoid to get multiple searching results
    private ResDatabaseHelper resDB;
    final SensorEventListener mSensorListener = new SensorEventListener() {

        /* Sensor Listener  */
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 15 && avoid_doubleShake == true) {
                getResultPage();
                avoid_doubleShake = false;
                mySound.play(boomId, 1, 1, 1, 0, 1);
            }
            mAccelLast = mAccelCurrent;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private RestaurantFinder mySearch;
    private Restaurant[] restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String gpsProvider = LocationManager.GPS_PROVIDER;
        String networkProvider = LocationManager.NETWORK_PROVIDER;
        String serviceAvailable;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getBooleanExtra("is_started", false)) {
            Intent locationSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(locationSetting);
        }

        mySound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        touchId = mySound.load(this, R.raw.touch, 1);
        boomId = mySound.load(this, R.raw.boom, 1);

        resDB = new ResDatabaseHelper(MainActivity.this);
        mySearch = new RestaurantFinder(getApplicationContext());

        new Eula(this).show();

         /* Shake Sensor  */
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

         /* Components  */
        mShakeImageButton = (ImageButton) findViewById(R.id.shake_ImageButton);

         /* Listeners  */
        mShakeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySound.play(boomId, 1, 1, 1, 0, 1);
                mySound.play(touchId, 1, 1, 1, 1, 1f);
                getResultPage();
            }
        });

        /* Location service, get current location */
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(gpsProvider)
                || locationManager.isProviderEnabled(networkProvider)) {
            if (!locationManager.isProviderEnabled(gpsProvider)) {
                serviceAvailable = networkProvider;
            } else {
                serviceAvailable = gpsProvider;
            }
            locationManager.requestLocationUpdates(serviceAvailable, 5000, 0, this);
            Location currentLocation = locationManager.getLastKnownLocation(serviceAvailable);
            mySearch.setLatitude(currentLocation.getLatitude());
            Log.e("####lati", String.valueOf(mySearch.getLatitude()));
            mySearch.setLongitude(currentLocation.getLongitude());
            Log.e("####longi", String.valueOf(mySearch.getLongitude()));
            restaurants = mySearch.filteredSearch();
            resDB.insertRestaurants(restaurants);
        } else {
            Log.e("####Location Err", "No location provider is not available. Does the device have location services enabled?");
        }
    } //end onCreat

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
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
        avoid_doubleShake = true;
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    /* pass a random restaurant object to result page */

    protected void getResultPage() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
        Intent getResultIntent = new Intent(MainActivity.this, LoadingActivity.class);
        int Random_Number = new Random().nextInt(20) + 1;
        getResultIntent.putExtra("restaurant_picked", resDB.getRestaurant(Random_Number));
        startActivity(getResultIntent);

    }

    /* fillter button */
    public void onFilterClick(View view) {
        Button filterButton = (Button) findViewById(R.id.filter_button);
        LayoutInflater mLayoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = mLayoutInflater.inflate(R.layout.fragment_filter, null);
        final PopupWindow mPopupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cancelFilterButton = (Button) mView.findViewById(R.id.cancel_filter_button);
        Button applyFilterButton = (Button) mView.findViewById(R.id.apply_options_button);


        final Spinner distanceSpinner = (Spinner) mView.findViewById(R.id.distance_spinner);
        final Spinner categorySpinner = (Spinner) mView.findViewById(R.id.category_spinner);
        cancelFilterButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        applyFilterButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                /* category parameters */
                if (categorySpinner.getSelectedItem().toString().equals("All")) {
                    mySearch.setCategory("restaurants");
                } else if (categorySpinner.getSelectedItem().toString().equals("Japanese")) {
                    mySearch.setCategory("japanese");
                } else if (categorySpinner.getSelectedItem().toString().equals("American (Traditional)")) {
                    mySearch.setCategory("tradamerican");
                } else if (categorySpinner.getSelectedItem().toString().equals("Chinese")) {
                    mySearch.setCategory("chinese");
                } else if (categorySpinner.getSelectedItem().toString().equals("Indian")) {
                    mySearch.setCategory("indpak");
                } else if (categorySpinner.getSelectedItem().toString().equals("Pizza")) {
                    mySearch.setCategory("pizza");
                } else if (categorySpinner.getSelectedItem().toString().equals("American (New)")) {
                    mySearch.setCategory("newamerican");
                } else if (categorySpinner.getSelectedItem().toString().equals("Mexican")) {
                    mySearch.setCategory("mexican");
                } else if (categorySpinner.getSelectedItem().toString().equals("Middle Eastern")) {
                    mySearch.setCategory("mediterranean");
                } else if (categorySpinner.getSelectedItem().toString().equals("Modern European")) {
                    mySearch.setCategory("modern_european");
                } else if (categorySpinner.getSelectedItem().toString().equals("French")) {
                    mySearch.setCategory("french");
                } else if (categorySpinner.getSelectedItem().toString().equals("Thai")) {
                    mySearch.setCategory("thai");
                } else if (categorySpinner.getSelectedItem().toString().equals("Steakhouses")) {
                    mySearch.setCategory("steak");
                } else if (categorySpinner.getSelectedItem().toString().equals("Asian Fusion")) {
                    mySearch.setCategory("asianfusion");
                } else if (categorySpinner.getSelectedItem().toString().equals("Tapas Bars")) {
                    mySearch.setCategory("tapas");
                } else if (categorySpinner.getSelectedItem().toString().equals("Latin American")) {
                    mySearch.setCategory("latin");
                } else if (categorySpinner.getSelectedItem().toString().equals("Seafood")) {
                    mySearch.setCategory("seafood");
                } else if (categorySpinner.getSelectedItem().toString().equals("Italian")) {
                    mySearch.setCategory("italian");
                } else if (categorySpinner.getSelectedItem().toString().equals("Greek")) {
                    mySearch.setCategory("greek");
                }
                /* distance parameters */
                if (distanceSpinner.getSelectedItem().toString().equals("Best Match")) {
                    mySearch.setRange(2000);
                } else if (distanceSpinner.getSelectedItem().toString().equals("2 blocks")) {
                    mySearch.setRange(161);
                } else if (distanceSpinner.getSelectedItem().toString().equals("6 blocks")) {
                    mySearch.setRange(483);
                } else if (distanceSpinner.getSelectedItem().toString().equals("1 mile")) {
                    mySearch.setRange(1609);
                } else if (distanceSpinner.getSelectedItem().toString().equals("5 mile")) {
                    mySearch.setRange(5 * 1609);
                } else if (distanceSpinner.getSelectedItem().toString().equals("10 mile")) {
                    mySearch.setRange(10000);
                }
                Log.e("###categorySpinner", categorySpinner.getSelectedItem().toString());

                mPopupWindow.dismiss();


                restaurants = mySearch.filteredSearch();

                Log.e("####", String.valueOf(restaurants.length));
                if (restaurants.length == 0) {
                    Toast.makeText(getApplicationContext(),
                            "No Results, please choose again!",
                            Toast.LENGTH_LONG).show();
                } else {
                    resDB.insertRestaurants(restaurants);
                    Toast.makeText(getApplicationContext(),
                            "Results Updated. Read to Shake",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onLocationChanged(Location location) {
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

    @Override
    public void onSensorChanged(SensorEvent event) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
