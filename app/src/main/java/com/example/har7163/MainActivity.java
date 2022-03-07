package com.example.har7163;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button GnnButton, LstmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        GnnButton = findViewById(R.id.GNN_Button);
        LstmButton = findViewById(R.id.LSTM_Button);

        // go to GnnActivity
        GnnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GnnActivity.class);
                startActivity(intent);
            }
        });


        // go to LstmActivity
        LstmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LstmActivity.class);
                startActivity(intent);
            }
        });

    }

}