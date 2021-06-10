package com.example.androidarduino;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import android.widget.AdapterView;
import android.widget.Toast;


public class Calendario extends MainActivity {

    //CalendarView calendari;
    Button btn_calendar;
    DatePickerDialog picker;
    EditText eText;
    Button btnGet;
    TextView tvw;
    Button refresh;
    TextView textmed;
    int SELECT_PICTURE = 200;
    String encodedImage = "";
    Button Bselectimage;
    String[] days = { "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
    ImageView foto_med;
    private byte[] imageToByteArray(Bitmap bitmapImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        return baos.toByteArray();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        //calendari = (CalendarView)findViewById(R.id.calendari_box);
        refresh = findViewById(R.id.btn_sendcalendar);
        textmed = findViewById(R.id.nombre_medicina);
        TextView textlun = findViewById(R.id.textViewLun);
        TextView textmar = findViewById(R.id.textViewMar);
        TextView textmie = findViewById(R.id.textViewMier);
        TextView textjue = findViewById(R.id.textViewJue);
        TextView textvie = findViewById(R.id.textViewVie);
        TextView textsab = findViewById(R.id.textViewSab);
        TextView textdom = findViewById(R.id.textViewDom);
        foto_med =findViewById(R.id.imageView);
        Bselectimage = findViewById(R.id.btn_image2);
        Spinner spin = (Spinner) findViewById(R.id.spinner2);
        spin.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,days));


        //long selectedDate = calendari.getDate();
        //calendari = (CalendarView)findViewById(R.id.calendari_box);
        //calendari.setDate(System.currentTimeMillis(),false,true);
        ///calendari.setFirstDayOfWeek(2);
       // int firstDayOfWeek= calendari.getFirstDayOfWeek();
        //btn_calendar = findViewById(R.id.btn_date_selection);
        Bselectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                imagechooser();
            }
        });


        tvw=(TextView)findViewById(R.id.textView1);
        //eText=(EditText) findViewById(R.id.editText1);
//        eText.setInputType(InputType.TYPE_NULL);

        Intent intent = getIntent();
        String dates[] = intent.getStringArrayExtra("date");
        String meds[] = intent.getStringArrayExtra("med");

        //primer get.textbox --- nom textbox + meds[]
        int count = 0;
        for(String s: dates){
            //s --> dia
            //meds[count] --> pasti

            if( s == "Lunes")
            {
                String n = (String)textlun.getText();
                n = n + meds[count];
                textlun.setText(n);
            }
            if( s == "Martes")
            {
                String n = (String)textmar.getText();
                n = n + meds[count];
                textmar.setText(n);
            }
            if( s == "Miercoles")
            {
                String n = (String)textmie.getText();
                n = n + meds[count];
                textmie.setText(n);
            }
            if( s == "Jueves")
            {
                String n = (String)textjue.getText();
                n = n + meds[count];
                textjue.setText(n);
            }
            if( s == "Viernes")
            {
                String n = (String)textvie.getText();
                n = n + meds[count];
                textvie.setText(n);
            }
            if( s == "Sabado")
            {
                String n = (String)textsab.getText();
                n = n + meds[count];
                textsab.setText(n);
            }
            if( s == "Domingo")
            {
                String n = (String)textdom.getText();
                n = n + meds[count];
                textdom.setText(n);
            }
            count++;
        }
        String r_foto = intent.getStringExtra("foto_med");
        if(r_foto != null) {
            byte[] decodedString = Base64.decode(r_foto, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            foto_med.setImageBitmap(decodedByte);
        }
        /*eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Calendario.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        /*btnGet=(Button)findViewById(R.id.button1);
        //btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvw.setText("Selected Date: "+ eText.getText());
            }
        });*/



        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent o = new Intent();
                o.putExtra("date", spin.getSelectedItem().toString());
                o.putExtra("med", textmed.getText());
                o.putExtra("foto_med", encodedImage);
                setResult(-1, o);
                finish();
            }
        });

    }
    private void showDatePickerDialog() {
        // DatePickerFragment newFragment = new DatePickerFragment();
        //newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
    public void imagechooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Selecciona la foto del medicamento"), SELECT_PICTURE);
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

                    foto_med.setImageURI(selectedImageUri);
                }
            }
        }
    }




}

