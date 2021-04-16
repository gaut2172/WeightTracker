package com.josh.weighttracker.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.josh.weighttracker.model.DailyWeight;

import java.util.Date;
import java.util.List;

@Dao
public interface DailyWeightDao {

    @Query("SELECT * FROM dailyWeights ORDER BY date")
    public List<DailyWeight> getAllDailyWeights();

    @Query("SELECT * FROM dailyWeights WHERE username = :input ORDER BY date ASC")
    public List<DailyWeight> getDailyWeightsOfUser(String input);

    /**
     * Change a records date and weight
     * By username
     */
    @Query("UPDATE dailyWeights SET date = :newDate, weight = :newWeight WHERE username = :username")
    void updateDailyWeight(String username, Date newDate, double newWeight);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertDailyWeight(DailyWeight dailyWeight);

    @Update
    public void updateDailyWeight(DailyWeight dailyWeight);

    @Delete
    public void deleteDailyWeight(DailyWeight dailyWeight);

}
