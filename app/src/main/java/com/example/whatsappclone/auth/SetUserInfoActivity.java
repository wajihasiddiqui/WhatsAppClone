package com.example.whatsappclone.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsappclone.MainActivity;
import com.example.whatsappclone.R;
import com.example.whatsappclone.model.users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

public class SetUserInfoActivity extends AppCompatActivity {


    Button btnNext;
    CircularImageView profileImage;
    EditText edname;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_info);

        btnNext = findViewById(R.id.btn_next);
        profileImage = findViewById(R.id.image_profile);
        edname = findViewById(R.id.ed_name);

        progressDialog = new ProgressDialog(this);
        initButtinClicks();
    }

    private void initButtinClicks() {

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(edname.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please input username",Toast.LENGTH_SHORT).show();
                }
                else {

                    doUpdate();
                }

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"This function is not ready to use",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void doUpdate() {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            String userId = firebaseUser.getUid();
            users users = new users(userId,
                    edname.getText().toString(),
                    firebaseUser.getPhoneNumber(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "");
            firebaseFirestore.collection("users").document(firebaseUser.getUid()).set(users)
                    //.update("userName",edname.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Update successfull",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Log.d("Update","onFailure: "+e.getMessage());
                    Toast.makeText(getApplicationContext(),"update failed "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(getApplicationContext(),"You Need to login First",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }
}