package com.codepath.apps.mysimpletweets.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.compose.ComposeActivity;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.twitter_api.TwitterClient;

public class TimelineActivity extends ActionBarActivity {


    //    private TweetsListFragment fragmentTweetsList;
    private Fragment fgHomeTimeLine;
    private Fragment fgMentionsTimeLine;
    private User currentUser;
    private TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApplication.getRestClient();

        currentUser = (User) getIntent().getSerializableExtra("current_user");
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        String[] titles = {"home", "mentions"};
        Fragment[] fragments = {
                new TweetsListFragment(client.home_timeline()),
                new TweetsListFragment(client.mentions_timeline())
        };
        viewPager.setAdapter(new SimpleTweetsFragmentPagerAdapter(getSupportFragmentManager(), titles, fragments));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
        if (savedInstanceState == null) viewPager.setCurrentItem(0);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_compose) {
            Intent i = new Intent(this, ComposeActivity.class);
            i.putExtra("data", currentUser);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


}
