package com.codepath.apps.mysimpletweets;

import org.scribe.builder.api.TwitterApi;
import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.TokenExtractorImpl;
import org.scribe.model.Token;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bkuo on 5/26/15.
 */
public class MyTwitterApi extends TwitterApi{
    private static final Pattern USER_ID_REGEX = Pattern.compile("user_id=([^&]*)");
    private static final Pattern SCREEN_NAME_REGEX = Pattern.compile("screen_name=([^&]*)");
    private static final Pattern TOKEN_REGEX = Pattern.compile("oauth_token=([^&]+)");
    private static final Pattern SECRET_REGEX = Pattern.compile("oauth_token_secret=([^&]*)");

    public AccessTokenExtractor getAccessTokenExtractor() {
        return new MyTokenExtractor();
    }
    class MyTokenExtractor extends TokenExtractorImpl{

        public MyToken extract(String response) {
            Preconditions.checkEmptyString(response, "Response body is incorrect. Can\'t extract a token from an empty string");
            String token = this.extract(response, TOKEN_REGEX);
            String secret = this.extract(response, SECRET_REGEX);
            String user_id_str = this.extract(response, USER_ID_REGEX);
            String screen_name = this.extract(response, SCREEN_NAME_REGEX);
            return new MyToken(token, secret, response, user_id_str, screen_name);
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

    class MyToken extends Token {
        public String user_id_str;
        public String screen_name;

        public MyToken(String token, String secret, String response, String user_id_str, String screen_name){
            super(token, secret,response);
            this.user_id_str = user_id_str;
            this.screen_name = screen_name;
        }
    }
}
