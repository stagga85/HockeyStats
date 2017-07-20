package com.stats.shottracker.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.stats.shottracker.R;
import com.stats.shottracker.core.exceptions.DuplicateEntryException;
import com.stats.shottracker.models.Team;
import com.stats.shottracker.sql.DatabaseHelper;

public class NewTeamActivity extends AppCompatActivity {

    private int activityId;

    private TextView teamNameInput;
    private TextView teamAbbreviationInput;
    private TextView teamLocationInput;
    private TextView teamNotesInput;
    private TextView teamPeriodLengthInput;
    private TextView teamOvertimeLengthInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_team);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settingsToolbar);
        toolbar.setTitle("New Team");
        setSupportActionBar(toolbar);

        activityId = R.id.activity_new_team;

        initTextViews();
//
//        Spinner spinner = (Spinner) findViewById(R.id.teams_spinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.teams_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTeam();
            }
        });
    }

    private void initTextViews () {
        teamNameInput = (TextView) findViewById(R.id.teamName);
        teamAbbreviationInput = (TextView) findViewById(R.id.teamAbbreviation);
        teamLocationInput = (TextView) findViewById(R.id.teamLocation);
        teamNotesInput = (TextView) findViewById(R.id.teamNotes);
        teamPeriodLengthInput = (TextView) findViewById(R.id.teamPeriodLength);
        teamOvertimeLengthInput = (TextView) findViewById(R.id.teamOvertimeLength);
    }

    private void addTeam () {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        String teamName = teamNameInput.getText().toString();
        String teamAbbreviation = teamAbbreviationInput.getText().toString();

        Team team = new Team(teamName, teamAbbreviation, 5);

        try {
            databaseHelper.addTeam(team);
            showMessage(team.teamName + " added!");
            finish();
        } catch (DuplicateEntryException dee) {
            showMessage(dee.getMessage());
        }
    }

    private void showMessage (String message) {
        Snackbar.make(findViewById(activityId), message, Snackbar.LENGTH_LONG).show();
    }
}
