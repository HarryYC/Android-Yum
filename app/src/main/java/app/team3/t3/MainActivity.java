package app.team3.t3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


import java.util.Random;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SoundPool mySound;
    int touchId, boomId;
    private int Random_Number;
    private ImageButton shakeIB;
    private TextView rangeTV;
    private SeekBar rangeSB;
    private int currentRange = 1;
    private boolean preferencesChanged = true;
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
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 15 && avoid_doubleShake == true) {
                runningSearch();
                avoid_doubleShake = false;
                mySound.play(boomId, 1, 1, 1, 0, 1);
            }
            mAccelLast = mAccelCurrent;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

     /* Sensor Listener  */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra("is_started", false)) {
            Intent locationSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(locationSetting);
        }

        mySound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        touchId = mySound.load(this, R.raw.touch, 1);
        boomId = mySound.load(this, R.raw.boom, 1);

        restoreChanges();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Eula(this).show();

         /* Shake Sensor  */
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

         /* Components  */
        shakeIB = (ImageButton) findViewById(R.id.shakeIB);
        rangeTV = (TextView) findViewById(R.id.rangeTV);
        rangeSB = (SeekBar) findViewById(R.id.rangeSB);

         /* Listeners  */
        shakeIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySound.play(boomId, 1, 1, 1, 0, 1);
                // mySound.play(touchId, 1, 1, 1, 1, 1f);
                runningSearch();
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
                preferencesChanged = true;
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

    @Override
    protected void onStart() {
        super.onStart();
        restoreChanges();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
        saveChanges();
        getActionBar();
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

    /*
     * run yelp search and add data to database for random pick
     * if location and user preferences changed
     */
    protected void runningSearch() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
        getResultIntent = new Intent(MainActivity.this, LoadingActivity.class);
        Random_Number = rn.nextInt(20) + 1;
        getResultIntent.putExtra("random_number", Random_Number);
        preferencesChanged = false;
        startActivity(getResultIntent);
    }
}
