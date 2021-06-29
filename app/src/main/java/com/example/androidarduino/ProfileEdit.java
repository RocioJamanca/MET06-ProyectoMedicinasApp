package com.example.androidarduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
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
        Intent intent = getIntent();
        dataBasePath = intent.getStringExtra("path");


       EditText inputName = findViewById(R.id.text_name_editProfile);
//        EditText inputSurname = findViewById(R.id.text_surname_editProfile);
//        EditText inputAge = findViewById(R.id.text_age_editProfile);
//        EditText inputEmail = findViewById(R.id.text_email_profile);
//        EditText inputDevice = findViewById(R.id.text_device_editProfile);
//        ImageView inputUserProfile = findViewById(R.id.img_userProfile_editProfile);
//        Button btnSave = findViewById(R.id.btn_saveChanges_editProfile);
//        Button btnCancel = findViewById(R.id.btn_cancel_editProfile);

         readUser (dataBasePath);

        inputName.setHint("hola");//no deja set Text
//        inputSurname.setText(userGet.getSurname());
//        inputAge.setText(userGet.getAge());
//        inputEmail.setText(userGet.getEmail());
//
//        if(userGet.getDevice().equals("")){
//            inputDevice.setText("Device has not been assigned yet");
//        }
//        else {
//            inputDevice.setText(userGet.getDevice());
//        }
//
//        if(userGet.getProfile_photo().equals("")){
//
//        }
//        else {
//            Picasso.get().load(userGet.getProfile_photo()).into(inputUserProfile);
//        }

//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = inputName.getText().toString();
//                String surname = inputSurname.getText().toString();
//                String email = inputEmail.getText().toString();
//                String age = inputAge.getText().toString();
//
//                //El primer if es para asegurarnos que los campos están completados
//                if (!email.isEmpty() &&  !surname.isEmpty() && !name.isEmpty() && !age.isEmpty() ) {
//
//                    //Asegurarnos que el formato de email es correcto la contraseña tiene almenos 6 caracteres
//                    // y la contraseña y la confirmación de contraseña coinciden, en caso de que no se cumpla alguna condicion entramos
//                    if (!validarEmail(email)){
//                        //En caso de que t0do esté mal se muestra los errores
//
//                    }
//
//                    //En caso de que todos los parámetros son correctos.
//                    else {
//                        editUser("Prueba","","","","","","","");
//                    }
//                    }
//
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    Intent intent = new Intent(ProfileEdit.this, Profile.class);
//                    startActivity(intent);
//                } finally {
//                    finish();
//                }
//            }
//        });




    }

    public void readUser (String path){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
                    dataBasePath = path;
                    DatabaseReference myUser = database.getReference(path);
                    myUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                String name  =  Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                                String surname  =  Objects.requireNonNull(snapshot.child("surname").getValue()).toString();
                                String email  =  snapshot.child("email").getValue().toString();
                                String password  =  snapshot.child("password").getValue().toString();
                                String age  =  snapshot.child("age").getValue().toString();
                                String device  =  snapshot.child("device").getValue().toString();
                                String patient  =  snapshot.child("patient").getValue().toString();
                                String profilePhoto  =  snapshot.child("profile_photo").getValue().toString();

                                User user = new User(email,password,name,surname,age,device,patient,profilePhoto);
                                //Toast.makeText(Profile.this, email, Toast.LENGTH_SHORT).show();
                                userGet=user;

                            }}

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            Log.w(TAG, "loadPost:onCancelled", error.toException());
                            Toast.makeText(ProfileEdit.this, "Failed to load post.", Toast.LENGTH_SHORT).show();
                        }
                    });

    }

    public void editUser (String email,String password,String name, String surname, String age,String device,String patient ,String url){
        User user = new User(email,password,name,surname,age,device,patient,url);
        databaseReference.setValue(user);
        Toast.makeText(getApplicationContext(), "El usuario ha sido correctamente añadido", Toast.LENGTH_SHORT).show();

    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}