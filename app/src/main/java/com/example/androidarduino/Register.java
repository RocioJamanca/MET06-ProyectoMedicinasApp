package com.example.androidarduino;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidarduino.MainActivity;
import com.example.androidarduino.R;

public class Register extends MainActivity {
    EditText txt_Name;
    EditText txt_surname;
    EditText EditTextEmail;
    EditText editPassword;
    EditText serialNum;
    EditText age;
    EditText phone;
    Button btn_register_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txt_Name = findViewById(R.id.EditTextEmail);
        txt_surname = findViewById(R.id.text_surname);
        age = findViewById(R.id.EditTextEmail);
        phone = findViewById(R.id.EditTextEmail);
        EditTextEmail = findViewById(R.id.EditTextEmail);
        editPassword = findViewById(R.id.editPassword);
        serialNum = findViewById(R.id.EditTextEmail);

        btn_register_2 = findViewById(R.id.btn_register_2);


        btn_register_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int err = checkDataEntered();
                if (err == 0) {
                    Intent o = new Intent();
                    o.putExtra("mail", EditTextEmail.getText());
                    o.putExtra("pass", editPassword.getText());
                    setResult(-1, o);
                    finish();
                }
            }
        });
    }
    boolean isEmail(EditText text){
        CharSequence EditTextEmail = text.getText().toString();
        return(!TextUtils.isEmpty(EditTextEmail)&& Patterns.EMAIL_ADDRESS.matcher(EditTextEmail).matches());
    }
    boolean isEmpty(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    int checkDataEntered(){
        if(isEmpty(EditTextEmail)){
            Toast t = Toast.makeText(this, "Introduce el nombre", Toast.LENGTH_SHORT);
            t.show();
            return 1;
        }
        if(isEmpty(editPassword)){
            Toast t = Toast.makeText(this, "Introduce la contraseña", Toast.LENGTH_SHORT);
            t.show();
            return 1;
        }
        if(isEmpty(editPassword)){
            Toast t = Toast.makeText(this, "Introduce la contraseña", Toast.LENGTH_SHORT);
            t.show();
            return 1;
        }
        return 0;
    }

}