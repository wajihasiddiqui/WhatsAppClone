package com.example.whatsappclone.profile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;

import java.util.Objects;

public class User_ProfileActivity extends AppCompatActivity {


    private ImageView ImageView;

    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile);


        ImageView = findViewById(R.id.image_profile);
        toolbar = findViewById(R.id.toolbar);


        Intent intent =  getIntent();
        String userName = intent.getStringExtra("userName");
        String receverID = intent.getStringExtra("userId");
        String userProfile = intent.getStringExtra("imageProfile");


        if(receverID != null){

            toolbar.setTitle(userName);
            if(userProfile != null){
                if(userProfile.equals("")){

                    ImageView.setImageResource(R.drawable.personicon);  // Set default image when user is null
                }
                else{

                    Glide.with(this).load(userProfile).into(ImageView);
                }
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar(){

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}