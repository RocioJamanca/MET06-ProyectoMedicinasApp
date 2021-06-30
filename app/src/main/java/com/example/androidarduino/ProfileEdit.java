package com.example.androidarduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import id.zelory.compressor.Compressor;


public class ProfileEdit extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    String isPatient;

    //To compress the profile image and the upload to the storage
    Bitmap thumb_bitmap =null;
    private static final int GalleryPick = 1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    Uri image_uri;
    Uri downloadUri;
    String downUri = "";

    ImageView inputUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);



        EditText inputName = findViewById(R.id.text_name_editProfile);
        EditText inputSurname = findViewById(R.id.text_surname_editProfile);
        EditText inputAge = findViewById(R.id.text_age_editProfile);
        EditText inputDevice = findViewById(R.id.text_device_editProfile);
        inputUserProfile = findViewById(R.id.img_userProfile_editProfile);
        Button btnSave = findViewById(R.id.btn_saveChanges_editProfile);
        Button btnCancel = findViewById(R.id.btn_cancel_editProfile);

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
                                    inputName.setHint(user.getName());
                                    inputSurname.setHint(user.getSurname());
                                    inputAge.setHint(user.getAge());


                                    if(user.getDevice().equals("")){
                                    inputDevice.setHint("Device has not been assigned yet");
                                    }else {
                                        inputDevice.setText(user.getDevice());
                                    }if(user.getProfile_photo().equals("")){

                                    }else {
                                        Picasso.get().load(user.getProfile_photo()).into(inputUserProfile);
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

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileEdit.this, Profile.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User();
                //Name
                if(inputName.getText().toString().isEmpty()) {
                    user.setName(inputName.getHint().toString());
                }
                else {
                    user.setName(inputName.getText().toString());
                }
                //Surname
                if(inputSurname.getText().toString().isEmpty()) {
                    user.setSurname(inputSurname.getHint().toString());
                }
                else {
                    user.setSurname(inputSurname.getText().toString());
                }
                //Age
                if(inputAge.getText().toString().isEmpty()) {
                    user.setAge(inputAge.getHint().toString());
                }
                else {
                    user.setAge(inputAge.getText().toString());
                }
                //Device
                if(inputDevice.getText().toString().isEmpty()) {
                    user.setDevice(inputDevice.getHint().toString());
                }
                else {
                    user.setDevice(inputDevice.getText().toString());
                }
                //Photo
                if(!downUri.isEmpty()) {
                    user.setProfile_photo(downUri);
                }


                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                DatabaseReference dataRef = firebaseDatabase.getReference("usuarios").child(mFirebaseUser.getUid());

                dataRef.child("name").setValue(user.getName());
                dataRef.child("surname").setValue(user.getSurname());
                dataRef.child("age").setValue(user.getAge());
                dataRef.child("device").setValue(user.getDevice());
                if (!user.getProfile_photo().isEmpty())
                dataRef.child("profile_photo").setValue(user.getProfile_photo());


                if (!isPatient.isEmpty()){
                    user.setPatient(isPatient);
                    dataRef.child("patient").setValue(user.getPatient());
                }

            }
        });

        inputUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(ProfileEdit.this);
            }
        });




    }

    public void onRadioGroup_EditBtn(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.btn_patient_editProfile:
                if (checked)
                    isPatient = "Patient";
                break;
            case R.id.btn_relative_editProfile:
                if (checked)
                    isPatient = "Relative";
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileEdit.this, HomeMenu.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            image_uri = CropImage.getPickImageResultUri(this, data);

            //Crop Image
            CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1)
                    .start(this);

        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                File url = new File(resultUri.getPath());
                Picasso.get().load(url).into(inputUserProfile);

                //Now we compress the image
                try{
                    thumb_bitmap = new Compressor(this).setMaxWidth(500).setMaxHeight(500).setQuality(80).compressToBitmap(url);

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