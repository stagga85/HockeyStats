package com.stats.shottracker.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.stats.shottracker.R;
import com.stats.shottracker.models.Game;
import com.stats.shottracker.models.Player;
import com.stats.shottracker.models.Team;
import com.stats.shottracker.sql.DatabaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewGameActivity extends AppCompatActivity {

    DatabaseHelper db;

    // Home Team
    private ArrayAdapter<Team> homeTeamAdapter;
    private ArrayAdapter<Player> homeTeamPlayersAdapter;
    private List<Player> homePlayers;

    private ArrayAdapter<Team> awayTeamAdapter;
    private ArrayAdapter<Player> awayTeamPlayersAdapter;
    private List<Player> awayPlayers;

    Spinner homeTeamSpinner;
    Spinner homeGoalieSpinner;

    Spinner awayTeamSpinner;
    Spinner awayGoalieSpinner;


    Team selectedHomeTeam;
    Team selectedAwayTeam;

    Player selectedHomePlayer;
    Player selectedAwayPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        // Activity Init
        Toolbar toolbar = (Toolbar) findViewById(R.id.playerToolbar);
        toolbar.setTitle("New Game");
        setSupportActionBar(toolbar);
        db = new DatabaseHelper(this);

        setUpSpinnerHandles();

        List<Team> teams = db.getAllTeams();
        teams.add(0, getDummyTeam());

        homeTeamAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, teams);
        homeTeamAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        homeTeamSpinner.setAdapter(homeTeamAdapter);

        homeTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    clearHomeGoalieSpinner();
                    selectedHomeTeam = null;
                }else {
                    selectedHomeTeam = homeTeamAdapter.getItem(position);
                    getSelectedHomeTeamPlayers(selectedHomeTeam.id);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });


        awayTeamAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, teams);
        awayTeamAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        awayTeamSpinner.setAdapter(homeTeamAdapter);

        awayTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    clearAwayGoalieSpinner();
                    selectedAwayTeam = null;
                }else {
                    selectedAwayTeam = awayTeamAdapter.getItem(position);
                    getSelectedAwayTeamPlayers(selectedAwayTeam.id);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });


        // Home Team Goalie Spinner
        homePlayers = new ArrayList<>();
        homePlayers.add(0, getDummyPlayer());

        homeTeamPlayersAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, homePlayers);
        homeTeamPlayersAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        homeGoalieSpinner.setAdapter(homeTeamPlayersAdapter);

        homeGoalieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                }else {
                    selectedHomePlayer = homeTeamPlayersAdapter.getItem(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });


        // Away Team Goalie Spinner
        awayPlayers = new ArrayList<>();
        awayPlayers.add(0, getDummyPlayer());

        awayTeamPlayersAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, awayPlayers);
        awayTeamPlayersAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        awayGoalieSpinner.setAdapter(awayTeamPlayersAdapter);

        awayGoalieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                }else {
                    selectedAwayPlayer = awayTeamPlayersAdapter.getItem(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.new_game_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedHomeTeam != null && selectedAwayTeam != null) {
                    createNewGame();
                }
            }
        });

    }

    private void createNewGame() {
        Game game = new Game();
        game.home = selectedHomeTeam;
        game.away = selectedAwayTeam;
        game.homePlayer = selectedHomePlayer;
        game.awayPlayer = selectedAwayPlayer;
        game.date = Calendar.getInstance().getTime();
        game.period = 1;
        db.addGame(game);
        finish();
    }

    private void setUpSpinnerHandles() {
        // Home Team Spinners
        homeTeamSpinner = (Spinner) findViewById(R.id.home_team_spinner);
        homeGoalieSpinner = (Spinner) findViewById(R.id.home_team_goalie);

        // Away Team Spinners
        awayTeamSpinner = (Spinner) findViewById(R.id.away_team_spinner);
        awayGoalieSpinner = (Spinner) findViewById(R.id.away_team_goalie);
    }

    private void getSelectedHomeTeamPlayers(long id) {
        homeTeamPlayersAdapter.clear();
        homeTeamPlayersAdapter.add(getDummyPlayer());
        homeGoalieSpinner.setSelection(0);
        List<Player> players = db.getAllPlayersByTeam(id);
        //Toast.makeText(this, "Players: " + players, Toast.LENGTH_SHORT).show();
        homeTeamPlayersAdapter.addAll(players);
        homeTeamPlayersAdapter.notifyDataSetChanged();

    }

    private void getSelectedAwayTeamPlayers(long id) {
        awayTeamPlayersAdapter.clear();
        awayTeamPlayersAdapter.add(getDummyPlayer());
        awayGoalieSpinner.setSelection(0);
        List<Player> players = db.getAllPlayersByTeam(id);
        //Toast.makeText(this, "Players: " + players, Toast.LENGTH_SHORT).show();
        awayTeamPlayersAdapter.addAll(players);
        awayTeamPlayersAdapter.notifyDataSetChanged();

    }

    private void clearHomeGoalieSpinner() {
        homeTeamPlayersAdapter.clear();
        homePlayers = new ArrayList<>();
        homeTeamPlayersAdapter.add(getDummyPlayer());
    }

    private void clearAwayGoalieSpinner() {
        awayTeamPlayersAdapter.clear();
        awayPlayers = new ArrayList<>();
        awayTeamPlayersAdapter.add(getDummyPlayer());
    }

    private Player getDummyPlayer () {
        Player dummyPlayer = new Player();
        dummyPlayer.firstName = "Pick Player";
        dummyPlayer.id = -1;
        return dummyPlayer;
    }

    private Team getDummyTeam () {
        Team dummyTeam = new Team();
        dummyTeam.teamName = "Pick Team";
        dummyTeam.id = -1;
        return dummyTeam;
    }
    
}
