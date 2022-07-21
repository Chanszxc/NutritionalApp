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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Progress extends AppCompatActivity {

    EditText latestWeight;
    TextView progRegWeight;
    Button mAchievements;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String regweight;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        latestWeight = findViewById(R.id.latestWeight);
        progRegWeight = findViewById(R.id.progRegWeight);
        mAchievements = findViewById(R.id.btnachievements);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userID.isEmpty()){

        } else {
            getUserData();
        }


        //achievements accessible lng pag naglagay na ng latest weight si user
        //pagcnlick to mappunta muna sa progress.class para maglagay ng latest weight
        mAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String latestWeightt =  latestWeight.getText().toString();
                if (latestWeightt.isEmpty()) {
                    Toast.makeText(Progress.this, "Enter Values", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LatestWeight").setValue(latestWeightt);
                    Intent intent = new Intent(Progress.this, Achievements.class);
                    startActivity(intent);
                    latestWeight.setText("");
                }
            }
        });
    }
    private void getUserData() {


        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("regWeight")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int in = Integer.valueOf(regweight = snapshot.getValue().toString());

                        progRegWeight.setText(regweight + " " + "KG");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(Progress.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}