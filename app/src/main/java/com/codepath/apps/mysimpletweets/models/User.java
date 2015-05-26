package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by bkuo on 5/21/15.
 */
public class User implements Serializable {
    private String profileImageUrl;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    private long uid;
    private String name;
    private String screenName;

    public User(long uid, String name, String screenName, String profileImageUrl) {
        this.name = name;
        this.uid = uid;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
    }
    public User(){}

    public static User fromJson(JSONObject json) {
        User u;

        try {
            u = new User(
                    json.getLong("id"),
                    json.getString("name"),
                    json.getString("screen_name"),
                    json.getString("profile_image_url")
            );

        } catch (JSONException e) {
            e.printStackTrace();
            u = new User();
        }
        return u;
    }
}
