package com.example.nutritional;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone,mregWeight;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    DatabaseReference DBNutritional;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName   = findViewById(R.id.fullName);
        mEmail      = findViewById(R.id.Email);
        mPassword   = findViewById(R.id.password);
        mLoginBtn   = findViewById(R.id.loginTxt);
        mregWeight = findViewById(R.id.regWeight);
        mRegisterBtn = findViewById(R.id.registerBtn);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        DBNutritional = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            registerUser();
        }
    });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }

    //eto ung kkuha ng laman ng input fields na fullname email password at weight upon registration
    private void registerUser() {
        final String fullName = mFullName.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();
        String passWord = mPassword.getText().toString().trim();
        final String regWeight = mregWeight.getText().toString().trim();


        if (fullName.isEmpty()) {
            mFullName.setError("enter a name");
            mFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            mEmail.setError("email is empty");
            mEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("enter correct email format");
            mEmail.requestFocus();
            return;
        }

        if (passWord.isEmpty()) {
            mPassword.setError("enter a password");
            mPassword.requestFocus();
            return;
        }

        if (passWord.length() < 6) {
            mPassword.setError("Password should be at least 6");
            mPassword.requestFocus();
            return;
        }

        if (regWeight.isEmpty()) {
            mregWeight.setError("Please enter current weight");
            mregWeight.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        //eto ung function ni firebase na magsstore ng data papasok sa database ng firebase
        mAuth.createUserWithEmailAndPassword(email, passWord)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(
                                    fullName,
                                    regWeight,
                                    email
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(getApplicationContext(),Login.class));
                                                Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                            } else {
                                                //display a failure message
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

}