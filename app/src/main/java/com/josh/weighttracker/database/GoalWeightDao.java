package com.josh.weighttracker.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.josh.weighttracker.model.DailyWeight;
import com.josh.weighttracker.model.GoalWeight;

import java.util.List;

@Dao
public interface GoalWeightDao {

    /**
     * Get list of all this user's goal weights
     * There should only be one record
     */
    @Query("SELECT * FROM goalWeight WHERE username = :username ORDER BY id")
    public List<GoalWeight> getAllUserGoalWeights(String username);

    /**
     * Read single record in this table
     */
    @Query("SELECT * FROM goalWeight WHERE username = :username AND id = 1")
    public GoalWeight getSingleGoalWeight(String username);

    /**
     * Update the single record from current user
     */
    @Query("UPDATE goalWeight SET goal = :newGoal WHERE username = :username AND id = 1")
    public void updateGoalWeight(double newGoal, String username);

    /**
     * Count number of records in this table
     */
    @Query("SELECT count(*) FROM goalWeight WHERE username = :username")
    public int countGoalEntries(String username);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertGoalWeight(GoalWeight goalWeight);
}
