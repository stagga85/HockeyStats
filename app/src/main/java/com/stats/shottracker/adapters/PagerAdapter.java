package com.stats.shottracker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.stats.shottracker.fragments.GamesFragment;
import com.stats.shottracker.fragments.PlayersFragment;
import com.stats.shottracker.fragments.TeamsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TeamsFragment teams = new TeamsFragment();
                return teams;
            case 1:
                PlayersFragment players = new PlayersFragment();
                return players;
            case 2:
                GamesFragment games = new GamesFragment();
                return games;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}