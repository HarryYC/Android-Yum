package app.team3.t3;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;


public class Transparencia implements View.OnClickListener {
    ShowcaseView showcaseView;
    int contador = 0;
    private Target t1, t2, t3;
    private SharedPreferences firstRunprefs = null;

    public Transparencia(Activity activity) {
        firstRunprefs = activity.getSharedPreferences("app.team3.t3", activity.MODE_PRIVATE);
        t1 = new ViewTarget(R.id.filter_button, activity);
        t2 = new ViewTarget(R.id.shake_ImageButton, activity);
        t3 = new ViewTarget(R.id.set_location_textView, activity);
        showcaseView = new ShowcaseView.Builder(activity)
                .setTarget(Target.NONE)
                .setOnClickListener(this)
                .setContentTitle("User Guided")
                .setContentText("Help you use this app quickly")
                .build();
        showcaseView.setButtonText("Next");
    }

    @Override
    public void onClick(View v) {
        switch (contador) {
            case 0:
                showcaseView.setShowcase(t1, true);
                showcaseView.setContentTitle("FILTER");
                showcaseView.setContentText("Choose the distance and Category.Make smart choice.");
                break;
            case 1:
                showcaseView.setShowcase(t2, true);
                showcaseView.setContentTitle("Shake Me");

                showcaseView.setContentText("Shake or Touch me, I will help you");
                // showcaseView.setButtonText("Touch me, if the internet does not work, type the city name");
                break;

            case 2:
                showcaseView.setShowcase(t3, true);
                showcaseView.setContentTitle("Searching Restaurant in the City");
                showcaseView.setContentText("Leave blank to search restaurant near you.");
                // showcaseView.setButtonText("thetesting2");
                break;

            case 3:
                firstRunprefs.edit().putBoolean("transparencia", false).commit();
                showcaseView.hide();
                break;

        }
        contador++;

    }


}
