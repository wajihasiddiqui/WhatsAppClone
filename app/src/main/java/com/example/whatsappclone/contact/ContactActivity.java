package com.example.whatsappclone.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.whatsappclone.R;
import com.example.whatsappclone.adapter.contactsadapter;
import com.example.whatsappclone.model.users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {



    RecyclerView recyclerView;
    private static final String TAG = "ContactActivity";
    private List<users> list = new ArrayList<>();
    private contactsadapter adapter;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        recyclerView = findViewById(R.id.recyclerview);

        getcontactlist();
    }

    private void getcontactlist() {

        firestore.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                    Log.d(TAG, "onSuccess: data "+ snapshot.toString());
                    String Uid = snapshot.getString("userId");
                    String username = snapshot.getString("username");
                    String description = snapshot.getString("bio");
                    String urlprofile = snapshot.getString("urlprofile");


                    users user = new users();

                    user.setUserId(Uid);
                    user.setUserName(username);
                    user.setBio(description);
                    user.setImageProfile(urlprofile);

                    if(Uid != null && !Uid.equals(user.getUserId())) {
                        list.add(user);
                    }

                }

                adapter = new contactsadapter(list, ContactActivity.this);
                recyclerView.setAdapter(adapter);

            }
        });
    }
}