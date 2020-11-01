package com.example.whatsappclone.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.whatsappclone.MainActivity;
import com.example.whatsappclone.R;

public class StartupScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_screen);


        Button buttonagree = findViewById(R.id.btn_agree);

        buttonagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartupScreenActivity.this, MainActivity.class  ));
            }
        });
    }
}