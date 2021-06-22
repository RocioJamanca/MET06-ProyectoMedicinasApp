package com.example.androidarduino;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Perfil extends MainActivity {
    TextView mail_t;
    TextView name_t;
    Button reg;
    Button Bselectimage;
    ImageView foto_perfil;
    EditText txt_Name;
    EditText txt_surname;
    EditText EditTextEmail;
    EditText editPassword;
    EditText serialNum;
    EditText age;
    EditText phone;
    TextView in_mail;
    RadioGroup clase;
    RadioButton radibut;
    int j;
    int SELECT_PICTURE = 200;
    String encodedImage = "";

    private byte[] imageToByteArray(Bitmap bitmapImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        return baos.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        clase = (RadioGroup)findViewById(R.id.tipo_paciente);

        txt_Name = findViewById(R.id.text_name);
        txt_surname = findViewById(R.id.text_surname);
        age = findViewById(R.id.text_age);
        phone = findViewById(R.id.text_phone);
        in_mail = (TextView) findViewById(R.id.textView4);
        //mail_t = (TextView) findViewById(R.id.EditTextEmail); //originals
        //name_t = (TextView) findViewById(R.id.text_surname); //originals
        reg = (Button) findViewById(R.id.btn_registrarse_register);
        serialNum = findViewById(R.id.text_serialnum);
        Bselectimage = findViewById(R.id.btn_image);
        foto_perfil =findViewById(R.id.image_perfil);



        Intent intent = getIntent();
        String mail = intent.getStringExtra("mail");
        //mail_t.setText(mail);
        in_mail.setText(mail);

        String r_name = intent.getStringExtra("name");
        txt_Name.setText(r_name);

        String r_surname = intent.getStringExtra("surname");
        txt_surname.setText(r_surname);

        String r_age = intent.getStringExtra("age");
        age.setText(r_age);

        String r_phone = intent.getStringExtra("phone");
        phone.setText(r_phone);

        String r_serialnumb = intent.getStringExtra("serial");
        serialNum.setText(r_serialnumb);

        String r_foto = intent.getStringExtra("foto");
        if(r_foto != null) {
            byte[] decodedString = Base64.decode(r_foto, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            foto_perfil.setImageBitmap(decodedByte);
        }
        Bselectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                imagechooser();
            }
        });




        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent o = new Intent();
                if (j == 1)
                {
                    o.putExtra("paciente", "Familiar");
                }
                else if (j == 2)
                {
                    o.putExtra("paciente", "Paciente");
                }


                o.putExtra("mail", mail);
                o.putExtra("surname", txt_surname.getText());
                o.putExtra("name", txt_Name.getText());
                o.putExtra("age", age.getText());
                o.putExtra("phone", phone.getText());
                o.putExtra("serial", serialNum.getText());
                o.putExtra("foto", encodedImage);
                setResult(-1, o);
                finish();
            }
        });
    }
    public void onRadioButtonClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
            switch (view.getId()){
                case R.id.radioButton:
                    if (checked)
                        j =1;
                    break;
                case R.id.radioButton2:
                    if (checked)
                        j = 2;
                    break;
            }
    }

    public void imagechooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Selecciona la foto de perfil"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // update the preview image in the layout
                    byte[] imageBytes = imageToByteArray(bitmap);
                    encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                    foto_perfil.setImageURI(selectedImageUri);
                }
            }
        }
    }
}
