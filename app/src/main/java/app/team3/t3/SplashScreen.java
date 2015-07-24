package app.team3.t3;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.content.Context;
import android.content.DialogInterface;
import android.app.Dialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

/**
 * Created by jdoan on 6/30/2015.
 * Splash screen to appear on app start up
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // Get Location Manager and check for GPS & Network location services
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            setContentView(R.layout.activity_splash);
            showLocationPrompt();
        }else{
            new LoadViewTask().execute();
        }
    }

    private void showLocationPrompt() {
        // Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Services Not Active");
        builder.setMessage("We notice your location services are not enabled, please go to settings and enable them.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                // Show location settings when the user acknowledges the alert dialog
                Intent intent = new Intent("app.team3.t3.MAINACTIVITY");
                intent.putExtra("is_started", true);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent cont = new Intent("app.team3.t3.MAINACTIVITY");
                startActivity(cont);
            }

        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {

            setContentView(R.layout.activity_splash);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                synchronized (this) {
                    wait(3000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Intent i = new Intent("app.team3.t3.MAINACTIVITY");
                startActivity(i);
            }
            return null;
        }
    }
}
