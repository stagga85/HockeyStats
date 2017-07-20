package com.stats.shottracker.models;


import java.util.Date;

public class Game {

    public int id;
    public Team home;
    public Team away;
    public int period;
    public Date date;
    public Player homePlayer;
    public Player awayPlayer;

    public int homeFaceOffsWon = 0;
    public int awayFaceOffsWon = 0;

    public Game () {}

    public Game (int id, Team home, Team away) {
        this.id = id;
        this.home = home;
        this.away = away;
    }

    @Override
    public String toString() {
        return "Game{" +
                "home=" + home +
                ", away=" + away +
                '}';
    }
}
