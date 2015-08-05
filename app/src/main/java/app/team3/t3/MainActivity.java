package app.team3.t3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.Toast;

import java.text.DecimalFormat;

import app.team3.t3.helper.AppConstant;
import app.team3.t3.helper.AppUtiles;
import app.team3.t3.tabs.NavigationDrawerFragment;
import app.team3.t3.yelp.RestaurantAdapter;
import app.team3.t3.yelp.RestaurantFinder;
import app.team3.t3.yelp.RestaurantSearchException;


public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {
    // Log
    private static final String TAG = MainActivity.class.getSimpleName();

    SoundPool mySound;
    int touchId, boomId;

    private boolean soundIsEnabled = true;
    private boolean vibrateIsEnabled = true;

    private ImageButton mShakeImageButton;
    private myAutoCompleteTextView changeLocation;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private boolean locationIsEnable;
    private RestaurantFinder mySearch;
    private RestaurantAdapter restaurant;
    private boolean avoid_doubleShake = true; //use to avoid to get multiple searching results
    private double latitude;
    private double longitude;
    private SharedPreferences firstRunprefs = null;
    private SharedPreferences.Editor prefEditor;
    private SharedPreferences sharedPref;
    private int execption_id = 0;
    private Toolbar mToolbar;

    final SensorEventListener mSensorListener = new SensorEventListener() {

        /* Sensor Listener  */
        @Override
        public void onSensorChanged(SensorEvent event) {

            if (!firstRunprefs.getBoolean("transparencia", true)) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta;
                if (mAccel > 21 && avoid_doubleShake) {
                    avoid_doubleShake = false;
                    if (validateResult()) {
                        getResultPage();
                    }
                    if (soundIsEnabled) {
                        mySound.play(boomId, 1, 1, 1, 0, 1);
                    }
                }
                mAccelLast = mAccelCurrent;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_main);
        setSupportActionBar(mToolbar);
        NavigationDrawerFragment navigationDrawerFragment =
                (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_fragment);
        navigationDrawerFragment.setUp(R.id.fragment_navigation_drawer_fragment, (DrawerLayout) findViewById(R.id.drawer_layout_main), mToolbar);

        locationIsEnable = getIntent().getBooleanExtra("location_service", true);
        final Button clearTextButton = (Button) findViewById(R.id.cleartext_button);
        clearTextButton.setVisibility(View.INVISIBLE);

        sharedPref = getSharedPreferences("settings", 0);
        prefEditor = sharedPref.edit();

        firstRunprefs = getSharedPreferences("app.team3.t3", MODE_PRIVATE);
        firstRunprefs.edit().putBoolean("firstrun", true).commit();

        if (firstRunprefs.getBoolean("transparencia", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            new Transparencia(this);
        }

        if (getIntent().getBooleanExtra("is_started", false)) {
            firstRunprefs.edit().putBoolean("goto_setting", true).commit();
            Intent locationSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(locationSetting);
        }

        mySound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        touchId = mySound.load(this, R.raw.touch, 1);
        boomId = mySound.load(this, R.raw.boom, 1);

        mySearch = new RestaurantFinder(getApplicationContext());
        mySearch.setCategory(getPreferenceCategory(sharedPref.getInt("categorySpinner", 0)));
        mySearch.setRange(getPreferenceDistance(sharedPref.getInt("distanceSpinner", 0)));

        new Eula(this).show();

         /* Shake Sensor  */
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

         /* Components  */
        mShakeImageButton = (ImageButton) findViewById(R.id.shake_ImageButton);
        // mShakeImageButton.requestFocus();

         /* Listeners  */
        mShakeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundIsEnabled) {
                    mySound.play(touchId, 1, 1, 1, 1, 1f);
                }
                if (validateResult()) {
                    getResultPage();
                }
            }
        });


        changeLocation = (myAutoCompleteTextView) findViewById(R.id.set_location_textView);
        final InputMethodManager inputMethodManager = (InputMethodManager) changeLocation.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (!getIntent().getBooleanExtra("location_service", true)) {
            changeLocation.setHint("Enter Location");
        }

        String[] cityArray = AppConstant.CITY_ARRAY;

        myArrayAdapter<String> mAdapter = new myArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cityArray);
        changeLocation.setThreshold(1);
        changeLocation.setAdapter(mAdapter);

        clearTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLocation.setText("");
            }
        });
        changeLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!changeLocation.getText().toString().isEmpty()) {
                        changeLocation.selectAll();
                        clearTextButton.setVisibility(View.VISIBLE);
                    }
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                } else {
                    mShakeImageButton.requestFocus();
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    clearTextButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        changeLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!changeLocation.getText().toString().isEmpty()) {
                    clearTextButton.setVisibility(View.VISIBLE);
                } else {
                    clearTextButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        /* Listener for Location TextView */

        changeLocation.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!changeLocation.getText().toString().isEmpty()) {
                        mySearch.setLongitude(0.0);
                        mySearch.setLatitude(0.0);
                        mySearch.setLocation(changeLocation.getText().toString());
                    } else {
                        changeLocation.setText("");
                        mySearch.setLocation(null);
                        getLocation();
                    }

                    try {
                        restaurant = mySearch.filteredSearch();
                        AppUtiles.showToast(MainActivity.this, "Results Updated. Read to Shake");
                        mShakeImageButton.requestFocus();
                        return true;
                    } catch (RestaurantSearchException rse) {
                        rse.printStackTrace();
                        Log.e(TAG, "restaurantFinder: " + rse.toString());
                        AlertDialog.Builder mAlert = new AlertDialog.Builder(MainActivity.this);
                        mAlert.setTitle("Invalid City");
                        mAlert.setMessage("Please enter city again.");
                        mAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        mAlert.create().show();
                    }
                }

                return false;
            }
        });
    } //end onCreate

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
        if (firstRunprefs.getBoolean("goto_setting", true)) {
            firstRunprefs.edit().putBoolean("goto_setting", false).commit();
        }
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
        if (!firstRunprefs.getBoolean("goto_setting", false)) {
            if (firstRunprefs.getBoolean("firstrun", true)) {
                Log.e(TAG, "###onResume first time run");
                // Do first run stuff here then set 'firstrun' as false
                // using the following line to edit/commit prefs
                if (!locationIsEnable) {
                    if (!firstRunprefs.getBoolean("transparencia", true)) {
                        changeLocation.requestFocus();
                    }
                } else {
                    mShakeImageButton.requestFocus();
                    getLocation();
                }
                firstRunprefs.edit().putBoolean("firstrun", false).commit();
            } else {
                // pick restaurant from the previous search 20 results
                // if no user preferences change
                mShakeImageButton.requestFocus();
                if (!firstRunprefs.getBoolean("app_setting", true)) {
                    try {
                        restaurant = mySearch.getFromPreviousSearch();
                    } catch (RestaurantSearchException e) {
                        AppUtiles.showToast(MainActivity.this, e.getMessage());
                    }
                }
                firstRunprefs.edit().putBoolean("app_setting", false);
            }
        }


        soundIsEnabled = sharedPref.getBoolean("sound_enabled", true);
        vibrateIsEnabled = sharedPref.getBoolean("vibrate_enabled", true);
        avoid_doubleShake = true;
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /* pass a random restaurant object to result page */

    protected void getResultPage() {
        if (vibrateIsEnabled) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        }
        float distance = 0.0f;

        // Calculate distance
        if (locationIsEnable) {
            Location myLocation = new Location("my_location");
            myLocation.setLatitude(latitude);
            myLocation.setLongitude(longitude);
            Location restaurantLocation = new Location("restaurant_location");
            restaurantLocation.setLatitude(restaurant.getLatitude());
            restaurantLocation.setLongitude(restaurant.getLongitude());

            distance = (float) (myLocation.distanceTo(restaurantLocation) / 1609.34);
            int multiplier = 10;
            String distanceFormat = "##.#";
            if (distance < 1) {
                while (distance * multiplier < 1) {
                    multiplier *= 10;
                    distanceFormat += "#";
                }
            }
            distance = Float.parseFloat(new DecimalFormat(distanceFormat).format(distance));
        }

        // Intent getResultIntent = new Intent(MainActivity.this, ActionBarTabsPager.class);
        Intent getResultIntent = new Intent(MainActivity.this, ActionBarTabsPagerActivity.class);
        getResultIntent.putExtra("restaurant_picked", restaurant);
        getResultIntent.putExtra("distance", distance);
        startActivity(getResultIntent);

    }

    /* fillter button */
    public void onFilterClick(View view) {

        LayoutInflater mLayoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = mLayoutInflater.inflate(R.layout.fragment_filter, null);

        Button filterButton = (Button) findViewById(R.id.filter_button);
        final PopupWindow mPopupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        final Button cancelFilterButton = (Button) mView.findViewById(R.id.cancel_filter_button);
        Button applyFilterButton = (Button) mView.findViewById(R.id.apply_options_button);

        final Spinner distanceSpinner = (Spinner) mView.findViewById(R.id.distance_spinner);
        final Spinner categorySpinner = (Spinner) mView.findViewById(R.id.category_spinner);


        cancelFilterButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        final int categoryValue = sharedPref.getInt("categorySpinner", -1);
        Log.e(TAG, "####CaValue: " + String.valueOf(categoryValue));
        if (categoryValue != -1)
            categorySpinner.setSelection(categoryValue);
        final int distanceValue = sharedPref.getInt("distanceSpinner", -1);
        Log.e(TAG, "####DisValue: " + String.valueOf(distanceValue));
        if (distanceValue != -1)
            distanceSpinner.setSelection(distanceValue);

        applyFilterButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                /* category parameters */
                mySearch.setCategory(getPreferenceCategory(categorySpinner.getSelectedItemPosition()));

                /* distance parameters */
                mySearch.setRange(getPreferenceDistance(distanceSpinner.getSelectedItemPosition()));

                try {
                    prefEditor.putInt("categorySpinner", categorySpinner.getSelectedItemPosition());
                    prefEditor.putInt("distanceSpinner", distanceSpinner.getSelectedItemPosition());
                    prefEditor.commit();
                    restaurant = mySearch.filteredSearch();
                    mPopupWindow.dismiss();
                    AppUtiles.showToast(MainActivity.this, "Results Updated. Ready to Shake");
                } catch (RestaurantSearchException rse) {

                    rse.printStackTrace();
                    Log.e(TAG, "restaurantFinder: " + restaurant.toString());
                    AppUtiles.showAlertDialog(MainActivity.this, R.string.title_no_result, R.string.message_choose_again);
                }
            }
        });

        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void getLocation() {
        String gpsProvider = LocationManager.GPS_PROVIDER;
        String networkProvider = LocationManager.NETWORK_PROVIDER;
        String serviceAvailable;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(gpsProvider)
                || locationManager.isProviderEnabled(networkProvider)) {
            if (locationManager.isProviderEnabled(networkProvider)) {
                serviceAvailable = networkProvider;
            } else {
                serviceAvailable = gpsProvider;
            }
            locationManager.requestLocationUpdates(serviceAvailable, 3000, 161, this);
            Location currentLocation = locationManager.getLastKnownLocation(serviceAvailable);
            latitude = currentLocation.getLatitude();
            mySearch.setLatitude(latitude);
            Log.e(TAG, "####lati: " + String.valueOf(mySearch.getLatitude()));
            longitude = currentLocation.getLongitude();
            mySearch.setLongitude(longitude);
            Log.e(TAG, "####longi: " + String.valueOf(mySearch.getLongitude()));
            locationManager.removeUpdates(this);
            try {
                restaurant = mySearch.filteredSearch();
            } catch (RestaurantSearchException rse) {
                final int distanceValue = sharedPref.getInt("distanceSpinner", -1);
                if (distanceValue == 0 && distanceValue > 2) {
                    execption_id = 1;
                    validateResult();
                } else {
                    execption_id = 2;
                    validateResult();
                }
                rse.printStackTrace();
                Log.e(TAG, "restaurantFinder: " + rse.toString());
            }
        } else {
            Log.e(TAG, "####Location Err: No location provider is not available. Does the device have location services enabled?");

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        /*
        if(locationIsEnable) {
            getLocation();
        }
        */
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

    public String getPreferenceCategory(int index) {

        String[] categoryList = AppConstant.CATEGORY_LIST;

        return categoryList[index];
    }

    public int getPreferenceDistance(int index) {
        int[] distanceList = {2000, 161, 483, 1609, (5 * 1609), (10 * 1609)};
        return distanceList[index];
    }

    public boolean validateResult() {

        String exceptionText = "";
        String exceptionTitle = "";
        AlertDialog.Builder mAlert = new AlertDialog.Builder(MainActivity.this);

        switch (execption_id) {
            case 1:
                exceptionTitle = "Invalid Location Provider";
                exceptionText = "No location provider is not available." +
                        " Does the device have location services enabled?";
                mAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            case 2:
                exceptionTitle = "Invalid Preference";
                exceptionText = "There is no restaurant matches for selected preferences."
                        + " Please change your preferences.";
                mAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onFilterClick(findViewById(R.id.filter_button).getRootView());
                    }
                });
                break;
            case 3:
                exceptionTitle = "Invalid City";
                exceptionText = "Please enter the city again";
                mAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeLocation.requestFocus();
                    }
                });
                break;
            default:
                if (!locationIsEnable && mySearch.getLocation() == null) {
                    exceptionTitle = "Invalid City";
                    exceptionText = "Please enter the city again";
                    mAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            changeLocation.requestFocus();
                        }
                    });

                } else {
                    return true;
                }
                break;
        }

        mAlert.setTitle(exceptionTitle);
        mAlert.setMessage(exceptionText);
        mAlert.create().show();
        execption_id = 0;
        return false;
    }

}
