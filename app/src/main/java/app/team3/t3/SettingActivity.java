package app.team3.t3;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SettingActivity extends AppCompatActivity {

    CheckBox soundCB;
    CheckBox vibrateCB;
    Button backBtn;
    SharedPreferences settingPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settingPreferences = getSharedPreferences("settings", 0);

        soundCB = (CheckBox) findViewById(R.id.soundCB);
        vibrateCB = (CheckBox) findViewById(R.id.vibrateCB);
        backBtn = (Button) findViewById(R.id.backBtn);

        soundCB.setChecked(settingPreferences.getBoolean("sound_enabled", true));
        soundCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingPreferences.edit().putBoolean("sound_enabled", isChecked).commit();
            }
        });
        vibrateCB.setChecked(settingPreferences.getBoolean("vibrate_enabled", true));
        vibrateCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingPreferences.edit().putBoolean("vibrate_enabled", isChecked).commit();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
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
}
