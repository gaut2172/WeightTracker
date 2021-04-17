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
import com.josh.weighttracker.helpers.ParseNumbers;
import com.josh.weighttracker.model.DailyWeight;
import com.josh.weighttracker.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChangeRecordActivity extends AppCompatActivity {

    private WeightTrackerDatabase mWeightTrackerDb;
    private DailyWeightDao mDailyWeightDao;
    private User mUser;
    private EditText mDate_editText;
    private EditText mWeight_editText;
    private Button mSaveButton;
    private Button mChangeButton;
    private Button mCancelButton;
    TextView mPrompt;
    Date mDate;
    DateFormat mFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            this.getSupportActionBar().hide();
            setContentView(R.layout.activity_change_record);

            // get singleton instance of database
            mWeightTrackerDb = WeightTrackerDatabase.getInstance(getApplicationContext());
            mDailyWeightDao = mWeightTrackerDb.dailyWeightDao();

            // get views from layout file
            mDate_editText = (EditText) this.findViewById(R.id.editTextDate);
            mWeight_editText = (EditText) this.findViewById(R.id.deleteRecord_editTextWeight);
            mSaveButton = (Button) this.findViewById(R.id.goalWeight_saveButton);
            mChangeButton = (Button) this.findViewById(R.id.changeRecord_button);
            mCancelButton = (Button) this.findViewById(R.id.goalWeight_cancelButton);
            mPrompt = (TextView) this.findViewById(R.id.change_record_prompt);

            // get current user from intent
            Intent intent = getIntent();
            mUser = (User) intent.getSerializableExtra("user");

            // set formatter
            mFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void changeDate_buttonClick(View view) {
        try {
            // hide keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mChangeButton.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

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

            // change widget properties so user can edit the weight
            mPrompt.setText("ENTER NEW WEIGHT");
            mPrompt.setTextColor(getResources().getColor(R.color.gray));
            mChangeButton.setVisibility(View.INVISIBLE);
            mSaveButton.setVisibility(View.VISIBLE);
            mWeight_editText.setVisibility(View.VISIBLE);
            // lock the date so user can't change it
            mDate_editText.setFocusable(false);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void change_saveButtonClick(View view) {
        try {
            if (mDate == null) {
                mPrompt.setText("SOMETHING WENT WRONG");
                mPrompt.setTextColor(Color.RED);
                return;
            }

            // get weight input
            String weight_string = mWeight_editText.getText().toString();
            // if weight input is not a parsable double
            if (!ParseNumbers.isParsableDouble(weight_string)) {
                mPrompt.setText("WEIGHT ENTERED COULD NOT BE PARSED");
                mPrompt.setTextColor(Color.RED);
            }
            // parse input to double
            double weight = Double.parseDouble(weight_string);

            // perform update on database record
            mDailyWeightDao.updateDailyWeight(mUser.getUsername(), mDate, weight);

            // end this activity and return to calling activity
            Intent returnableIntent = getIntent();
            setResult(Activity.RESULT_OK, returnableIntent);
            finish();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void change_cancelButtonClick(View view) {
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}