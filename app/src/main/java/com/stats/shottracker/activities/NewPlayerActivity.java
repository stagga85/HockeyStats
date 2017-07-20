package com.stats.shottracker.activities;

import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stats.shottracker.R;
import com.stats.shottracker.models.Player;
import com.stats.shottracker.models.Team;
import com.stats.shottracker.sql.DatabaseHelper;

import java.util.List;

public class NewPlayerActivity extends AppCompatActivity {

    private int selectedTeamId = -1;

    private TextView playerFirstName;
    private TextView playerLastName;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);

        Toolbar toolbar = (Toolbar) findViewById(R.id.playerToolbar);
        toolbar.setTitle("New Goalie");
        setSupportActionBar(toolbar);

        initTextViews();

        Spinner spinner = (Spinner) findViewById(R.id.teams_spinner);

        db = new DatabaseHelper(this);
        List<Team> teams = db.getAllTeams();

        final ArrayAdapter<Team> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, teams);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                // Here you get the current item (a User object) that is selected by its position
                Team team = adapter.getItem(position);

                updateSelectedTeamId(team.id);

                //Toast.makeText(NewPlayerActivity.this, "" + team.id, Toast.LENGTH_SHORT).show();
                // Here you can do the action you want to...
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player player = new Player();
                player.firstName = playerFirstName.getText().toString();
                player.lastName = playerLastName.getText().toString();
                player.team = db.getTeam(selectedTeamId);
                addPlayer(player);
            }
        });
    }

    private void initTextViews () {
        playerFirstName = (TextView) findViewById(R.id.playerFirstName);
        playerLastName = (TextView) findViewById(R.id.playerLastName);
    }

    private void addPlayer(Player player) {

        // Need to send out a broad cast to the playerfragment to load the new list.
        db.addPlayer(player);

        finish();
    }

    private void updateSelectedTeamId (int teamId) {
        selectedTeamId = teamId;
    }
}
