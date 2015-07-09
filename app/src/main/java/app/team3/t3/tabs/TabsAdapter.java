package app.team3.t3.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by nanden on 6/30/15.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private static final String TAG = TabsAdapter.class.getSimpleName();

    final int TAB_COUNT = 3;

    public TabsAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: {
                TestFragmentOne testFragmentOne = new TestFragmentOne();
                Log.d(TAG, "TestFragmentOne called");
                return testFragmentOne;
            }
            case 1: {
                MapsFragment mapsFragment = new MapsFragment();
                Log.d(TAG, "TestFragmentTwo called");
                return mapsFragment;
            }
            case 2: {
                TestFragmentThree testFragmentThree = new TestFragmentThree();
                Log.d(TAG, "TestFragmentThree called");
                return testFragmentThree;
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
