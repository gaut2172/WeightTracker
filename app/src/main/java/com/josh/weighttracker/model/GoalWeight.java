package com.josh.weighttracker.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * GoalWeight table
 * Should only have one record per user at all times
 */
@Entity(tableName = "goalWeight",
        foreignKeys = @ForeignKey(entity = User.class,
            parentColumns = "username",
            childColumns = "username",
            onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"username"}, unique = true)})
public class GoalWeight {

    public GoalWeight() {}

    public GoalWeight(double goal, String username) {;
        this.id = 1;
        this.username = username;
        this.goal = goal;
    }

    @PrimaryKey(autoGenerate = false)
    private int id;

    private String username;

    private double goal;

    // Getters
    public int getId() { return id; }

    public String getUsername() { return username; }

    public double getGoal() { return goal; }

    // Setters
    public void setId(int id) { this.id = 1; }

    public void setUsername(String username) { this.username = username; }

    public void setGoal(double goal) { this.goal = goal; }
}
