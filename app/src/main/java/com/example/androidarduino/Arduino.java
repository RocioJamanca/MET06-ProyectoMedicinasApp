package com.example.androidarduino;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class Arduino extends HomeMenu {
    Button btnSoloBack;
    String device, isPatient;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView textFallSensor,textTemperature,textHumidity;
    int servoPosition, ledIntensity;
    SeekBar ledIntensityBar, servoPositionBar;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null){
                    firebaseDatabase.getReference("usuarios").child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                User user = snapshot.getValue(User.class);
                                patient = user.getPatient();
                                if (isPatient.equals("Patient")){
                                    ledIntensityBar.setEnabled(false);
                                    servoPositionBar.setEnabled(false);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }
            }
        };



        //Cogemos los datos de la base de datos
        getDataValue();

        final long[] start = {0};

        servoPositionBar = findViewById(R.id.seekBar);
        servoPositionBar.setProgress(servoPosition);
        servoPositionBar.incrementProgressBy(1);
        servoPositionBar.setMax(4);
        servoPositionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setDataValue(progress,"servo");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ledIntensityBar = findViewById(R.id.seekBar2);
        ledIntensityBar.setProgress(ledIntensity);
        ledIntensityBar.incrementProgressBy(1);
        ledIntensityBar.setMax(5);
        ledIntensityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar2, int progress, boolean fromUser) {
                setDataValue(progress,"leds");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void getDataValue(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("usuarios");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase.getReference("usuarios").child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                device = user.getDevice();
                isPatient = user.getPatient();
                // Query for all entries with a certain child with value equal to something
                Query devicePlan = myRef.orderByChild("device").equalTo(device);

                devicePlan.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot deviceSnap : snapshot.getChildren()){
                                DatabaseReference dataRef = deviceSnap.child("data").getRef();
                                dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){

                                            Long sensorFall = deviceSnap.child("data").child("sensor").getValue(Long.class);
                                            Long temperature = deviceSnap.child("data").child("temp").getValue(Long.class);
                                            Long humidity = deviceSnap.child("data").child("humidity").getValue(Long.class);
                                            Long leds = deviceSnap.child("data").child("leds").getValue(Long.class);
                                            Long servo = deviceSnap.child("data").child("servo").getValue(Long.class);
                                            Long btnSOS = deviceSnap.child("data").child("btnSOS").getValue(Long.class);

                                            if(servo != null)
                                            servoPosition = servo.intValue();
                                            if (leds != null)
                                            ledIntensity = leds.intValue();

                                            if (sensorFall !=null ){
                                                textFallSensor.setText(sensorFall.toString());
                                            }
                                            if((humidity !=null))
                                            {
                                                textHumidity.setText(humidity.toString());
                                                if (humidity>70){
                                                    //Notificación
                                                }
                                            }
                                            if(temperature !=null) {
                                                textTemperature.setText(temperature.toString());
                                                if (temperature>40){
                                                    //Notificación
                                                }
                                            }
                                            if(btnSOS !=null){
                                                if (btnSOS==1){
                                                    //Notificación

                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                        else{
                            DatabaseReference tempRef = snapshot.child("data").child("temp").getRef();
                            DatabaseReference humRef = snapshot.child("data").child("humidity").getRef();
                            DatabaseReference sensorFallRef = snapshot.child("data").child("sensor").getRef();
                            DatabaseReference ledRef = snapshot.child("data").child("leds").getRef();
                            DatabaseReference servoRef = snapshot.child("data").child("servo").getRef();
                            DatabaseReference sosRef = snapshot.child("data").child("sosBtn").getRef();

                            tempRef.setValue("0");
                            humRef.setValue("0");
                            sensorFallRef.setValue("0");
                            ledRef.setValue("0");
                            servoRef.setValue("0");
                            sosRef.setValue("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    public void setDataValue(int progress,String data){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("usuarios");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase.getReference("usuarios").child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                device = user.getDevice();
                isPatient = user.getPatient();
                // Query for all entries with a certain child with value equal to something
                Query devicePlan = myRef.orderByChild("device").equalTo(device);

                devicePlan.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot deviceSnap : snapshot.getChildren()){
                                DatabaseReference dataRef = deviceSnap.child("data").child(data).getRef();
                                dataRef.setValue(progress);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void callNotification(String title, String detail){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();
        try{
            String token = "";
            json.put("to",token);
            JSONObject notification = new JSONObject();
            notification.put("title","I'm a title");
            notification.put("detail", "i'm a detail");

            json.put("data",notification);
            String URL = "https://androidarduino-68fe7.googleapis.com/androidarduino-68fe7/send";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
