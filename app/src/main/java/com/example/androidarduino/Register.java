package com.example.androidarduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog loading;
    //To compress the profile image and the upload to the storage
    Bitmap thumb_bitmap =null;
    private static final int GalleryPick = 1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String cameraPermission[];
    String storagePermission[];
    de.hdodenhof.circleimageview.CircleImageView profilePhoto;
    Uri image_uri;
    Uri downloadUri;
    String downUri = "";
    String is_patient = "";
    //Para la comprobación de la lista de emails para que no se pueda "rerregistrar" un usuario con el mismo email
    ArrayList<String> check_emailList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Declaracion de la ruta de la base de datos,
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        storageReference= FirebaseStorage.getInstance().getReference().child("UsersProfilePhoto");

        //Inputs del layout del registro
        EditText input_name = findViewById(R.id.txt_name_register);
        EditText input_surname = findViewById(R.id.txt_surname_register);
        EditText input_email = findViewById(R.id.txt_email_register);
        EditText input_pass = findViewById(R.id.txt_password_register);
        EditText input_age = findViewById(R.id.txt_age_register);
        EditText input_confirm_pass = findViewById(R.id.txt_passconfirm_register);
        Button register = findViewById(R.id.btn_registrarse_register);
        Button newPhoto = findViewById(R.id.btn_newPhoto_register);
        profilePhoto = findViewById(R.id.img_user_register);



        //Acción del botón registrarse
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Una vez se hace click en el boton se obtienen los datos que el usuario introduce
                String name = input_name.getText().toString();
                String surname = input_surname.getText().toString();
                String email = input_email.getText().toString();
                String password =input_pass.getText().toString();
                String age = input_age.getText().toString();
                String confirmationPass = input_confirm_pass.getText().toString();

                //El primer if es para asegurarnos que los campos están completados
                if (!email.isEmpty() && !password.isEmpty()&& !surname.isEmpty() && !name.isEmpty() && !age.isEmpty() && !confirmationPass.isEmpty()) {

                    //Asegurarnos que el formato de email es correcto la contraseña tiene almenos 6 caracteres
                    // y la contraseña y la confirmación de contraseña coinciden, en caso de que no se cumpla alguna condicion entramos
                    if (!validarEmail(email)|| password.length()<6 ||!password.equals(confirmationPass) ){
                        //En caso de que t0do esté mal se muestra los errores
                        if(!validarEmail(email) && (password.length()<6) && (!password.equals(confirmationPass))) {
                            input_email.setError("Email is not valid!");
                            input_pass.setError("Password must have at least 6 characters");
                            input_confirm_pass.setError("Passwords do not match try again!");
                        }
                        //Si únicamente el email no es correcto.
                        else if (!validarEmail(email)){
                            input_email.setError("Email is not valid!");
                        }
                        //Si la contraseña y confirmación de contraseña no coinciden
                        else if (!password.equals(confirmationPass)){
                            input_confirm_pass.setError("Passwords do not match try again!");
                        }
                        //Si la contraseña no tiene los carácteres correctos
                        else {
                            input_pass.setError("Password must have at least 6 characters");
                        }
                    }

                    //En caso de que todos los parámetros son correctos.
                    else {
                             //Comprobamos si el email ya está en uso
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                                check_emailList = new ArrayList<>();
                                if (snapshot.exists()) {

                                    //Añadimos cada uno de los emails de los diferentes usuarios de la db en la lista
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        String email = ds.child("email").getValue().toString();
                                        check_emailList.add(email);
                                    }
                                }
                                boolean emailUsed = false;
                                //Buscamos si alguno de los emails de la lista ya está en uso
                                for(int i=0; i<check_emailList.size();i++){
                                    if (email.equals(check_emailList.get(i))){
                                        Toast.makeText(getApplicationContext(), "This email is already registered", Toast.LENGTH_SHORT).show();
                                        emailUsed=true;
                                    }
                                }
                                if(!emailUsed){ //Añadimos el usuario si el email no está registrado
                                    input_age.setText("");
                                    input_confirm_pass.setText("");
                                    input_email.setText("");
                                    input_name.setText("");
                                    input_pass.setText("");
                                    input_surname.setText("");

                                    registNewUser(email,password,name,surname,age,"",is_patient,downUri);}
                            }
                            @Override
                            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), "No ha funcionado", Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }

            }
        }); //Final on click botón register

        ImageButton btnSeePass = findViewById(R.id.btn_seePassword_register);
        btnSeePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_pass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    btnSeePass.setImageResource(R.drawable.hide_password);

                    //Show Password
                    input_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    btnSeePass.setImageResource(R.drawable.view_password);
                    //Hide Password
                    input_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });

        ImageButton btnConfirmSeePass = findViewById(R.id.btn_seeConfirmPass_register);
        btnConfirmSeePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_confirm_pass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    btnConfirmSeePass.setImageResource(R.drawable.hide_password);

                    //Show Password
                    input_confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    btnConfirmSeePass.setImageResource(R.drawable.view_password);
                    //Hide Password
                    input_confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });


        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(Register.this);
            }
        });
        newPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //System is >= marshmellow
//                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
//                        String [] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                        requestPermissions(permission, PERMISSION_CODE);
//                    }
//                    else{
//                        //Granted
                        CropImage.startPickImageActivity(Register.this);
//                    }
//                }
//                else{ //System is < marshmellow
//                    openCamera();
//                }
            }
        });
    }//On create end


    public void registNewUser (String email,String password,String name, String surname, String age,String device,String patient ,String url){
        User user = new User(email,password,name,surname,age,device,patient,url);
        databaseReference.push().setValue(user);
        Toast.makeText(getApplicationContext(), "El usuario ha sido correctamente añadido", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(intent);
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

//    private void openCamera() {
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE,"New Picture");
//        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
//        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
//
//        //Camera intent
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
//        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSION_CODE: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    openCamera();
//                } else {
//                    Toast.makeText(this, "Permission denied..", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            //Set image to image View
//            profilePhoto.setImageURI(image_uri);
//        }
        if (requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            image_uri = CropImage.getPickImageResultUri(this, data);

            //Crop Image
            CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);

        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                File url = new File(resultUri.getPath());
               Picasso.with(this).load(url).into(profilePhoto);

                //Now we compress the image
                try{
                    thumb_bitmap = new Compressor(this).setQuality(80).compressToBitmap(url);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress((Bitmap.CompressFormat.JPEG),80,(byteArrayOutputStream));
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();
                //File compressed

                //Now we create a random name to name the file
                int p = (int) (Math.random() * 25 + 1); int s = (int) (Math.random() * 25 + 1);
                int t = (int) (Math.random() * 25 + 1); int c = (int) (Math.random() * 25 + 1);
                int number1 = (int) (Math.random() * 1012 + 2111);
                int number2 = (int) (Math.random() * 1012 + 2111);

                String[] elements = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

                final String randomName = elements[p] + elements[s] + number1  + elements[t] + elements[c] + number2 + "_compressed.jpg";

                StorageReference ref = storageReference.child(randomName);
                UploadTask uploadTask = ref.putBytes(thumb_byte);
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw Objects.requireNonNull(task.getException());
                        }
                        return ref.getDownloadUrl();
                    }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Uri> task) {
                       downloadUri = task.getResult();
                        assert downloadUri != null;
                        downUri = downloadUri.toString();
                     //  Toast.makeText(Register.this, "Image correctly added", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }
}