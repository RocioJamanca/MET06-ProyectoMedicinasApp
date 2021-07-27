package com.example.androidarduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraConstrainedHighSpeedCaptureSession;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class TextReconigtion extends AppCompatActivity {

    Button btn_captureImage, btn_detectText;
    ImageView imageView;
    TextView textRecognition;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    String textDisplayed,med_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_reconigtion);
        btn_captureImage = findViewById(R.id.btn_captureImage_recognition);
        btn_detectText = findViewById(R.id.btn_detectext_recognition);
        imageView = findViewById(R.id.img_image_recognition);
        textRecognition = findViewById(R.id.text_display_recognition);

        btn_captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            dispatchTakePictureIntent();
            }
        });

        btn_detectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectTextFromImage();
                textRecognition.setText("");
            }
        });

    }

    private void detectTextFromImage() {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    displayTextFromImage(firebaseVisionText);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+ e.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("Error: ", e.getMessage());
            }
        });
    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks();
        if(blockList.size()==0){
            Toast.makeText(getApplicationContext(),"No text found in imag",Toast.LENGTH_LONG).show();
        }
        else {
            ArrayList<String> medicines = new ArrayList<>();
            for (FirebaseVisionText.Block block: firebaseVisionText.getBlocks()){
                textDisplayed = block.getText();
                medicines.add(textDisplayed);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textRecognition.setText(String.join(", ",medicines));
            }

            Intent returnIntent = new Intent();
            returnIntent.putExtra("datos",medicines);
            setResult(medicineDay.RESULT_OK,returnIntent);
            finish();

        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 0);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap= (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }



}