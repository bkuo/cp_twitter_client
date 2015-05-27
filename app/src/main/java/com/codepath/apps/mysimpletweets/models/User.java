package com.codepath.apps.mysimpletweets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by bkuo on 5/21/15.
 */
@Table(name="Users")
public class User extends Model implements Serializable {

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
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;
    @Column(name = "name")
    private String name;
    @Column(name = "screenName")
    private String screenName;
    @Column(name = "profileImageUrl")
    private String profileImageUrl;

    public User(long uid, String name, String screenName, String profileImageUrl) {
        this.name = name;
        this.uid = uid;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
    }
    public User(){
        super();

    }

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
