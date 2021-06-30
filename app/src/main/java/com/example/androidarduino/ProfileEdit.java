package com.example.androidarduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ProfileEdit extends AppCompatActivity {
    private static final String TAG = "ProfileEdit";
    User userGet = null;
    String dataBasePath;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);



        EditText inputName = findViewById(R.id.text_name_editProfile);
        EditText inputSurname = findViewById(R.id.text_surname_editProfile);
        EditText inputAge = findViewById(R.id.text_age_editProfile);
        EditText inputEmail = findViewById(R.id.text_email_profile);
        EditText inputDevice = findViewById(R.id.text_device_editProfile);
        ImageView inputUserProfile = findViewById(R.id.img_userProfile_editProfile);
        Button btnSave = findViewById(R.id.btn_saveChanges_editProfile);
        Button btnCancel = findViewById(R.id.btn_cancel_editProfile);




    }
}