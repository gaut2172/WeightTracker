package com.josh.weighttracker.Models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "dailyWeights", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"date"}, unique = true)})
public class DailyWeight implements Serializable {

    public DailyWeight() {}

    public DailyWeight(Date date, double weight, String username) {
        this.date = date;
        this.weight = weight;
        this.username = username;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date date;

    private double weight;

    private String username;

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
