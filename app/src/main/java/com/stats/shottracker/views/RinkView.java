package com.stats.shottracker.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.stats.shottracker.R;
import com.stats.shottracker.models.Player;
import com.stats.shottracker.models.Shot;

/**
 * This view is the presenter / controller for the game data.
 */
public class RinkView extends android.support.v7.widget.AppCompatImageView {

    GestureDetector gestureDetector;

    private String TAG = "RinkView";

    List<Shot> shots; // This should only be the visible shots for the period.

    private Paint drawPaint;
    private final int paintColor = Color.BLACK;

    Bitmap rinkImage;

    int homeGoals = 0;
    int awayGoals = 0;

    String homeTeamName = "";
    String awayTeamName = "";

    int period = 0;

    Context context;

    public RinkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        gestureDetector = new GestureDetector(context, new GestureListener());
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }

    public void updateAwayTeamScore() {
        awayGoals++;
    }

    public void updateHomeTeamScore() {
        homeGoals++;
    }

    public void setHomeTeamName(String teamName) {
        homeTeamName = teamName;
    }

    public void setAwayTeamName(String teamName) {
        awayTeamName = teamName;
    }

    public void changePeriod(int periodFromGameActivity) {
        period = periodFromGameActivity;
        invalidate();
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");
            Point p = new Point((int) x, (int) y);

            if (listener != null) {
                if (shotIsAgainstHome(p)) {
                    listener.shotAttempt(true, p);
                    //placePuck(p.x, p.y, false);
                } else {
                    listener.shotAttempt(false, p);
                    //placePuck(p.x, p.y, false);
                }
            }

            return super.onSingleTapConfirmed(event);
        }

        @Override
        public boolean onDown(MotionEvent event) {

            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");
            Point p = new Point((int) x, (int) y);

            if (listener != null) {
                if (shotIsAgainstHome(p)) {
                    listener.shotMade(true, p);
                    //placePuck(p.x, p.y, true);
                } else {
                    listener.shotMade(false, p);
                    //placePuck(p.x, p.y, true);
                }
            }

            return true;
        }

        private boolean shotIsAgainstHome(Point p) {
           return p.x < getWidth() / 2;
        }
    }

    public ShotActionListener listener;

    public interface ShotActionListener {
        void shotMade(boolean againstHome, Point p);
        void shotAttempt(boolean againstHome, Point p);
    }

    public void setShotActionListener(ShotActionListener listener) {
        this.listener = listener;
    }

    public void undoPuck () {
        if (shots.size() > 0) {
            shots.remove(shots.size() - 1);
            invalidate();
        }
    }

    private void init () {
        rinkImage = BitmapFactory.decodeResource(getResources(), R.drawable.rink, null);
        shots = new ArrayList<>();
        setupPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.FILL);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Shot shot : shots) {
            if (shot.isGoal) {
                drawPaint.setColor(Color.GREEN);
            } else {
                drawPaint.setColor(Color.BLACK);
            }
            canvas.drawCircle(shot.x, shot.y, 4, drawPaint);
        }

        int topOffest = 30;

        drawPaint.setColor(Color.BLACK);
        drawPaint.setTextSize(20);
        drawPaint.setFakeBoldText(true);
        drawPaint.setAlpha(50);
        canvas.drawText("Period : " + period, (getWidth() / 2) / 2, topOffest, drawPaint);
        drawPaint.setAlpha(100);
        canvas.drawText("" + homeGoals, (getWidth() / 2) - 25, topOffest, drawPaint);
        canvas.drawText("" + awayGoals, (getWidth() / 2) + 13, topOffest, drawPaint);
    }

    public void placePuck (int x, int y, boolean goal) {
        shots.add(new Shot(x, y, new Player(), goal));
        invalidate();
    }

    public void loadShots (List<Shot> shotsFromGameActivity) {
        shots.clear();
        shots.addAll(shotsFromGameActivity);
        invalidate();
    }

    public void updateScoreBoard (int home, int away) {
        homeGoals = home;
        awayGoals = away;
        invalidate();
    }
}
