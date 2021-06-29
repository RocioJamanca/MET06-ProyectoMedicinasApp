package com.example.androidarduino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeMenu extends AppCompatActivity {

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("email");

        Button btnProfile = findViewById(R.id.btn_profile_home);
        Button btnCalendar = findViewById(R.id.btn_calendar_home);
        Button btnRemote = findViewById(R.id.btn_remote_home);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                //Pasamos el usuario
                intent.putExtra("email", userEmail);
                startActivity(intent);
            }
        });


    }
}