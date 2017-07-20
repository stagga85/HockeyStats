package com.stats.shottracker.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stats.shottracker.core.exceptions.DuplicateEntryException;
import com.stats.shottracker.models.Game;
import com.stats.shottracker.models.Player;
import com.stats.shottracker.models.Shot;
import com.stats.shottracker.models.Team;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TeamDatabaseHelper
 *
 * Database class that controls all of the data persistence
 * for a Team object. Having tried many different libraries
 * the best route seems to be make custom database helper
 * classes that give fine grain control.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private String TAG = DatabaseHelper.class.getName();

    private static final int DATABASE_VERSION = 14;

    private static final String DATABASE_NAME = "shot_tracker";

    // Tables
    private static final String TABLE_TEAMS = "teams";
    private static final String TABLE_PLAYERS = "players";
    private static final String TABLE_GAMES = "games";
    private static final String TABLE_SHOTS = "shots";

    // Common column names
    private static final String _ID = "id";
    private static final String _CASCADE_DELETE = "ON DELETE CASCADE";

    // Team columns
    private static final String TEAM_NAME = "name";
    private static final String TEAM_ABBREVIATION = "abbreviation";
    private static final String TEAM_OVERTIME_LENGTH = "overtime_length";
    private static final String TEAM_LOCATION = "team_location";
    private static final String TEAM_PERIOD_LENGTH = "period_length";
    private static final String TEAM_NOTES = "notes";

    // Player columns
    private static final String PLAYER_FIRST_NAME = "first_name";
    private static final String PLAYER_LAST_NAME = "last_name";
    private static final String PLAYER_TEAM_ID = "team_id";

    // Games column
    private static final String GAME_HOME_TEAM = "home_team";
    private static final String GAME_AWAY_TEAM = "away_team";
    private static final String GAME_PERIOD = "period"; // 1 - 3
    private static final String GAME_DATE = "game_date";
    private static final String GAME_CURRENT_HOME_PLAYER = "current_home_player";
    private static final String GAME_CURRENT_AWAY_PLAYER = "current_away_player";
    private static final String GAME_HOME_FACE_OFFS_WON = "home_face_offs";
    private static final String GAME_AWAY_FACE_OFFS_WON = "away_face_offs";

    // Shots columns
    public static final String SHOT_PLAYER_ID = "player_id"; // FK
    public static final String SHOT_GAME_ID = "game_id"; // FK
    public static final String SHOT_PERIOD = "shot_period";
    public static final String SHOT_MADE = "shot_made";
    public static final String SHOT_LOCATION = "shot_location";
    public static final String SHOT_TIME_ON_CLOCK = "time_on_clock"; // MM:SS
    public static final String SHOT_OCCURRENCE = "shot_occurrence";
    public static final String SHOT_TYPE = "shot_type";
    public static final String SHOT_NOTES = "shot_notes";
    public static final String SHOT_X = "shot_x";
    public static final String SHOT_Y = "shot_y";
    public static final String SHOT_PLAY_TYPE = "shot_play_type";

    // Team create statement
    private static final String CREATE_TEAM = "CREATE TABLE " + TABLE_TEAMS + "("
            + _ID + " INTEGER PRIMARY KEY," + TEAM_NAME + " TEXT, " + TEAM_ABBREVIATION + " TEXT,"
            + TEAM_OVERTIME_LENGTH + " INTEGER" + ")";

    // Player create statement
    private static final String CREATE_PLAYER = "CREATE TABLE " +
            TABLE_PLAYERS + "("
            + _ID + " INTEGER PRIMARY KEY,"
            + PLAYER_FIRST_NAME + " TEXT, "
            + PLAYER_LAST_NAME + " TEXT,"
            + PLAYER_TEAM_ID + " INTEGER,"
            + " FOREIGN KEY (" + PLAYER_TEAM_ID + ") REFERENCES " + TABLE_TEAMS + "(" + _ID + "))";

    // Game create statement
    private static final String CREATE_GAME = "CREATE TABLE " + TABLE_GAMES + "("
            + _ID + " INTEGER PRIMARY KEY,"
            + GAME_HOME_TEAM + " INTEGER, "
            + GAME_AWAY_TEAM + " INTEGER, "
            + GAME_CURRENT_HOME_PLAYER + " INTEGER, "
            + GAME_CURRENT_AWAY_PLAYER + " INTEGER, "
            + GAME_DATE + " TEXT, "
            + GAME_PERIOD + " INTEGER, "
            + GAME_HOME_FACE_OFFS_WON + " INTEGER, "
            + GAME_AWAY_FACE_OFFS_WON + " INTEGER, "
            + " FOREIGN KEY (" + GAME_HOME_TEAM + ") REFERENCES " + TABLE_TEAMS + "(" + _ID + "),"
            + " FOREIGN KEY (" + GAME_AWAY_TEAM + ") REFERENCES " + TABLE_TEAMS + "(" + _ID + "),"
            + " FOREIGN KEY (" + GAME_CURRENT_HOME_PLAYER + ") REFERENCES " + TABLE_PLAYERS + "(" + _ID + ") " + _CASCADE_DELETE + ","
            + " FOREIGN KEY (" + GAME_CURRENT_AWAY_PLAYER + ") REFERENCES " + TABLE_PLAYERS + "(" + _ID + ") " + _CASCADE_DELETE + ")";

    private static final String CREATE_SHOT = "CREATE TABLE " + TABLE_SHOTS + "("
            + _ID + " INTEGER PRIMARY KEY,"
            + SHOT_PLAYER_ID + " INTEGER, "
            + SHOT_GAME_ID + " INTEGER, "
            + SHOT_PERIOD + " INTEGER, "
            + SHOT_MADE + " INTEGER, "
            + SHOT_LOCATION + " INTEGER, "
            + SHOT_TIME_ON_CLOCK + " TEXT, "
            + SHOT_OCCURRENCE + " TEXT, "
            + SHOT_TYPE + " TEXT, "
            + SHOT_NOTES + " TEXT, "
            + SHOT_X + " INTEGER, "
            + SHOT_Y + " INTEGER, "
            + SHOT_PLAY_TYPE + " TEXT, "
            + " FOREIGN KEY (" + SHOT_PLAYER_ID + ") REFERENCES " + TABLE_PLAYERS + "(" + _ID + ") " + _CASCADE_DELETE + ","
            + " FOREIGN KEY (" + SHOT_GAME_ID + ") REFERENCES " + TABLE_GAMES + "(" + _ID + ") " + _CASCADE_DELETE + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TEAM);
        sqLiteDatabase.execSQL(CREATE_PLAYER);
        sqLiteDatabase.execSQL(CREATE_GAME);
        sqLiteDatabase.execSQL(CREATE_SHOT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOTS);
        onCreate(sqLiteDatabase);
    }

    /**
     * Add a team into the database.
     *
     * @param team a team POJO.
     */
    public void addTeam (Team team) throws DuplicateEntryException {
        if (containsTeamName(team)) {
            throw new DuplicateEntryException("That team name already exists.");
        }

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEAM_NAME, team.teamName);
        values.put(TEAM_ABBREVIATION, team.abbreviation);
        values.put(TEAM_OVERTIME_LENGTH, team.overTimeLength);

        database.insert(TABLE_TEAMS, null, values);
        database.close();
    }

    public Team getTeam (long teamId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TEAMS + " WHERE "
                + _ID + " = " + teamId;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Team team = new Team();
        team.id = (c.getInt(c.getColumnIndex(_ID)));
        team.teamName = ((c.getString(c.getColumnIndex(TEAM_NAME))));
        team.abbreviation = ((c.getString(c.getColumnIndex(TEAM_ABBREVIATION))));
        team.overTimeLength = ((c.getInt(c.getColumnIndex(TEAM_OVERTIME_LENGTH))));

        return team;
    }

    private boolean containsTeamName (Team team) {
        return false;
    }

    public List<Team> getAllTeams () {
        List<Team> teams = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_TEAMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Team team = new Team();
                team.id = Integer.parseInt(cursor.getString(0));
                team.teamName = cursor.getString(1);
                team.abbreviation = cursor.getString(2);
                team.overTimeLength = Integer.parseInt(cursor.getString(3));
                teams.add(team);
            } while (cursor.moveToNext());
        }

        return teams;
    }


    /**
     * Add a team into the database.
     *
     * @param player a player POJO.
     */
    public void addPlayer (Player player) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYER_FIRST_NAME, player.firstName);
        values.put(PLAYER_LAST_NAME, player.lastName);
        values.put(PLAYER_TEAM_ID, player.team.id);

        database.insert(TABLE_PLAYERS, null, values);
        database.close();
    }

    public void removePlayer (Player player) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("PRAGMA foreign_keys=ON");
        database.execSQL("DELETE FROM " + TABLE_PLAYERS + " WHERE " + _ID + " = " + player.id);
        database.close();
    }

    public List<Player> getAllPlayers () {
        List<Player> players = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_PLAYERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Player player = new Player();
                player.id = Integer.parseInt(cursor.getString(0));
                player.firstName = cursor.getString(1);
                player.lastName = cursor.getString(2);
                player.team = getTeam(cursor.getLong(3));
                players.add(player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        return players;
    }

    public Player getPlayer (long playerId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PLAYERS + " WHERE "
                + _ID + " = " + playerId;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Player player = new Player();
        player.id = (c.getInt(c.getColumnIndex(_ID)));
        player.firstName = ((c.getString(c.getColumnIndex(PLAYER_FIRST_NAME))));
        player.lastName = ((c.getString(c.getColumnIndex(PLAYER_LAST_NAME))));
        player.team = getTeam((c.getInt(c.getColumnIndex(PLAYER_TEAM_ID))));

        c.close();
        db.close();
        return player;
    }

    public List<Player> getAllPlayersByTeam(long id) {
        List<Player> players = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PLAYERS + " WHERE " + PLAYER_TEAM_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Player player = new Player();
                player.id = Integer.parseInt(cursor.getString(0));
                player.firstName = cursor.getString(1);
                player.lastName = cursor.getString(2);
                player.team = getTeam(cursor.getLong(3));
                players.add(player);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return players;
    }

    /**
     * Add a game into the database.
     *
     * @param game a game POJO.
     */
    public long addGame (Game game) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(GAME_HOME_TEAM, game.home.id);
        values.put(GAME_AWAY_TEAM, game.away.id);

        values.put(GAME_CURRENT_HOME_PLAYER, game.homePlayer.id);
        values.put(GAME_CURRENT_AWAY_PLAYER, game.awayPlayer.id);

        values.put(GAME_HOME_FACE_OFFS_WON, game.homeFaceOffsWon);
        values.put(GAME_AWAY_FACE_OFFS_WON, game.awayFaceOffsWon);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
        String date = formatter.format(game.date);

        values.put(GAME_DATE, date);
        values.put(GAME_PERIOD, game.period);

        long id = database.insert(TABLE_GAMES, null, values);
        database.close();

        return id;
    }

    /**
     * Update the game.
     *
     * @param game a game POJO.
     */
    public void updateGame (Game game) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(GAME_HOME_TEAM, game.home.id);
        values.put(GAME_AWAY_TEAM, game.away.id);

        values.put(GAME_CURRENT_HOME_PLAYER, game.homePlayer.id);
        values.put(GAME_CURRENT_AWAY_PLAYER, game.awayPlayer.id);

        values.put(GAME_HOME_FACE_OFFS_WON, game.homeFaceOffsWon);
        values.put(GAME_AWAY_FACE_OFFS_WON, game.awayFaceOffsWon);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
        String date = formatter.format(game.date);

        values.put(GAME_DATE, date);
        values.put(GAME_PERIOD, game.period);

        database.update(TABLE_GAMES, values, _ID + " = " + game.id, null);
        database.close();
    }

    public List<Game> getAllGames () {
        List<Game> games = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_GAMES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Game game = new Game();
                game.id = Integer.parseInt(cursor.getString(0));
                game.home = getTeam(cursor.getLong(1));
                game.away = getTeam(cursor.getLong(2));
                game.homePlayer = getPlayer(cursor.getLong(3));
                game.awayPlayer = getPlayer(cursor.getLong(4));
                game.date = stringToDate(cursor.getString(5));
                game.period = (int)(cursor.getLong(6));
                game.homeFaceOffsWon = (int)(cursor.getLong(7));
                game.awayFaceOffsWon = (int)(cursor.getLong(8));

                games.add(game);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return games;
    }

    private Date stringToDate (String dateString) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
        Date gameDate = null;
        try {
            gameDate = df.parse(dateString);
        } catch (ParseException pe) {
            // Error
        }
        return gameDate;
    }

    public Game getGame (long id) {

        String selectQuery = "SELECT * FROM " + TABLE_GAMES + " WHERE " + _ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            Game game = new Game();
            if(cursor.moveToFirst()) {
                game.id = Integer.parseInt(cursor.getString(0));
                game.home = getTeam(cursor.getLong(1));
                game.away = getTeam(cursor.getLong(2));
                game.homePlayer = getPlayer(cursor.getLong(3));
                game.awayPlayer = getPlayer(cursor.getLong(4));
                game.date = stringToDate(cursor.getString(5));
                game.period = (int)(cursor.getLong(6));
                game.homeFaceOffsWon = (int)(cursor.getLong(7));
                game.awayFaceOffsWon = (int)(cursor.getLong(8));
            }
            return game;
        } finally {
            cursor.close();
            db.close();

        }

    }

    public long addShot (Shot shot) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SHOT_PLAYER_ID, shot.player.id);
        values.put(SHOT_GAME_ID, shot.game.id);
        values.put(SHOT_PERIOD, shot.game.period);

        if (shot.isGoal) {
            values.put(SHOT_MADE, 1);
        } else {
            values.put(SHOT_MADE, 0);
        }

        values.put(SHOT_LOCATION, shot.location);
        values.put(SHOT_TIME_ON_CLOCK, shot.timeOnClock);
        values.put(SHOT_OCCURRENCE, shot.occurrence);
        values.put(SHOT_TYPE, shot.type);
        values.put(SHOT_X, shot.x);
        values.put(SHOT_Y, shot.y);
        values.put(SHOT_PLAY_TYPE, shot.playType);

        long id = database.insert(TABLE_SHOTS, null, values);
        database.close();

        return id;
    }

    public Shot getShot (long id) {
        String selectQuery = "SELECT * FROM " + TABLE_SHOTS + " WHERE " + _ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            Shot shot = new Shot();
            if(cursor.moveToFirst()) {
                shot.id = Integer.parseInt(cursor.getString(0));
                shot.player = getPlayer(cursor.getLong(1));
                shot.game = getGame(cursor.getLong(2));
                shot.period = (int)cursor.getLong(3);

                if (cursor.getLong(4) == 1) {
                    shot.isGoal = true;
                } else {
                    shot.isGoal = false;
                }

                shot.location = (int)cursor.getLong(5);
                shot.timeOnClock = (int)cursor.getLong(6);
                shot.occurrence = cursor.getString(7);
                shot.type = cursor.getString(8);
                shot.notes = cursor.getString(9);

                shot.x = (int)cursor.getLong(10);
                shot.y = (int)cursor.getLong(11);

            }
            return shot;
        } finally {
            cursor.close();
            db.close();

        }

    }

    public long updateShot (Shot shot) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SHOT_PLAYER_ID, shot.player.id);
        values.put(SHOT_GAME_ID, shot.game.id);
        values.put(SHOT_PERIOD, shot.game.period);

        if (shot.isGoal) {
            values.put(SHOT_MADE, 1);
        } else {
            values.put(SHOT_MADE, 0);
        }

        values.put(SHOT_LOCATION, shot.location);
        values.put(SHOT_TIME_ON_CLOCK, shot.timeOnClock);
        values.put(SHOT_OCCURRENCE, shot.occurrence);
        values.put(SHOT_TYPE, shot.type);
        values.put(SHOT_X, shot.x);
        values.put(SHOT_Y, shot.y);
        values.put(SHOT_PLAY_TYPE, shot.playType);

        long id = database.update(TABLE_SHOTS, values, _ID + " = " + shot.id, null);
        database.close();
        return id;
    }

    public boolean deleteShot (long shotId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.delete(TABLE_SHOTS, _ID + "=" + shotId, null) > 0;
        db.close();
        return success;
    }


    public int getShotsAgianstPlayer (long playerId, long gameId) {
        String countQuery = "SELECT * FROM " + TABLE_SHOTS + " WHERE " + SHOT_MADE + " = '1' AND " + SHOT_PLAYER_ID + " = " + playerId + " AND " + SHOT_GAME_ID + " = " + gameId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public int getPlayerSaves (long playerId, long gameId) {
        String countQuery = "SELECT * FROM " + TABLE_SHOTS + " WHERE " + SHOT_MADE + " = '0' AND " + SHOT_PLAYER_ID + " = " + playerId + " AND " + SHOT_GAME_ID + " = " + gameId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public List<Shot> getShotsByPeriod (long period) {
        List<Shot> shots = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_SHOTS + " WHERE " + SHOT_PERIOD + " = " + period;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Shot shot = new Shot();
                shot.id = Integer.parseInt(cursor.getString(0));
                shot.player = getPlayer(cursor.getLong(1));
                shot.game = getGame(cursor.getLong(2));
                shot.period = (int)cursor.getLong(3);

                if (cursor.getLong(4) == 1) {
                    shot.isGoal = true;
                } else {
                    shot.isGoal = false;
                }

                shot.x = (int)cursor.getLong(10);
                shot.y = (int)cursor.getLong(11);


                shots.add(shot);
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();


        return shots;
    }

    public List<Shot> currentShotsForGame (Game game) {
        List<Shot> shots = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_SHOTS + " WHERE (" + SHOT_PERIOD + " = " + game.period + " AND " + SHOT_GAME_ID + " = " + game.id + ")";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Shot shot = new Shot();
                 shot.id = Integer.parseInt(cursor.getString(0));

                if (cursor.getLong(4) == 1) {
                    shot.isGoal = true;
                } else {
                    shot.isGoal = false;
                }

                shot.x = (int)cursor.getLong(10);
                shot.y = (int)cursor.getLong(11);

                shots.add(shot);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return shots;
    }

    public boolean removeGame(Game game) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.delete(TABLE_GAMES, _ID + "=" + game.id, null) > 0;
        db.close();
        return success;
    }

}
