package app.team3.t3;

import android.database.DatabaseUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    Restaurant res;
    ResDatabaseHelper resDB;
    private Button b1;
    private Button b2;
    private TextView t1;
    private ResDatabaseHelper.ResCursor mCursor;
    private DatabaseUtils a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res = new Restaurant("abc", 5, "4153382929", "Chinese", "1600 holloway", "San Francisco", 94110, 0, 0);
        resDB = new ResDatabaseHelper(getApplicationContext());
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        t1 = (TextView) findViewById(R.id.textView2);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setText("12312312312");
                resDB.insertRes(res);
                t1.setText("insert success");

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rn = new Random();
                int Random_Number = rn.nextInt(10) + 1;
                mCursor = resDB.queryRes(Random_Number);
                t1.setText(DatabaseUtils.dumpCursorToString(mCursor));


            }
        });


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

}
