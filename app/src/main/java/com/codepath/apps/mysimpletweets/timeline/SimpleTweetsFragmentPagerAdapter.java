package com.codepath.apps.mysimpletweets.timeline;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by bkuo on 5/31/15.
 */
public class SimpleTweetsFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private Fragment[] fgTimelines;
    private String[] tabTitles;

    public SimpleTweetsFragmentPagerAdapter(FragmentManager fm, String[] titles, Fragment[] fragments) {
        super(fm);
        tabTitles = titles;
        fgTimelines= fragments;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return fgTimelines[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}