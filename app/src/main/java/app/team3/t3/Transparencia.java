package app.team3.t3;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
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
                .setContentTitle("User Guide")
                .setContentText("Let's take tour of the main screen.")
                .build();
        showcaseView.setButtonText("");
        RelativeLayout.LayoutParams showcaseLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        showcaseView.setButtonPosition(showcaseLP);
    }

    @Override
    public void onClick(View v) {
        switch (contador) {
            case 0:
                showcaseView.setShowcase(t1, true);
                showcaseView.setContentTitle("Filter");
                showcaseView.setContentText("Filter the type of restaurants you'd like us to recommend by distance and category.");
                break;
            case 1:
                showcaseView.setShowcase(t2, true);
                showcaseView.setContentTitle("Recommend a restaurant");
                showcaseView.setContentText("Tap the button or shake the phone, and we will recommend you a restaurant.");

                // showcaseView.setButtonText("Touch me, if the internet does not work, type the city name");
                break;

            case 2:
                showcaseView.setShowcase(t3, true);
                showcaseView.setContentTitle("Set city location of search.");
                showcaseView.setContentText("Input the city of which you'd like us to recommend a restaurant from.");
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
