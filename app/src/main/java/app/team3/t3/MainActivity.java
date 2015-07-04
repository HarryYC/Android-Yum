package app.team3.t3;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Random;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();

        final YelpSearch mySearch = new YelpSearch(context);
        final Restaurant[] allRestaurant = mySearch.filteredSearch(null, "San Francisco, CA", null, 0, 1, 0, 0);
        Button pickRestaurant = (Button) findViewById(R.id.randomBtn);
        final ImageView businessIV = (ImageView) findViewById(R.id.businessIV);
        final ImageView ratingIV = (ImageView) findViewById(R.id.ratingIV);
        final TextView nameTV = (TextView) findViewById(R.id.nameTV);
        final TextView countTV = (TextView) findViewById(R.id.countTV);

        pickRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int key = Math.abs(random.nextInt() % 20);
                new ImageDownloader(context, businessIV, true).execute(allRestaurant[key].getBusinessImgURL());
                new ImageDownloader(context, ratingIV, false).execute(allRestaurant[key].getRatingImgURL());
                nameTV.setText(allRestaurant[key].getName());
                countTV.setText("(" + allRestaurant[key].getReviewCount() + ")");
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
