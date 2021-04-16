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

    @Query("SELECT * FROM dailyWeights WHERE username = :username AND date = :date LIMIT 1")
    public DailyWeight getRecordWithDate(String username, Date date);

    /**
     * Change a records date and weight
     * By username
     */
    @Query("UPDATE dailyWeights SET weight = :newWeight WHERE username = :username AND date = :date")
    void updateDailyWeight(String username, Date date, double newWeight);

    /**
     * Delete record by date
     */
    @Query("DELETE FROM dailyWeights WHERE date = :date")
    void deleteDailyWeight(Date date);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertDailyWeight(DailyWeight dailyWeight);

    @Update
    public void updateDailyWeight(DailyWeight dailyWeight);

    @Delete
    public void deleteDailyWeight(DailyWeight dailyWeight);

}
