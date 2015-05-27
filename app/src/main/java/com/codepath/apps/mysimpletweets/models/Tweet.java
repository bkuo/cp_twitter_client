package com.codepath.apps.mysimpletweets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
/**
 * Created by bkuo on 5/21/15.
 */
@Table(name="Tweets")
public class Tweet extends Model implements Serializable {

    @Column(name = "createdAt")
    private long createdAt;

    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;
    @Column(name = "body")
    private String body;
    @Column(name = "in_reply_to")
    private Long in_reply_to;
    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;

    @Column(name = "retweet_count")
    private int retweet_count;
    @Column(name = "favorite_count")
    private int favorite_count;

    public Long getIn_reply_to() {
        return in_reply_to;
    }

    public void setIn_reply_to(long in_reply_to) {
        this.in_reply_to = in_reply_to;
    }


    public int getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Tweet findOrCreateFromJson(JSONObject jsonObject) {
        Long rId = jsonObject.optLong("id");
        if(rId!=null){
            
        }
        Tweet tweet = new Tweet();
        try {
            tweet.uid = jsonObject.getLong("id");
            tweet.body = jsonObject.getString("text");
            tweet.retweet_count = jsonObject.optInt("retweet_count", 0);
            tweet.favorite_count = jsonObject.optInt("favorite_count", 0);
            SimpleDateFormat parserSDF = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            tweet.createdAt = parserSDF.parse(jsonObject.getString("created_at")).getTime();
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tweet;
    }
    

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                tweets.add(Tweet.findOrCreateFromJson(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;

    }
    public Tweet(){
        super();
    }

    public User getUser() {
        return user;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUid() {
        return uid;
    }

    public String getBody() {
        return body;
    }
}
