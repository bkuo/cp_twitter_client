package com.codepath.apps.mysimpletweets.detail;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.fragments.UserCardFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.twitter_api.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TweetDetailActivity extends ActionBarActivity {

    private Tweet tweet;
    private User current_user;
    private EditText etReply;
    private TextView tvStats;
    private TextView tvBody;
    private TextView tvCreatedAt;
    private TwitterClient client;
    private UserCardFragment frUserCard;
    private TweetsListFragment frTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        tweet = (Tweet) getIntent().getSerializableExtra("tweet");
        current_user = (User) getIntent().getSerializableExtra("user");
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            frTweets = TweetsListFragment.fromBundle(client.user_timeline_by_screen_name(screenName).toBundle());
//            ft.replace(R.id.flContainer, frTweets);
            frUserCard = new UserCardFragment();
            frUserCard.setArguments(getIntent().getExtras());
            ft.replace(R.id.flUserCard, frUserCard);
            ft.commit();
        }
//        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
//        TextView tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);

        tvCreatedAt.setText( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(tweet.getCreatedAt())));
//        tvUsername.setText(tweet.getUser().getName());
//        tvHandle.setText("@" + tweet.getUser().getScreenName());
        tvBody = (TextView)findViewById(R.id.tvBody);
        tvBody.setText(tweet.getBody());
//        ImageView ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        tvStats =(TextView) findViewById(R.id.tvStats);
        tvStats.setText(Html.fromHtml(
                "<b>"+tweet.getRetweet_count() + "</b> RETWEETS"+
                        " "+
                "<b>"+tweet.getFavorite_count() + "</b> FAVORITES"

        ));
        client = TwitterApplication.getRestClient();
        etReply = (EditText) findViewById(R.id.etReply);
        etReply.setHint("Reply to "+ tweet.getUser().getScreenName());
        etReply.setText("@"+tweet.getUser().getScreenName());
        etReply.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Tweet new_tweet = new Tweet();
                new_tweet.setBody(etReply.getText().toString());
                new_tweet.setIn_reply_to(tweet.getUid());
                client.submitTweet(new JsonHttpResponseHandler() {
                                       @Override
                                       public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                           finish();
                                       }

                                       @Override
                                       public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                           Log.d("TWEEEEEET", errorResponse.toString());
                                           Toast.makeText(TweetDetailActivity.this, "submit failed due to :" + errorResponse.toString(), Toast.LENGTH_SHORT).show();
                                       }
                                   },
                        new_tweet);
                return false;
            }
        });
//        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivAvatar);

   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet_detail, menu);
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
