package com.example.whatsappclone.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.whatsappclone.BuildConfig;
import com.example.whatsappclone.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.provider.ContactsContract.RawContacts.*;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fabcamera;
    private BottomSheetDialog bottomsheetdialog;

    private int Image_gallry_request = 111;
    private Uri imageUrl;

    ImageView img_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        fabcamera = findViewById(R.id.fab_camera);

        img_profile = findViewById(R.id.img_profile);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        initActionClick();
    }

    private void initActionClick() {
        fabcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetpickPhoto();

            }
        });

    }

    private void showBottomSheetpickPhoto() {

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pick, null);


        ((View) view.findViewById(R.id.In_gallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
                bottomsheetdialog.dismiss();

            }
        });

        ((View) view.findViewById(R.id.In_camera)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkCameraPermission();
            }
        });


        bottomsheetdialog = new BottomSheetDialog(this);

        bottomsheetdialog.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomsheetdialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        bottomsheetdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomsheetdialog = null;
            }
        });
        bottomsheetdialog.show();

    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    221);


        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    222);


        } else {
            opencamera();
        }
    }

    private void opencamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMDD_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_ " + timeStamp + ".jpg";

        try {
            File file = File.createTempFile("IMG_" + timeStamp, "jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            imageUrl = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
            intent.putExtra("listPhotoName", imageFileName);
            startActivityForResult(intent, 440);

        }
        catch (IOException e) {

        }
    }

    private void opengallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "select image"), Image_gallry_request);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_gallry_request && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUrl = data.getData();
        }

        if (requestCode == 440 && resultCode == RESULT_OK) {


        }
    }
}