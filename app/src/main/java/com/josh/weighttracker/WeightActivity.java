package com.josh.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.josh.weighttracker.Models.DailyWeight;
import com.josh.weighttracker.Models.User;
import com.josh.weighttracker.Models.WeightTrackerDatabase;

import java.util.Date;

public class WeightActivity extends AppCompatActivity {

    private WeightTrackerDatabase mWeightTrackerDb;
    private UserDao mUserDao;
    private User mUser;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    TableLayout tableLayout;
    TableRow tableRow;

    private EditText mAddPopup_date;
    private EditText mAddPopup_weight;
    private Button mAddPopup_saveButton;
    private Button mAddPopup_cancelButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_weight);
        tableLayout = (TableLayout)findViewById(R.id.dailyWeightTable);
        updateTable();


    }



    public void updateTable() {

    }


    // Callback for add record button
    public void addRecordOnClick(View view) {
        DailyWeight newWeightRecord = addRecordPopup();
        if (newWeightRecord != null) {
            // FIXME: add the new record to the database
        }
    }

    // Callback for delete record button
    public void deleteRecordOnClick(View view) {

    }

    // Callback for edit record button
    public void editRecordOnClick(View view) {

    }


    // Popup window function for add record button
    public DailyWeight addRecordPopup() {

        DailyWeight newWeightRecord = null;
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_add_record, null);

        mAddPopup_date = (EditText) contactPopupView.findViewById(R.id.editTextDate);
        mAddPopup_weight = (EditText) contactPopupView.findViewById(R.id.editTextWeight);
        mAddPopup_saveButton = (Button) contactPopupView.findViewById(R.id.popup_saveButton) ;
        mAddPopup_cancelButton = (Button) contactPopupView.findViewById(R.id.popup_cancelButton) ;

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        // save button listener
        mAddPopup_saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // cancel button listener
        mAddPopup_cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return newWeightRecord;
    }
}