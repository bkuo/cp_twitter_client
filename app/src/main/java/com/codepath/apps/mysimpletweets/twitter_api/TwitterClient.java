package com.codepath.apps.mysimpletweets.twitter_api;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.oauth.OAuthAsyncHttpClient;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
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
    public static final Class<? extends Api> REST_API_CLASS = EnhancedTwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "W6fzSCWjG4ICwVzXvoz011NnK";       // Change this
    public static final String REST_CONSUMER_SECRET = "nup3z9y7pYCzISGqsOwKSWQLY0BglIyT8XlclicHvfED504Zyn"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

    public TwitterClient(Context context) {

        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);

        this.client = new OAuthAsyncHttpClient(REST_API_CLASS, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, callbackUrl, new OAuthAsyncHttpClient.OAuthTokenHandler() {
            public void onReceivedRequestToken(Token requestToken, String authorizeUrl) {

                if (requestToken != null) {
                    TwitterClient.this.editor.putString("request_token", requestToken.getToken());
                    TwitterClient.this.editor.putString("request_token_secret", requestToken.getSecret());
                    TwitterClient.this.editor.commit();
                }

                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(authorizeUrl + "&perms=delete"));
                if (TwitterClient.this.requestIntentFlags != -1) {
                    intent.setFlags(TwitterClient.this.requestIntentFlags);
                }

                TwitterClient.this.context.startActivity(intent);
            }

            public void onReceivedAccessToken(Token accessToken) {
                TwitterAccessToken twitter_accessToken = (TwitterAccessToken) accessToken;
                TwitterClient.this.client.setAccessToken(twitter_accessToken);
                TwitterClient.this.editor.putString("oauth_token", twitter_accessToken.getToken());
                TwitterClient.this.editor.putString("oauth_token_secret", twitter_accessToken.getSecret());
                TwitterClient.this.editor.putString("twitter_user_id", twitter_accessToken.getUser_id());
                TwitterClient.this.editor.putString("twitter_screen_name", twitter_accessToken.getScreen_name());

                TwitterClient.this.editor.commit();
                TwitterClient.this.accessHandler.onLoginSuccess();
            }

            public void onFailure(Exception e) {
                TwitterClient.this.accessHandler.onLoginFailure(e);
            }
        });
        if(this.checkAccessToken() != null) {
            this.client.setAccessToken(this.checkAccessToken());
        }


    }

    @Override
    public void authorize(Uri uri, OAuthAccessHandler handler) {
        Log.d("TWEEEEEE", "authorize");
        super.authorize(uri, handler);
    }


    public TwitterAccessToken checkAccessToken() {
        boolean q = (this.prefs.contains("oauth_token") &&
                this.prefs.contains("oauth_token_secret") &&
                this.prefs.contains("twitter_user_id") &&
                this.prefs.contains("twitter_screen_name"));

        TwitterAccessToken t = q ? new TwitterAccessToken(
                this.prefs.getString("oauth_token", ""),
                this.prefs.getString("oauth_token_secret", ""),
                this.prefs.getString("twitter_user_id", ""),
                this.prefs.getString("twitter_screen_name", "")) : null;
        return t;
    }

    public class Timeline{
        private  String apiUrl;
        Timeline(String resource_path){
            apiUrl = resource_path;
        }
        public void tweets_since(AsyncHttpResponseHandler handler, Long since_id) {
            RequestParams params = new RequestParams();
            params.put("count", 100);
            String qs =  (since_id != null) ? "?count=100&since_id=" + since_id : "";
            getClient().get(apiUrl+qs, params, handler);
        }
        public void tweets_before(AsyncHttpResponseHandler handler, Long max_id) {
            RequestParams params = new RequestParams();
            params.put("count", 100);
            String qs =  (max_id != null) ? "?count=100&max_id=" + max_id : "";
            getClient().get(apiUrl+qs, params, handler);
        }

    }
    public Timeline mentions_timeline(){  return new Timeline(getApiUrl("statuses/mentions_timeline.json"));}
    public Timeline home_timeline(){return  new Timeline(getApiUrl("statuses/home_timeline.json"));}

//
//    public void getHomeTimelineSince(AsyncHttpResponseHandler handler, Long since_id) {
//        String apiUrl = getApiUrl("statuses/home_timeline.json");
//        RequestParams params = new RequestParams();
//        params.put("count", 25);
//        if (since_id != null)
//            apiUrl = apiUrl + "?since_id=" + since_id;
//        RequestHandle r = getClient().get(apiUrl, params, handler);
//    }
//
//    public void getHomeTimelineBefore(AsyncHttpResponseHandler handler, Long max_id) {
//        String apiUrl = getApiUrl("statuses/home_timeline.json");
//        RequestParams params = new RequestParams();
//        params.put("count", 25);
//        if (max_id != null)
//            apiUrl = apiUrl + "?max_id=" + max_id; //params.put("max_id", max_id);
//        RequestHandle r = getClient().get(apiUrl, params, handler);
//
//    }

    public void submitTweet(AsyncHttpResponseHandler handler, Tweet tweet) {
        String apiUrl = getApiUrl("/statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet.getBody());
        if (tweet.getIn_reply_to() != null)
            params.put("in_reply_to_status_id", tweet.getIn_reply_to());
        RequestHandle r = getClient().post(apiUrl, params, handler);

    }


    public void getUser(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/users/show.json?user_id=" + getAccessToken().getUser_id());
        getClient().get(apiUrl, handler);

    }

    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

//    public OAuthAsyncHttpClient getClient(){
//        return super.getClient();
//    }

    public TwitterAccessToken getAccessToken() {
        return (TwitterAccessToken) getClient().getAccessToken();
    }
}
