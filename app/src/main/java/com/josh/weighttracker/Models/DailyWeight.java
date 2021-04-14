package com.josh.weighttracker.Models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "dailyWeights", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"date"}, unique = true)})
public class DailyWeight {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date date;

    private double weight;

    private int userId;
}
