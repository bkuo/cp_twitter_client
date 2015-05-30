package com.codepath.apps.mysimpletweets.fragments;

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
import com.codepath.apps.mysimpletweets.detail.TweetDetailActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.timeline.TweetsArrayAdapter;
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
    private ListView lvTweets;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private SwipeRefreshLayout swipeContainer;
    private TwitterClient client;

    private User current_user;
    private Long maxId;
    private Long sinceId;
    private JsonHttpResponseHandler timelineResponseHandler;

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
                client.getHomeTimelineBefore(timelineResponseHandler(), maxId);
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
                Intent i = new Intent(getActivity(), TweetDetailActivity.class);
                i.putExtra("tweet", (Tweet) parent.getItemAtPosition(position));
                i.putExtra("current_user", current_user);
                startActivity(i);
            }
        });
        return v;
    }

    private JsonHttpResponseHandler timelineResponseHandler() {
        if (timelineResponseHandler == null) {
            timelineResponseHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    tweets = Tweet.fromJSONArray(response);
                    for (int i = 0; i < tweets.size(); i++) {
                        if (maxId == null || tweets.get(i).getUid() < maxId)
                            maxId = tweets.get(i).getUid();
                        if (sinceId == null || tweets.get(i).getUid() > sinceId)
                            sinceId = tweets.get(i).getUid();
                    }
                    aTweets.addAll(tweets);
                    swipeContainer.setRefreshing(false);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("TWEEEEEET", errorResponse.toString());
                }
            };
        }
        return timelineResponseHandler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void populateTimeline() {
        Log.d("TWEEEEEEET", "loading after " + sinceId);
        client.getHomeTimelineSince(timelineResponseHandler(), sinceId);

    }
}
