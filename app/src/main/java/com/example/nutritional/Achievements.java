package com.example.nutritional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Achievements extends AppCompatActivity {

    EditText mPrevkg, mCurrKg;
    Button retHome, logOut;
    TextView achvReport, totCaloriesConsumed;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Task<Void> totalCaloriesDailyCal;
    String regweight;
    String latestWeight;
    Integer latestWeightt;
    Integer regWeightt;
    Integer compute;
    String mess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        //totCaloriesConsumed = findViewById(R.id.totCaloriesConsumed);
        achvReport = findViewById(R.id.achvReport);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        retHome = findViewById(R.id.retHome);
        logOut = findViewById(R.id.logOut);


        //sa string userID nasstore ung UID ng user mula firebase
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //If user is empty pagsabhin pag blanko ung uid field pagsabihin di pa naka login user.
        if (userID.isEmpty()){
            Toast.makeText(Achievements.this, "Please Log in First", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(),Login.class));
        } else {
            getUserData();
        }

        retHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Achievements.this, MainActivity.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Achievements.this, Login.class);
                startActivity(intent);
            }
        });



    }




    //dto ung function na kumukuha ng data mula firebase
    //dto compute ugn formula kung nang gain o lose ng weight si user
    private void getUserData() {

        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int inRegWeight = Integer.valueOf(regweight = snapshot.child("regWeight").getValue().toString());
                        int inLatestWeight = Integer.valueOf(latestWeight = snapshot.child("LatestWeight").getValue().toString());

                        latestWeightt = inLatestWeight;
                        regWeightt = inRegWeight;

                        compute = inRegWeight - inLatestWeight;

                        int sum,calories;
                        sum = Math.abs(compute);


                        if(compute < 0){
                            calories = sum * 7700;
                            mess = "Not progressing!";
                            achvReport.setText("You gained"+" " +sum+"kg Equivalent of " + calories + " calories." + mess);
                        } else if (compute > 0){
                            calories = sum * 7700;
                            mess = "Great Progress!";
                            achvReport.setText("You lose"+" " +sum+"kg Equivalent of " + calories + " calories."+ mess);
                        }


                        //achvReport.setText(Integer.valueOf(inRegWeight) - Integer.valueOf(inLatestWeight));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(Achievements.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {

    }


}