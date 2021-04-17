package com.josh.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.josh.weighttracker.database.GoalWeightDao;
import com.josh.weighttracker.database.WeightTrackerDatabase;
import com.josh.weighttracker.helpers.ParseNumbers;
import com.josh.weighttracker.model.GoalWeight;
import com.josh.weighttracker.model.User;

public class ChangeTargetActivity extends AppCompatActivity {

    private WeightTrackerDatabase mWeightTrackerDb;
    private GoalWeightDao mGoalWeightDao;
    User mUser;
    EditText mGoalWeightEditText;
    Button mSaveButton;
    Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_target);

        // get singleton instance of database
        mWeightTrackerDb = WeightTrackerDatabase.getInstance(getApplicationContext());
        mGoalWeightDao = mWeightTrackerDb.goalWeightDao();

        // get views from layout file
        mGoalWeightEditText = (EditText) this.findViewById(R.id.goalWeight_editText);
        mSaveButton = (Button) this.findViewById(R.id.goalWeight_saveButton);
        mCancelButton = (Button) this.findViewById(R.id.goalWeight_cancelButton);

        // get current user from intent
        Intent intent = getIntent();
        mUser = (User) intent.getSerializableExtra("user");
    }


    public void saveButtonClick(View view) {
        try {
            String weight_string = mGoalWeightEditText.getText().toString();

            // if weight input is not a parsable double
            if (!ParseNumbers.isParsableDouble(weight_string)) {
                System.out.println("WEIGHT ENTERED COULD NOT BE PARSED");
            }
            // parse input to double
            double weight = Double.parseDouble(weight_string);

            System.out.println("This is what we want it to become: " + weight);

            // FIXME: change database GoalWeight model so there is a singleton column
            //GoalWeight dumby = mGoalWeightDao.getSingleGoalWeight();
            //System.out.println("dumby.getId()......" + dumby.getId());

            // perform update on database record
            mGoalWeightDao.updateGoalWeight(weight, mUser.getUsername());
            System.out.println("THE UPDATEGOALWEIGHT FUNCTION RAN!!.................................................................................................");

            // end this activity and return to calling activity
            Intent returnableIntent = getIntent();
            setResult(Activity.RESULT_OK, returnableIntent);
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
}