package app.team3.t3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import app.team3.t3.tabs.NavigationDrawerFragment;
import app.team3.t3.tabs.SlidingTabLayout;
import app.team3.t3.tabs.TabsAdapter;

/**
 * Created by nanden on 6/30/15.
 */
public class ActionBarTabsPager extends ActionBarActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_bar_tabs_pager);
        Bundle bundle = getIntent().getExtras();

        mToolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerFragment navigationDrawerFragment =
                (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_fragment);
        navigationDrawerFragment.setUp(R.id.fragment_navigation_drawer_fragment, (DrawerLayout)findViewById(R.id.drawer_layout),mToolbar);

        mViewPager = (ViewPager)findViewById(R.id.view_pager);
        if (bundle.getBoolean("is_new")) {
            mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), activity, bundle.getInt("Random_Number")));
        } else {
            mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), activity, bundle.getString("Business_ID")));
        }
        mSlidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }
}
