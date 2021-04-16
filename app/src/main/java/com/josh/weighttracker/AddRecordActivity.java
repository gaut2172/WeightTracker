package com.josh.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.josh.weighttracker.helpers.ParseNumbers;
import com.josh.weighttracker.model.DailyWeight;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddRecordActivity extends AppCompatActivity {

    private EditText mDate_editText;
    private EditText mWeight_editText;
    private Button mSaveButton;
    private Button mCancelButton;
    DailyWeight mNewRecord = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            this.getSupportActionBar().hide();
            setContentView(R.layout.activity_add_record);

            // get views from layout file
            mDate_editText = (EditText) this.findViewById(R.id.editTextDate);
            mWeight_editText = (EditText) this.findViewById(R.id.deleteRecord_editTextWeight);
            mSaveButton = (Button) this.findViewById(R.id.popup_saveButton);
            mCancelButton = (Button) this.findViewById(R.id.popup_cancelButton);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveButtonClick(View view) {
        try {
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date date;

            // get date input and parse
            String date_string = mDate_editText.getText().toString();
            date = formatter.parse(date_string);

            // get weight input
            String weight_string = mWeight_editText.getText().toString();
            // if weight input is not a parsable double
            if (!ParseNumbers.isParsableDouble(weight_string)) {
                System.out.println("COULD NOT PARSE DOUBLE!!");
                cancelButtonClick(mCancelButton);
            }
            // parse input to double
            double weight = Double.parseDouble(weight_string);

//            // make new DailyWeight object
            DailyWeight newWeight = new DailyWeight();
            newWeight.setDate(date);
            newWeight.setWeight(weight);

            // pass parsable string input to calling activity
            Intent returnableIntent = getIntent();
            returnableIntent.putExtra("newDailyWeight", newWeight);
//            returnableIntent.putExtra("newDailyWeight", weight_string);
//            returnableIntent.putExtra("newDate", date_string);
            setResult(Activity.RESULT_OK, returnableIntent);

//            System.out.println("AddRecordActivity...newWeight.date---- " + newWeight.getDate());
//            Intent intent = getIntent();
//            intent.putExtra("newDailyWeight", newWeight);
//            DailyWeight dailyWeight = (DailyWeight) intent.getSerializableExtra("newDailyWeight");
            // end this activity and return to calling activity
            finish();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelButtonClick(View view) {
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public void closeScreen() {

    }
}