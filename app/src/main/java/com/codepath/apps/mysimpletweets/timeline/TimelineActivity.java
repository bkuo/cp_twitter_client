package com.codepath.apps.mysimpletweets.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.compose.ComposeActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.util.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends ActionBarActivity {

    private TwitterClient client;
    private ListView lvTweets;
    private TweetsArrayAdapter aTweets;
    private List<Tweet> tweets;
    private SwipeRefreshLayout swipeContainer;
    private User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
//                nextPage(page);
            }
        });
        lvTweets.setAdapter(aTweets);
        swipeContainer.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.darker_gray,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setProgressViewOffset(false, 100, 100);
        requestCurrentUser();
        populateTimeline();
    }

    private void populateCurrentUser(String screen_name) {
        client.getUserByScreenName(screen_name, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TWEET", response.toString());
                current_user = User.fromJson(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TWEET", errorResponse.toString());
            }
        });

    }

    private void requestCurrentUser() {
        client.getCurrentScreenName(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TWEET", response.toString());
                try {
                    String screen_name = response.getString("screen_name");
                    populateCurrentUser(screen_name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TWEET", errorResponse.toString());
            }
        });

    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TWEET", response.toString());
                tweets = Tweet.fromJSONArray(response);
                aTweets.addAll(tweets);
                Log.d("TWEET", aTweets.toString());
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TWEET", errorResponse.toString());
            }
        });

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
            i.putExtra("data", current_user);
            startActivityForResult(i, ComposeActivity.requestCode);
        }

        return super.onOptionsItemSelected(item);
    }
}
