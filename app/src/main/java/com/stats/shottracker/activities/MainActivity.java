package com.stats.shottracker.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.stats.shottracker.fragments.GamesFragment;
import com.stats.shottracker.fragments.PlayersFragment;
import com.stats.shottracker.fragments.TeamsFragment;
import com.stats.shottracker.R;
import com.stats.shottracker.models.Team;
import com.stats.shottracker.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * This is the host for all of the 'ViewPager' that cycles through the three main fragments.
 */
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    Adapter adapter;
    ViewPager viewPager;
    TabLayout tabs;
    View root;

    int currentTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grab the root view, we might be needing this.
        root = findViewById(R.id.main_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.addOnTabSelectedListener(this);


    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new GamesFragment(), "Games");
        adapter.addFragment(new TeamsFragment(), "Teams");
        adapter.addFragment(new PlayersFragment(), "Players");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        currentTab = tabs.getSelectedTabPosition();
        //toast("Current Tab: " + currentTab);
        invalidateOptionsMenu();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        if (currentTab == 0) {
            getMenuInflater().inflate(R.menu.game_menu, menu);
        } else if (currentTab == 1) {
            getMenuInflater().inflate(R.menu.team_menu, menu);
        } else if (currentTab == 2) {
            getMenuInflater().inflate(R.menu.player_menu, menu);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.new_team) {
            Intent i = new Intent(this, NewTeamActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.new_player) {
            if (userHasTeams()) {
                Intent i = new Intent(this, NewPlayerActivity.class);
                startActivity(i);
                return true;
            }
            showSnackBar("You need at least one team!");
            return false;
        } else if (id == R.id.new_game) {
            if (getTeams().size() > 1) {
                Intent i = new Intent(this, NewGameActivity.class);
                startActivity(i);
                return true;
            }
            showSnackBar("You need at least two teams!");
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toast (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackBar (String message) {
        Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
    }

    private boolean userHasTeams () {
        return new DatabaseHelper(this).getAllTeams().size() > 0;
    }

    private List<Team> getTeams () {
        return new DatabaseHelper(this).getAllTeams();
    }

}