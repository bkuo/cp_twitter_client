package com.codepath.apps.mysimpletweets.timeline;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.twitter_api.TwitterClient;

public class SearchResultsActivity extends ActionBarActivity {

    private TwitterClient client;
    private TweetsListFragment frTweets;
    private User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        current_user = (User) getIntent().getSerializableExtra("user");
        Log.i("TWEEEEE", "compose " + current_user.toString());
//        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
//        TextView tvHandle = (TextView) findViewById(R.id.tvHandle);
//        tvUsername.setText(current_user.getName());
//        tvHandle.setText("@" + current_user.getScreenName());
//        ImageView ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
//        Picasso.with(this).load(current_user.getProfileImageUrl()).into(ivAvatar);
        client = TwitterApplication.getRestClient();

        String query = getIntent().getStringExtra("query");
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            frTweets = new TweetsListFragment();
            ftTweets.setArguments(query);
            ft.replace(R.id.flContainer, frTweets);
//            frUserCard = new UserCardFragment();
//            frUserCard.setArguments(getIntent().getExtras());
//            ft.replace(R.id.flUserCard, frUserCard);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
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
