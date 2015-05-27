package com.codepath.apps.mysimpletweets.twitter_api;

import android.util.Log;

import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.TokenExtractorImpl;
import org.scribe.model.Token;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bkuo on 5/27/15.
 */
public class TwitterTokenExtractorImpl extends TokenExtractorImpl {
    private static final Pattern TOKEN_REGEX = Pattern.compile("oauth_token=([^&]+)");
    private static final Pattern SECRET_REGEX = Pattern.compile("oauth_token_secret=([^&]*)");
    private static final Pattern USER_ID_REGEX = Pattern.compile("user_id=([^&]+)");
    private static final Pattern SCREEN_NAME_REGEX = Pattern.compile("screen_name=([^&]+)");


    public Token extract(String response) {
        Log.d("TWEEEEEE", "TwitterTokenExtractorImpl.extract" + response.toString());

        Preconditions.checkEmptyString(response, "Response body is incorrect. Can\'t extract a token from an empty string");
        String token = this.extract(response, TOKEN_REGEX);
        String secret = this.extract(response, SECRET_REGEX);
        String user_id = this.extract(response, USER_ID_REGEX);
        String screen_name = this.extract(response, SCREEN_NAME_REGEX);
        TwitterAccessToken ttoken  =new TwitterAccessToken(token, secret, user_id, screen_name, response);
        Log.d("TWEEEEEE", "TwitterTokenExtractorImpl.extract" + ttoken.toString());

        return ttoken;
    }
    private String extract(String response, Pattern p) {
        Matcher matcher = p.matcher(response);
        if(matcher.find() && matcher.groupCount() >= 1) {
            return OAuthEncoder.decode(matcher.group(1));
        } else {
            throw new OAuthException("Response body is incorrect. Can\'t extract token and secret from this: \'" + response + "\'", (Exception)null);
        }
    }
}
