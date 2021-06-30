package com.example.androidarduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        EditText txt_name = findViewById(R.id.text_name_profile);
        EditText txt_surname = findViewById(R.id.text_surname_profile);
        EditText txt_age = findViewById(R.id.text_age_profile);
        EditText txt_email = findViewById(R.id.text_email_profile);
        EditText txt_device = findViewById(R.id.text_device_profile);
        EditText txt_patient = findViewById(R.id.text_patient_profile);
        ImageView img_userProfile = findViewById(R.id.img_userProfile_profile);
        Button btnChangePass = findViewById(R.id.btn_changePass_profile);
        Button btnEditProfile = findViewById(R.id.btn_editProfile_profile);

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
                                    txt_name.setText(user.getName());
                                    txt_surname.setText(user.getSurname());
                                    txt_age.setText(user.getAge());
                                    txt_email.setText(user.getEmail());

                                    if(user.getPatient().equals("")){
                                        txt_patient.setText("User profile has not been assigned yet");
                                    }else {
                                        txt_patient.setText("I'm a "+ user.getPatient());
                                    }if(user.getDevice().equals("")){
                                        txt_device.setText("Device has not been assigned yet");
                                    }else {
                                        txt_device.setText(user.getDevice());
                                    }if(user.getProfile_photo().equals("")){

                                    }else {
                                        Picasso.get().load(user.getProfile_photo()).into(img_userProfile);
                                    }
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


        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, ProfileEdit.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }


}