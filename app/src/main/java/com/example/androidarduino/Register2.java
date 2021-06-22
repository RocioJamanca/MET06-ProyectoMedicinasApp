package com.example.androidarduino;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register2 extends MainActivity {
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
        setContentView(R.layout.activity_register3);

        txt_Name = findViewById(R.id.txt_email_register);
        txt_surname = findViewById(R.id.text_surname);
        age = findViewById(R.id.txt_email_register);
        phone = findViewById(R.id.txt_email_register);
        EditTextEmail = findViewById(R.id.txt_email_register);
        editPassword = findViewById(R.id.txt_password_register);
        serialNum = findViewById(R.id.txt_email_register);

        btn_register_2 = findViewById(R.id.btn_registrarse_register);


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