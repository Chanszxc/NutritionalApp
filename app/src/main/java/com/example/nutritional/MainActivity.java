package com.example.nutritional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ImageView imgRecipe, imgProgress, imgRecord, imgAchievements;
    Button delAcc;
    TextView signupWeight, userData;
    String progStatus;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth userID;
    String regweight;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userData = findViewById(R.id.userInfo);
        delAcc = findViewById(R.id.delAcc);
        imgRecipe = findViewById(R.id.recipe);
        imgProgress= findViewById(R.id.progress);
        imgRecord = findViewById(R.id.record);
        imgAchievements = findViewById(R.id.achievement);
        signupWeight = findViewById(R.id.signupWeight);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        //eto magccheck kung may nakalogin ba na user pag meron makkuha nya ung data pag wala may llabas na notif na need mag login muna
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userID.isEmpty()){
        Toast.makeText(MainActivity.this, "Please log in first", Toast.LENGTH_LONG).show();
        } else {
            getUserData();
            getUserInfo();
        }
        //getdata();


        imgRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Recipe.class);
                startActivity(intent);
            }
        });

        imgProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Progress.class);
                startActivity(intent);
            }
        });

        imgRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Record.class);
                startActivity(intent);
            }
        });

        imgAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Progress.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Please enter latest Weight", Toast.LENGTH_LONG).show();
            }
        });

        signupWeight.setText(userID);

        delAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delAccount();
            }
        });

    }

    //eto yung funcntion sa pag delete ng acc ni user
    private void delAccount() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential("user@example.com", "password1234");

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(getApplicationContext(),Login.class));
                                            Toast.makeText(MainActivity.this, "Account Deleted", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                    }
                });
    }

    //eto ungn functionn sa pagkuha ng data ni user mula sa database
    //dto ung register weight kkunnin tas kung need ba magbawas o magdagdag
    private void getUserData() {


        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("regWeight")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int in = Integer.valueOf(regweight = snapshot.getValue().toString());


                if (in >= 60) {
                progStatus = "Need to lose Weight!";
                } if (in <= 59) {
                    progStatus = "Eat more";
                }
                signupWeight.setText(regweight + " " + "KG" + "\n" + progStatus);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo() {


        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("fullName").getValue().toString();

                        userData.setText("Welcome" + " " +name);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

    }

}