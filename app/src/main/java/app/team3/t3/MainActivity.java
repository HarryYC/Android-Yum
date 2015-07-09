package app.team3.t3;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends ActionBarActivity implements SensorEventListener {
    ResDatabaseHelper resDB;
    private Button b1;
    private TextView t1;

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    private boolean avoid_doubleShake = true; //use to avoid to get multiple searching results
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
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);

//                Intent getResultIntent = new Intent(MainActivity.this, Result.class);
                Intent getResultIntent = new Intent(MainActivity.this, ActionBarTabsPager.class);
                Random rn = new Random();
                int Random_Number = rn.nextInt(20) + 1;
                getResultIntent.putExtra("Random_Number", Random_Number);
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


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         /* Yelp  */

        final Context context = getApplicationContext();
        final YelpSearch mySearch = new YelpSearch(context);
        final Restaurant[] allRestaurant = mySearch.filteredSearch(null, "San Francisco, CA", null, 0, 1, 0, 0);
//        mySearch.defaultSearch();
//        mySearch.filteredSearch(null, "San Francisco", "Food", 2000, 1, 0, 0);

         /* Shake Sensor  */

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

         /* Database  */

        resDB = new ResDatabaseHelper(context);

//        Restaurant r = resDB.getRestaurant('')

         /* Components  */

        b1 = (Button) findViewById(R.id.button);
        t1 = (TextView) findViewById(R.id.textView2);

         /* Listeners  */

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setText("inserting data ...");
                resDB.insertRestaurants(allRestaurant);
                t1.setText("insert success");

            }
        });

    } //end onCreat

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
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

    public void onGetResultClick(View view) {
        Intent getResultIntent = new Intent(this, Result.class);
        Random rn = new Random();
        int Random_Number = rn.nextInt(20) + 1;
        getResultIntent.putExtra("Random_Number", Random_Number);
        getResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(getResultIntent);

    }

}
