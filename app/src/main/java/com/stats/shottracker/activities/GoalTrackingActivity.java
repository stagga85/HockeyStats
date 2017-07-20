package com.stats.shottracker.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.stats.shottracker.R;
import com.stats.shottracker.models.Shot;
import com.stats.shottracker.sql.DatabaseHelper;

public class GoalTrackingActivity extends AppCompatActivity implements View.OnTouchListener {

    DatabaseHelper db;
    long shotId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_tracking);

        ImageView iv = (ImageView) findViewById (R.id.goal_view);
        if (iv != null) {
            iv.setOnTouchListener (this);
        }

        shotId = getIntent().getExtras().getLong("SHOT_ID");


        Toast.makeText(this, shotId + "", Toast.LENGTH_SHORT).show();

        db = new DatabaseHelper(this);




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
                ColorTool ct = new ColorTool ();
                int tolerance = 25;
                int selection = 0;

                if (ct.closeMatch (Color.BLACK, touchColor, tolerance)) {
                    selection = 1;
                    Toast.makeText(this, "Clicked Region 1", Toast.LENGTH_SHORT).show();
                }
                if (ct.closeMatch (Color.BLUE, touchColor, tolerance)) {
                    selection = 2;
                    Toast.makeText(this, "Clicked Region 2", Toast.LENGTH_SHORT).show();
                }
                if (ct.closeMatch (Color.CYAN, touchColor, tolerance)) {
                    selection = 3;
                    Toast.makeText(this, "Clicked Region 3", Toast.LENGTH_SHORT).show();
                }
                if (ct.closeMatch (Color.DKGRAY, touchColor, tolerance)) {
                    selection = 4;
                    Toast.makeText(this, "Clicked Region 4", Toast.LENGTH_SHORT).show();
                }
                if (ct.closeMatch (Color.GRAY, touchColor, tolerance)) {
                    selection = 5;
                    Toast.makeText(this, "Clicked Region 5", Toast.LENGTH_SHORT).show();
                }
                if (ct.closeMatch (Color.GREEN, touchColor, tolerance)) {
                    selection = 6;
                    Toast.makeText(this, "Clicked Region 6", Toast.LENGTH_SHORT).show();
                }
                if (ct.closeMatch (Color.LTGRAY, touchColor, tolerance)) {
                    selection = 7;
                    Toast.makeText(this, "Clicked Region 7", Toast.LENGTH_SHORT).show();
                }


                Shot shot = db.getShot(shotId);
                shot.location = selection;

                db.updateShot(shot);

                Shot b = db.getShot(shotId);
                Toast.makeText(this, "new shot " + b.toString(), Toast.LENGTH_SHORT).show();

                finish();

                Intent i = new Intent(this, GoalAttributesActivity.class);
                i.putExtra("GOAL_ID", shot.id);
                startActivity(i);
                break;
        } // end switch

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
