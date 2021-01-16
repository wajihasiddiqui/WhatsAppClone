package com.example.whatsappclone.display;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.whatsappclone.R;
import com.example.whatsappclone.common.common;
import com.jsibbold.zoomage.ZoomageView;

public class ViewImageActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        ZoomageView imageView = findViewById(R.id.imageView);

        imageView.setImageBitmap(common.IMAGE_BITMAP);

    }
}