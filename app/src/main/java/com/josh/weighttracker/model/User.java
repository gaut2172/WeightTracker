package com.josh.weighttracker.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * User table class
 * Room Database class for SQLite
 */
@Entity(tableName = "users",
        indices = {@Index(value = {"username"}, unique = true)})
public class User implements Serializable {

    // Columns AKA data members
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;

    private String password;

    // Constructors
    public User() {
        username = null;
        password = null;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}