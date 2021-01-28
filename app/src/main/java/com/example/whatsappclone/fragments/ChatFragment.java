package com.example.whatsappclone.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.whatsappclone.R;
import com.example.whatsappclone.adapter.chatlistadapter;
import com.example.whatsappclone.model.chatlist;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ChatFragment extends Fragment {


    RecyclerView recyclerView;
    private static final String TAG = "ChatFragment";
    ProgressBar progressBar;


    public ChatFragment() {
        // Required empty public constructor
    }

    private FirebaseUser firebaseUser;
    private DatabaseReference referance;
    private FirebaseFirestore firestore;
    private Handler Handler = new Handler();
    private chatlistadapter  chatsadapter;

    private List<chatlist> list ;
    private ArrayList<String> allUserId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        list = new ArrayList<>();
        allUserId = new ArrayList<>();

//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatsadapter = new chatlistadapter(list, getContext());
//        recyclerView.setAdapter(chatsadapter);


       // progressBar = (ProgressBar) view.findViewById(R.id.progress_circular);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        referance = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();



        if(firebaseUser != null){
            getChatList();
        }
//
//        RecyclerView recyclerview = view.findViewById(R.id.recyclerview);
//
//        recyclerview.setAdapter(new chatlistadapter(list,getContext()));
        return view;
    }

    private void getChatList() {
//        progressBar.setVisibility(View.VISIBLE);
        referance.child("ChatList").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                allUserId.clear();
                for(DataSnapshot dsnapshot : snapshot.getChildren()){
                    String userId = Objects.requireNonNull(dsnapshot.child("chatid").getValue()).toString();
                    Log.d(TAG, "OnDataChange: userId"+ userId);

                    progressBar.setVisibility(View.GONE);
                    allUserId.add(userId);

                }

                   getUserData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserData() {

        Handler.post(new Runnable() {
            @Override
            public void run() {
                for (String userId : allUserId){
                    firestore.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d(TAG, "onSuccess: ddd" + documentSnapshot.getString("userName"));
                            try {
                                chatlist chat = new chatlist(
                                        documentSnapshot.getString("userId"),
                                        documentSnapshot.getString("userName"),
                                        "this is description",
                                        "",
                                        documentSnapshot.getString("imageProfile")


                                );
                                list.add(chat);
                            }
                            catch(Exception e){
                                Log.d(TAG, "onSuccess: " + e.getMessage());
                            }
                            if(chatsadapter != null){
                                chatsadapter.notifyItemInserted(0);
                                chatsadapter.notifyDataSetChanged();

                                Log.d(TAG, "onSuccess: chatadapter" + chatsadapter.getItemCount());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Error L" + e.getMessage());
                        }
                    });
                }
            }
        });
    }

}