package com.stats.shottracker.models;

public class Team  {

    // PK
    public int id;

    public String teamName;
    public String abbreviation;
    public String notes;
    public String location;
    public int periodLength;
    public int overTimeLength;

    public Team () {}

    public Team (String teamName, String abbreviation, int overTimeLength) {
        this.teamName = teamName;
        this.abbreviation = abbreviation;
        this.overTimeLength = overTimeLength;
    }

    @Override
    public String toString() {
        return teamName;
    }

}

