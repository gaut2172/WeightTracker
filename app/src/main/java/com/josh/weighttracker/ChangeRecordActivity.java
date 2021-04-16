package com.josh.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ChangeRecordActivity extends AppCompatActivity {

    private EditText mWeight_editText;
    private Button mSaveButton;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            this.getSupportActionBar().hide();
            setContentView(R.layout.activity_add_record);

            mWeight_editText = (EditText) this.findViewById(R.id.editTextDate);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}