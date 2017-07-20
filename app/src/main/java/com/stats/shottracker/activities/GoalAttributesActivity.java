package com.stats.shottracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.stats.shottracker.R;
import com.stats.shottracker.models.Shot;
import com.stats.shottracker.models.TempShot;
import com.stats.shottracker.sql.DatabaseHelper;

public class GoalAttributesActivity extends AppCompatActivity implements View.OnTouchListener {

    DatabaseHelper db;
    int selection = 0;

    Spinner shotTypeSpinner;
    Spinner shotOccurrenceSpinner;
    Spinner playTypeSpinner;

    EditText notesEditText;

    Button submitButton;

    TempShot tempShot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_attributes);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageView iv = (ImageView) findViewById (R.id.goal_view);
        if (iv != null) {
            iv.setOnTouchListener (this);
        }

        init();

        db = new DatabaseHelper(this);
        tempShot = getIntent().getExtras().getParcelable("NEW_MADE_SHOT");

        //Toast.makeText(this, "" + tempShot.toString(), Toast.LENGTH_SHORT).show();
    }

    public void init () {
        shotTypeSpinner = (Spinner) findViewById(R.id.shot_type);
        shotOccurrenceSpinner = (Spinner) findViewById(R.id.shot_occurrence);
        playTypeSpinner = (Spinner) findViewById(R.id.play_type);
        notesEditText = (EditText) findViewById(R.id.shot_notes);
        submitButton = (Button) findViewById(R.id.submit_shot);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selection != 0) {
                    saveShot();
                } else {
                    Toast.makeText(GoalAttributesActivity.this, "Pick a zone first.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveShot() {
        Shot shot = new Shot();
        shot.player = db.getPlayer(tempShot.playerId);
        shot.game = db.getGame(tempShot.gameId);
        shot.isGoal = tempShot.isGoal;
        shot.period = tempShot.period;
        shot.x = tempShot.x;
        shot.y = tempShot.y;

        shot.location = selection;
        shot.occurrence = shotOccurrenceSpinner.getSelectedItem().toString();
        shot.playType = playTypeSpinner.getSelectedItem().toString();
        shot.type = shotTypeSpinner.getSelectedItem().toString();
        shot.notes = notesEditText.getText().toString();

        long shotId = db.addShot(shot);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("MADE_PUCK", shotId);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }



    public int getHotspotColor (int hotspotId, int x, int y) {
        ImageView img = (ImageView) findViewById (hotspotId);
        img.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
        img.setDrawingCacheEnabled(false);
        return hotspots.getPixel(x, y);
    }

    public boolean onTouch (View v, MotionEvent ev) {
        final int action = ev.getAction();
        // (1)
        final int evX = (int) ev.getX();
        final int evY = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN :

                break;
            case MotionEvent.ACTION_UP :
                int touchColor = getHotspotColor (R.id.goal_areas, evX, evY);
                ColorTool ct = new ColorTool();
                int tolerance = 25;

                if (ct.closeMatch (Color.BLACK, touchColor, tolerance)) {
                    selection = 1;
                    Toast.makeText(this, "Clicked Zone 1", Toast.LENGTH_SHORT).show();
                }
                if (ct.closeMatch (Color.BLUE, touchColor, tolerance)) {
                    selection = 2;
                    Toast.makeText(this, "Clicked Zone 2", Toast.LENGTH_SHORT).show();
                }
                if (ct.closeMatch (Color.CYAN, touchColor, tolerance)) {
                    selection = 3;
                    Toast.makeText(this, "Clicked Zone 3", Toast.LENGTH_SHORT).show();
                }
                if (ct.closeMatch (Color.DKGRAY, touchColor, tolerance)) {
                    selection = 4;
                    Toast.makeText(this, "Clicked Zone 4", Toast.LENGTH_SHORT).show();
                }
                if (ct.closeMatch (Color.GRAY, touchColor, tolerance)) {
                    selection = 5;
                    Toast.makeText(this, "Clicked Zone 5", Toast.LENGTH_SHORT).show();
                }
                if (ct.closeMatch (Color.GREEN, touchColor, tolerance)) {
                    selection = 6;
                    Toast.makeText(this, "Clicked Zone 6", Toast.LENGTH_SHORT).show();
                }
                if (ct.closeMatch (Color.LTGRAY, touchColor, tolerance)) {
                    selection = 7;
                    Toast.makeText(this, "Clicked Zone 7", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return true;
    }

    class ColorTool {

        public ColorTool () {}

        public boolean closeMatch (int color1, int color2, int tolerance) {
            if ((int) Math.abs (Color.red (color1) - Color.red (color2)) > tolerance )
                return false;
            if ((int) Math.abs (Color.green (color1) - Color.green (color2)) > tolerance )
                return false;
            if ((int) Math.abs (Color.blue (color1) - Color.blue (color2)) > tolerance )
                return false;
            return true;
        } // end match
    }
}
