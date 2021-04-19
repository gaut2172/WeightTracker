package com.josh.weighttracker.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

/**
 * Daily weight records table class
 * Room Database class for SQLite
 */
@Entity(tableName = "dailyWeights",
        foreignKeys = @ForeignKey(entity = User.class,
            parentColumns = "username",
            childColumns = "username",
            onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"date", "username"}, unique = true)})
public class DailyWeight implements Serializable {

    // Columns AKA data members
    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date date;

    private double weight;

    private String username;

    // Constructors
    public DailyWeight() {}

    public DailyWeight(Date date, double weight, String username) {
        this.date = date;
        this.weight = weight;
        this.username = username;
    }


    // Getters
    public int getId() { return id; }

    public Date getDate() { return date; }

    public double getWeight() { return weight; }

    public String getUsername() { return username; }

    // Setters
    public void setId(int id) { this.id = id; }

    public void setDate(Date date) { this.date = date; }

    public void setWeight(double weight) { this.weight = weight; }

    public void setUsername(String username) { this.username = username; }
}
