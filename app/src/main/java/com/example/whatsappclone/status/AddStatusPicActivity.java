package com.example.whatsappclone.status;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.MainActivity;
import com.example.whatsappclone.R;
import com.example.whatsappclone.managers.ChatService;
import com.example.whatsappclone.model.status;
import com.example.whatsappclone.service.FirebaseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class AddStatusPicActivity extends AppCompatActivity {

    private Uri imageUri;
    private ImageView imageView;
    private ImageButton btnback;
    private FloatingActionButton btnSend;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_status_pic);

        imageView = findViewById(R.id.image_view);
        btnback = findViewById(R.id.btn_back);
        description = findViewById(R.id.description);
        btnSend = findViewById(R.id.btn_send);


        imageUri = MainActivity.imageUri;

        setInfo();
        initclick();
    }

    private void initclick() {

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseService(AddStatusPicActivity.this).uploadImageToFirebaseStorage(imageUri, new FirebaseService.OnCallBack() {
                    @Override
                    public void onUploadSuccess(String imageUrl) {
                        status statusmodel = new status();
                        statusmodel.setId(UUID.randomUUID().toString());
                        statusmodel.setCreateDate(new ChatService(AddStatusPicActivity.this).getCurrentDate());
                        statusmodel.setImageStatus(imageUrl);
                        statusmodel.setUserId(FirebaseAuth.getInstance().getUid());
                        statusmodel.setViewCount("0");
                        statusmodel.setTextStatus(description.getText().toString());

                        new FirebaseService(AddStatusPicActivity.this).addNewStatus(statusmodel, new FirebaseService.onAddNewStatusCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(AddStatusPicActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                Toast.makeText(AddStatusPicActivity.this, "Something", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onUploadFailure(Exception e) {
                        Log.e("TAG", "onUploadFailure: ", e);
                    }
                });
            }
        });

    }

    private void setInfo() {
        Glide.with(this).load(imageUri).into(imageView);
    }
}