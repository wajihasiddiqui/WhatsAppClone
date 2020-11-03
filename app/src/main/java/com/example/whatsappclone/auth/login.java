package com.example.whatsappclone.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsappclone.MainActivity;
import com.example.whatsappclone.R;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void nextbtn(View view) {
        startActivity(new Intent(login.this, MainActivity.class  ));
    }
}