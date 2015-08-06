package app.team3.t3;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class Setting extends AppCompatActivity {

    private TextView toolbarTitle;
    private TextView soundStatus;
    private TextView vibrateStatus;
    private Switch soundSwitch;
    private Switch vibrateSwitch;
    private SharedPreferences settingPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        settingPreferences = getSharedPreferences("settings", 0);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        soundStatus = (TextView) findViewById(R.id.sound_status);
        vibrateStatus = (TextView) findViewById(R.id.vibrate_status);
        soundSwitch = (Switch) findViewById(R.id.sound_switch);
        vibrateSwitch = (Switch) findViewById(R.id.vibrate_switch);

        soundSwitch.setChecked(settingPreferences.getBoolean("sound_enabled", true));
        if (settingPreferences.getBoolean("sound_enabled", true)) {
            soundStatus.setText("On");
        } else {
            soundStatus.setText("Off");
        }
        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingPreferences.edit().putBoolean("sound_enabled", isChecked).commit();
                if (isChecked) {
                    soundStatus.setText("On");
                } else {
                    soundStatus.setText("Off");
                }
            }
        });
        vibrateSwitch.setChecked(settingPreferences.getBoolean("vibrate_enabled", true));
        if (settingPreferences.getBoolean("vibrate_enabled", true)) {
            vibrateStatus.setText("On");
        } else {
            vibrateStatus.setText("Off");
        }
        vibrateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingPreferences.edit().putBoolean("vibrate_enabled", isChecked).commit();
                if (isChecked) {
                    vibrateStatus.setText("On");
                } else {
                    vibrateStatus.setText("Off");
                }
            }
        });
        toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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
    public void onPause() {
        super.onPause();
        settingPreferences.edit().putBoolean("app_setting", true).commit();
    }
}
