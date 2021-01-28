package com.example.whatsappclone.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.whatsappclone.adapter.chatsadapter;
import com.example.whatsappclone.chats.ChatsActivity;
import com.example.whatsappclone.interfafaces.OnReadChatCallback;
import com.example.whatsappclone.model.chat.chat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatService {

    private Context context;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String receverID;
    private DatabaseReference databaseReference;

    public ChatService(Context context, String receverID) {
        this.context = context;
        this.receverID = receverID;
    }

    public ChatService(Context context) {
        this.context = context;
    }

    public void readChatData(final OnReadChatCallback onCallBack){

        reference.child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<chat> list = new ArrayList<>();
                for(DataSnapshot snapshots : snapshot.getChildren()) {
                    chat chats = snapshots.getValue(chat.class);
                    try{

                        if(chats != null && chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(receverID)
                                || chats.getReceiver().equals(firebaseUser.getUid()) && chats.getSender().equals(receverID)
                        )
                        {
                            list.add(chats);
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                }
                onCallBack.onReadSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onCallBack.onReadFailed();
            }
        });
    }

    public void sendTextMessage(String text){

        chat chats = new chat(
                getCurrentDate(),
                text,
                "",
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

    public void sendImage(String imageUrl){

        chat chats = new chat(
                getCurrentDate(),
                "",
                imageUrl,
                "IMAGE",
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

    public String getCurrentDate(){

        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String today = simpleDateFormat.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String currentTime = df.format(currentDateTime.getTime());

        return today+","+currentTime;

    }
}
