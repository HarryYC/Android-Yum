package app.team3.t3;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sssbug on 7/5/15.
 */
public class History extends Activity {
    private List<Map<String, Object>> data;

    /**
     * Called when the activity is starting.  This is where most initialization
     * should go: calling {@link #setContentView(int)} to inflate the
     * activity's UI, using {@link #findViewById} to programmatically interact
     * with widgets in the UI, calling
     * {@link #managedQuery(Uri, String[], String, String[], String)} to retrieve
     * cursors for data being displayed, etc.
     * <p/>
     * <p>You can call {@link #finish} from within this function, in
     * which case onDestroy() will be immediately called without any of the rest
     * of the activity lifecycle ({@link #onStart}, {@link #onResume},
     * {@link #onPause}, etc) executing.
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * @see #onStart
     * @see #onSaveInstanceState
     * @see #onRestoreInstanceState
     * @see #onPostCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView restaurantListView = new ListView(this);
        final ResDatabaseHelper db = new ResDatabaseHelper(this);

        ArrayList<HashMap<String, String>> restaurantsList = db.getHistory();

        /* use simpleAdapter to display data */
        SimpleAdapter adapter = new SimpleAdapter(this, restaurantsList, R.layout.history,
                new String[]{"Name", "Count"}, new int[]{R.id.history_name_view, R.id.history_count_view});
        restaurantListView.setAdapter(adapter);
        setContentView(restaurantListView);


        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tvShowPiced = "You selected " + String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(History.this, tvShowPiced, Toast.LENGTH_SHORT).show();
            }
        };

        restaurantListView.setOnItemClickListener(listener);


    }

}
