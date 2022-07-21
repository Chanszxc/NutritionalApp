package com.example.nutritional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Record extends AppCompatActivity {

        private Button button2, btnDel;
        private EditText edFoodName;
        private EditText foodCalorie;
        private ListView listView;
        Button comCal;
        TextView totCal;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Task<Void> foodDataBaseReference;
    int[] namecount = {1};
    int[] caloriecount = {1};

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        comCal = findViewById(R.id.comCal);
        totCal = findViewById(R.id.totCal);
        edFoodName = findViewById(R.id.edFoodName);
        foodCalorie = findViewById(R.id.edtCalories);
        button2 = findViewById(R.id.button2);
        btnDel = findViewById(R.id.delBtn);
        listView = findViewById(R.id.listView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Calorie1").setValue(0);
        firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Calorie2").setValue(0);
        firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Calorie3").setValue(0);
        firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Calorie4").setValue(0);
        firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Calorie5").setValue(0);
        firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Name1").setValue("");
        firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Name2").setValue("");
        firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Name3").setValue("");
        firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Name4").setValue("");
        firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Name5").setValue("");

        //eto unng magbbigay ng child name sa laman ng Food Data
        //kumbaga para lng maging unique ung id ng kada food



        //nagccrash pag nagcompute calorie then delete data
        final int[] clicks = {0};
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clicks[0]++;
                if (clicks[0] == 6){
                    button2.setClickable(false);
                    Toast.makeText(Record.this, "Cant Insert More Food!", Toast.LENGTH_LONG).show();
                } else {

                    String food_name = edFoodName.getText().toString();
                    String food_Calorie = foodCalorie.getText().toString();
                    if (food_name.isEmpty()) {
                        Toast.makeText(Record.this, "Please Enter Values", Toast.LENGTH_SHORT).show();
                    } else {
                        //FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Name and Calories"+ " " + namecount[0]++).setValue(food_name + "  " + food_Calorie);
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Name" + namecount[0]++).setValue(food_name).toString();
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Calorie" + caloriecount[0]++).setValue(food_Calorie).toString();
                        edFoodName.setText("");
                        foodCalorie.setText("");

                    }
                }
            }
        });

        //eto ung button na magddelete sa daily food data ni user
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delFood();
            }


        });

        comCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInfo();
            }
        });





        //dto nasstore ungn laman ng food data ni user
        //una ggawa muna sya ng array pangalan list
        final ArrayList<String> list = new ArrayList<>();
        //array adapter ung magfformat sa pagdisplay ng data o laman ng array list na nakuha sa database
        final ArrayAdapter adapter = new ArrayAdapter<String >(this, R.layout.list_item, list);
        listView.setAdapter(adapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                //eto ung magiiterate na kada item row sa database under ng FoodData ay kukunin at ilagay sa list array
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    list.add(snapshot.getValue().toString());


                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getUserInfo() {


        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String tot1Val;
                        String tot2Val;
                        String tot3Val;
                        String tot4Val;
                        String tot5Val;
                        Integer tot1 = Integer.valueOf(tot1Val = String.valueOf(snapshot.child("Food Calorie1").getValue()));
                        Integer tot2 = Integer.valueOf(tot2Val = String.valueOf(snapshot.child("Food Calorie2").getValue()));
                        Integer tot3 = Integer.valueOf(tot3Val = String.valueOf(snapshot.child("Food Calorie3").getValue()));
                        Integer tot4 = Integer.valueOf(tot4Val = String.valueOf(snapshot.child("Food Calorie4").getValue()));
                        Integer tot5 = Integer.valueOf(tot5Val = String.valueOf(snapshot.child("Food Calorie5").getValue()));

                        String calCompute = String.valueOf((Integer.parseInt(tot1Val) + Integer.parseInt(tot2Val) + Integer.parseInt(tot3Val) + Integer.parseInt(tot4Val) + Integer.parseInt(tot5Val)));

                        totCal.setText(calCompute);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(Record.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //eto ung function para madelete sa database ung food Data ni user
   private void delFood() {
            firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Calorie1").setValue(0);
            firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Calorie2").setValue(0);
            firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Calorie3").setValue(0);
            firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Calorie4").setValue(0);
            firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Calorie5").setValue(0);
            firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Name1").setValue("");
            firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Name2").setValue("");
            firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Name3").setValue("");
            firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Name4").setValue("");
            firebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodData").child("Food Name5").setValue("");
    }
}
