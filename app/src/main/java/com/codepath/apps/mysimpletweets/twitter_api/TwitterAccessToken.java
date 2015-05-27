package com.codepath.apps.mysimpletweets.twitter_api;

import org.scribe.model.Token;

/**
 * Created by bkuo on 5/27/15.
 */
public class TwitterAccessToken extends Token {
    public String getUser_id() {
        return user_id;
    }

    public String getScreen_name() {
        return screen_name;
    }

    private final String user_id;
    private String screen_name;

    public TwitterAccessToken(String token, String secret, String user_id, String screen_name) {
        this(token, secret, user_id, screen_name, (String)null);
    }
    public TwitterAccessToken(String token, String secret, String user_id, String screen_name, String rawResponse) {
        super(token, secret, rawResponse);
        this.user_id = user_id;
        this.screen_name = screen_name;
    }

    public String toString() {
        return String.format("Token[%s , %s]", new Object[]{this.user_id, this.screen_name}) + super.toString();
    }
}
