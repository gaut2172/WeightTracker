package com.josh.weighttracker.Models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users",
        indices = {@Index(value = {"username"}, unique = true)})
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;

    private String password;

    public User() {
        username = null;
        password = null;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

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