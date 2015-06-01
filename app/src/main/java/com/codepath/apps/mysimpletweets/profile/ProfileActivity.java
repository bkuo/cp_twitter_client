package com.codepath.apps.mysimpletweets.profile;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.fragments.UserCardFragment;
import com.codepath.apps.mysimpletweets.twitter_api.TwitterClient;

public class ProfileActivity extends ActionBarActivity {

    private TwitterClient client;
    private TweetsListFragment frTweets;
    private UserCardFragment frUserCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screenName = getIntent().getStringExtra("screen_name");
        client = TwitterApplication.getRestClient();
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            frTweets = new TweetsListFragment(client.user_timeline_by_screen_name(screenName));
            ft.replace(R.id.flContainer, frTweets);
            frUserCard = new UserCardFragment();
            frUserCard.setArguments(getIntent().getExtras());
            ft.replace(R.id.flUserCard, frUserCard);
            ft.commit();
        }
        setTitle("@"+screenName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
