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
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Random;

import app.team3.t3.yelp.Restaurant;
import app.team3.t3.yelp.RestaurantFinder;


public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    protected Location currentlocation;
    SoundPool mySound;
    int touchId, boomId;
    //    private int Random_Number;
    private ImageButton shakeIB;
    //    private TextView rangeTV;
//    private SeekBar rangeSB;
//    private int currentRange = 1;
//    private boolean preferencesChanged = true;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private boolean avoid_doubleShake = true; //use to avoid to get multiple searching results
    private Random rn = new Random();
    private Intent getResultIntent;
    //    protected String serviceAvailable = "";
    private ResDatabaseHelper resDB;
    final SensorEventListener mSensorListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 15 && avoid_doubleShake == true) {
                runningSearch();
                avoid_doubleShake = false;
//                mySound.play(boomId, 1, 1, 1, 0, 1);
//                preferencesChanged = false;
            }
            mAccelLast = mAccelCurrent;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private RestaurantFinder mySearch;
    private Restaurant[] restaurants;

     /* Sensor Listener  */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
//        restoreChanges();

        new Eula(this).show();

         /* Shake Sensor  */
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

         /* Components  */
        shakeIB = (ImageButton) findViewById(R.id.shakeIB);
//        rangeTV = (TextView) findViewById(R.id.rangeTV);
//        rangeSB = (SeekBar) findViewById(R.id.rangeSB);

         /* Listeners  */
        shakeIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mySound.play(boomId, 1, 1, 1, 0, 1);
                // mySound.play(touchId, 1, 1, 1, 1, 1f);
                runningSearch();
//                preferencesChanged = false;
            }
        });

//        rangeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                currentRange = progress + 1;
//                String unit = "";
//                if (currentRange > 1) {
//                    unit = " Miles";
//                } else {
//                    unit = " Mile";
//                }
//                rangeTV.setText("Range: " + currentRange + unit);
//                preferencesChanged = true;
//                saveChanges();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


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
//            double latitude = currentlocation.getLatitude();
            mySearch.setLatitude(currentlocation.getLatitude());
            Log.e("####lati", String.valueOf(mySearch.getLatitude()));
//            double longitude = currentlocation.getLongitude();
            mySearch.setLongitude(currentlocation.getLongitude());
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
//        restoreChanges();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
//        saveChanges();
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

    // Session of methods for preferences save and restore
//    protected void restoreChanges() {
//        SharedPreferences sharedPref = this.getSharedPreferences("user_preferences", 0);
//        currentRange = sharedPref.getInt("range", 3);
//    }
//
//    protected void saveChanges() {
//        SharedPreferences sharedPref = this.getSharedPreferences("user_preferences", 0);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt("range", currentRange);
//        editor.commit();
//    }

    /*
     * run yelp search and add data to database for random pick
     * if location and user preferences changed
     */
    protected void runningSearch() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
//        getResultIntent = new Intent(MainActivity.this, LoadingActivity.class);
//        Random_Number = rn.nextInt(20) + 1;
//        getResultIntent.putExtra("random_number", Random_Number);
//        getResultIntent.putExtra("range", currentRange);
//        getResultIntent.putExtra("preferences_changed", preferencesChanged);
//        preferencesChanged = false;
//        startActivity(getResultIntent);


        getResultIntent = new Intent(MainActivity.this, ActionBarTabsPager.class);
        int Random_Number = rn.nextInt(20) + 1;
        getResultIntent.putExtra("restaurant_picked", resDB.getRestaurant(Random_Number));
        startActivity(getResultIntent);
    }

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
                Toast.makeText(getApplicationContext(),
                        "Results Updated. Read to Shake",
                        Toast.LENGTH_LONG).show();
                restaurants = mySearch.filteredSearch();
                resDB.insertRestaurants(restaurants);
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
}
