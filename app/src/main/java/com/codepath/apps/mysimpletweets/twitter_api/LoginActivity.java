package com.codepath.apps.mysimpletweets.twitter_api;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.timeline.TimelineActivity;
import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

    private User current_user;
    private MenuItem miActionProgressItem;
    private ProgressBar v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    // OAuth authenticated successfully, launch primary authenticated activity
    // i.e Display application "homepage"
    @Override
    public void onLoginSuccess() {
        showProgressBar();
        getClient().getUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                current_user = User.fromJson(response);
                hideProgressBar();
                Intent i = new Intent(LoginActivity.this, TimelineActivity.class);
                i.putExtra("current_user", current_user);
                startActivity(i);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                hideProgressBar();
                Toast.makeText(LoginActivity.this, "Download of current user failed ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }

    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToRest(View view) {
        getClient().connect();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        Log.d("TWEEEEEEEEEEEEE", miActionProgressItem.toString());
        // Extract the action-view from the menu item
        v = (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        if(miActionProgressItem!=null) miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        if(miActionProgressItem!=null) miActionProgressItem.setVisible(false);
    }
}
