package com.josh.weighttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.josh.weighttracker.database.DailyWeightDao;
import com.josh.weighttracker.database.GoalWeightDao;
import com.josh.weighttracker.database.WeightTrackerDatabase;
import com.josh.weighttracker.model.DailyWeight;
import com.josh.weighttracker.model.GoalWeight;
import com.josh.weighttracker.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class WeightActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final int PERMISSION_READ_STATE = 2;
    private final int LAUNCH_ADD_RECORD_ACTIVITY = 1;
    private final int LAUNCH_CHANGE_RECORD_ACTIVITY = 2;
    private final int LAUNCH_DELETE_RECORD_ACTIVITY = 3;
    private final int LAUNCH_CHANGE_TARGET_ACTIVITY = 4;
    private WeightTrackerDatabase mWeightTrackerDb;
    private DailyWeightDao mDailyWeightDao;
    private GoalWeightDao mGoalWeightDao;
    private User mUser;
    DailyWeight mNewDailyWeight;
    TableLayout mTableLayout;
    TextView mTargetWeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            this.getSupportActionBar().hide();
            setContentView(R.layout.activity_weight);
            mTableLayout = (TableLayout)findViewById(R.id.dailyWeightTable);
            mTargetWeight = (TextView)findViewById(R.id.goalWeightText);

            // get singleton instance of database
            mWeightTrackerDb = WeightTrackerDatabase.getInstance(getApplicationContext());
            mDailyWeightDao = mWeightTrackerDb.dailyWeightDao();

            // get User object from login screen
            Intent intent = getIntent();
            mUser = (User) getIntent().getSerializableExtra("user");

            // refresh the table and target weight textview
            refreshTable();
            refreshTargetWeight();

            // check for permissions, ask user for permissions if need be
            checkForAllPermissions();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void refreshTargetWeight() {
        try {
            mGoalWeightDao = mWeightTrackerDb.goalWeightDao();
            int count = mGoalWeightDao.countGoalEntries(mUser.getUsername());
            System.out.println("countGoalEntries():____________________________---- " + count);
            // if there is no goal weight, insert default goal weight of 150lbs
            if (count == 0) {
                GoalWeight defaultGoal = new GoalWeight(150.0, mUser.getUsername());
                mGoalWeightDao.insertGoalWeight(defaultGoal);
                return;
            }
            else if (count < 0 || count > 1) {
                System.out.println("Something went wrong, counting entries of GoalWeight DB" +
                        "returns number other than 0 or 1");
                return;
            }

            count = mGoalWeightDao.countGoalEntries(mUser.getUsername());
            System.out.println("countGoalEntries() NOW:____________________________---- " + count);

            GoalWeight currentGoal = mGoalWeightDao.getSingleGoalWeight(mUser.getUsername());
            mTargetWeight.setText(currentGoal.getGoal() + " lbs");

            System.out.println(currentGoal.getGoal());

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void refreshTable() {
        try {
            // remove all but the header from the TableLayout
            cleanTable(mTableLayout);
            // get all DailyWeight records of this user
            List<DailyWeight> userDailyWeights = mDailyWeightDao.getDailyWeightsOfUser(mUser.getUsername());
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            TableRow header = (TableRow) findViewById(R.id.headerRow);
            TextView headerDate = (TextView) findViewById(R.id.headerDate);
            TextView headerWeight = (TextView) findViewById(R.id.headerWeight);

            // layout parameters for row:
            TableLayout.LayoutParams layoutParamsTable = (TableLayout.LayoutParams) header.getLayoutParams();
            // layout parameters for textViews:
            TableRow.LayoutParams layoutParamsRow = (TableRow.LayoutParams) headerDate.getLayoutParams();

            for (int i = 0; i < userDailyWeights.size(); i++) {
                TableRow row = new TableRow(this);
                TextView dateTextView = new TextView(this);
                TextView weightTextView = new TextView(this);

                // activate the layout parameters
                row.setLayoutParams(layoutParamsTable);
                dateTextView.setLayoutParams(layoutParamsRow);
                weightTextView.setLayoutParams(layoutParamsRow);
                // set additional view properties
                dateTextView.setWidth(0);
                dateTextView.setGravity(Gravity.CENTER);
                dateTextView.setPadding(20, 20, 20, 20);
                weightTextView.setWidth(0);
                weightTextView.setGravity(Gravity.CENTER);
                weightTextView.setPadding(20, 20, 20, 20);

                // set the value of the date TextView
                dateTextView.setText(formatter.format(userDailyWeights.get(i).getDate()));
                // set the value of the weight TextView
                weightTextView.setText(Double.toString(userDailyWeights.get(i).getWeight()));

                // add the 2 TextViews to the current row
                row.addView(dateTextView);
                row.addView(weightTextView);

                // add row to TableLayout
                mTableLayout.addView(row);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Remove all but the header from the TableLayout
    private void cleanTable(TableLayout table) {
        try {
            int childCount = table.getChildCount();

            // Remove all rows except the first one
            if (childCount > 1) {
                table.removeViews(1, childCount - 1);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Check if user has reached target weight
    private void reachedGoalCheck() {
        try {
            // get all DailyWeight records of this user
            List<DailyWeight> userDailyWeights = mDailyWeightDao
                    .getDailyWeightsOfUser(mUser.getUsername());
            // get most recent weight. Latest date in the list
            double currentWeight = userDailyWeights.get(userDailyWeights.size() - 1).getWeight();
            // get target weight for this user
            double targetWeight = mGoalWeightDao.getSingleGoalWeight(mUser.getUsername()).getGoal();
            System.out.println("THIS PART WORKED, THANK GOD..................................................");
            // if current weight is lower than goal
            if (currentWeight <= targetWeight) {
                sendTextToUser();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendTextToUser() {
        try {
            // if SEND_SMS and READ_PHONE_STATE permissions granted, send the text
            checkForAllPermissions();
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Sending a text congragulating user...");
                SmsManager smsManager = SmsManager.getDefault();
                TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
                String phoneNum = telephonyManager.getLine1Number();
                if (phoneNum != null) {
                    smsManager.sendTextMessage(phoneNum, null,
                            "Congratulations, you reached your target weight!",
                            null, null);
                }
            }
            else {
                System.out.println("A necessary permission was not granted for text notification");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void checkForPhoneStatePermissions() {
        try {
            // if we do not have this permission, ask the user
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Asking for READ_PHONE_STATE permission...");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_READ_STATE);
            }
            // else the app already has the permission
            else {
                System.out.println("App already has READ_PHONE_STATE permission");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void checkForAllPermissions() {
        try {
            // if we do not have one or both of necessary permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED)
            {
                System.out.println("Asking for READ_PHONE_STATE permission...");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.SEND_SMS},
                        PERMISSION_READ_STATE);
            }
            // else the app already has the permission
            else {
                System.out.println("App already has READ_PHONE_STATE permission");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Callback for add record button
    public void addRecordOnClick(View view) {
        try {
            // call AddRecordActivity to get new date and weight input from user
            Intent intent = new Intent(this, AddRecordActivity.class);
            startActivityForResult(intent, LAUNCH_ADD_RECORD_ACTIVITY);
//            intent.putExtra("user", mUser);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Callback for delete record button
    public void deleteRecordOnClick(View view) {
        try {
            // call DeleteRecordActivity
            Intent intent = new Intent(this, DeleteRecordActivity.class);
            intent.putExtra("user", mUser);
            startActivityForResult(intent, LAUNCH_DELETE_RECORD_ACTIVITY);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Callback for edit record button
    public void editRecordOnClick(View view) {
            try {
                // call ChangeRecordActivity
                Intent intent = new Intent(this, ChangeRecordActivity.class);
                intent.putExtra("user", mUser);
                startActivityForResult(intent, LAUNCH_CHANGE_RECORD_ACTIVITY);

            }catch (Exception e) {
                e.printStackTrace();
            }
    }


    // Callback for edit target weight button
    public void editTargetOnClick(View view) {
        try {
            // call ChangeRecordActivity
            Intent intent = new Intent(this, ChangeTargetActivity.class);
            intent.putExtra("user", mUser);
            startActivityForResult(intent, LAUNCH_CHANGE_TARGET_ACTIVITY);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Check for SEND_SMS permission and ask user for permission if not granted
     */
    private void checkForSmsPermission() {
        // if app doesn't have SEND_SMS permission yet
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {

            System.out.println("System didn't have permission for texting yet. " +
                    "Asking permission now...");

            // ask user for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        // else permission already granted
        else {
            System.out.println("SEND_SMS permission already granted");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("resultCode: " + resultCode + "... requestCode: " + requestCode);

        if(resultCode == Activity.RESULT_OK) {

            // for adding a record
            if (requestCode == LAUNCH_ADD_RECORD_ACTIVITY) {
                mNewDailyWeight = (DailyWeight) data.getSerializableExtra("newDailyWeight");
                mNewDailyWeight.setUsername(mUser.getUsername());

                mDailyWeightDao.insertDailyWeight(mNewDailyWeight);
                refreshTable();
                reachedGoalCheck();

                // show toast
                Toast toast = Toast.makeText(WeightActivity.this, "Weight record successfully added",
                        Toast.LENGTH_LONG);
                toast.getView().setBackgroundColor(0xFFCC99FF);
                toast.show();
            }

            // for changing a record
            if (requestCode == LAUNCH_CHANGE_RECORD_ACTIVITY) {
                System.out.println("result code was LAUNCH_CHANGE_RECORD_ACTIVITY");
                refreshTable();
                reachedGoalCheck();

                // show toast
                Toast toast = Toast.makeText(WeightActivity.this, "Weight record successfully changed",
                        Toast.LENGTH_LONG);
                toast.getView().setBackgroundColor(0xFFCC99FF);
                toast.show();
            }

            // for deleting a record
            if (requestCode == LAUNCH_DELETE_RECORD_ACTIVITY) {
                refreshTable();

                // show toast
                Toast toast = Toast.makeText(WeightActivity.this, "Weight record successfully deleted",
                        Toast.LENGTH_LONG);
                toast.getView().setBackgroundColor(0xFFCC99FF);
                toast.show();
            }

            // for changing target weight
            if (requestCode == LAUNCH_CHANGE_TARGET_ACTIVITY) {
                refreshTable();
                refreshTargetWeight();
                reachedGoalCheck();

                // show toast
                Toast toast = Toast.makeText(WeightActivity.this, "Target weight successfully updated",
                        Toast.LENGTH_LONG);
                toast.getView().setBackgroundColor(0xFFCC99FF);
                toast.show();
            }
        }
    }

}