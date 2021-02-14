package com.example.whatsappclone.chats;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ChatsActivity extends AppCompatActivity {

    private TextView tv_username;
    private ImageView ImageView, btn_file,btn_camera,btn_imoji;
    private ImageButton btnback;
    private EditText edMessage;
    private FloatingActionButton btnSend;
    private String receverID, userName;
    private com.example.whatsappclone.adapter.chatsadapter chatsadapter;
    private List<chat> list;
    private RecyclerView recyclerView;
    private String TAG;
    private CardView layout_action;
    private ChatService chatService;
    private int Image_gallry_request = 111;
    private Uri imageUri;
    private static final int REQUEST_CORD_PERMISSION = 332;
    //Audio
    private MediaRecorder mediaRecorder;
    private String audio_path;
    private String sTime;
    private RecordButton recordButton;
    private RecordView recordView;

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
        recordButton = findViewById(R.id.record_button);
        recordView = findViewById(R.id.record_view);

        btn_file = findViewById(R.id.btn_file);
        btn_camera = findViewById(R.id.btn_camera);
        btn_imoji = findViewById(R.id.btn_emoji);
        btn_gallry = findViewById(R.id.btn_gallry);

        layout_action = findViewById(R.id.layout_action);

        initialize();
        initBtnclick();
        readData();
    }

    private void initialize() {

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        receverID = intent.getStringExtra("userId");
        userProfile = intent.getStringExtra("imageProfile");

        chatService = new ChatService(this, receverID);


        if (receverID != null) {

            Log.d(TAG, "onCreate receverId" + receverID);
            tv_username.setText(userName);
            if (userProfile != null) {
                if (userProfile.equals("")) {

                    ImageView.setImageResource(R.drawable.personicon);  // Set default image when user is null
                } else {

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

                if (TextUtils.isEmpty(edMessage.getText().toString())){
                    btnSend.setVisibility(View.INVISIBLE);
                    recordButton.setVisibility(View.VISIBLE);
                } else {
                    btnSend.setVisibility(View.VISIBLE);
                    recordButton.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        readData();

        //initialize record button
        recordButton.setRecordView(recordView);
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {

                //Start Recording..
                if (!checkPermissionFromDevice()) {
                    btn_imoji.setVisibility(View.INVISIBLE);
                    btn_file.setVisibility(View.INVISIBLE);
                    btn_camera.setVisibility(View.INVISIBLE);
                    edMessage.setVisibility(View.INVISIBLE);

                    startRecord();
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator != null) {
                        vibrator.vibrate(100);
                    }

                } else {
                    requestPermission();
                }

            }

            @Override
            public void onCancel() {
                try {
                    mediaRecorder.reset();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish(long recordTime) {
                btn_imoji.setVisibility(View.VISIBLE);
                btn_file.setVisibility(View.VISIBLE);
                btn_camera.setVisibility(View.VISIBLE);
                edMessage.setVisibility(View.VISIBLE);

                //Stop Recording..
                try {
                    sTime = getHumanTimeText(recordTime);
                    stopRecord();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLessThanSecond() {
                btn_imoji.setVisibility(View.VISIBLE);
                btn_file.setVisibility(View.VISIBLE);
                btn_camera.setVisibility(View.VISIBLE);
                edMessage.setVisibility(View.VISIBLE);
            }
        });
        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                btn_imoji.setVisibility(View.VISIBLE);
                btn_file.setVisibility(View.VISIBLE);
                btn_camera.setVisibility(View.VISIBLE);
                edMessage.setVisibility(View.VISIBLE);
            }
        });

    }



    @SuppressLint("DefaultLocale")
    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d",
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
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



    private boolean checkPermissionFromDevice() {
        int write_external_strorage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_strorage_result == PackageManager.PERMISSION_DENIED || record_audio_result == PackageManager.PERMISSION_DENIED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_CORD_PERMISSION);
    }

    private void startRecord(){
        setUpMediaRecorder();

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            //  Toast.makeText(InChatActivity.this, "Recording...", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ChatsActivity.this, "Recording Error , Please restart your app ", Toast.LENGTH_LONG).show();
        }

    }

    private void stopRecord(){
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;

                chatService.sendVoice(audio_path);

            } else {
                Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Stop Recording Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void setUpMediaRecorder() {
        String path_save = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "audio_record.m4a";
        audio_path = path_save;

        mediaRecorder = new MediaRecorder();
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(path_save);
        } catch (Exception e) {
            Log.d(TAG, "setUpMediaRecord: " + e.getMessage());
        }


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
                    progressDialog.show();

                    //hide action buttonss
                    layout_action.setVisibility(View.GONE);
                    initActionShown = false;

                    new FirebaseService(ChatsActivity.this).uploadImageToFirebaseStorage(imageUri, new FirebaseService.OnCallBack() {
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