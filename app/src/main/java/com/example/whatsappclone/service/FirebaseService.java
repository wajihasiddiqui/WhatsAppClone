package com.example.whatsappclone.service;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.whatsappclone.model.status;
import com.example.whatsappclone.profile.ProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static com.example.whatsappclone.MainActivity.imageUri;

public class FirebaseService {

    private Context context;

    public FirebaseService(Context context) {
        this.context = context;
    }

    public void uploadImageToFirebaseStorage(Uri uri, final onCallBack onCallBack) {

            StorageReference reverseref = FirebaseStorage.getInstance().getReference().child("ImagesChats/" + System.currentTimeMillis()+ "." +getFileExtension(imageUri));
            reverseref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadUrl = uriTask.getResult();

                    final String downlod_url = String.valueOf(downloadUrl);
                    onCallBack.onUploadSuccess(downlod_url);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    onCallBack.onUploadFailure(e);
                }
            });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentRes = context.getContentResolver();
        MimeTypeMap mimeType = MimeTypeMap.getSingleton();
        return mimeType.getExtensionFromMimeType(contentRes.getType(uri));
    }

    public interface onCallBack{
        void onUploadSuccess(String imageUrl);
        void onUploadFailure(Exception e);
    }

    public void addNewStatus(status statusModel, final onAddNewStatusCallback onAddNewStatusCallback){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Status Daily").document(statusModel.getId()).set(statusModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onAddNewStatusCallback.onSuccess();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onAddNewStatusCallback.onFailure();
            }
        });
        ;
    }

    public interface onAddNewStatusCallback{
        void onSuccess();
        void onFailure();
    }

}
