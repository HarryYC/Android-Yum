package app.team3.t3.tabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;

import app.team3.t3.ResDatabaseHelper;
import app.team3.t3.Restaurant;


/**
 * Created by nanden on 6/30/15.
 */
public class TabsAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = TabsAdapter.class.getSimpleName();
    final int TAB_COUNT = 3;
    private int number;
    private int width;
    private int height;
    private String businessId = null;

    public TabsAdapter(FragmentManager supportFragmentManager, Activity activity, int randomNumber) {
        super(supportFragmentManager);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        number = randomNumber;
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
    }

    public TabsAdapter(FragmentManager supportFragmentManager, Activity activity, String businessId) {
        super(supportFragmentManager);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        this.businessId = businessId;
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: {
                ResultFragment resultFragment = new ResultFragment();
                Bundle resultBundle = new Bundle();
                resultBundle.putInt("number", number);
                resultBundle.putString("Business_ID", businessId);
                resultBundle.putInt("screen_width", width);
                resultBundle.putInt("screen_height", height);

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
