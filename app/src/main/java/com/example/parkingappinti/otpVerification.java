package com.example.parkingappinti;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class otpVerification extends AppCompatActivity {

    TextView phoneNum;
    Button verifyOTP;
    EditText otpInput;
    String getVerificationId ;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.otp_verificationlayout);

        phoneNum = findViewById(R.id.phoneNumber);
        verifyOTP = findViewById(R.id.verifyOtp);
        otpInput = findViewById(R.id.otpInput);
        phoneNum.setText(String.format(
                "+60%s",getIntent().getStringExtra("phone")));
        getVerificationId = getIntent().getStringExtra("verificationId");

        verifyOTP.setOnClickListener(view -> {

            if(!otpInput.getText().toString().isEmpty()){
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(getVerificationId,otpInput.getText().toString());
                FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String email = getIntent().getStringExtra("email");
                                String password = getIntent().getStringExtra("password");
                                createAccount(email,password);
                                updateUI();
                            } else {
                                Toast.makeText(otpVerification.this, "Verification failed .",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

            }else{
                Toast.makeText(otpVerification.this, "Please fill in OTP .",
                        Toast.LENGTH_SHORT).show();
            }
        });





    }
    private void updateUI() {
        final Dialog loadingdialog = new Dialog(otpVerification.this);
        loadingdialog.startLoadingdialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // after 4 seconds
                loadingdialog.dismissdialog();
                Intent intent = new Intent(getApplicationContext(),BaseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 4000);

    }
    public  void sendData(){

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        insetUserData(userId);
    }
    private void insetUserData (String userId){

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String phone = String.format(
                "+60%s",getIntent().getStringExtra("phone"));

        User user = new User(email,name,phone);
        mDatabase.child("users").child(userId).setValue(user);


    }
    public void createAccount(String email, String password) {

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(otpVerification.this, "SignUp Success.",
                                    Toast.LENGTH_SHORT).show();
                            sendData();
                        } else {

                            Toast.makeText(otpVerification.this, "Email already exist.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    }
                });

    }
}
