package com.josh.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.josh.weighttracker.database.UserDao;
import com.josh.weighttracker.database.WeightTrackerDatabase;
import com.josh.weighttracker.model.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private WeightTrackerDatabase mWeightTrackerDb;
    private UserDao mUserDao;
    private User mUser;
    private TextView mFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // get singleton instance of database
        mWeightTrackerDb = WeightTrackerDatabase.getInstance(getApplicationContext());
        mUserDao = mWeightTrackerDb.userDao();
        mFeedback = (TextView) findViewById(R.id.feedbackTextView);
    }


    // login button callback
    public void onLoginClick(View view) {
        try {
            // get user input from EditText fields
            String username = ((EditText) findViewById(R.id.editTextUsername)).getText().toString();
            String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();

            // search SQLite database for matching username and password
            boolean isAuthenticated = login(username, password);
            if (isAuthenticated) {
                mUser = new User(username, password);
                changeToWeightActivity();
                mFeedback.setText(R.string.login_textView_2);
                mFeedback.setTextColor(Color.parseColor("#767676"));
            } else {
                mFeedback.setText(R.string.login_failed);
                mFeedback.setTextColor(Color.RED);
            }
        }catch(Exception e) {
            String error = "";
            for(StackTraceElement elem: e.getStackTrace()) {
                error += elem.toString();
            }
            Log.e("LoginActivity", error);
        }
    }


    // callback for account creation button
    public void onCreateAccountClick(View view) {

        // get user input from EditText fields
        String username = ((EditText) findViewById(R.id.editTextUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();

        try {
            // if user input is not blank
            if (!username.isEmpty() && !password.isEmpty()) {
                // get all users from database
                List<User> userList = mUserDao.getUsers();
                boolean found = false;
                // see if username is already taken
                if (userList.size() > 0) {
                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getUsername().equals(username)) {
                            found = true;
                        }
                    }
                }
                // if username is not in the database already
                if (!found) {
                    mUserDao.insertUser(new User(username, password));
                    mFeedback.setText(R.string.user_create_success);
                    mFeedback.setTextColor(Color.GREEN);
                }
                // else if username is already taken, notify user
                else {
                    mFeedback.setText(R.string.username_found);
                    mFeedback.setTextColor(Color.RED);
                }
            }
            // else if user input is blank notify user
            else {
                mFeedback.setText(R.string.user_create_fail);
                mFeedback.setTextColor(Color.RED);
            }

        }catch(Exception e) {
            String error = "";
            for(StackTraceElement elem: e.getStackTrace()) {
                error += elem.toString();
            }
            Log.e("LoginActivity - ", error);
        }

    }

    // query users table in SQLite database for matching username and password
    private boolean login(String username, String password) {
        List<User> userList = mUserDao.getUsers();
        System.out.println("size of userList: " + userList.size());
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equals(username) &&
                    userList.get(i).getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }


    // Change to WeightActivity
    public void changeToWeightActivity() {
        Intent intent = new Intent(this, WeightActivity.class);
        intent.putExtra("user", mUser);
        startActivity(intent);
    }
}