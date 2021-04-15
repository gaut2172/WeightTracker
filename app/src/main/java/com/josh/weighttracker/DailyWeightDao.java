package com.josh.weighttracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.josh.weighttracker.Models.DailyWeight;
import com.josh.weighttracker.Models.User;

import java.util.List;

@Dao
public interface DailyWeightDao {

    @Query("SELECT * FROM dailyWeights ORDER BY date")
    public List<DailyWeight> getDailyWeights();

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertDailyWeight(DailyWeight dailyWeight);

    @Update
    public void updateDailyWeight(DailyWeight dailyWeight);

    @Delete
    public void deleteDailyWeight(DailyWeight dailyWeight);

}
