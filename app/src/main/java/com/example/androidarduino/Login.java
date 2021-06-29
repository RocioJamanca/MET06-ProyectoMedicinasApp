package com.example.androidarduino;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class Login extends AppCompatActivity {

    EditText input_email;
    EditText input_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        input_email = findViewById(R.id.txt_email_login);
        input_password = findViewById(R.id.txt_password_login);
        Button btnLogin = findViewById(R.id.btn_login_login);
        Button btnRegister = findViewById(R.id.btn_register_login);





        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
               String email = Objects.requireNonNull(input_email.getText().toString().trim());
               String password = Objects.requireNonNull(input_password.getText().toString().trim());

                if (!email.isEmpty() && !password.isEmpty()) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myUserReference = database.getReference("usuarios");

                        myUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    ArrayList<String> emailList = new ArrayList<>();
                                    ArrayList<String> passwordList = new ArrayList<>();
                                    for(DataSnapshot ds: snapshot.getChildren()){
                                        String email = ds.child("email").getValue().toString();
                                        String pass = ds.child("password").getValue().toString();
                                        emailList.add(email);
                                        passwordList.add(pass);
                                    }
                                    boolean userFound = false;
                                    for(int i=0; i<emailList.size();i++){
                                        if (email.equals(emailList.get(i)) && password.equals(passwordList.get(i))){
                                            userFound = true;
                                            Toast.makeText(getApplicationContext(), "Email and password are correct, welcome again!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    if(!userFound){
                                        Toast.makeText(getApplicationContext(), "Email or password incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Intent intent = new Intent(getApplicationContext(), HomeMenu.class);
                                        //Pasamos el usuario
                                        intent.putExtra("email", email);
                                        startActivity(intent);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), "No ha funcionado", Toast.LENGTH_LONG).show();
                            }
                        });
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_email.setText("");
                input_password.setText("");

                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });


        ImageButton btnSeePass = findViewById(R.id.btn_seePassword_login);
        btnSeePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    btnSeePass.setImageResource(R.drawable.hide_password);

                    //Show Password
                    input_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    btnSeePass.setImageResource(R.drawable.view_password);
                    //Hide Password
                    input_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });

    }

}

