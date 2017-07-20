package com.stats.shottracker.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

public class Shot {

    public int id;

    public int x;
    public int y;

    public boolean isGoal;

    // The goalie that the shot was taken against.
    public Player player;
    public Game game;

    public String type = "";
    public String occurrence = "";
    public String playType = "";
    public int location = 0;
    public int timeOnClock = 0;
    public String notes = "";

    public int period = 0;

    public Shot () {

    }

    public Shot (int x, int y, Player player, boolean isGoal) {

        this.x = x;
        this.y = y;
        this.isGoal = isGoal;
        this.player = player;
    }

    @Override
    public String toString() {
        return "Shot{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", isGoal=" + isGoal +
                ", player=" + player +
                ", game=" + game +
                ", type='" + type + '\'' +
                ", occurrence='" + occurrence + '\'' +
                ", playType='" + playType + '\'' +
                ", location=" + location +
                ", timeOnClock=" + timeOnClock +
                ", notes='" + notes + '\'' +
                ", period=" + period +
                '}';
    }
}
