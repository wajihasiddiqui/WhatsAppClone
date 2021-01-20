package com.example.whatsappclone.chats;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.adapter.chatsadapter;
import com.example.whatsappclone.model.chat.chat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private TextView tv_username;
    private ImageView ImageView;
    private ImageButton btnback;
    private EditText edMessage;
    private FloatingActionButton btnSend;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String receverID;
    private com.example.whatsappclone.adapter.chatsadapter chatsadapter;
    private List<chat>list;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        tv_username = findViewById(R.id.tv_username);
        ImageView = findViewById(R.id.imageProfile);
        btnback = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.btnSend);
        edMessage = findViewById(R.id.edMessage);
        recyclerView = findViewById(R.id.recyclerview);

        Intent intent =  getIntent();
        String userName = intent.getStringExtra("userName");
        String receverID = intent.getStringExtra("userId");
        String userProfile = intent.getStringExtra("imageProfile");



        if(receverID != null){
            tv_username.setText(userName);
            Glide.with(this).load(userProfile).into(ImageView);
        }

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(TextUtils.isEmpty(edMessage.getText().toString())) {
                    btnSend.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_voice_24));
                }
                else{
                    btnSend.setImageDrawable(getDrawable(R.drawable.ic_baseline_send_24));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initBtnclick();

        list = new ArrayList<>();
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this, RecyclerView.VERTICAL,true);
        layoutmanager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutmanager);

        readData();
    }

    private void readData() {
        try{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("chat").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for(DataSnapshot snapshots : snapshot.getChildren()) {
                        chat chats = snapshots.getValue(chat.class);

                        if(chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(receverID)) {
                            list.add(chats);
                        }
                    }
                    if(chatsadapter != null){
                        chatsadapter.notifyDataSetChanged();
                    }
                    else {
                        chatsadapter = new chatsadapter(list,ChatsActivity.this);
                        recyclerView.setAdapter(chatsadapter);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        catch(Exception e){

            e.printStackTrace();
        }


    }

    private void initBtnclick() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edMessage.getText().toString())){
                    sendTextMessage(edMessage.getText().toString());

                    edMessage.setText("");
                }
            }
        });


    }

    private void sendTextMessage(String text) {
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String today = simpleDateFormat.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String currentTime = df.format(currentDateTime.getTime());

        chat chats = new chat(
                today+","+currentTime,
                text,
                "TEXT",
                firebaseUser.getUid(),
                receverID
        );
        databaseReference.child("chat").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Send", "on Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Send", "onFailure"+e.getMessage());
            }
        });

        //add to chat list

        DatabaseReference chatref1 = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(receverID);
        chatref1.child("chatid").setValue(receverID);

        DatabaseReference chatref2 = FirebaseDatabase.getInstance().getReference("ChatList").child(receverID).child(firebaseUser.getUid());
        chatref2.child("chatid").setValue(firebaseUser.getUid());
    }
}