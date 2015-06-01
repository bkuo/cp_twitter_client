package com.codepath.apps.mysimpletweets.profile;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.timeline.TweetsListFragment;
import com.codepath.apps.mysimpletweets.fragments.UserCardFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.twitter_api.TwitterClient;

public class ProfileActivity extends ActionBarActivity {

    private TwitterClient client;
    private TweetsListFragment frTweets;
    private UserCardFragment frUserCard;
    private TextView tvFollowersCount;
    private TextView tvFollowingCount;
    private TextView tvTweetCount;
    private User user;
    private TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = (User) getIntent().getSerializableExtra("user");

        client = TwitterApplication.getRestClient();
//        User user = client.getUser();
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            frTweets = TweetsListFragment.fromBundle(client.user_timeline(user).toBundle());
            ft.replace(R.id.flContainer, frTweets);
            frUserCard = new UserCardFragment();
            frUserCard.setArguments(getIntent().getExtras());
            ft.replace(R.id.flUserCard, frUserCard);
            ft.commit();
        }
        setTitle("@" + user.getScreenName());
        tvFollowersCount = (TextView) findViewById(R.id.tvFollowerCount);
        tvFollowingCount = (TextView) findViewById(R.id.tvFollowingCount);
        tvTweetCount = (TextView) findViewById(R.id.tvTweetCount);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvDescription.setText(user.getDescription());
        tvTweetCount.setText(Html.fromHtml("<b>" + user.getTweetCount() + "</b><br>Tweets"));
        tvFollowersCount.setText(Html.fromHtml("<b>" + user.getFollowersCount() + "</b><br>Followers"));

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
