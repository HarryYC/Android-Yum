package app.team3.t3;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import app.team3.t3.tabs.NavigationDrawerFragment;
import app.team3.t3.tabs.SlidingTabLayout;
import app.team3.t3.tabs.TabsAdapter;
import io.fabric.sdk.android.Fabric;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;


/**
 * Created by nanden on 6/30/15.
 */
public class ActionBarTabsPager extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "rm2LGxzj9Vau2txvO1kS2Goys";
    private static final String TWITTER_SECRET = "UvthPORHyIowiLHFulbSooSnenNroIv74R1nrll724va6pUWkT";
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private SlidingTabLayout mSlidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_bar_tabs_pager);

//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(this, new Twitter(authConfig));

        mToolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerFragment navigationDrawerFragment =
                (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_fragment);
        navigationDrawerFragment.setUp(R.id.fragment_navigation_drawer_fragment, (DrawerLayout)findViewById(R.id.drawer_layout),mToolbar);
        mViewPager = (ViewPager)findViewById(R.id.view_pager);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(tabsAdapter);
        mSlidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }
}
