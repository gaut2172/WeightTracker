package com.josh.weighttracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.josh.weighttracker.Models.DailyWeight;
import com.josh.weighttracker.Models.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeightActivity extends AppCompatActivity {


    int LAUNCH_ADD_RECORD_ACTIVITY = 1;
    private WeightTrackerDatabase mWeightTrackerDb;
    private User mUser;
    private DailyWeightDao mDailyWeightDao;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    TableLayout tableLayout;
    TableRow tableRow;
    String mNewWeightRecord_string;
    String mNewDate_string;
    DailyWeight mNewDailyWeight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            this.getSupportActionBar().hide();
            setContentView(R.layout.activity_weight);
            tableLayout = (TableLayout)findViewById(R.id.dailyWeightTable);
            updateTable();

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

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateTable() {

    }


    // Callback for add record button
    public void addRecordOnClick(View view) {

        try {
            mNewDate_string = null;
            mNewWeightRecord_string = null;

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


    // change to add record screen
    public DailyWeight makeDailyWeight() {
        DailyWeight newWeight = null;

        try {
            if (mNewWeightRecord_string != null && mNewDate_string != null) {
                // parse strings to date and double
                DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
                System.out.println("mNewDate_string ... " + mNewDate_string);
                Date date = formatter.parse(mNewDate_string);
                double weight = Double.parseDouble(mNewWeightRecord_string);

                // create new DailyWeight object using date, weight, and current user's ID
                newWeight = new DailyWeight(date, weight, mUser.getId());
                if (newWeight == null) {
                    System.out.println("newWeight WAS NULL !!!!! WTF!!!!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newWeight;
    }


    public void addWeightRecordToDb(DailyWeight newWeightRecord) {
        System.out.println("newDailyWeight.userId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + newWeightRecord.getUserId());
        System.out.println("newDailyWeight.date >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + newWeightRecord.getDate());
        System.out.println("newDailyWeight.weight >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + newWeightRecord.getWeight());
        //mDailyWeightDao.insertDailyWeight(new DailyWeight(newWeightRecord.getDate(), newWeightRecord.getWeight(), newWeightRecord.getUserId()));
        mDailyWeightDao.insertDailyWeight(newWeightRecord);

//        List<DailyWeight> list = mDailyWeightDao.getDailyWeights();
//        System.out.println("number of dailyweights in table: " + list.size());
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i).getDate() + " - " + list.get(i).getWeight());
//        }
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
                mNewDailyWeight.setUserId(mUser.getId());
                System.out.println("------><<>><><><>** mNewDailyWeight.USERid()** = " + mNewDailyWeight.getUserId());

                addWeightRecordToDb(mNewDailyWeight);
            }
            //FIXME: add other activities
        }
        // else if resultCode == Activity.RESULT_CANCELED...
        else {
            // If there's no result, what to do
        }

    }

}