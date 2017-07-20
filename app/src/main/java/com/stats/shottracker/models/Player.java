package com.stats.shottracker.models;

public class Player  {

    public int id;

    public String firstName;

    public String lastName;

    public Team team;

    public Player () {}

    public Player (String firstName, String lastName, Team team) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
    }

    @Override
    public String toString() {
        if (lastName != null) {
            return firstName + " " + lastName;
        }

        return firstName;
    }

}
