package com.example.androidarduino;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Login extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText username = findViewById(R.id.txt_email_login);
        final EditText password = findViewById(R.id.txt_password_login);
        Button btnLogin = findViewById(R.id.btn_login_login);
        Button btnRegister = findViewById(R.id.btn_register_login);


        ArrayList<String> emailList = new ArrayList<>();
        ArrayList<String> passwordList = new ArrayList<>();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
               String input_email = Objects.requireNonNull(username.getText().toString().trim());
               String input_pass = Objects.requireNonNull(password.getText().toString().trim());

                if (!input_email.isEmpty() && !input_pass.isEmpty()) {


                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myUserReference = database.getReference("usuarios");
                    try {
                        myUserReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for(DataSnapshot ds: snapshot.getChildren()){
                                        String email = ds.child("email").getValue().toString();
                                        String pass = ds.child("password").getValue().toString();
                                        emailList.add(email);
                                        passwordList.add(pass);
                                    }
                                    for(int i=0; i<emailList.size();i++){
                                        if (input_email.equals(emailList.get(i))){
                                            for(int j=0; j<passwordList.size();j++)
                                            {
                                                if (input_pass.equals(passwordList.get(i))){
                                                    Toast.makeText(getApplicationContext(), "Email and password are correct, wolcome again!", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(getApplicationContext(), "Email or password are incorrect", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Email or password incorrect", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), "No ha funcionado", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "No ha funcionado", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
    }


}

