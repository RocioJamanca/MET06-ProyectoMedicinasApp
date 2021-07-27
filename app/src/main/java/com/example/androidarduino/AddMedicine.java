package com.example.androidarduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AddMedicine extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String day;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        Spinner spinnerDay = findViewById(R.id.spinner_addMedicine);
        Button btn_accept = findViewById(R.id.btn_accept_addMedicine);
        EditText medicineName = findViewById(R.id.txt_medName_addMedicine);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapter);
        spinnerDay.setOnItemSelectedListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null){
                    firebaseDatabase.getReference("usuarios").child(mFirebaseUser.getUid());
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (day.isEmpty() || medicineName.getText().toString().isEmpty()){
                if (day.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please select a day!", Toast.LENGTH_SHORT).show();
                }
                }
                else {
                    FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                    DatabaseReference dataRef = firebaseDatabase.getReference("usuarios").child(mFirebaseUser.getUid());
                    dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                               if(snapshot.child("medicine").child(day).getChildrenCount()>=2){
                                   Toast toast = Toast.makeText(getApplicationContext(),"No more medicines can be added for this day", Toast.LENGTH_LONG);
                                   toast.show();
                               }
                               else {
                                   Medicine medicine = new Medicine("",medicineName.getText().toString(),""); //Aqui aun no es ni si ni no
                                   Toast.makeText(getApplicationContext(),"The drug has been added to the list", Toast.LENGTH_LONG).show();
                                   dataRef.child("medicine").child(day).push().setValue(medicine);
                               }
                            }
                            else{
                                Medicine medicine = new Medicine("",medicineName.getText().toString(),""); //Aqui aun no es ni si ni no
                                Toast.makeText(getApplicationContext(),"The drug has been added to the list", Toast.LENGTH_LONG).show();
                                dataRef.child("medicine").child(day).push().setValue(medicine);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }

            }
        });




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedDay = parent.getItemAtPosition(position).toString();
        day = selectedDay;
       // Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        day = "";
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }



}