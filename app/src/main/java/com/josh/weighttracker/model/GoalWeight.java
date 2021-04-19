package com.josh.weighttracker.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * GoalWeight table class
 * Room Database class for SQLite
 * Should only have one record per user at all times
 */
@Entity(tableName = "goalWeight",
        foreignKeys = @ForeignKey(entity = User.class,
            parentColumns = "username",
            childColumns = "username",
            onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"username"}, unique = true)})
public class GoalWeight {

    // Columns AKA data members
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;

    private double goal;

    // Constructors
    public GoalWeight() {}

    public GoalWeight(double goal, String username) {;
        this.username = username;
        this.goal = goal;
    }

    // Getters
    public int getId() { return id; }

    public String getUsername() { return username; }

    public double getGoal() { return goal; }

    // Setters
    public void setId(int id) { this.id = id; }

    public void setUsername(String username) { this.username = username; }

    public void setGoal(double goal) { this.goal = goal; }
}
