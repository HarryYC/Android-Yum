package app.team3.t3;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import app.team3.t3.tabs.NavigationDrawerFragment;
import app.team3.t3.tabs.TabsAdapter;
import io.fabric.sdk.android.Fabric;


/**
 * Created by nanden on 6/30/15.
 */
public class ActionBarTabsPagerActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "rm2LGxzj9Vau2txvO1kS2Goys";
    private static final String TWITTER_SECRET = "UvthPORHyIowiLHFulbSooSnenNroIv74R1nrll724va6pUWkT";
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_bar_tabs_pager);

        mViewPager = (ViewPager)findViewById(R.id.view_pager);
        mToolbar = (Toolbar)findViewById(R.id.tool_bar);
        mTabLayout = (TabLayout) findViewById(R.id.action_bar_tabs_tv);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabsAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (position < 2) {
                    inputMethodManager.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);
                } else {
                    inputMethodManager.toggleSoftInputFromWindow(mViewPager.getWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerFragment navigationDrawerFragment =
                (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_fragment);
        navigationDrawerFragment.setUp(R.id.fragment_navigation_drawer_fragment, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
    }
}
