package com.example.androidarduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class medicineDay extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    RecyclerView rv_planification;
    MedicineDayAdapter adapter;
    ArrayList<Medicine> planificationArrayList;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Medicine> medicineListMonday;
    ArrayList<Medicine> medicineListTuesday;
    ArrayList<Medicine> medicineListWednesday;
    ArrayList<Medicine> medicineListThursday;
    ArrayList<Medicine> medicineListFriday;
    ArrayList<Medicine> medicineListSaturday;
    ArrayList<Medicine> medicineListSunday;
    Button btnYESTakeMed, btnNOTakeMed, btnPASSTakeMed;
    TextView message_medDay;
    ArrayList<String> weekDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
    int currentDay = 0;
    TextView textDay;
    int selected = 0;
    String device, isPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_day);
        linearLayoutManager = new LinearLayoutManager(this);

        textDay = findViewById(R.id.txt_day_medDay);
        rv_planification = findViewById(R.id.recyView_medDay);
        rv_planification.setLayoutManager(linearLayoutManager);

        planificationArrayList = new ArrayList<>();
        adapter = new MedicineDayAdapter(planificationArrayList, this, selected);
        rv_planification.setAdapter(adapter);
        btnYESTakeMed = findViewById(R.id.btn_takeMed_medDay);
        btnNOTakeMed = findViewById(R.id.btn_noMed_medDay);
        btnPASSTakeMed = findViewById(R.id.btn_pass_medDay);
        TextView message_medDay = findViewById(R.id.txt_message_medDay);
        message_medDay.setText("");

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();



        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
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
                                    Query devicePlan = ref.orderByChild("device").equalTo(device);

                                    // Add listener for Firebase response on said query
                                    devicePlan.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot deviceSnap : dataSnapshot.getChildren()) {
                                                User user = deviceSnap.getValue(User.class);
                                                String key = deviceSnap.getKey();
                                                String device = user.getDevice();

                                                DatabaseReference devRef = FirebaseDatabase.getInstance().getReference("usuarios/" + key);
                                                devRef.child("medicine").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            medicineListMonday = new ArrayList<Medicine>();
                                                            medicineListTuesday = new ArrayList<Medicine>();
                                                            medicineListWednesday = new ArrayList<Medicine>();
                                                            medicineListThursday = new ArrayList<Medicine>();
                                                            medicineListFriday = new ArrayList<Medicine>();
                                                            medicineListSaturday = new ArrayList<Medicine>();
                                                            medicineListSunday = new ArrayList<Medicine>();
                                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                                for (DataSnapshot medicines : ds.getChildren()) {
                                                                    String medicine_name = medicines.child("medicine_name").getValue().toString();
                                                                    String taken = medicines.child("took").getValue().toString();
                                                                    String medicine_photo = medicines.child("medicine_photo").getValue().toString();
                                                                    Medicine med = new Medicine(medicine_photo, medicine_name, taken);
                                                                    switch (ds.getKey()) {
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

                                                            if (whichDayList(currentDay).size() != 0) {
                                                                textDay.setText(weekDays.get(currentDay));
                                                                adapter = new MedicineDayAdapter(whichDayList(currentDay), getApplicationContext(), selected);
                                                                rv_planification.setAdapter(adapter);
                                                                adapter.notifyDataSetChanged();

                                                            }

                                                        } else {
                                                            Toast toast = Toast.makeText(getApplicationContext(), "Aún no hay medicinas en la plani", Toast.LENGTH_SHORT);
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
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnYESTakeMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), TextReconigtion.class);
                startActivityForResult(i, 1);
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
                if(whichDayList(currentDay).size() == 0){
                    currentDay += 1;
                    selected = 0;
                    textDay.setText(weekDays.get(currentDay));
                    adapter = new MedicineDayAdapter(whichDayList(currentDay), getApplicationContext(), selected);
                    rv_planification.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                else{
                    //Si pasamos de dia ponemos no en los medicamentos restantes
                    for (int i = selected; i < whichDayList(currentDay).size(); i++) {
                        searchMedicine("NO");
                    }
                }

            }
        });
    }


    public void searchMedicine(String status) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

        if (mFirebaseUser != null) {
            firebaseDatabase.getReference("usuarios").child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    device = user.getDevice();
                    isPatient = user.getPatient();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios");

                    // Query for all entries with a certain child with value equal to something
                    Query devicePlan = ref.orderByChild("device").equalTo(device);

                    // Add listener for Firebase response on said query
                    devicePlan.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot deviceSnap : dataSnapshot.getChildren()) {
                                String key = deviceSnap.getKey();
                                assert key != null;
                                if (key.equals(mFirebaseUser.getUid())){
                                    continue;
                                }

                                DatabaseReference devRef = FirebaseDatabase.getInstance().getReference("usuarios/" + key + "/medicine/" + weekDays.get(currentDay));
                                devRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot medicines : snapshot.getChildren()) {
                                                String medicine_name = medicines.child("medicine_name").getValue().toString();
                                                String keyMed = medicines.getKey();
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios/" + key + "/medicine/" + weekDays.get(currentDay) + "/" + keyMed).child("took");
                                                if (medicine_name.equals(whichDayList(currentDay).get(selected).medicine_name)) {
                                                    ref.setValue(status);
                                                    selected += 1;
                                                    adapter.selected += 1;
                                                    adapter.notifyDataSetChanged();
//                                                    Toast toast = Toast.makeText(getApplicationContext(), "Actualizado!", Toast.LENGTH_SHORT);
//                                                    toast.show();
                                                    if (selected == whichDayList(currentDay).size()) {
                                                        if(currentDay < 6){
                                                            currentDay += 1;
                                                        }
                                                        else{
                                                            currentDay = 0;
                                                        }
                                                        selected = 0;
                                                        textDay.setText(weekDays.get(currentDay));
                                                        adapter = new MedicineDayAdapter(whichDayList(currentDay), getApplicationContext(), selected);
                                                        rv_planification.setAdapter(adapter);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                    break;
                                                }
                                            }
                                        } else {
                                            if(currentDay < 6){
                                                currentDay += 1;
                                            }
                                            else{
                                                currentDay = 0;
                                            }
                                            selected = 0;
                                            textDay.setText(weekDays.get(currentDay));
//                                            if (whichDayList(currentDay).size()==0){
//                                                message_medDay.setText("There are no medications in the planning for this day");
//                                            }
//                                            else {
//                                                message_medDay.setText("");
//                                            }
                                            adapter = new MedicineDayAdapter(whichDayList(currentDay), getApplicationContext(), selected);
                                            rv_planification.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please login!", Toast.LENGTH_SHORT).show();
        }
    }



    public List<Medicine>   whichDayList(int day){

        switch (day){
            case 0:
                return medicineListMonday;

            case 1:
                return medicineListTuesday;

            case 2:
                return medicineListWednesday;

            case 3:
                return medicineListThursday;

            case 4:
                return medicineListFriday;

            case 5:
                return medicineListSaturday;

            case 6:
                return medicineListSunday;


        }
        return null;
    }



    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // el "1" es el numero que pasaste como parametro
            if (resultCode == medicineDay.RESULT_OK) {

                ArrayList<String> medicinesName = data.getStringArrayListExtra("datos");
                // tu codigo para continuar procesando
                for(int med=0; med < medicinesName.size(); med++){
                    if(medicinesName.get(med).contains(whichDayList(currentDay).get(selected).getMedicine_name())){
                        searchMedicine("YES");
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(),"The drug was not found in the list",Toast.LENGTH_LONG).show();

            }
            if (resultCode == medicineDay.RESULT_CANCELED) {
                // código si no hay resultado
                Toast.makeText(getApplicationContext(),"Action was not possible",Toast.LENGTH_LONG);

            }
        }
    }//onActivityResult


}