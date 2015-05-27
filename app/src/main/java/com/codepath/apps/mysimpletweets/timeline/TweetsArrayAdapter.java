package com.codepath.apps.mysimpletweets.timeline;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bkuo on 5/21/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetsArrayAdapter(Context context, List<Tweet> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder;
        Tweet tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewholder = new ViewHolder();
            viewholder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewholder.tvUserName = (TextView) convertView.findViewById(R.id.tvUsername);
            viewholder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewholder.tvCreatedAt = (TextView) convertView.findViewById(R.id.tvCreatedAt);
            viewholder.tvHandle = (TextView) convertView.findViewById(R.id.tvHandle);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        viewholder.tvUserName.setText(tweet.getUser().getScreenName());
        viewholder.tvCreatedAt.setText(
                ((String) DateUtils.getRelativeTimeSpanString(tweet.getCreatedAt(),
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_RELATIVE
                )).replaceFirst("(\\d+) (\\S).*", "$1$2")
        );
        viewholder.tvHandle.setText("@"+tweet.getUser().getScreenName());
        viewholder.tvBody.setText(tweet.getBody());
        viewholder.ivProfileImage.setImageResource(0);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewholder.ivProfileImage);
        return convertView;
    }

    private class ViewHolder {
        public TextView tvUserName;
        public ImageView ivProfileImage;
        public TextView tvBody;
        public TextView tvHandle;
        public TextView tvCreatedAt;
    }
}
