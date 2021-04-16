package com.josh.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.josh.weighttracker.database.DailyWeightDao;
import com.josh.weighttracker.database.WeightTrackerDatabase;
import com.josh.weighttracker.model.DailyWeight;
import com.josh.weighttracker.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class WeightActivity extends AppCompatActivity {


    int LAUNCH_ADD_RECORD_ACTIVITY = 1;
    int LAUNCH_CHANGE_RECORD_ACTIVITY = 2;
    int LAUNCH_DELETE_RECORD_ACTIVITY = 3;
    private WeightTrackerDatabase mWeightTrackerDb;
    private DailyWeightDao mDailyWeightDao;
    private User mUser;
    DailyWeight mNewDailyWeight;
    TableLayout mTableLayout;

//    String mNewWeightRecord_string;
//    String mNewDate_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            this.getSupportActionBar().hide();
            setContentView(R.layout.activity_weight);
            mTableLayout = (TableLayout)findViewById(R.id.dailyWeightTable);

            // get singleton instance of database
            mWeightTrackerDb = WeightTrackerDatabase.getInstance(getApplicationContext());
            mDailyWeightDao = mWeightTrackerDb.dailyWeightDao();

            // get User object from login screen
            Intent intent = getIntent();
            mUser = (User) getIntent().getSerializableExtra("user");
            System.out.println("HERE IS THE USERs ID: " + mUser.getId());

//            // get newDailyWeight if last activity was AddRecordActivity
//            mNewDailyWeight = (DailyWeight) getIntent().getSerializableExtra("newDailyWeight");
//            if (mNewDailyWeight != null) {
//                mDailyWeightDao.insertDailyWeight(mNewDailyWeight);
//            }

            updateTable();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateTable() {
        cleanTable(mTableLayout);
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
    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
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
                updateTable();

                // show toast
                Toast toast = Toast.makeText(WeightActivity.this, "Weight record successfully added",
                        Toast.LENGTH_LONG);
                toast.getView().setBackgroundColor(0xFFCC99FF);
                toast.show();
            }

            // for changing a record
            if (requestCode == LAUNCH_CHANGE_RECORD_ACTIVITY) {
                System.out.println("result code was LAUNCH_CHANGE_RECORD_ACTIVITY");
                updateTable();

                // show toast
                Toast toast = Toast.makeText(WeightActivity.this, "Weight record successfully changed",
                        Toast.LENGTH_LONG);
                toast.getView().setBackgroundColor(0xFFCC99FF);
                toast.show();
            }

            // for deleting a record
            if (requestCode == LAUNCH_DELETE_RECORD_ACTIVITY) {
                updateTable();

                // show toast
                Toast toast = Toast.makeText(WeightActivity.this, "Weight record successfully deleted",
                        Toast.LENGTH_LONG);
                toast.getView().setBackgroundColor(0xFFCC99FF);
                toast.show();
            }
        }
//        Toast toast = Toast.makeText(WeightActivity.this,
//                "Weight record successfully added",
//                Toast.LENGTH_LONG);
//        View view = toast.getView();
//        view.setBackgroundColor(0xFFCC99FF);
//        view.getBackground().setAlpha(255);
//        toast.show();

    }

}