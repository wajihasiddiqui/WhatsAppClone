package com.example.whatsappclone.chats;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.adapter.chatsadapter;
import com.example.whatsappclone.dialog.DialogReviewSendImage;
import com.example.whatsappclone.interfafaces.OnReadChatCallback;
import com.example.whatsappclone.managers.ChatService;
import com.example.whatsappclone.model.chat.chat;
import com.example.whatsappclone.profile.User_ProfileActivity;
import com.example.whatsappclone.service.FirebaseService;
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
    private ImageView ImageView, btn_file;
    private ImageButton btnback;
    private EditText edMessage;
    private FloatingActionButton btnSend;
    private String receverID, userName;
    private com.example.whatsappclone.adapter.chatsadapter chatsadapter;
    private List<chat>list = new ArrayList<>();
    RecyclerView recyclerView;
    private String TAG;
    private CardView layout_action;
    private ChatService chatService;
    private int Image_gallry_request = 111;
    private Uri imageUri;

    LinearLayout btn_gallry;

    private boolean initActionShown = false;

    private String userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        tv_username = findViewById(R.id.tv_username);
        ImageView = findViewById(R.id.imageProfile);
        btnback = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.btnSend);
        edMessage = findViewById(R.id.edMessage);
        recyclerView = findViewById(R.id.recyclerview);

        btn_file = findViewById(R.id.btn_file);
        btn_gallry = findViewById(R.id.btn_gallry);

        layout_action = findViewById(R.id.layout_action);

        initialize();
        initBtnclick();
        readData();
    }

    private void initialize(){

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent =  getIntent();
        userName = intent.getStringExtra("userName");
        receverID = intent.getStringExtra("userId");
        userProfile = intent.getStringExtra("imageProfile");

        chatService = new ChatService(this,receverID);


        if(receverID != null){

            Log.d(TAG,"onCreate receverId" + receverID);
            tv_username.setText(userName);
            if(userProfile != null){
                if(userProfile.equals("")){

                    ImageView.setImageResource(R.drawable.personicon);  // Set default image when user is null
                }
                else{

                    Glide.with(this).load(userProfile).into(ImageView);
                }
            }
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

        list = new ArrayList<>();
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this, RecyclerView.VERTICAL,true);
        layoutmanager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutmanager);

        chatsadapter = new chatsadapter(list, this);
        recyclerView.setAdapter(chatsadapter);
    }

    private void readData() {
        chatService.readChatData(new OnReadChatCallback() {
            @Override
            public void onReadSuccess(List<chat> list) {

               Log.d(TAG, "onReadSuccess: List" + list.size());
                recyclerView.setAdapter(new chatsadapter(list, ChatsActivity.this));

            }

            @Override
            public void onReadFailed() {
                Log.e(TAG, "onReadFailed: ");

            }
        });
    }

    private void initBtnclick() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edMessage.getText().toString())){
                    chatService.sendTextMessage(edMessage.getText().toString());

                    edMessage.setText("");
                }
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatsActivity.this, User_ProfileActivity.class)
                .putExtra("userId", receverID)
                                .putExtra("imageProfile",userProfile)
                        .putExtra("userName",userName)
                );
            }
        });

        btn_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(initActionShown){
                    layout_action.setVisibility(View.GONE);
                    initActionShown = false;
                }
                else{
                    layout_action.setVisibility(View.VISIBLE);
                    initActionShown = true;
                }
            }
        });

        btn_gallry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
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

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                reviewImage(bitmap);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
        }

    }

    private void  reviewImage(Bitmap bitmap){
        new DialogReviewSendImage(ChatsActivity.this, bitmap).show(new DialogReviewSendImage.OnCallBack() {
            @Override
            public void onButtonSendClick() {
                // to Upload image to Firebase storage to get url image....
                if(imageUri!=null){
                    final ProgressDialog progressDialog = new ProgressDialog(ChatsActivity.this);
                    progressDialog.setMessage("Sending image...");
                    new FirebaseService(ChatsActivity.this).uploadImageToFirebaseStorage(imageUri, new FirebaseService.onCallBack() {
                        @Override
                        public void onUploadSuccess(String imageUrl) {
                            // to send chat image//
                            chatService.sendImage(imageUrl);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onUploadFailure(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }



}