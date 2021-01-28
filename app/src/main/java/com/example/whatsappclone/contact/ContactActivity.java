package com.example.whatsappclone.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.adapter.contactsadapter;
import com.example.whatsappclone.model.users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
    ImageButton btnback;

    public static final int REQUEST_READ_CONTACTS = 79;
    private ListView contactlist;
    private ArrayList mobileArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        btnback = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();
        firestore =  FirebaseFirestore.getInstance();

        if (firebaseUser != null) {

            getContactFromPhone();  //If they using this app
//            getcontactlist();
        }

        if(mobileArray != null){
            getcontactlist();
        }
    }

    private void getContactFromPhone() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            mobileArray = getAllPhoneContacts();
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mobileArray = getAllPhoneContacts();
                } else {
                   finish();
                }
                return;
            }
        }
    }
    private ArrayList getAllPhoneContacts() {
        ArrayList<String> phoneList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cur.getString(cur.getColumnIndex(
//                        ContactsContract.Contacts.DISPLAY_NAME));
//                nameList.add(name);
                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        phoneList.add(phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return phoneList;
    }

    private void getcontactlist() {

        firestore.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                    // Log.d(TAG, "onSuccess: data "+ snapshot.toString());
                    String UserId = snapshot.getString("userId");
                    String username = snapshot.getString("userName");
                    String description = snapshot.getString("bio");
                    String imageProfile = snapshot.getString("imageProfile");
                    String userPhone = snapshot.getString("userPhone");


                    users user = new users();

                    user.setUserId(UserId);
                    user.setUserName(username);
                    user.setBio(description);
                    user.setImageProfile(imageProfile);
                    user.setUserPhone(userPhone);


//                    if (UserId != null && !UserId.equals(firebaseUser.getUid())) {

//                        if (mobileArray.contains(user.getUserPhone())) {
                           list.add(user);
                       // }

                 //   }

                }

//
//                for(users user : list){
//                    if(mobileArray.contains(user.getUserPhone())) {
//                        Log.d(TAG, "getContactList: true" + user.getUserPhone());
//                    }
//                    else{
//                        Log.d(TAG, "getContactList: false" + user.getUserPhone());
//                    }
//
//                }

                adapter = new contactsadapter(list, ContactActivity.this);
                recyclerView.setAdapter(adapter);

            }
        });
    }
}