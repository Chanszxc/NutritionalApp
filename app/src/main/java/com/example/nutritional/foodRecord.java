package com.example.nutritional;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class foodRecord extends AppCompatActivity {

    TextView displayTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_record);

        displayTxt = findViewById(R.id.displayTxt);
        Record record = new Record();

        displayTxt.setText("Record test");


    }
}