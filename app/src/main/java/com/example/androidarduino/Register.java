package com.example.androidarduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    DatabaseReference databaseReference;
    String is_patient;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    de.hdodenhof.circleimageview.CircleImageView profilePhoto;
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // [START declare_database_ref]
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        EditText input_name = findViewById(R.id.txt_name_register);
        EditText input_surname = findViewById(R.id.txt_surname_register);
        EditText input_email = findViewById(R.id.txt_email_register);
        EditText input_pass = findViewById(R.id.txt_password_register);
        EditText input_age = findViewById(R.id.txt_age_register);
        EditText input_confirm_pass = findViewById(R.id.txt_passconfirm_register);
        Button register = findViewById(R.id.btn_registrarse_register);
        Button newPhoto = findViewById(R.id.btn_newPhoto_register);
        profilePhoto = findViewById(R.id.img_user_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = input_name.getText().toString();
                String surname = input_surname.getText().toString();
                String email = input_email.getText().toString();
                String password =input_pass.getText().toString();
                String age = input_age.getText().toString();
                String confirmationPass = input_confirm_pass.getText().toString();

                //Todo: Incluir fotografia de profile
                //Todo: Incluir galeria

                if (!email.isEmpty() && !password.isEmpty()&& !surname.isEmpty() && !name.isEmpty() && !age.isEmpty() && !confirmationPass.isEmpty()) {
                    if (!validarEmail(email)|| password.length()<6 ||!password.equals(confirmationPass) ){
                        if(!validarEmail(email) && (password.length()<6) && (!password.equals(confirmationPass))) {
                            input_email.setError("Email is not valid!");
                            input_pass.setError("Password must have at least 6 characters");
                            input_confirm_pass.setError("Passwords do not match try again!");
                        }
                        else if (!validarEmail(email)){
                            input_email.setError("Email is not valid!");
                        }
                        else if (!password.equals(confirmationPass)){
                            input_confirm_pass.setError("Passwords do not match try again!");
                        }
                        else {
                            input_pass.setError("Password must have at least 6 characters");
                        }
                    }

                    else {
                        User user = new User(email,password,name,surname,age,"",is_patient,"");
                        databaseReference.push().setValue(user);
                        Toast.makeText(getApplicationContext(), "El usuario ha sido correctamente añadido", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }

            }
        });

        newPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //System is >= marshmellow
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String [] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }

                    else{
                        //Granted
                        openCamera();
                    }
                }
                else{ //System is < marshmellow
                    openCamera();
                }
            }
        });



    }


    private boolean validarEmail(String email) {
    Pattern pattern = Patterns.EMAIL_ADDRESS;
    return pattern.matcher(email).matches();
    }

    public void onRadioBtnClicked_Register(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.btn_patient:
                if (checked)
                    is_patient = "Patient";
                    break;
            case R.id.btn_relative:
                if (checked)
                    is_patient = "Relative";
                    break;
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission denied..", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //Set image to image View
            profilePhoto.setImageURI(image_uri);
        }
    }
}