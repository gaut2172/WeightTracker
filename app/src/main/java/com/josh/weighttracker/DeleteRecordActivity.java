package com.josh.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.josh.weighttracker.database.DailyWeightDao;
import com.josh.weighttracker.database.WeightTrackerDatabase;
import com.josh.weighttracker.model.DailyWeight;
import com.josh.weighttracker.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DeleteRecordActivity extends AppCompatActivity {

    private WeightTrackerDatabase mWeightTrackerDb;
    private DailyWeightDao mDailyWeightDao;
    private User mUser;
    private EditText mDate_editText;
    private Button mDeleteButton;
    private Button mConfirmButton;
    TextView mPrompt;
    Date mDate;
    DateFormat mFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            this.getSupportActionBar().hide();
            setContentView(R.layout.activity_delete_record);

            // get singleton instance of database
            mWeightTrackerDb = WeightTrackerDatabase.getInstance(getApplicationContext());
            mDailyWeightDao = mWeightTrackerDb.dailyWeightDao();

            // get views from layout file
            mDate_editText = (EditText) this.findViewById(R.id.editTextDate);
            mPrompt = (TextView) this.findViewById(R.id.change_record_prompt);
            mDeleteButton = (Button) this.findViewById(R.id.deleteRecord_button);
            mConfirmButton = (Button) this.findViewById(R.id.confirmDelete_button);

            // get current user from intent
            Intent intent = getIntent();
            mUser = (User) intent.getSerializableExtra("user");

            // set formatter
            mFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Callback for delete button
     * Finds record in DailyWeight table that matches the specified date
     * Asks user for to confirm they want to delete the record
     */
    public void deleteButtonOnClick(View view) {
        try {
            // hide keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mDeleteButton.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

            // get date input and parse
            String date_string = mDate_editText.getText().toString();
            mDate = mFormatter.parse(date_string);

            // query the database for the date
            DailyWeight existingRecord = mDailyWeightDao.getRecordWithDate(mUser.getUsername(), mDate);

            // if date not found
            if (existingRecord == null) {
                mPrompt.setText("DATE NOT FOUND IN DATABASE");
                mPrompt.setTextColor(Color.RED);
                return;
            }

            // change widget properties so user can confirm the deletion
            mPrompt.setText("DO YOU REALLY WANT TO DELETE THE RECORD?");
            mPrompt.setTextColor(Color.RED);
            mDeleteButton.setVisibility(View.INVISIBLE);
            mConfirmButton.setVisibility(View.VISIBLE);
            // lock the date so user can't change it
            mDate_editText.setFocusable(false);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Callback for confirm button
     * Performs delete query on DailyWeight table
     * Deletes one record matching the user's date input
     */
    public void confirmButtonOnClick(View view) {
        if (mDate == null) {
            mPrompt.setText("SOMETHING WENT WRONG");
            mPrompt.setTextColor(Color.RED);
            return;
        }

        List<DailyWeight> userDailyWeights = mDailyWeightDao
                .getDailyWeightsOfUser(mUser.getUsername());


        // delete record from database
        mDailyWeightDao.deleteDailyWeight(mUser.getUsername(), mDate);

        // did we just delete the only record?
        boolean onlyRecordDeleted = false;
        if (userDailyWeights.size() == 0) {
            onlyRecordDeleted = true;
        }

        // end this activity and return to calling activity
        Intent returnableIntent = getIntent();
        returnableIntent.putExtra("onlyRecordDeleted", onlyRecordDeleted);
        setResult(Activity.RESULT_OK, returnableIntent);
        System.out.println("confirmButtonOnClick success...... what could be wrong");
        finish();
    }


    /**
     * Callback for cancel button
     * Stops this activity and returns to calling activity
     */
    public void cancelButtonClick(View view) {
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}