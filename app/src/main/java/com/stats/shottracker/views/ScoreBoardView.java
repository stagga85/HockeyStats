package com.stats.shottracker.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stats.shottracker.R;

public class ScoreBoardView extends RelativeLayout {

    View rootView;

    TextView homeTeam;
    TextView awayTeam;

    public ScoreBoardView(Context context) {
        super(context);
        init(context);
    }

    public ScoreBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.score_board, this);
//        homeTeam = (TextView) rootView.findViewById(R.id.homeTeamName);
//        awayTeam = (TextView) rootView.findViewById(R.id.awayTeamName);
    }

}