package com.josh.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

public class WeightActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_weight);
    }

    // use an AppCompat Toolbar with add button
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // inflate menu and use specific action bar
//        getMenuInflater().inflate(R.menu.appbar_menu, menu);
//        MenuItem item = menu.findItem(R.id.actionbar);
//        item.setTitle(R.string.title_weight_activity);
//        return true;
//    }


    public void updateTable() {

    }
}