package com.example.har7163;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class GnnActivity extends AppCompatActivity {

    private TextView WALKING_TextView, WALKING_UPSTAIRS_TextView, WALKING_DOWNSTAIRS_TextView, SITTING_TextView, STANDING_TextView, LAYING_TextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnn);

        initLayoutItems();

        WALKING_TextView.setText("Walking: \t" + "");
        WALKING_UPSTAIRS_TextView.setText("Upstairs: \t" + "");
        WALKING_DOWNSTAIRS_TextView.setText("Downstairs: \t" + "");
        SITTING_TextView.setText("Sitting: \t" + "");
        STANDING_TextView.setText("Standing: \t" + "");
        LAYING_TextView.setText("Laying: \t" + "");
    }


    private void initLayoutItems() {
        WALKING_TextView = findViewById(R.id.WALKING_TextView);
        WALKING_UPSTAIRS_TextView = findViewById(R.id.WALKING_UPSTAIRS_TextView);
        WALKING_DOWNSTAIRS_TextView = findViewById(R.id.WALKING_DOWNSTAIRS_TextView);
        SITTING_TextView  = findViewById(R.id.SITTING_TextView);
        STANDING_TextView = findViewById(R.id.STANDING_TextView);
        LAYING_TextView = findViewById(R.id.LAYING_TextView);
    }
}
