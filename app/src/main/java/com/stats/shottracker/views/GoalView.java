package com.stats.shottracker.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;


/**
 * Created by tron on 4/27/17.
 */

public class GoalView extends android.support.v7.widget.AppCompatImageView {

    private GestureDetector gestureListener;

    public GoalView(Context context, AttributeSet attrs) {
        super(context, attrs);

        gestureListener = new GestureDetector(context, new GoalView.GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return gestureListener.onTouchEvent(e);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        float radius = 50;
        float x = 150;
        float y = 165;

        canvas.drawCircle(x, y, radius, paint);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            int viewLocation[] = new int[2];
            getLocationInWindow(viewLocation);



            Log.d("View Location", "(" + viewLocation[0] + "," + viewLocation[1] + ")");

            Log.d("Touch Location", "(" + x + "," + y + ")");

            Point p = new Point((int) x, (int) y);

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


            return true;
        }

    }
}
