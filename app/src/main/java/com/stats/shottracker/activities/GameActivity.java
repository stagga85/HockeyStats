package com.stats.shottracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Matrix;
import android.graphics.Point;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.stats.shottracker.R;
import com.stats.shottracker.fragments.GamesFragment;
import com.stats.shottracker.fragments.StatsDialogFragment;
import com.stats.shottracker.models.Game;
import com.stats.shottracker.models.Shot;
import com.stats.shottracker.models.TempShot;
import com.stats.shottracker.sql.DatabaseHelper;
import com.stats.shottracker.views.RinkView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements RinkView.ShotActionListener {

    private RinkView rink;
    private Game game;
    private DatabaseHelper db;

    private List<Shot> shots;

    TextView homePlayerName;
    TextView awayPlayerName;

    TextView homeGoalieSavesTextView;
    TextView awayGoalieSavesTextView;

    TextView homeFaceOffsWon;
    TextView awayFaceOffsWon;

    ImageButton homeFaceOffsUpButton;
    ImageButton homeFaceOffsDownButton;

    ImageButton awayFaceOffsUpButton;
    ImageButton awayFaceOffsDownButton;

    List<Integer> backStack;

    MenuItem undoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        backStack = new LinkedList<>();

        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_game);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        shots = new ArrayList<>();


        db = new DatabaseHelper(this);

        int gameId = getIntent().getIntExtra(GamesFragment.GAME_ID, -1);

        game = db.getGame(gameId);

        homePlayerName = (TextView) findViewById(R.id.home_player);
        awayPlayerName = (TextView) findViewById(R.id.away_player);

        homePlayerName.setText(game.homePlayer.firstName + " " + game.homePlayer.lastName);
        awayPlayerName.setText(game.awayPlayer.firstName + " " + game.awayPlayer.lastName);

        //showSnackBar("Current Game ID = " + game.homePlayer.firstName);

        // Setup the shot controller.
        rink = (RinkView) findViewById(R.id.rink);
        rink.setShotActionListener(this);

        TextView homeTeamName = (TextView)findViewById(R.id.home_team_name);
        TextView awayTeamName = (TextView)findViewById(R.id.away_team_name);

        homeTeamName.setText(game.home.teamName);
        awayTeamName.setText(game.away.teamName);



        homeFaceOffsUpButton = (ImageButton)findViewById(R.id.face_off_home_up);
        homeFaceOffsDownButton = (ImageButton)findViewById(R.id.face_off_home_down);

        awayFaceOffsUpButton = (ImageButton)findViewById(R.id.face_off_away_up);
        awayFaceOffsDownButton = (ImageButton)findViewById(R.id.face_off_away_down);

        homeFaceOffsWon = (TextView)findViewById(R.id.face_off_home_won);
        awayFaceOffsWon = (TextView)findViewById(R.id.face_off_away_won);

        homeGoalieSavesTextView = (TextView)findViewById(R.id.home_player_saves);
        awayGoalieSavesTextView = (TextView)findViewById(R.id.away_player_saves);

        homeFaceOffsWon.setText("" + game.homeFaceOffsWon);
        awayFaceOffsWon.setText("" + game.awayFaceOffsWon);

        homeFaceOffsUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFaceOffsUp();
            }
        });

        homeFaceOffsDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFaceOffsDown();
            }
        });

        awayFaceOffsUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                awayFaceOffsUp();
            }
        });

        awayFaceOffsDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                awayFaceOffsDown();
            }
        });


        // Controllers for the period.

        findViewById(R.id.period_up).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                periodUp();
            }

        });

        findViewById(R.id.period_down).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                periodDown();
            }

        });

        rink.changePeriod(game.period);

        int homeGoals = db.getShotsAgianstPlayer(game.homePlayer.id, game.id);
        int awayGoals = db.getShotsAgianstPlayer(game.homePlayer.id, game.id);

        rink.updateScoreBoard(homeGoals, awayGoals);

        updateShotList();
        updateSaves();
    }

    private void periodUp() {
        if (game.period < 3) {
            game.period++;
            db.updateGame(game);
            rink.changePeriod(game.period);
            updateShotList();
        }
    }

    private void periodDown() {
        if (game.period > 1) {
            game.period--;
            db.updateGame(game);
            rink.changePeriod(game.period);
            updateShotList();
        }
    }

    private void homeFaceOffsUp() {
        game.homeFaceOffsWon++;
        db.updateGame(game);
        homeFaceOffsWon.setText("" + game.homeFaceOffsWon);
    }

    private void homeFaceOffsDown() {
        if (game.homeFaceOffsWon > 0) {
            game.homeFaceOffsWon--;
            db.updateGame(game);
            homeFaceOffsWon.setText("" + game.homeFaceOffsWon);
        }
    }

    private void awayFaceOffsUp() {
        game.awayFaceOffsWon++;
        db.updateGame(game);
        awayFaceOffsWon.setText("" + game.awayFaceOffsWon);
    }

    private void awayFaceOffsDown() {
        if (game.awayFaceOffsWon > 0) {
            game.awayFaceOffsWon--;
            db.updateGame(game);
            awayFaceOffsWon.setText("" + game.awayFaceOffsWon);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_controlls, menu);
        undoButton = menu.findItem(R.id.undo_puck);
        return true;
    }

    private void showSnackBar (String message) {
        Snackbar.make(findViewById(R.id.activity_game), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.undo_puck) {

            if (backStack.size() > 0) {
                int shotToRemoveId = backStack.remove(backStack.size() - 1);
                db.deleteShot(shotToRemoveId);
                //Toast.makeText(this, backStack.toString(), Toast.LENGTH_SHORT).show();
                updateShotList();
            }


            return true;
        } else if (id == R.id.swap_sides) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void shotMade(boolean againstHome, Point p) {

        if (againstHome) {
            //showStatsDialog("Against - " + game.home.teamName);
            TempShot shot = new TempShot();

            shot.playerId = game.homePlayer.id;
            shot.isGoal = true;
            shot.x = p.x;
            shot.y = p.y;
            shot.gameId = game.id;
            shot.period = game.period;


            //rink.updateAwayTeamScore();

            Intent i = new Intent(this, GoalAttributesActivity.class);
            i.putExtra("NEW_MADE_SHOT", shot);

            startActivityForResult(i, 1);
        } else {
            //showStatsDialog("Against - " + game.away.teamName);

            TempShot shot = new TempShot();

            shot.playerId = game.awayPlayer.id;
            shot.isGoal = true;
            shot.x = p.x;
            shot.y = p.y;
            shot.gameId = game.id;
            shot.period = game.period;

            //rink.updateAwayTeamScore();

            Intent i = new Intent(this, GoalAttributesActivity.class);
            i.putExtra("NEW_MADE_SHOT", shot);

            startActivityForResult(i, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                int madeShotId = (int) data.getLongExtra("MADE_PUCK", -1);

                backStack.add(madeShotId);
                //Toast.makeText(this, backStack.toString(), Toast.LENGTH_SHORT).show();


                Shot madeShot = db.getShot(madeShotId);

//                if (madeShot.player.team.toString().equals(game.home.teamName)) {
//                    rink.updateAwayTeamScore();
//                } else {
//                    rink.updateHomeTeamScore();
//                }
                rink.placePuck(madeShot.x, madeShot.y, madeShot.isGoal);
                updateShotList();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    @Override
    public void shotAttempt(boolean againstHome, Point p) {
        Shot shot = new Shot();

        if (againstHome) {
            showSnackBar("Against - " + game.home.teamName);
            shot.player = game.homePlayer;
            shot.game = game;
            shot.isGoal = false;
            shot.period = game.period;
            shot.x = p.x;
            shot.y = p.y;
        } else {
            showSnackBar("Against - " + game.away.teamName);
            shot.player = game.awayPlayer;
            shot.game = game;
            shot.isGoal = false;
            shot.period = game.period;
            shot.x = p.x;
            shot.y = p.y;
        }

        int shotId = (int) db.addShot(shot);
        backStack.add(shotId);

        rink.placePuck(p.x, p.y, false);

        updateSaves();

    }

    private void updateSaves() {
        int awayGoalieSaves = db.getPlayerSaves(game.awayPlayer.id, game.id);
        int homeGoalieSaves = db.getPlayerSaves(game.homePlayer.id, game.id);

        homeGoalieSavesTextView.setText("Saves: " + homeGoalieSaves);
        awayGoalieSavesTextView.setText("Saves: " + awayGoalieSaves);
    }

    private void showStatsDialog (String title) {
        FragmentManager fm = getSupportFragmentManager();
        StatsDialogFragment editNameDialogFragment = StatsDialogFragment.newInstance(title);
        editNameDialogFragment.show(fm, "stats_dialog");
    }

    private void updateShotList () {
        shots.clear();
        shots.addAll(db.currentShotsForGame(game));
        rink.loadShots(shots);

        int homeGoals = db.getShotsAgianstPlayer(game.awayPlayer.id, game.id);
        int awayGoals = db.getShotsAgianstPlayer(game.homePlayer.id, game.id);

        rink.updateScoreBoard(homeGoals, awayGoals);

        updateSaves();
        //Log.d("SHOTS", shots.toString());
    }
}
