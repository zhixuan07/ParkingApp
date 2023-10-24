package com.example.parkingappinti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {
    EditText nameInput, passwordInput,phoneInput,emailInput;
    Button register  ;
    TextView countryCode;
    String phoneNumber;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        setContentView(R.layout.registerlayout);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameInput = findViewById(R.id.nameInput);
        passwordInput = findViewById(R.id.passwordInput);
        phoneInput = findViewById(R.id.phoneInput);
        countryCode = findViewById(R.id.countryCode);
        emailInput = findViewById(R.id.emailInput);
        register = findViewById(R.id.Signup);
        phoneNumber = phoneInput.getText().toString() ;


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nameInput.getText().toString().trim().isEmpty() && !passwordInput.getText().toString().trim().isEmpty() && !emailInput.getText().toString().trim().isEmpty() && !phoneInput.getText().toString().trim().isEmpty() ){
                    mAuth.fetchSignInMethodsForEmail(emailInput.getText().toString())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    List<String> signInMethods = task.getResult().getSignInMethods();
                                    if (signInMethods != null && !signInMethods.isEmpty()) {
                                        // Email already exists, display an error message
                                        Toast.makeText(SignUpActivity.this, "Email already registered.",
                                                Toast.LENGTH_SHORT).show();
                                    }else{
                                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                                "+60"+phoneInput.getText().toString(),
                                                60, // Timeout duration
                                                TimeUnit.SECONDS,
                                                SignUpActivity.this,
                                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                    @Override
                                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                                    }

                                                    @Override
                                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                                        Toast.makeText(SignUpActivity.this,"Invalid Phone number",Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onCodeSent(@NonNull String verificationId,
                                                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                                        Intent intent = new Intent(getApplicationContext(),otpVerification.class);
                                                        intent.putExtra("name",nameInput.getText().toString());
                                                        intent.putExtra("email",emailInput.getText().toString());
                                                        intent.putExtra("password",passwordInput.getText().toString());
                                                        intent.putExtra("phone",phoneInput.getText().toString());
                                                        intent.putExtra("verificationId",verificationId);
                                                        startActivity(intent);
                                                    }
                                                }
                                        );

                                    }
                                }
                            });



                }else {
                    Toast.makeText(SignUpActivity.this,"Please fill up all the blank",Toast.LENGTH_SHORT).show();
                }
            }

        });




    }

    public void insetUserData (String userId){
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String phone = phoneInput.getText().toString();

        User user = new User(name,email,phone);
        mDatabase.child("users").child(userId).setValue(user);
        Toast.makeText(SignUpActivity.this, "SignUp Success.",
                Toast.LENGTH_SHORT).show();
    }
}
