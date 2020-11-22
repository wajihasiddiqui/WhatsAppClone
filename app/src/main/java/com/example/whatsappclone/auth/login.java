 package com.example.whatsappclone.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.whatsappclone.MainActivity;
import com.example.whatsappclone.R;
import com.example.whatsappclone.model.users;
import com.example.whatsappclone.settings.SettingsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity {

    Button button;
    EditText phone;
    EditText resendCode,countryCode ;

    private static String TAG = "login";
    private FirebaseAuth auth;
    private String verificationid;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    private ProgressDialog progressDialog;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    

//  String[] countery = {"Pakistan","USA","China","other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            startActivity(new Intent(this, SetUserInfoActivity.class));
        }

        button = findViewById(R.id.btnNext);
        phone = findViewById(R.id.phone);
        resendCode = findViewById(R.id.resendCode);
        countryCode = findViewById(R.id.codeCountry);


//        Spinner spin = findViewById(R.id.spinnercountry);
//        spin.setOnItemSelectedListener(this);
//
//        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,countery);
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spin.setAdapter(aa);

        auth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().toString().equals("NEXT")) {
                    progressDialog.setMessage("please wait");
                    progressDialog.show();

                    String phonee = "+" + countryCode.getText().toString() + phone.getText().toString();
                    startPhoneNumberVerification(phonee);
                }
                else {
                    verifyPhoneNumberWithCode(verificationid, resendCode.getText().toString());
                }
            }
        });


        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                 Log.d(TAG,"onVerificationComplete: Complete");
                 signInWithPhoneAuthCredential(phoneAuthCredential);
                 progressDialog.dismiss();


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG,"onVerificationFaild: "+e.getMessage());
            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                verificationid = verificationId;
                resendingToken = token;

                button.setText("Confirm");
                progressDialog.dismiss();

            }
        };
    }

    private void startPhoneNumberVerification(String PhoneNumber){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                PhoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(login.this, SetUserInfoActivity.class));
                            //if(user != null){
//                                String userId = user.getUid();
//                                users users = new users(userId,
//                                        "",
//                                        user.getPhoneNumber(),
//                                        "",
//                                        "",
//                                        "",
//                                        "",
//                                        "",
//                                        "",
//                                        "");
//
//                                firestore.collection("users").document("userInfo").collection(userId).add(users)
//                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                            @Override
//                                            public void onSuccess(DocumentReference documentReference) {
//
//                                                startActivity(new Intent(login.this, SetUserInfoActivity.class));
//                                            }
//                                        });
//                            }
//                            else{
//                                Toast.makeText(getApplicationContext(),"Something Errror",Toast.LENGTH_SHORT).show();
//                            }


                        } else {
                            progressDialog.dismiss();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.d(TAG,"onComplete: Error Code");

                            }
                        }
                    }
                });
    }


//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getApplicationContext(),countery[position], Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }


}