package com.codepath.apps.mysimpletweets.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.detail.TweetDetailActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.twitter_api.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by bkuo on 5/27/15.
 */
public class TweetsListFragment extends Fragment {
    public TweetsListFragment(){}
//    public TweetsListFragment(TwitterClient.Timeline timeline){
//        super();
//        this.timeline = timeline;
//    }
    public static TweetsListFragment fromBundle(Bundle bundle){
        TweetsListFragment t = new TweetsListFragment();
        t.setArguments(bundle);
        return t;
    }

    private TwitterClient.Timeline timeline;
    private ListView lvTweets;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private SwipeRefreshLayout swipeContainer;
    private TwitterClient client;

    private User current_user;
    private Long maxId;
    private boolean finalmaxId = false;

    private Long sinceId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);


        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("TWEEEEEEET", "loading before " + maxId);
                if(finalmaxId==false) timeline.tweets_before(wrapped_tweets_handler(), maxId);
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

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i;
                i = new Intent(getActivity(), TweetDetailActivity.class);
                i.putExtra("tweet", (Tweet) parent.getItemAtPosition(position));
                i.putExtra("user", ((Tweet) parent.getItemAtPosition(position)).getUser());
                startActivity(i);
            }
        });
        populateTimeline();
        return v;
    }

    private class WrappedHandler extends JsonHttpResponseHandler {
        private JsonHttpResponseHandler handler;

        public WrappedHandler(JsonHttpResponseHandler handler) {
            this.handler = handler;
        }
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            handler.onSuccess( statusCode, headers, response);
            if(response.length()<2)
                finalmaxId = true;
        }
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            handler.onFailure( statusCode, headers, throwable, errorResponse);
        }
    }
    private JsonHttpResponseHandler wrapped_tweets_handler() {
        return new WrappedHandler(tweets_handler());
    }
    private JsonHttpResponseHandler tweets_handler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TWEEEEEET", response.toString());
                tweets = Tweet.fromJSONArray(response);
                for (int i = 0; i < tweets.size(); i++) {
                    if (maxId == null || tweets.get(i).getUid() < maxId)
                        maxId = tweets.get(i).getUid();
                    if (sinceId == null || tweets.get(i).getUid() > sinceId)
                        sinceId = tweets.get(i).getUid();
                }
                aTweets.addAll(tweets);
                Log.d("TWEEEEEET", aTweets.toString());
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TWEEEEEET", errorResponse.toString());
            }
        };

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        timeline = client.timeline_from_bundle(getArguments());
    }
    private void populateTimeline() {
        Log.d("TWEEEEEEET", "loading after " + sinceId);
        timeline.tweets_since(tweets_handler(), sinceId);
    }

}
