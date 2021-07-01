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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        input_email = findViewById(R.id.txt_email_login);
        input_password = findViewById(R.id.txt_password_login);
        Button btnLogin = findViewById(R.id.btn_login_login);
        Button btnRegister = findViewById(R.id.btn_register_login);
        firebaseDatabase= FirebaseDatabase.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference("usuarios");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null){
                    moveToHomeActivity(mFirebaseUser);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }
            }
        };



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
               String email = input_email.getText().toString();
               String password = input_password.getText().toString();

                if (email.isEmpty()) {
                    input_email.setError("Please provide email");
                    input_email.requestFocus();

                }else if (password.isEmpty()){
                    input_password.setError("Please provide password");
                    input_password.requestFocus();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Login error, please try again!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                moveToHomeActivity(task.getResult().getUser());
                            }
                        }
                    });

                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_email.setText("");
                input_password.setText("");

                Intent intent = new Intent(getApplicationContext(), Register.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    @Override
    protected void onStart() {
        super.onStart();
       firebaseAuth.addAuthStateListener(mAuthStateListener);
    }
    private void moveToHomeActivity(FirebaseUser mFirebaseUser) {
        firebaseDatabase.getReference("usuarios").child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
               // String completeName = snapshot.child("name").getValue(String.class);
                User user = snapshot.getValue(User.class);
                assert user != null;
                //Toast.makeText(getApplicationContext(), user.getEmail(), Toast.LENGTH_SHORT).show();

              String completeName = user.getName() + " " +user.getSurname();
                Intent i = new Intent(getApplicationContext(), HomeMenu.class);
               // Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("completeName",completeName);
                startActivity(i);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }




}

