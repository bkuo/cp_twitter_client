package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by bkuo on 5/31/15.
 */
public class UserCardFragment extends Fragment{

    private TextView tvHandle;
    private TextView tvUsername;
    private ImageView ivAvatar;
    private User user ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_card, container, false);

        ivAvatar = (ImageView) v.findViewById(R.id.ivAvatar);
        tvUsername = (TextView) v.findViewById(R.id.tvUsername);
        tvHandle = (TextView) v.findViewById(R.id.tvHandle);
        tvUsername.setText(user.getName());
        tvHandle.setText("@" + user.getScreenName());

        Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(ivAvatar);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getArguments().getSerializable("user");
        Log.i("TWEEEEEEE", "usercardfragement:  " +user.toString());
    }
}
