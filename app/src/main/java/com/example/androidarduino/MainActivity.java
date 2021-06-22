package com.example.androidarduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    Button btn_register_2;
    Button btn_log;
    Button btn_profile;
    Button btn_calendar;
    Button btn_arduino;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_register_2 = (Button) findViewById(R.id.btn_register_login);
        btn_log = (Button) findViewById(R.id.btn_login);
        btn_profile = (Button) findViewById(R.id.btn_profile);
        btn_calendar = (Button) findViewById(R.id.btn_calendar);
        btn_arduino = (Button) findViewById(R.id.btn_arduino);
        mAuth = FirebaseAuth.getInstance();
        btn_profile.setEnabled(false);
        btn_calendar.setEnabled(false);
        btn_arduino.setEnabled(false);
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());

        ////////////// ARDUINO - ANDROID ////////////////
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://androidarduino-68fe7-default-rtdb.firebaseio.com");
        DatabaseReference myRef = database.getReference("/UsersData/BDd0FDJDogWDFXkFx6TS0Ft120L2/dades/int");
        DatabaseReference myRef2 = database.getReference("/UsersData/BDd0FDJDogWDFXkFx6TS0Ft120L2/dades/leds");

        //Pujar valor
        ///myRef.setValue(4);

        //LLegir valor
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Long value = dataSnapshot.getValue(Long.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Long value = dataSnapshot.getValue(Long.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        //LLegir valor
        ////////////// ARDUINO - ANDROID ////////////////
        btn_register_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Register2.class);
                startActivityForResult(i,2);
            }
        });
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        1);
            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("users")
                        .whereEqualTo("mail", currentUser.getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Map<String, Object> entry = document.getData();
                                        String mail = (String) entry.get("mail");
                                        String name = (String) entry.get("name");
                                        String surname = (String) entry.get("surname");
                                        String age = (String) entry.get("age");
                                        String phone = (String) entry.get("phone");
                                        String foto = (String) entry.get("foto");
                                        Intent i = new Intent(getApplicationContext(), Perfil.class);
                                        i.putExtra("mail", mail);
                                        i.putExtra("name", name);
                                        i.putExtra("surname", surname);
                                        i.putExtra("age", age);
                                        i.putExtra("phone", phone);
                                        i.putExtra("foto", foto);
                                        startActivityForResult(i,4);
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                /*Intent i = new Intent(getApplicationContext(), Perfil.class);
                startActivityForResult(i,3);*/
            }
        });

        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("medicine")
                        .whereEqualTo("mail", currentUser.getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String meds[] = new String[200];
                                    String dates[] = new String[200];
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Map<String, Object> entry = document.getData();
                                        String date = (String) entry.get("date");
                                        String med = (String) entry.get("med");
                                        meds = ArrayUtils.appendToArray(meds, med);
                                        dates = ArrayUtils.appendToArray(dates, date);
                                    }
                                    Intent i = new Intent(getApplicationContext(), Calendario.class);
                                    i.putExtra("date", dates);
                                    i.putExtra("med", meds);
                                    startActivityForResult(i,5);
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        btn_arduino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Arduino.class);
                startActivityForResult(i,6);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                btn_profile.setEnabled(true);
                btn_calendar.setEnabled(true);
            } else {
            }
        } else  if (requestCode == 2){
            Bundle extras = data.getExtras();
            Object mail = extras.get("mail");
            String s_mail = String.valueOf(mail);
            Object pass = extras.get("pass");
            String s_pass = String.valueOf(pass);


            mAuth.createUserWithEmailAndPassword(s_mail,s_pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        currentUser = mAuth.getCurrentUser();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("mail", s_mail);
                        newUser.put("passwd", s_pass);
                        db.collection("users")
                                .add(newUser)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        btn_profile.setEnabled(true);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                    }else{
                        Log.w(TAG, "error", task.getException());
                    }
                }
            });
        }
        else  if (requestCode == 4) {
            Bundle extras = data.getExtras();
            Object mail = extras.get("mail");
            String s_mail = String.valueOf(mail);
            Object name = extras.get("name");
            String s_name = String.valueOf(name);

            Object surname = extras.get("surname");
            String s_surname = String.valueOf(surname);
            Object age = extras.get("age");
            String s_age = String.valueOf(age);
            Object phone = extras.get("phone");
            String s_phone = String.valueOf(phone);
            Object paciente = extras.get("paciente");
            String s_paciente = String.valueOf(paciente);
            Object serialnumb = extras.get("serial");
            String s_serialnumb = String.valueOf(serialnumb);
            Object foto = extras.get("foto");
            String s_foto = String.valueOf(foto);


            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("mail", mail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> entry = document.getData();
                                    String id = document.getId();

                                    Map<String, Object> newUser = new HashMap<>();
                                    newUser.put("mail", s_mail);
                                    newUser.put("name", s_name);
                                    newUser.put("surname", s_surname);
                                    newUser.put("age", s_age);
                                    newUser.put("phone", s_phone);
                                    newUser.put("paciente", s_paciente);
                                    newUser.put("serial", s_serialnumb);
                                    newUser.put("foto", s_foto);
                                    db.collection("users").document(id)
                                            .set(newUser)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        else  if (requestCode == 5) {
            Bundle extras = data.getExtras();
            Object date = extras.get("date");
            String s_date = String.valueOf(date);
            Object med = extras.get("med");
            String s_med = String.valueOf(med);
            Object fotomed = extras.get("foto_med");
            String s_fotomed = String.valueOf(fotomed);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            currentUser = mAuth.getCurrentUser();
            Map<String, Object> newMed = new HashMap<>();
            newMed.put("date", s_date);
            newMed.put("med", s_med);
            newMed.put("foto_med", s_fotomed);
            newMed.put("mail", currentUser.getEmail());
            db.collection("medicine")
                    .add(newMed)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }
}