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

    public TabsAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: {
                ResultFragment resultFragment = new ResultFragment();
                Log.d(TAG, "ResultFragment called");
                return resultFragment;
            }
            case 1: {
                MapsFragment mapsFragment = new MapsFragment();
                Log.d(TAG, "mapsFragment called");
                return mapsFragment;
            }
            case 2: {
                TestFragmentTwo testFragmentTwo = new TestFragmentTwo();
                Log.d(TAG, "TestFragmentTwo called");
                return testFragmentTwo;
            }
            default:
                return null;
        }
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
                title = "Map";
                break;
            }
            case 2: {
                title = "Tweet";
                break;
            }
        }
        return title;
    }
}
