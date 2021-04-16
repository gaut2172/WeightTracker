package com.josh.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

        for (int i = 0; i < userDailyWeights.size(); i++) {
            TableRow row = new TableRow(this);
            TextView dateTextView = new TextView(this);
            TextView weightTextView = new TextView(this);
            // start setting the layout parameters
//            row.setLayoutParams(new TableRow.LayoutParams(
//                    TableRow.LayoutParams.MATCH_PARENT,
//                    TableRow.LayoutParams.MATCH_PARENT));
//            dateTextView.setLayoutParams(new TableLayout.LayoutParams(
//                    TableRow.LayoutParams.MATCH_PARENT,
//                    TableRow.LayoutParams.MATCH_PARENT, 0.5f));
//            weightTextView.setLayoutParams(new TableLayout.LayoutParams(
//                    TableRow.LayoutParams.MATCH_PARENT,
//                    TableRow.LayoutParams.MATCH_PARENT, 0.5f));

            // this will be for the row:
            TableLayout.LayoutParams layoutParamsTable = (TableLayout.LayoutParams) header.getLayoutParams();
            // this will be for the textView:
            TableRow.LayoutParams layoutParamsRow = (TableRow.LayoutParams) headerDate.getLayoutParams();
//            layoutParamsRow.width = 0;
//            layoutParamsRow.weight = 0.5f;
//            layoutParamsRow.gravity = Gravity.CENTER;


//            TableRow.LayoutParams layoutParamsDate = (TableRow.LayoutParams) header.getLayoutParams();
//            TableRow.LayoutParams layoutParamsWeight = (TableRow.LayoutParams) weightTextView.getLayoutParams();
//            layoutParamsDate.width = 0;
//            layoutParamsDate.weight = (float) .5;
//            layoutParamsDate.gravity = Gravity.CENTER;
            dateTextView.setWidth(0);
            dateTextView.setGravity(Gravity.CENTER);
            dateTextView.setPadding(20, 20, 20, 20);
            weightTextView.setWidth(0);
            weightTextView.setGravity(Gravity.CENTER);
            weightTextView.setPadding(20, 20, 20, 20);

            // activate the layout parameters
            row.setLayoutParams(layoutParamsTable);
            dateTextView.setLayoutParams(layoutParamsRow);
            weightTextView.setLayoutParams(layoutParamsRow);

            System.out.println("THE FOR LOOP WORKS......");

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
//            mNewDate_string = null;
//            mNewWeightRecord_string = null;

            // call AddRecordActivity to get new date and weight input from user
            Intent intent = new Intent(this, AddRecordActivity.class);
            startActivityForResult(intent, LAUNCH_ADD_RECORD_ACTIVITY);
//            intent.putExtra("user", mUser);
        }catch (Exception e) {
            e.printStackTrace();
        }



//        DailyWeight newWeightRecord = changeToAddScreen();

//        if (newWeightRecord != null) {
//            // FIXME: add the new record to the database
//            //System.out.println(formatter.format(date));
//
//
//            mDailyWeightDao.insertDailyWeight(newWeightRecord);
//
//            List<DailyWeight> list = mDailyWeightDao.getDailyWeights();
//            System.out.println("number of dailyweights in table: " + list.size());
//            for (int i = 0; i < list.size(); i++) {
//                System.out.println(list.get(i).getDate() + " - " + list.get(i).getWeight());
//            }
//        }
    }

    // Callback for delete record button
    public void deleteRecordOnClick(View view) {

    }

    // Callback for edit record button
    public void editRecordOnClick(View view) {

    }


//    // change to add record screen
//    public DailyWeight makeDailyWeight() {
//        DailyWeight newWeight = null;
//
//        try {
//            if (mNewWeightRecord_string != null && mNewDate_string != null) {
//                // parse strings to date and double
//                DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
//                System.out.println("mNewDate_string ... " + mNewDate_string);
//                Date date = formatter.parse(mNewDate_string);
//                double weight = Double.parseDouble(mNewWeightRecord_string);
//
//                // create new DailyWeight object using date, weight, and current user's ID
//                newWeight = new DailyWeight(date, weight, mUser.getUsername());
//                if (newWeight == null) {
//                    System.out.println("newWeight WAS NULL !!!!! WTF!!!!");
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return newWeight;
//    }


    public void addWeightRecordToDb(DailyWeight newWeightRecord) {
        System.out.println("newDailyWeight.userId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + newWeightRecord.getUsername());
        System.out.println("newDailyWeight.date >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + newWeightRecord.getDate());
        System.out.println("newDailyWeight.weight >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + newWeightRecord.getWeight());
        //mDailyWeightDao.insertDailyWeight(new DailyWeight(newWeightRecord.getDate(), newWeightRecord.getWeight(), newWeightRecord.getUserId()));
        mDailyWeightDao.insertDailyWeight(newWeightRecord);

        List<DailyWeight> list = mDailyWeightDao.getAllDailyWeights();
        System.out.println("number of dailyweights in table: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getDate() + " - " + list.get(i).getWeight());
        }
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == LAUNCH_ADD_RECORD_ACTIVITY) {
                mNewDailyWeight = (DailyWeight) data.getSerializableExtra("newDailyWeight");
                System.out.println("------><<>><><><>** mNewDailyWeight: " + mNewDailyWeight.getDate());
//                System.out.println("mNewWeightRecord_string... " + mNewWeightRecord_string);
//                mNewDate_string = data.getStringExtra("newDate");
//                DailyWeight newDailyWeight = makeDailyWeight();
                mNewDailyWeight.setUsername(mUser.getUsername());
                System.out.println("------><<>><><><>** mNewDailyWeight.USERid()** = " + mNewDailyWeight.getUsername());

                addWeightRecordToDb(mNewDailyWeight);
                updateTable();
            }
            //FIXME: add other activities
        }
        // else if resultCode == Activity.RESULT_CANCELED...
        else {
            // If there's no result, what to do
        }

    }

}