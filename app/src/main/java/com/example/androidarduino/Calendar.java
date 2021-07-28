package com.example.androidarduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Calendar extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    RecyclerView rv_plani,rv_tuesday,rv_wednesday,rv_thursday,rv_friday,rv_saturday,rv_sunday;




    PlaniAdapter adapter;
    ArrayList<Medicine> planificationArrayList;
    LinearLayoutManager linearLayoutManager,linearLayoutManagerTuesday,linearLayoutManagerWednesday,linearLayoutManagerThursday,linearLayoutManagerFriday,linearLayoutManagerSaturday,linearLayoutManagerSunday;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManagerTuesday = new LinearLayoutManager(this);
        linearLayoutManagerTuesday.setReverseLayout(true);
        linearLayoutManagerTuesday.setStackFromEnd(true);
        linearLayoutManagerWednesday = new LinearLayoutManager(this);
        linearLayoutManagerWednesday.setReverseLayout(true);
        linearLayoutManagerWednesday.setStackFromEnd(true);
        linearLayoutManagerThursday = new LinearLayoutManager(this);
        linearLayoutManagerThursday.setReverseLayout(true);
        linearLayoutManagerThursday.setStackFromEnd(true);
        linearLayoutManagerFriday = new LinearLayoutManager(this);
        linearLayoutManagerFriday.setReverseLayout(true);
        linearLayoutManagerFriday.setStackFromEnd(true);
        linearLayoutManagerSaturday = new LinearLayoutManager(this);
        linearLayoutManagerSaturday.setReverseLayout(true);
        linearLayoutManagerSaturday.setStackFromEnd(true);
        linearLayoutManagerSunday = new LinearLayoutManager(this);
        linearLayoutManagerSunday.setReverseLayout(true);
        linearLayoutManagerSunday.setStackFromEnd(true);


        rv_plani = findViewById(R.id.recyView_plani);
        rv_tuesday = findViewById(R.id.recyView_tuesday);
        rv_wednesday = findViewById(R.id.recyView_wednesday);
        rv_thursday = findViewById(R.id.recyView_thursday);
        rv_friday = findViewById(R.id.recyView_friday);
        rv_saturday = findViewById(R.id.recyView_saturday);
        rv_sunday = findViewById(R.id.recyView_sunday);

        rv_plani.setLayoutManager(linearLayoutManager);
        rv_tuesday.setLayoutManager(linearLayoutManagerTuesday);
        rv_wednesday.setLayoutManager(linearLayoutManagerWednesday);
        rv_thursday.setLayoutManager(linearLayoutManagerThursday);
        rv_friday.setLayoutManager(linearLayoutManagerFriday);
        rv_saturday.setLayoutManager(linearLayoutManagerSaturday);
        rv_sunday.setLayoutManager(linearLayoutManagerSunday);

        planificationArrayList = new ArrayList<>();
        adapter = new PlaniAdapter(planificationArrayList,this);
        rv_plani.setAdapter(adapter);



        Button btnAddMedicine = findViewById(R.id.btn_addMedicine_calendar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
               DatabaseReference ref =  firebaseDatabase.getReference("usuarios").child(mFirebaseUser.getUid()).child("medicine");
               ref.addValueEventListener(new ValueEventListener() {

                   @Override
                   public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                       ArrayList<Medicine> medicineListMonday = new ArrayList<Medicine>();
                       ArrayList<Medicine> medicineListTuesday = new ArrayList<Medicine>();
                       ArrayList<Medicine> medicineListWednesday = new ArrayList<Medicine>();
                       ArrayList<Medicine> medicineListThursday = new ArrayList<Medicine>();
                       ArrayList<Medicine> medicineListFriday = new ArrayList<Medicine>();
                       ArrayList<Medicine> medicineListSaturday = new ArrayList<Medicine>();
                       ArrayList<Medicine> medicineListSunday = new ArrayList<Medicine>();

                      for (DataSnapshot ds: snapshot.getChildren()){
                        for(DataSnapshot medicines: ds.getChildren()){
                            String medicine_name = medicines.child("medicine_name").getValue().toString();
                            String taken = medicines.child("took").getValue().toString();
                            String medicine_photo = medicines.child("medicine_photo").getValue().toString();
                            Medicine med = new Medicine(medicine_photo, medicine_name, taken);
                            switch (ds.getKey()){
                                case "Monday":
                                    medicineListMonday.add(med);
                                    break;
                                case "Tuesday":
                                    medicineListTuesday.add(med);
                                    break;
                                case "Wednesday":
                                    medicineListWednesday.add(med);
                                    break;
                                case "Thursday":
                                    medicineListThursday.add(med);
                                    break;
                                case "Friday":
                                    medicineListFriday.add(med);
                                    break;
                                case "Saturday":
                                    medicineListSaturday.add(med);
                                    break;
                                case "Sunday":
                                    medicineListSunday.add(med);
                                    break;
                            }
                          }
                      }
                       adapter = new PlaniAdapter(medicineListMonday,getApplicationContext());
                       rv_plani.setAdapter(adapter);
                       adapter.notifyDataSetChanged();
                       PlaniAdapter adapter2 = new PlaniAdapter(medicineListTuesday,getApplicationContext());
                       rv_tuesday.setAdapter(adapter2);
                       adapter2.notifyDataSetChanged();
                       adapter = new PlaniAdapter(medicineListWednesday,getApplicationContext());
                       rv_wednesday.setAdapter(adapter);
                       adapter.notifyDataSetChanged();
                       adapter = new PlaniAdapter(medicineListThursday,getApplicationContext());
                       rv_thursday.setAdapter(adapter);
                       adapter.notifyDataSetChanged();
                       adapter = new PlaniAdapter(medicineListFriday,getApplicationContext());
                       rv_friday.setAdapter(adapter);
                       adapter.notifyDataSetChanged();
                       adapter = new PlaniAdapter(medicineListSaturday,getApplicationContext());
                       rv_saturday.setAdapter(adapter);
                       adapter.notifyDataSetChanged();
                       adapter = new PlaniAdapter(medicineListSunday,getApplicationContext());
                       rv_sunday.setAdapter(adapter);
                       adapter.notifyDataSetChanged();
                   }
                   @Override
                   public void onCancelled(@NonNull @NotNull DatabaseError error) {
                   }
               });
            }
        };




        btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddMedicine.class);
                startActivity(i);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}