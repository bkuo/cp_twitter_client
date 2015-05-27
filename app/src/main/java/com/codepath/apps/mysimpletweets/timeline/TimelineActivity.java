package com.codepath.apps.mysimpletweets.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.compose.ComposeActivity;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.User;

public class TimelineActivity extends ActionBarActivity {


    private TweetsListFragment fragmentTweetsList;
    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        if(savedInstanceState==null)
            fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
        currentUser = (User)getIntent().getSerializableExtra("current_user");
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
