package com.example.whatsappclone.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.BuildConfig;
import com.example.whatsappclone.MainActivity;
import com.example.whatsappclone.R;
import com.example.whatsappclone.model.users;
import com.example.whatsappclone.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class SetUserInfoActivity extends AppCompatActivity {


    Button btnNext;
    CircularImageView profileImage;
    EditText edname;
    private ProgressDialog progressDialog;
    private BottomSheetDialog bottomsheetdialog;
    private Uri imageUri;
    private int Image_gallry_request = 111;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_info);

        btnNext = findViewById(R.id.btn_next);
        profileImage = findViewById(R.id.image_profile);
        edname = findViewById(R.id.ed_name);


        //Check is the user is new or not

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(firebaseUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                        edname.setText(task.getResult().getString("userName"));
                        Glide.with(SetUserInfoActivity.this).load(task.getResult().getString("userName")).into(profileImage);
                }

                else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });

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

                   // uploadImageToFirebase();

                    doUpdate();
                }

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetpickPhoto();

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

                Toast.makeText(SetUserInfoActivity.this, "camera", Toast.LENGTH_SHORT).show();
                bottomsheetdialog.dismiss();
                //checkCameraPermission();
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
            imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("listPhotoName", imageFileName);
            startActivityForResult(intent, 440);

        }
        catch (IOException e) {
            e.printStackTrace();
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

        if (requestCode == Image_gallry_request
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            imageUri = data.getData();
            Glide.with(SetUserInfoActivity.this).load(imageUri).into(profileImage);
           // uploadImageToFirebase();

        }


        if (requestCode == 440
                && resultCode == RESULT_OK){
            Glide.with(SetUserInfoActivity.this).load(imageUri).into(profileImage);
            //uploadImageToFirebase();
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentRes = getContentResolver();
        MimeTypeMap mimeType = MimeTypeMap.getSingleton();
        return mimeType.getExtensionFromMimeType(contentRes.getType(uri));
    }

//    private void uploadImageToFirebase() {
//
//        if(imageUri != null){
//
//            progressDialog.setMessage("Uploading.. ");
//            progressDialog.show();
//
//            StorageReference reverseref = FirebaseStorage.getInstance().getReference().child("ImagesProfile/" + System.currentTimeMillis()+ "." +getFileExtension(imageUri));
//            reverseref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                    while (!uriTask.isSuccessful());
//                    Uri downloadUrl = uriTask.getResult();
//
//                    final String downlod_url = String.valueOf(downloadUrl);
//                    //Toast.makeText(ProfileActivity.this, "upload Successfully", Toast.LENGTH_SHORT).show();
//
//                    HashMap<String, Object> hashMap = new HashMap<>();
//                    hashMap.put("imageProfile", downlod_url);
//                    hashMap.put("userName", edname.getText().toString());
//
//                    progressDialog.dismiss();
//
//                    db.collection("users").document(firebaseUser.getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//                            Toast.makeText(SetUserInfoActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                            finish();
//
//                        }
//                    });
//
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                    Toast.makeText(SetUserInfoActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//            });
//        }
//
//    }
}