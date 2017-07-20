package com.stats.shottracker.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TempShot implements Parcelable {

    public int playerId;
    public int gameId;

    public int x;
    public int y;

    public boolean isGoal;

    public int period;

    public TempShot () {}

    protected TempShot(Parcel in) {
        playerId = in.readInt();
        gameId = in.readInt();
        x = in.readInt();
        y = in.readInt();
        isGoal = in.readByte() != 0;
        period = in.readInt();
    }

    public static final Creator<TempShot> CREATOR = new Creator<TempShot>() {
        @Override
        public TempShot createFromParcel(Parcel in) {
            return new TempShot(in);
        }

        @Override
        public TempShot[] newArray(int size) {
            return new TempShot[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(playerId);
        dest.writeInt(gameId);
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeByte((byte) (isGoal ? 1 : 0));
        dest.writeInt(period);
    }

    @Override
    public String toString() {
        return "TempShot{" +
                "playerId=" + playerId +
                ", gameId=" + gameId +
                ", x=" + x +
                ", y=" + y +
                ", isGoal=" + isGoal +
                ", period=" + period +
                '}';
    }
}
