package com.example.whatsappclone.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.example.whatsappclone.BuildConfig;
import com.example.whatsappclone.R;
import com.example.whatsappclone.common.common;
import com.example.whatsappclone.display.ViewImageActivity;
import com.example.whatsappclone.startup.SplashScreen;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static android.provider.ContactsContract.RawContacts.*;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fabcamera;
    private BottomSheetDialog bottomsheetdialog, bottomsheeteditname;
    private int Image_gallry_request = 111;
    private Uri imageUri;
    ImageView img_profile;
    TextView tv_phone, tv_username;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private ProgressDialog progressDialog;
    Button signout;

    LinearLayout in_edit_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        fabcamera = findViewById(R.id.fab_camera);

        img_profile = findViewById(R.id.img_profile);
        in_edit_name = findViewById(R.id.in_edit_name);

        signout = findViewById(R.id.btn_logout);

        tv_username = findViewById(R.id.tv_username);
        tv_phone = findViewById(R.id.tv_phone);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        progressDialog = new ProgressDialog(this);

        if(firebaseUser != null){
            getInfo();
        }

        initActionClick();
    }

    private void initActionClick() {
        fabcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetpickPhoto();

            }
        });

        in_edit_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showBottomSheetEditName();
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img_profile.invalidate();
                Drawable drawable = img_profile.getDrawable();
                common.IMAGE_BITMAP = ((GlideBitmapDrawable)drawable.getCurrent()).getBitmap();
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ProfileActivity.this, img_profile, "image");
                Intent intent = new Intent(ProfileActivity.this, ViewImageActivity.class);
                startActivity(intent,activityOptionsCompat.toBundle());

            }
        });


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showDialogSignpout();
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

                Toast.makeText(ProfileActivity.this, "camera", Toast.LENGTH_SHORT).show();
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

    private void showBottomSheetEditName(){

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_edit_name, null);

        ((View) view.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomsheeteditname.dismiss();
            }
        });

        final EditText edusername = (EditText) view.findViewById(R.id.ed_username);

        ((View) view.findViewById(R.id.btn_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(edusername.getText().toString())){
                    Toast.makeText(ProfileActivity.this, "Name Can't be Empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    updatename(edusername.getText().toString());

                    checkCameraPermission();
                    bottomsheeteditname.dismiss();
                }


            }
        });

        bottomsheeteditname = new BottomSheetDialog(this);

        bottomsheeteditname.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomsheeteditname.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        bottomsheeteditname.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomsheeteditname = null;
            }
        });
        bottomsheeteditname.show();
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


    private void getInfo(){

        firestore.collection("users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String username = documentSnapshot.getString("userName");
                String userphone = documentSnapshot.getString("userPhone");
                String imageProfile = documentSnapshot.getString("imageProfile");

                tv_username.setText(username);
                tv_phone.setText(userphone);
                Glide.with(ProfileActivity.this).load(imageProfile).into(img_profile);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Get Data", "onFailure: "+e.getMessage());

            }
        });
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

            uploadImageToFirebase();

//            try{
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                img_profile.setImageBitmap(bitmap);
//            }
//            catch(Exception e){
//                e.printStackTrace();
//            }
        }


        if (requestCode == 440
                && resultCode == RESULT_OK){

            uploadImageToFirebase();

        }

    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentRes = getContentResolver();
        MimeTypeMap mimeType = MimeTypeMap.getSingleton();
        return mimeType.getExtensionFromMimeType(contentRes.getType(uri));
    }

    private void uploadImageToFirebase() {

        if(imageUri != null){

            progressDialog.setMessage("Uploading.. ");
            progressDialog.show();

            StorageReference reverseref = FirebaseStorage.getInstance().getReference().child("ImagesProfile/" + System.currentTimeMillis()+ "." +getFileExtension(imageUri));
            reverseref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadUrl = uriTask.getResult();

                    final String downlod_url = String.valueOf(downloadUrl);
                    //Toast.makeText(ProfileActivity.this, "upload Successfully", Toast.LENGTH_SHORT).show();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("imageProfile", downlod_url);
                    progressDialog.dismiss();

                    firestore.collection("users").document(firebaseUser.getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(ProfileActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();

                            getInfo();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(ProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }

    }

    private void updatename(String newName){
        firestore.collection("users").document(firebaseUser.getUid()).update("userName",newName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(ProfileActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                getInfo();
            }
        });

    }


    private void showDialogSignpout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setMessage("Do you want to sign out");
        builder.setPositiveButton("Sign put", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, SplashScreen.class));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}