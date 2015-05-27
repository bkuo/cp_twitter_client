package com.codepath.apps.mysimpletweets.compose;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends ActionBarActivity {

    public static int requestCode = 100;
    private User current_user;
    private TextView tvCharCount;
    private EditText etBody;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        current_user = (User) getIntent().getSerializableExtra("data");
        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        TextView tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvUsername.setText(current_user.getName());
        tvHandle.setText("@" + current_user.getScreenName());
        ImageView ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        Picasso.with(this).load(current_user.getProfileImageUrl()).into(ivAvatar);
        client = TwitterApplication.getRestClient();

        etBody=(EditText) findViewById(R.id.etBody);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionViewItem = menu.findItem(R.id.compose_charCount);
        // Retrieve the action-view from menu
        View v = MenuItemCompat.getActionView(actionViewItem);
        // Find the button within action-view
        tvCharCount = (TextView) v.findViewById(R.id.tvCharCount);
        etBody.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.d("TWEEEEEEET", etBody.getText().length()+"");
                        tvCharCount.setText(140 - etBody.getText().length() + "");
                    }
                }
        );
        // Handle button click here
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_submit) {
            Tweet tweet = new Tweet();
            tweet.setBody(etBody.getText().toString());
            client.submitTweet(new JsonHttpResponseHandler() {
                                   @Override
                                   public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                                       finishActivity(requestCode);
                                       finish();
                                   }

                                   @Override
                                   public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                       Log.d("TWEEEEEET", errorResponse.toString());
                                       Toast.makeText(ComposeActivity.this, "submit failed due to :" + errorResponse.toString(), Toast.LENGTH_SHORT).show();
                                   }
                               },
                    tweet);


//            setResult(Activity.RESULT_OK, result);
        }
        return true;
//        return super.onOptionsItemSelected(item);
    }
}
