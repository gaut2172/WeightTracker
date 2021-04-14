package com.josh.weighttracker.Models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.josh.weighttracker.DailyWeightDao;
import com.josh.weighttracker.UserDao;

@Database(entities = {User.class}, version = 1)
//@Database(entities = {User.class, DailyWeights.class, GoalWeight.class}, version = 1)
public abstract class WeightTrackerDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "weightTracker.db";

    private static WeightTrackerDatabase mWeightTrackerDatabase;

    // Singleton pattern
    public static WeightTrackerDatabase getInstance(Context context) {
        if (mWeightTrackerDatabase == null) {
            mWeightTrackerDatabase = Room.databaseBuilder(context, WeightTrackerDatabase.class,
                    DATABASE_NAME).allowMainThreadQueries().build();
        }
        return mWeightTrackerDatabase;
    }

    public abstract UserDao userDao();

    public abstract DailyWeightDao dailyWeightDao();
//    public abstract DailyWeightsDao dailyWeightsDao();
//    public abstract GoalWeightDao goalWeightDao();
}
