package com.example.androidarduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class medicineDay extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    RecyclerView rv_planification;
    MedicineDayAdapter adapter;
    ArrayList<Medicine> planificationArrayList;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Medicine> medicineListMonday ;
    ArrayList<Medicine> medicineListTuesday;
    ArrayList<Medicine> medicineListWednesday;
    ArrayList<Medicine> medicineListThursday ;
    ArrayList<Medicine> medicineListFriday ;
    ArrayList<Medicine> medicineListSaturday;
    ArrayList<Medicine> medicineListSunday;
    int selected = 0;
    String device, isPatient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_day);
        linearLayoutManager = new LinearLayoutManager(this);

        TextView textDay = findViewById(R.id.txt_day_medDay);
        rv_planification = findViewById(R.id.recyView_medDay);
        rv_planification.setLayoutManager(linearLayoutManager);

        planificationArrayList = new ArrayList<>();
        adapter = new MedicineDayAdapter(planificationArrayList,this, selected);
        rv_planification.setAdapter(adapter);
        Button btnYESTakeMed = findViewById(R.id.btn_takeMed_medDay);
        Button btnNOTakeMed = findViewById(R.id.btn_noMed_medDay);
        Button btnPASSTakeMed = findViewById(R.id.btn_pass_medDay);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();



        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null){
                    firebaseDatabase.getReference("usuarios").child(mFirebaseUser.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    device = user.getDevice();
                                    isPatient = user.getPatient();


                                    //devicePlanification(device);
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios");

                                    // Query for all entries with a certain child with value equal to something
                                    Query devicePlan= ref.orderByChild("device").equalTo(device);

                                    // Add listener for Firebase response on said query
                                    devicePlan.addValueEventListener( new ValueEventListener(){
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot deviceSnap : dataSnapshot.getChildren() ){
                                                User user = deviceSnap.getValue(User.class);
                                                String key = deviceSnap.getKey();
                                                String device = user.getDevice();

                                                DatabaseReference devRef = FirebaseDatabase.getInstance().getReference("usuarios/"+key);
                                                devRef.child("medicine").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()){
                                                            medicineListMonday = new ArrayList<Medicine>();
                                                            medicineListTuesday = new ArrayList<Medicine>();
                                                            medicineListWednesday = new ArrayList<Medicine>();
                                                            medicineListThursday = new ArrayList<Medicine>();
                                                            medicineListFriday = new ArrayList<Medicine>();
                                                            medicineListSaturday = new ArrayList<Medicine>();
                                                            medicineListSunday = new ArrayList<Medicine>();
                                                            for (DataSnapshot ds : snapshot.getChildren()){
                                                                for(DataSnapshot medicines : ds.getChildren()){
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

                                                            if(medicineListMonday.size()!=0){
                                                                textDay.setText("Monday");
                                                                adapter = new MedicineDayAdapter(medicineListMonday, getApplicationContext(), selected);
                                                                rv_planification.setAdapter(adapter);
                                                                adapter.notifyDataSetChanged();

                                                            }

                                                        }
                                                        else{
                                                            Toast toast = Toast.makeText(getApplicationContext(),"Aún no hay medicinas en la plani",Toast.LENGTH_SHORT);
                                                            toast.show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {}
                                    });

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnYESTakeMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMedicine("YES");
            }
        });


        btnNOTakeMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMedicine("NO");
            }
        });


        btnPASSTakeMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMedicine("PASS");
            }
        });
    }


    public void searchMedicine(String status){
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
        if(mFirebaseUser!=null){
            firebaseDatabase.getReference("usuarios").child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    device = user.getDevice();
                    isPatient = user.getPatient();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios");

                    // Query for all entries with a certain child with value equal to something
                    Query devicePlan= ref.orderByChild("device").equalTo(device);

                    // Add listener for Firebase response on said query
                    devicePlan.addListenerForSingleValueEvent( new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot deviceSnap : dataSnapshot.getChildren() ){
//                                User user = deviceSnap.getValue(User.class);
                                String key = deviceSnap.getKey();;

                                DatabaseReference devRef = FirebaseDatabase.getInstance().getReference("usuarios/"+key+"/medicine/Monday");
                                devRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            for(DataSnapshot medicines : snapshot.getChildren()){
                                                String medicine_name = medicines.child("medicine_name").getValue().toString();
                                                String keyMed = medicines.getKey();
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios/"+key+"/medicine/"+"Monday/"+keyMed).child("took");
                                                if (medicine_name.equals(medicineListMonday.get(selected).medicine_name)){
                                                    ref.setValue(status);
                                                    selected += 1;
                                                    adapter.selected += 1;
                                                    adapter.notifyDataSetChanged();
                                                    Toast toast = Toast.makeText(getApplicationContext(),"Actualizado!",Toast.LENGTH_SHORT);
                                                    toast.show();
                                                    break;
                                                }


                                            }
                                        }
                                        else{
                                            Toast toast = Toast.makeText(getApplicationContext(),"Aún no hay medicinas en la plani",Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Please login!", Toast.LENGTH_SHORT).show();
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }


}