package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.oauth.OAuthAsyncHttpClient;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Token;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = MyTwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "W6fzSCWjG4ICwVzXvoz011NnK";       // Change this
    public static final String REST_CONSUMER_SECRET = "nup3z9y7pYCzISGqsOwKSWQLY0BglIyT8XlclicHvfED504Zyn"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public TwitterClient(Context c, Class<? extends Api> apiClass, String consumerUrl, String consumerKey, String consumerSecret, String callbackUrl) {
        this.baseUrl = consumerUrl;
        this.callbackUrl = callbackUrl;
        this.client = new OAuthAsyncHttpClient(apiClass, consumerKey, consumerSecret, callbackUrl, new OAuthAsyncHttpClient.OAuthTokenHandler() {
            public void onReceivedRequestToken(Token requestToken, String authorizeUrl) {
                if(requestToken != null) {
                    TwitterClient.this.editor.putString("request_token", requestToken.getToken());
                    TwitterClient.this.editor.putString("request_token_secret", requestToken.getSecret());
                    TwitterClient.this.editor.commit();
                }

                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(authorizeUrl + "&perms=delete"));
                if(TwitterClient.this.requestIntentFlags != -1) {
                    intent.setFlags(TwitterClient.this.requestIntentFlags);
                }

                TwitterClient.this.context.startActivity(intent);
            }

            public void onReceivedAccessToken(Token accessToken) {
                MyTwitterApi.MyToken accessToken2 = (MyTwitterApi.MyToken) accessToken;
                TwitterClient.this.client.setAccessToken(accessToken);
                TwitterClient.this.editor.putString("oauth_token", accessToken.getToken());
                TwitterClient.this.editor.putString("oauth_token_secret", accessToken.getSecret());
                TwitterClient.this.editor.putString("user_id_str", accessToken2.user_id_str);
                TwitterClient.this.editor.putString("screen_name", accessToken2.screen_name);

                TwitterClient.this.editor.commit();
                TwitterClient.this.accessHandler.onLoginSuccess();
            }

            public void onFailure(Exception e) {
                TwitterClient.this.accessHandler.onLoginFailure(e);
            }
        });
        this.context = c;
        this.prefs = this.context.getSharedPreferences("OAuth_" + apiClass.getSimpleName() + "_" + consumerKey, 0);
        this.editor = this.prefs.edit();
        if(this.checkAccessToken() != null) {
            this.client.setAccessToken(this.checkAccessToken());
        }

    }


    public void getHomeTimeline(AsyncHttpResponseHandler handler) {

        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);
        getClient().get(apiUrl, params, handler);
    }

    public void submitTweet(AsyncHttpResponseHandler handler, Tweet tweet) {
        String apiUrl = getApiUrl("/statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet.getBody());
        getClient().get(apiUrl, params, handler);
    }

    public void getCurrentUser(){
        String apiUrl = getApiUrl("/statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet.getBody());
        getClient().get(apiUrl, params, handler);

    }
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}