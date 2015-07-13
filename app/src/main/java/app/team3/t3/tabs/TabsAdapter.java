package app.team3.t3.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import app.team3.t3.Restaurant;


/**
 * Created by nanden on 6/30/15.
 */
public class TabsAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = TabsAdapter.class.getSimpleName();
    final int TAB_COUNT = 3;
    private Bundle resultBundle;

    public TabsAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
        resultBundle = new Bundle();
    }

    public void setPassData(Restaurant restaurant, int width, int height) {
        resultBundle.putParcelable("result", restaurant);
        resultBundle.putInt("screen_width", width);
        resultBundle.putInt("screen_height", height);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: {
                ResultFragment resultFragment = new ResultFragment();
                resultFragment.setArguments(resultBundle);
                Log.d(TAG, "ResultFragment called");
                return resultFragment;
            }
            case 1: {
                TestFragmentTwo testFragmentTwo = new TestFragmentTwo();
                Log.d(TAG, "TestFragmentTwo called");
                return testFragmentTwo;
            }
            case 2: {
                MapsFragment mapsFragment = new MapsFragment();
                Log.d(TAG, "mapsFragment called");
                return mapsFragment;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0: {
                title = "Result";
                break;
            }
            case 1: {
                title = "Tweet";
                break;
            }
            case 2: {
                title = "Map";
                break;
            }
        }
        return title;
    }
}
