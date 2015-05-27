package com.codepath.apps.mysimpletweets.twitter_api;

import android.util.Log;

import org.scribe.builder.api.TwitterApi;
import org.scribe.extractors.AccessTokenExtractor;

/**
 * Created by bkuo on 5/27/15.
 */
public class EnhancedTwitterApi extends TwitterApi{
    public AccessTokenExtractor getAccessTokenExtractor() {
        Log.d("TWEEEEEE", "getAccessTokenExtractor" );

        return new TwitterTokenExtractorImpl();
    }
}
